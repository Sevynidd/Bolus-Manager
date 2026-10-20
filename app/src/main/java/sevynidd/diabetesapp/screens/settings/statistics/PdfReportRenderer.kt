package sevynidd.diabetesapp.screens.settings.statistics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import sevynidd.diabetesapp.data.export.FactorReportRow
import sevynidd.diabetesapp.data.export.FactorsPdfReportContent
import sevynidd.diabetesapp.data.export.PumpBasalReportSection
import java.io.OutputStream

private const val PAGE_WIDTH = 595
private const val PAGE_HEIGHT = 842
private const val MARGIN = 48f
private const val CONTENT_WIDTH = PAGE_WIDTH - 2 * MARGIN
private const val CONTENT_RIGHT = PAGE_WIDTH - MARGIN

private const val TITLE_TEXT_SIZE = 20f
private const val RUNNING_HEADER_TEXT_SIZE = 9f
private const val META_TEXT_SIZE = 10f
private const val SECTION_TEXT_SIZE = 11f
private const val BODY_TEXT_SIZE = 11f
private const val FOOTER_TEXT_SIZE = 9f

private const val LETTER_SPACING = 0.04f
private const val HAIRLINE_STROKE_WIDTH = 0.75f

private const val LINE_HEIGHT = 16f
private const val ROW_HEIGHT = 22f
private const val SECTION_GAP = 20f
private const val HAIRLINE_GAP = 10f

private const val VALUE_COLUMN_RIGHT = PAGE_WIDTH - MARGIN - 160f
private const val LOG_DESCRIPTION_X = MARGIN + 100f

private const val FOOTER_MARGIN = 24f
private const val LOG_ENTRY_GAP = 6f

/** Material-theme-matched colors (see `ui/theme/Color.kt`'s light scheme) used to style the PDF. */
private val accentColor = Color.parseColor("#8F4C38")
private val onSurfaceColor = Color.parseColor("#231917")
private val onSurfaceVariantColor = Color.parseColor("#53433F")
private val hairlineColor = Color.parseColor("#D8C2BC")

/**
 * Draws [content] onto a paginated A4 PDF and writes it to [outputStream]; returns whether the
 * write succeeded. Intentionally not unit-tested: it only positions already-localized/formatted
 * text (see [sevynidd.diabetesapp.data.export.buildFactorsPdfReportContent]) using the
 * Android-framework [PdfDocument]/[Paint] APIs, which aren't available outside an Android runtime.
 */
fun renderFactorsPdfReport(content: FactorsPdfReportContent, outputStream: OutputStream): Boolean {
    return runCatching {
        val document = PdfDocument()
        val writer = PdfReportWriter(document, content.title)
        writer.drawHeader(content.title, content.generatedAtLabel)
        writer.drawFactorsTable(content)
        writer.drawLogSection(content)
        writer.finishCurrentPage()
        document.writeTo(outputStream)
        document.close()
    }.isSuccess
}

/**
 * Lays out [FactorsPdfReportContent] top-to-bottom in a clean, clinical-report style: hairline
 * rules instead of filled bands, generous whitespace, and the accent color reserved for section
 * labels. Starts a new A4 page whenever content would overflow, repeating a compact running
 * header (see [drawRunningHeader]) on every page after the first.
 */
private class PdfReportWriter(private val document: PdfDocument, private val reportTitle: String) {
    private val titlePaint = Paint().apply {
        textSize = TITLE_TEXT_SIZE
        isFakeBoldText = true
        color = onSurfaceColor
    }
    private val runningHeaderPaint = Paint().apply {
        textSize = RUNNING_HEADER_TEXT_SIZE
        color = onSurfaceVariantColor
        letterSpacing = LETTER_SPACING
    }
    private val metaPaint = Paint().apply { textSize = META_TEXT_SIZE; color = onSurfaceVariantColor }
    private val sectionPaint = Paint().apply {
        textSize = SECTION_TEXT_SIZE
        isFakeBoldText = true
        color = accentColor
        letterSpacing = LETTER_SPACING
    }
    private val headerPaint = Paint().apply {
        textSize = BODY_TEXT_SIZE
        isFakeBoldText = true
        color = onSurfaceColor
    }
    private val bodyPaint = Paint().apply { textSize = BODY_TEXT_SIZE; color = onSurfaceColor }
    private val mutedBodyPaint = Paint().apply { textSize = BODY_TEXT_SIZE; color = onSurfaceVariantColor }
    private val footerPaint = Paint().apply {
        textSize = FOOTER_TEXT_SIZE
        color = onSurfaceVariantColor
        textAlign = Paint.Align.CENTER
    }
    private val hairlinePaint = Paint().apply { color = hairlineColor; strokeWidth = HAIRLINE_STROKE_WIDTH }
    private val tablePaints = TablePaints(headerPaint, bodyPaint, hairlinePaint)

