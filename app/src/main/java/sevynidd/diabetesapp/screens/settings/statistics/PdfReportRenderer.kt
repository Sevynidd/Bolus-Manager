package sevynidd.diabetesapp.screens.settings.statistics

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import sevynidd.diabetesapp.data.export.FactorsPdfReportContent
import java.io.OutputStream

private const val PAGE_WIDTH = 595
private const val PAGE_HEIGHT = 842
private const val MARGIN = 40f
private const val TITLE_TEXT_SIZE = 20f
private const val META_TEXT_SIZE = 10f
private const val SECTION_TEXT_SIZE = 14f
private const val BODY_TEXT_SIZE = 11f
private const val LINE_HEIGHT = 16f
private const val SECTION_GAP = 22f
private const val VALUE_COLUMN_OFFSET = 220f
private const val TIME_COLUMN_OFFSET = 320f
private const val CONTENT_WIDTH = PAGE_WIDTH - 2 * MARGIN

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
    private val titlePaint = Paint().apply { textSize = TITLE_TEXT_SIZE; isFakeBoldText = true }
    private val metaPaint = Paint().apply { textSize = META_TEXT_SIZE; color = Color.DKGRAY }
    private val sectionPaint = Paint().apply { textSize = SECTION_TEXT_SIZE; isFakeBoldText = true }
    private val headerPaint = Paint().apply { textSize = BODY_TEXT_SIZE; isFakeBoldText = true }
    private val bodyPaint = Paint().apply { textSize = BODY_TEXT_SIZE }
    private val rulePaint = Paint().apply { color = Color.LTGRAY }

    private var pageNumber = 0
    private var page: PdfDocument.Page = startPage()
    private var y = MARGIN + TITLE_TEXT_SIZE

    private fun startPage(): PdfDocument.Page {
        pageNumber++
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        return document.startPage(pageInfo)
    }

    private fun ensureSpace(height: Float) {
        if (y + height > PAGE_HEIGHT - MARGIN) {
            document.finishPage(page)
            page = startPage()
            y = MARGIN + LINE_HEIGHT
        }
    }

    private fun drawLine(text: String, paint: Paint, x: Float = MARGIN) {
        ensureSpace(LINE_HEIGHT)
        page.canvas.drawText(text, x, y, paint)
        y += LINE_HEIGHT
    }

    private fun drawWrappedLines(text: String, paint: Paint) {
        wrapToWidth(text, paint, CONTENT_WIDTH).forEach { line -> drawLine(line, paint) }
    }

    private fun drawRule() {
        ensureSpace(LINE_HEIGHT / 2)
        page.canvas.drawLine(MARGIN, y - LINE_HEIGHT / 2, PAGE_WIDTH - MARGIN, y - LINE_HEIGHT / 2, rulePaint)
    }

    fun drawHeader(title: String, generatedAtLabel: String) {
        drawLine(title, titlePaint)
        drawLine(generatedAtLabel, metaPaint)
        y += SECTION_GAP - LINE_HEIGHT
    }

    fun drawFactorsTable(content: FactorsPdfReportContent) {
        drawLine(content.factorsSectionTitle, sectionPaint)
        ensureSpace(LINE_HEIGHT)
        page.canvas.drawText(content.factorNameHeader, MARGIN, y, headerPaint)
        page.canvas.drawText(content.factorValueHeader, MARGIN + VALUE_COLUMN_OFFSET, y, headerPaint)
        page.canvas.drawText(content.factorTimeHeader, MARGIN + TIME_COLUMN_OFFSET, y, headerPaint)
        y += LINE_HEIGHT
        drawRule()

        content.factorRows.forEach { row ->
            ensureSpace(LINE_HEIGHT)
            page.canvas.drawText(row.name, MARGIN, y, bodyPaint)
            page.canvas.drawText(row.value, MARGIN + VALUE_COLUMN_OFFSET, y, bodyPaint)
            page.canvas.drawText(row.timeRange, MARGIN + TIME_COLUMN_OFFSET, y, bodyPaint)
            y += LINE_HEIGHT
        }

        y += SECTION_GAP - LINE_HEIGHT
        drawLine("${content.basalRateLabel}: ${content.basalRateSummary}", bodyPaint)
        y += SECTION_GAP - LINE_HEIGHT
    }

    fun drawLogSection(content: FactorsPdfReportContent) {
        drawLine(content.logSectionTitle, sectionPaint)
        if (content.logEntries.isEmpty()) {
            drawLine(content.logEmptyLabel, bodyPaint)
            return
        }
        content.logEntries.forEach { entry ->
            drawWrappedLines("${entry.timestampLabel} - ${entry.description}", bodyPaint)
        }
    }

    fun finish() {
        document.finishPage(page)
    }
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
