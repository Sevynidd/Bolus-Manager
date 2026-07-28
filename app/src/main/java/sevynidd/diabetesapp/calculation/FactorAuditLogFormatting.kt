package sevynidd.diabetesapp.calculation

import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.util.Locale

private const val MINUTES_PER_HOUR = 60
private const val HOURS_PER_DAY = 24
private const val DECIMAL_PATTERN = "%.2f"
private const val TIME_LABEL_PATTERN = "%02d:%02d"

/** One historical value of a factor or the basal rate at a point in time, for charting. */
data class FactorHistoryPoint(val timestampMillis: Long, val value: Double)

/** A single factor's (or, when [factorName] is `null`, the basal rate's) value history, oldest first. */
data class FactorHistorySeries(val factorName: String?, val points: List<FactorHistoryPoint>)

/** Builds the localized, human-readable description of one [FactorAuditLogEntry]. */
fun FactorAuditLogEntry.describe(language: AppLanguage): String {
    val (templateKey, args) = templateAndArgs(language)
    return if (templateKey == null) {
        translate(TranslationKey.AuditUnknownChange, language)
    } else {
        applyTemplate(translate(templateKey, language), args)
    }
}

/** Substitutes `%1$s`, `%2$s`, ... placeholders in [template] with [args] by position, without a spread call. */
private fun applyTemplate(template: String, args: List<String>): String {
    return args.foldIndexed(template) { index, text, arg -> text.replace("%${index + 1}\$s", arg) }
}

private fun FactorAuditLogEntry.templateAndArgs(language: AppLanguage): Pair<TranslationKey?, List<String>> {
    val name = factorName.orEmpty()
    return when (changeType.toFactorChangeType()) {
        FactorChangeType.FACTOR_ADDED ->
            TranslationKey.AuditFactorAdded to listOf(name, newValue.toDisplayValue(language))

        FactorChangeType.FACTOR_VALUE_CHANGED ->
            TranslationKey.AuditFactorValueChanged to
                listOf(name, oldValue.toDisplayValue(language), newValue.toDisplayValue(language))

        FactorChangeType.FACTOR_TIME_CHANGED ->
            TranslationKey.AuditFactorTimeChanged to
                listOf(name, oldTimeMinutes.toTimeLabel(language), newTimeMinutes.toTimeLabel(language))

        FactorChangeType.FACTOR_DELETED ->
            TranslationKey.AuditFactorDeleted to listOf(name, oldValue.toDisplayValue(language))

        FactorChangeType.BASAL_RATE_CHANGED ->
            TranslationKey.AuditBasalRateChanged to
                listOf(oldValue.toDisplayValue(language), newValue.toDisplayValue(language))

        FactorChangeType.BASAL_TIME_CHANGED ->
            TranslationKey.AuditBasalTimeChanged to
                listOf(oldTimeMinutes.toTimeLabel(language), newTimeMinutes.toTimeLabel(language))

        null -> null to emptyList()
    }
}

private fun String.toFactorChangeType(): FactorChangeType? {
    return FactorChangeType.entries.firstOrNull { it.name == this }
}

private fun Double?.toDisplayValue(language: AppLanguage): String {
    return when {
        this == null -> translate(TranslationKey.AuditValueNotSet, language)
        this % 1.0 == 0.0 -> toInt().toString()
        else -> String.format(Locale.US, DECIMAL_PATTERN, this)
            .replace('.', ',')
            .trimEnd('0')
            .trimEnd(',')
    }
}

private fun Int?.toTimeLabel(language: AppLanguage): String {
    if (this == null) return translate(TranslationKey.AuditValueNotSet, language)

    val hours = (this / MINUTES_PER_HOUR) % HOURS_PER_DAY
    val minutes = this % MINUTES_PER_HOUR
    return String.format(Locale.ROOT, TIME_LABEL_PATTERN, hours, minutes)
}

/**
 * Reconstructs each factor's (and the basal rate's) value history from [entries], oldest first, so
 * it can be charted. Only additions and value changes carry a value; deletions and time-only
 * changes don't contribute a data point.
 */
fun factorHistorySeries(entries: List<FactorAuditLogEntry>): List<FactorHistorySeries> {
    val chronological = entries.sortedBy { it.timestampMillis }
    val basalPoints = mutableListOf<FactorHistoryPoint>()
    val factorPointsByName = linkedMapOf<String, MutableList<FactorHistoryPoint>>()

    for (entry in chronological) {
        when (entry.changeType.toFactorChangeType()) {
            FactorChangeType.FACTOR_ADDED, FactorChangeType.FACTOR_VALUE_CHANGED -> {
                val name = entry.factorName
                val value = entry.newValue
                if (name != null && value != null) {
                    val point = FactorHistoryPoint(entry.timestampMillis, value)
                    factorPointsByName.getOrPut(name) { mutableListOf() } += point
                }
            }

            FactorChangeType.BASAL_RATE_CHANGED -> {
                entry.newValue?.let { basalPoints += FactorHistoryPoint(entry.timestampMillis, it) }
            }

            else -> Unit
        }
    }

    val factorSeries = factorPointsByName.map { (name, points) -> FactorHistorySeries(name, points) }
    return if (basalPoints.isEmpty()) factorSeries else factorSeries + FactorHistorySeries(null, basalPoints)
}