    private var pageNumber = 0
    private var page: PdfDocument.Page = startPage()
    private var y = MARGIN + TITLE_TEXT_SIZE

    private fun startPage(): PdfDocument.Page {
        pageNumber++
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        val newPage = document.startPage(pageInfo)
        if (pageNumber > 1) y = drawRunningHeader(newPage.canvas)
        return newPage
    }

    /** Draws the compact repeating header shown on every page after the first; returns the next baseline y. */
    private fun drawRunningHeader(canvas: Canvas): Float {
        var headerY = MARGIN + RUNNING_HEADER_TEXT_SIZE
        canvas.drawText(reportTitle, MARGIN, headerY, runningHeaderPaint)
        headerY += HAIRLINE_GAP
        canvas.drawLine(MARGIN, headerY, CONTENT_RIGHT, headerY, hairlinePaint)
        return headerY + SECTION_GAP
    }

    /** Closes the current page with its footer; also called once after the last section. */
    fun finishCurrentPage() {
        val ruleY = PAGE_HEIGHT - FOOTER_MARGIN - FOOTER_TEXT_SIZE
        page.canvas.drawLine(MARGIN, ruleY, CONTENT_RIGHT, ruleY, hairlinePaint)
        page.canvas.drawText(pageNumber.toString(), PAGE_WIDTH / 2f, PAGE_HEIGHT - FOOTER_MARGIN, footerPaint)
        document.finishPage(page)
    }

    private fun ensureSpace(height: Float) {
        if (y + height > PAGE_HEIGHT - MARGIN) {
            finishCurrentPage()
            page = startPage()
        }
    }

    private fun drawLine(text: String, paint: Paint, x: Float = MARGIN) {
        ensureSpace(LINE_HEIGHT)
        page.canvas.drawText(text, x, y, paint)
        y += LINE_HEIGHT
    }

    private fun drawWrappedLines(text: String, paint: Paint, x: Float = MARGIN, width: Float = CONTENT_WIDTH) {
        wrapToWidth(text, paint, width).forEach { line -> drawLine(line, paint, x) }
    }

    fun drawHeader(title: String, generatedAtLabel: String) {
        page.canvas.drawText(title, MARGIN, y, titlePaint)
        y += LINE_HEIGHT
        page.canvas.drawText(generatedAtLabel, MARGIN, y, metaPaint)
        y += HAIRLINE_GAP
        page.canvas.drawLine(MARGIN, y, CONTENT_RIGHT, y, hairlinePaint)
        y += SECTION_GAP
    }

    fun drawFactorsTable(content: FactorsPdfReportContent) {
        ensureSpace(LINE_HEIGHT)
        y = drawSectionTitle(page.canvas, y, content.factorsSectionTitle, sectionPaint)

        ensureSpace(ROW_HEIGHT)
        y = drawTableHeaderRow(page.canvas, y, content, tablePaints)

        content.factorRows.forEach { row ->
            ensureSpace(ROW_HEIGHT)
            y = drawTableRow(page.canvas, y, row, tablePaints)
        }

        y += SECTION_GAP - ROW_HEIGHT
        val pumpBasal = content.pumpBasal
        if (pumpBasal != null) {
            drawPumpBasalTable(pumpBasal)
        } else {
            ensureSpace(LINE_HEIGHT * 2)
            y = drawBasalRateLine(page.canvas, y, content, tablePaints)
        }
    }

    private fun drawPumpBasalTable(section: PumpBasalReportSection) {
        y += SECTION_GAP
        ensureSpace(LINE_HEIGHT)
        y = drawSectionTitle(page.canvas, y, section.title, sectionPaint)

        ensureSpace(ROW_HEIGHT)
        y = drawTwoColumnRow(page.canvas, y, section.timeHeader to section.rateHeader, tablePaints.header, tablePaints)
        section.rows.forEach { row ->
            ensureSpace(ROW_HEIGHT)
            y = drawTwoColumnRow(page.canvas, y, row.timeRange to row.rate, tablePaints.body, tablePaints)
        }

        y += SECTION_GAP - ROW_HEIGHT
        ensureSpace(LINE_HEIGHT * 2)
        page.canvas.drawText(section.dailyTotalLabel, MARGIN, y, headerPaint)
        y += SECTION_GAP + LINE_HEIGHT
    }

