package sevynidd.diabetesapp.screens.settings.statistics

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import sevynidd.diabetesapp.data.export.FactorReportRow
import sevynidd.diabetesapp.data.export.FactorsPdfReportContent
import java.io.OutputStream

private const val PAGE_WIDTH = 595
private const val PAGE_HEIGHT = 842
private const val MARGIN = 40f

private const val TITLE_TEXT_SIZE = 20f
private const val META_TEXT_SIZE = 10f
private const val SECTION_TEXT_SIZE = 14f
private const val BODY_TEXT_SIZE = 11f
private const val FOOTER_TEXT_SIZE = 9f

private const val LINE_HEIGHT = 16f
private const val SECTION_GAP = 22f
private const val VALUE_COLUMN_OFFSET = 220f
private const val TIME_COLUMN_OFFSET = 320f
private const val CONTENT_WIDTH = PAGE_WIDTH - 2 * MARGIN

private const val HEADER_BAND_PADDING = 12f
private const val HEADER_BAND_CORNER_RADIUS = 8f
private const val HEADER_BAND_HEIGHT = TITLE_TEXT_SIZE + LINE_HEIGHT + 2 * HEADER_BAND_PADDING

private const val SECTION_BAR_WIDTH = 4f
private const val SECTION_BAR_GAP = 8f

private const val TABLE_HEADER_RULE_WIDTH = 1.5f
private const val ROW_FILL_TOP_INSET = 12f
private const val ROW_FILL_BOTTOM_INSET = 4f

private const val CALLOUT_PADDING = 10f
private const val CALLOUT_CORNER_RADIUS = 6f
private const val CALLOUT_HEIGHT = LINE_HEIGHT + 2 * CALLOUT_PADDING

private const val FOOTER_MARGIN = 24f

private const val LOG_BULLET_RADIUS = 2f
private const val LOG_BULLET_GAP = 10f
private const val LOG_ENTRY_GAP = 4f

/** Material-theme-matched colors (see `ui/theme/Color.kt`'s light scheme) used to style the PDF. */
private val accentColor = Color.parseColor("#8F4C38")
private val accentContainerColor = Color.parseColor("#FFDBD1")
private val onAccentContainerColor = Color.parseColor("#723523")
private val surfaceStripeColor = Color.parseColor("#FFF1ED")
private val onSurfaceColor = Color.parseColor("#231917")
private val onSurfaceVariantColor = Color.parseColor("#53433F")

/**
 * Draws [content] onto a paginated A4 PDF and writes it to [outputStream]; returns whether the
 * write succeeded. Intentionally not unit-tested: it only positions already-localized/formatted
 * text (see [sevynidd.diabetesapp.data.export.buildFactorsPdfReportContent]) using the
 * Android-framework [PdfDocument]/[Paint] APIs, which aren't available outside an Android runtime.
 */
fun renderFactorsPdfReport(content: FactorsPdfReportContent, outputStream: OutputStream): Boolean {
    return runCatching {
        val document = PdfDocument()
        val writer = PdfReportWriter(document)
        writer.drawHeader(content.title, content.generatedAtLabel)
        writer.drawFactorsTable(content)
        writer.drawLogSection(content)
        writer.finish()
        document.writeTo(outputStream)
        document.close()
    }.isSuccess
}

