package sevynidd.diabetesapp.data.export

import sevynidd.diabetesapp.calculation.MINUTES_PER_DAY
import sevynidd.diabetesapp.calculation.describe
import sevynidd.diabetesapp.calculation.formatTimeOfDay
import sevynidd.diabetesapp.calculation.formatUnits
import sevynidd.diabetesapp.calculation.pumpBasalDailyTotal
import sevynidd.diabetesapp.calculation.pumpBasalHourRangeLabel
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val EXPORT_FILE_NAME_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val REPORT_GENERATED_AT_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
private val REPORT_LOG_TIMESTAMP_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

/** Builds the suggested export file name for the PDF factor report, embedding [date] like [factorsExportFileName]. */
fun factorsPdfReportFileName(date: LocalDate): String =
    "bolus-manager-factor-report-${date.format(EXPORT_FILE_NAME_DATE_FORMATTER)}.pdf"

/** One row of the "current factors" table in [FactorsPdfReportContent]: a factor's name, value, and its time window. */
data class FactorReportRow(val name: String, val value: String, val timeRange: String)

/** One row of the change-log section in [FactorsPdfReportContent]: a formatted timestamp and its description. */
data class PdfLogEntry(val timestampLabel: String, val description: String)

/** One row of the hourly pump basal table: the hour's time range and its rate in U/h (`""` = not set). */
data class PumpBasalReportRow(val timeRange: String, val rate: String)

/**
 * The insulin-pump basal section of [FactorsPdfReportContent]: all 24 hourly rates and the daily
 * total line, already localized.
 */
data class PumpBasalReportSection(
    val title: String,
    val timeHeader: String,
    val rateHeader: String,
    val rows: List<PumpBasalReportRow>,
    val dailyTotalLabel: String
)

/**
 * What the PDF report is built from: the current [factors], the full [auditLog], and the
 * [therapyMode], which decides whether the basal rate is reported as the pen's daily dose or as
 * the pump's hourly rates.
 */
data class FactorsPdfReportData(
    val factors: FactorsData,
    val auditLog: List<FactorAuditLogEntry>,
    val therapyMode: TherapyMode = TherapyMode.Pen
)

/**
 * Everything a PDF renderer needs to lay out the "current factors" report, already localized and
 * formatted so the renderer itself does no locale- or domain-specific decision-making — only
 * positioning text on a page. Built by [buildFactorsPdfReportContent].
 */
data class FactorsPdfReportContent(
    val title: String,
    val generatedAtLabel: String,
    val factorsSectionTitle: String,
    val factorNameHeader: String,
    val factorValueHeader: String,
    val factorTimeHeader: String,
    val factorRows: List<FactorReportRow>,
    val basalRateLabel: String,
    val basalRateSummary: String,
    val pumpBasal: PumpBasalReportSection?,
    val logSectionTitle: String,
    val logEmptyLabel: String,
    val logEntries: List<PdfLogEntry>
)

/**
 * Assembles the localized content of the "current factors" PDF report: a table of every factor's
 * name, value, and active time window (see [FactorSlot]'s implicit-end-time convention), the
 * basal rate (for pump therapy: all 24 hourly rates plus their daily total, see
 * [pumpBasalDailyTotal]), and the full chronological change log — everything needed to hand a
 * printable snapshot to an endocrinologist. [zone] resolves each audit-log entry's stored UTC
 * timestamp and [generatedAt] to local date/time; both default to the device's current zone/time.
 */
fun buildFactorsPdfReportContent(
    report: FactorsPdfReportData,
    language: AppLanguage,
    zone: ZoneId = ZoneId.systemDefault(),
    generatedAt: LocalDateTime = LocalDateTime.now(zone)
): FactorsPdfReportContent {
    val factors = report.factors
    val auditLog = report.auditLog
    val slots = factors.factorSlots
    val factorRows = slots.indices.map { index ->
        FactorReportRow(
            name = slots[index].name,
            value = slots[index].factorValue,
            timeRange = slots.timeRangeLabel(index)
        )
    }

    val basalRateSummary = translate(TranslationKey.PdfBasalRateSummary, language)
        .replacePlaceholder(FIRST_PLACEHOLDER, factors.basalRate)
        .replacePlaceholder(SECOND_PLACEHOLDER, formatTimeOfDay(factors.basalTimeMinutes))

    val logEntries = auditLog.sortedBy { it.timestampMillis }.map { entry ->
        PdfLogEntry(
            timestampLabel = entry.timestampMillis.toReportTimestampLabel(zone),
            description = entry.describe(language)
        )
    }

    return FactorsPdfReportContent(
        title = translate(TranslationKey.PdfReportTitle, language),
        generatedAtLabel = translate(TranslationKey.PdfGeneratedOnLabel, language)
            .replacePlaceholder(FIRST_PLACEHOLDER, generatedAt.format(REPORT_GENERATED_AT_FORMATTER)),
        factorsSectionTitle = translate(TranslationKey.FactorsListTitle, language),
        factorNameHeader = translate(TranslationKey.FactorNameLabel, language),
        factorValueHeader = translate(TranslationKey.LabelFactor, language),
        factorTimeHeader = translate(TranslationKey.PdfFactorTimeWindowHeader, language),
        factorRows = factorRows,
        basalRateLabel = translate(TranslationKey.BasalRate, language),
        basalRateSummary = basalRateSummary,
        pumpBasal = factors.pumpBasalRates
            .takeIf { report.therapyMode == TherapyMode.Pump }
            ?.let { buildPumpBasalReportSection(it, language) },
        logSectionTitle = translate(TranslationKey.DocumentationSectionTitle, language),
        logEmptyLabel = translate(TranslationKey.DocumentationEmptyState, language),
        logEntries = logEntries
    )
}

private const val FIRST_PLACEHOLDER = $$"%1$s"
private const val SECOND_PLACEHOLDER = $$"%2$s"
private const val NOT_SET_LABEL = "-"

private fun buildPumpBasalReportSection(hourlyRates: List<String>, language: AppLanguage): PumpBasalReportSection {
    val total = pumpBasalDailyTotal(hourlyRates)?.let(::formatUnits) ?: NOT_SET_LABEL
    return PumpBasalReportSection(
        title = translate(TranslationKey.PumpBasalRatesTitle, language),
        timeHeader = translate(TranslationKey.PdfFactorTimeWindowHeader, language),
        rateHeader = translate(TranslationKey.PumpBasalRateUnit, language),
        rows = hourlyRates.mapIndexed { hour, rate ->
            PumpBasalReportRow(timeRange = pumpBasalHourRangeLabel(hour), rate = rate.ifEmpty { NOT_SET_LABEL })
        },
        dailyTotalLabel = translate(TranslationKey.PumpBasalDailyTotal, language)
            .replacePlaceholder(FIRST_PLACEHOLDER, total)
    )
}

private fun Long.toReportTimestampLabel(zone: ZoneId): String =
    Instant.ofEpochMilli(this).atZone(zone).format(REPORT_LOG_TIMESTAMP_FORMATTER)

private fun String.replacePlaceholder(placeholder: String, value: String): String = replace(placeholder, value)

/** The label for the slot at [index]'s active window, running until the next slot (see [FactorSlot]). */
private fun List<FactorSlot>.timeRangeLabel(index: Int): String {
    if (isEmpty()) return ""
    val nextStart = this[(index + 1) % size].startTimeMinutes
    val endMinutes = ((nextStart - 1) + MINUTES_PER_DAY) % MINUTES_PER_DAY
    return "${formatTimeOfDay(this[index].startTimeMinutes)} - ${formatTimeOfDay(endMinutes)}"
}