    fun drawLogSection(content: FactorsPdfReportContent) {
        ensureSpace(LINE_HEIGHT)
        y = drawSectionTitle(page.canvas, y, content.logSectionTitle, sectionPaint)

        if (content.logEntries.isEmpty()) {
            drawLine(content.logEmptyLabel, bodyPaint)
            return
        }

        content.logEntries.forEach { entry ->
            ensureSpace(LINE_HEIGHT)
            page.canvas.drawText(entry.timestampLabel, MARGIN, y, mutedBodyPaint)
            drawWrappedLines(
                entry.description,
                bodyPaint,
                x = LOG_DESCRIPTION_X,
                width = CONTENT_RIGHT - LOG_DESCRIPTION_X
            )
            y += LOG_ENTRY_GAP
        }
    }
}

/** Draws a small accent-colored, letter-spaced section label at [y]; returns the next baseline y. */
private fun drawSectionTitle(canvas: Canvas, y: Float, text: String, sectionPaint: Paint): Float {
    canvas.drawText(text, MARGIN, y, sectionPaint)
    return y + LINE_HEIGHT
}

/** Paints shared by the factors table's rows and the basal-rate line, grouped to keep draw functions short. */
private class TablePaints(val header: Paint, val body: Paint, val hairline: Paint)

/** Draws the factor-table header row with right-aligned columns and a hairline rule beneath; returns the next y. */
private fun drawTableHeaderRow(canvas: Canvas, y: Float, content: FactorsPdfReportContent, paints: TablePaints): Float {
    canvas.drawText(content.factorNameHeader, MARGIN, y, paints.header)
    canvas.drawRightAlignedText(content.factorValueHeader, VALUE_COLUMN_RIGHT, y, paints.header)
    canvas.drawRightAlignedText(content.factorTimeHeader, CONTENT_RIGHT, y, paints.header)
    val ruleY = y + HAIRLINE_GAP
    canvas.drawLine(MARGIN, ruleY, CONTENT_RIGHT, ruleY, paints.hairline)
    return y + ROW_HEIGHT
}

/** Draws one factor row at [y] with a thin hairline beneath it; returns the next baseline y. */
private fun drawTableRow(canvas: Canvas, y: Float, row: FactorReportRow, paints: TablePaints): Float {
    canvas.drawText(row.name, MARGIN, y, paints.body)
    canvas.drawRightAlignedText(row.value, VALUE_COLUMN_RIGHT, y, paints.body)
    canvas.drawRightAlignedText(row.timeRange, CONTENT_RIGHT, y, paints.body)
    val ruleY = y + HAIRLINE_GAP
    canvas.drawLine(MARGIN, ruleY, CONTENT_RIGHT, ruleY, paints.hairline)
    return y + ROW_HEIGHT
}

/** Draws a label and a right-aligned value (e.g. an hour and its U/h rate) over a hairline; returns the next y. */
private fun drawTwoColumnRow(
    canvas: Canvas,
    y: Float,
    cells: Pair<String, String>,
    paint: Paint,
    paints: TablePaints
): Float {
    canvas.drawText(cells.first, MARGIN, y, paint)
    canvas.drawRightAlignedText(cells.second, VALUE_COLUMN_RIGHT, y, paint)
    val ruleY = y + HAIRLINE_GAP
    canvas.drawLine(MARGIN, ruleY, VALUE_COLUMN_RIGHT, ruleY, paints.hairline)
    return y + ROW_HEIGHT
}

/** Draws the basal-rate summary as a plain label/value line under a hairline; returns the next baseline y. */
private fun drawBasalRateLine(canvas: Canvas, y: Float, content: FactorsPdfReportContent, paints: TablePaints): Float {
    canvas.drawText("${content.basalRateLabel}:", MARGIN, y, paints.header)
    val labelWidth = paints.header.measureText("${content.basalRateLabel}: ")
    canvas.drawText(content.basalRateSummary, MARGIN + labelWidth, y, paints.body)
    val ruleY = y + HAIRLINE_GAP
    canvas.drawLine(MARGIN, ruleY, CONTENT_RIGHT, ruleY, paints.hairline)
    return ruleY + SECTION_GAP - HAIRLINE_GAP
}

/** Draws [text] ending at [rightEdge] instead of starting at a fixed x, for right-aligned table columns. */
private fun Canvas.drawRightAlignedText(text: String, rightEdge: Float, y: Float, paint: Paint) {
    drawText(text, rightEdge - paint.measureText(text), y, paint)
}

/** Greedily wraps [text] into lines no wider than [maxWidth] under [paint], breaking on spaces. */
private fun wrapToWidth(text: String, paint: Paint, maxWidth: Float): List<String> {
    val words = text.split(" ")
    val lines = mutableListOf<String>()
    var current = StringBuilder()

    for (word in words) {
        val candidate = if (current.isEmpty()) word else "$current $word"
        if (current.isNotEmpty() && paint.measureText(candidate) > maxWidth) {
            lines += current.toString()
            current = StringBuilder(word)
        } else {
            current = StringBuilder(candidate)
        }
    }
    if (current.isNotEmpty()) lines += current.toString()
    return lines
}