/** Lays out [FactorsPdfReportContent] top-to-bottom, starting a new A4 page whenever content would overflow. */
private class PdfReportWriter(private val document: PdfDocument) {
    private val titlePaint = Paint().apply {
        textSize = TITLE_TEXT_SIZE
        isFakeBoldText = true
        color = onAccentContainerColor
    }
    private val metaPaint = Paint().apply { textSize = META_TEXT_SIZE; color = onAccentContainerColor }
    private val sectionPaint = Paint().apply {
        textSize = SECTION_TEXT_SIZE
        isFakeBoldText = true
        color = accentColor
    }
    private val headerPaint = Paint().apply {
        textSize = BODY_TEXT_SIZE
        isFakeBoldText = true
        color = onAccentContainerColor
    }
    private val bodyPaint = Paint().apply { textSize = BODY_TEXT_SIZE; color = onSurfaceColor }
    private val footerPaint = Paint().apply {
        textSize = FOOTER_TEXT_SIZE
        color = onSurfaceVariantColor
        textAlign = Paint.Align.CENTER
    }
    private val bandFillPaint = Paint().apply { color = accentContainerColor; style = Paint.Style.FILL }
    private val stripeFillPaint = Paint().apply { color = surfaceStripeColor; style = Paint.Style.FILL }
    private val accentFillPaint = Paint().apply { color = accentColor; style = Paint.Style.FILL }
    private val rulePaint = Paint().apply { color = accentColor; strokeWidth = TABLE_HEADER_RULE_WIDTH }

    private var pageNumber = 0
    private var page: PdfDocument.Page = startPage()
    private var y = MARGIN + TITLE_TEXT_SIZE

    private fun startPage(): PdfDocument.Page {
        pageNumber++
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        return document.startPage(pageInfo)
    }

    private fun finishCurrentPage() {
        page.canvas.drawText(pageNumber.toString(), PAGE_WIDTH / 2f, PAGE_HEIGHT - FOOTER_MARGIN, footerPaint)
        document.finishPage(page)
    }

