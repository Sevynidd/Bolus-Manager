package sevynidd.diabetesapp.data.export

import sevynidd.diabetesapp.calculation.describe
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val CSV_DELIMITER = ";"
private val EXPORT_FILE_NAME_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val CSV_TIMESTAMP_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

/** Builds the suggested export file name for the change log, embedding [date] like [factorsExportFileName]. */
fun auditLogExportFileName(date: LocalDate): String =
    "bolus-manager-factor-log-${date.format(EXPORT_FILE_NAME_DATE_FORMATTER)}.csv"

/**
 * Serializes this audit log to a semicolon-delimited CSV (the delimiter the app's own
 * comma-decimal number convention doesn't collide with), one row per entry with a localized,
 * human-readable description — suitable for sharing with an endocrinologist. [zone] resolves each
 * entry's stored UTC timestamp to a local date/time; defaults to the device's current zone.
 */
fun List<FactorAuditLogEntry>.toAuditLogCsv(language: AppLanguage, zone: ZoneId = ZoneId.systemDefault()): String {
    val header = listOf(
        translate(TranslationKey.AuditLogCsvDateHeader, language),
        translate(TranslationKey.AuditLogCsvDescriptionHeader, language)
    ).joinToString(CSV_DELIMITER) { it.toCsvField() }

    val rows = map { entry ->
        val timestamp = Instant.ofEpochMilli(entry.timestampMillis).atZone(zone).format(CSV_TIMESTAMP_FORMATTER)
        listOf(timestamp, entry.describe(language)).joinToString(CSV_DELIMITER) { it.toCsvField() }
    }

    return (listOf(header) + rows).joinToString("\n")
}

private fun String.toCsvField(): String {
    return if (contains(CSV_DELIMITER) || contains('"') || contains('\n')) {
        "\"${replace("\"", "\"\"")}\""
    } else {
        this
    }
}