    private fun ensureSpace(height: Float) {
        if (y + height > PAGE_HEIGHT - MARGIN) {
            finishCurrentPage()
            page = startPage()
            y = MARGIN + LINE_HEIGHT
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
        ensureSpace(HEADER_BAND_HEIGHT)
        val bandTop = y - TITLE_TEXT_SIZE
        val bandBottom = bandTop + HEADER_BAND_HEIGHT
        page.canvas.drawRoundRect(
            RectF(MARGIN, bandTop, PAGE_WIDTH - MARGIN, bandBottom),
            HEADER_BAND_CORNER_RADIUS,
            HEADER_BAND_CORNER_RADIUS,
            bandFillPaint
        )
        y = bandTop + HEADER_BAND_PADDING + TITLE_TEXT_SIZE
        page.canvas.drawText(title, MARGIN + HEADER_BAND_PADDING, y, titlePaint)
        y += LINE_HEIGHT
        page.canvas.drawText(generatedAtLabel, MARGIN + HEADER_BAND_PADDING, y, metaPaint)
        y = bandBottom + SECTION_GAP
    }

    fun drawFactorsTable(content: FactorsPdfReportContent) {
        ensureSpace(LINE_HEIGHT)
        y = drawSectionTitle(page.canvas, y, content.factorsSectionTitle, sectionPaint, accentFillPaint)

        val tablePaints = TablePaints(bandFillPaint, headerPaint, bodyPaint, stripeFillPaint, rulePaint)

        ensureSpace(LINE_HEIGHT)
        y = drawTableHeaderRow(page.canvas, y, content, tablePaints)

        content.factorRows.forEachIndexed { index, row ->
            ensureSpace(LINE_HEIGHT)
            y = drawTableRow(page.canvas, y, row, index, tablePaints)
        }

        y += SECTION_GAP - LINE_HEIGHT
        ensureSpace(CALLOUT_HEIGHT)
        y = drawBasalRateCallout(page.canvas, y, content, bandFillPaint, headerPaint)
    }

    fun drawLogSection(content: FactorsPdfReportContent) {
        ensureSpace(LINE_HEIGHT)
        y = drawSectionTitle(page.canvas, y, content.logSectionTitle, sectionPaint, accentFillPaint)

        if (content.logEntries.isEmpty()) {
            drawLine(content.logEmptyLabel, bodyPaint)
            return
        }

        content.logEntries.forEach { entry ->
            ensureSpace(LINE_HEIGHT)
            val bulletY = y - BODY_TEXT_SIZE / 2
            page.canvas.drawCircle(MARGIN + LOG_BULLET_RADIUS, bulletY, LOG_BULLET_RADIUS, accentFillPaint)
            drawWrappedLines(
                "${entry.timestampLabel} - ${entry.description}",
                bodyPaint,
                x = MARGIN + LOG_BULLET_GAP,
                width = CONTENT_WIDTH - LOG_BULLET_GAP
            )
            y += LOG_ENTRY_GAP
        }
    }

    fun finish() {
        finishCurrentPage()
    }
}

/** Paints shared by the factors table's header row and data rows, grouped to keep their draw functions short. */
private class TablePaints(val band: Paint, val header: Paint, val body: Paint, val stripe: Paint, val rule: Paint)

/** Fills the row band from [y]'s baseline upward, used for table header/zebra-stripe backgrounds. */
private fun drawRowFill(canvas: Canvas, y: Float, paint: Paint) {
    canvas.drawRect(MARGIN, y - ROW_FILL_TOP_INSET, PAGE_WIDTH - MARGIN, y + ROW_FILL_BOTTOM_INSET, paint)
}

/** Draws a colored accent bar followed by bold section text at [y]; returns the next baseline y. */
private fun drawSectionTitle(canvas: Canvas, y: Float, text: String, sectionPaint: Paint, accentPaint: Paint): Float {
    canvas.drawRect(MARGIN, y - SECTION_TEXT_SIZE, MARGIN + SECTION_BAR_WIDTH, y, accentPaint)
    canvas.drawText(text, MARGIN + SECTION_BAR_WIDTH + SECTION_BAR_GAP, y, sectionPaint)
    return y + LINE_HEIGHT
}

/** Draws the shaded factor-table header row and its accent rule at [y]; returns the next baseline y. */
private fun drawTableHeaderRow(canvas: Canvas, y: Float, content: FactorsPdfReportContent, paints: TablePaints): Float {
    drawRowFill(canvas, y, paints.band)
    canvas.drawText(content.factorNameHeader, MARGIN, y, paints.header)
    canvas.drawText(content.factorValueHeader, MARGIN + VALUE_COLUMN_OFFSET, y, paints.header)
    canvas.drawText(content.factorTimeHeader, MARGIN + TIME_COLUMN_OFFSET, y, paints.header)
    val nextY = y + LINE_HEIGHT
    val ruleY = nextY - LINE_HEIGHT / 2
    canvas.drawLine(MARGIN, ruleY, PAGE_WIDTH - MARGIN, ruleY, paints.rule)
    return nextY
}

/** Draws one factor row at [y], zebra-striped on odd [index]s; returns the next baseline y. */
private fun drawTableRow(canvas: Canvas, y: Float, row: FactorReportRow, index: Int, paints: TablePaints): Float {
    if (index % 2 == 1) drawRowFill(canvas, y, paints.stripe)
    canvas.drawText(row.name, MARGIN, y, paints.body)
    canvas.drawText(row.value, MARGIN + VALUE_COLUMN_OFFSET, y, paints.body)
    canvas.drawText(row.timeRange, MARGIN + TIME_COLUMN_OFFSET, y, paints.body)
    return y + LINE_HEIGHT
}

/** Draws the basal-rate summary as a rounded callout starting at [y]; returns the next baseline y. */
private fun drawBasalRateCallout(
    canvas: Canvas,
    y: Float,
    content: FactorsPdfReportContent,
    bandPaint: Paint,
    headerPaint: Paint
): Float {
    val calloutBottom = y + CALLOUT_HEIGHT
    canvas.drawRoundRect(
        RectF(MARGIN, y, PAGE_WIDTH - MARGIN, calloutBottom),
        CALLOUT_CORNER_RADIUS,
        CALLOUT_CORNER_RADIUS,
        bandPaint
    )
    val textY = y + CALLOUT_PADDING + BODY_TEXT_SIZE
    val calloutText = "${content.basalRateLabel}: ${content.basalRateSummary}"
    canvas.drawText(calloutText, MARGIN + CALLOUT_PADDING, textY, headerPaint)
    return calloutBottom + SECTION_GAP
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
