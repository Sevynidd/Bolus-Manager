package sevynidd.diabetesapp.data.export

import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.data.settings.profile.Gender
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Schema version of the JSON produced by [toExportJson]. Bumped whenever the exported field set
 * changes in a way that isn't backward compatible; [parseFactorsExportJson] rejects any other
 * version rather than guessing at a migration. Version 2 replaced the 7 fixed named factor
 * fields with a variable-length `"factors"` array (see [FactorSlot]). Version 3 added the
 * calculation-relevant app settings that live outside the Room-backed factor profile (bread
 * units, Period surcharge, correction threshold/step/unit, gender) so a restore doesn't silently
 * drop them. Version 4 added the low correction threshold used to subtract insulin below target
 * — v1 through v3 exports are rejected outright rather than upgraded.
 */
private const val EXPORT_SCHEMA_VERSION = 4

private val EXPORT_FILE_NAME_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

/** Builds the suggested export file name, embedding [date] so repeated exports don't overwrite each other. */
fun factorsExportFileName(date: LocalDate): String =
    "bolus-manager-factors-${date.format(EXPORT_FILE_NAME_DATE_FORMATTER)}.json"

/**
 * Everything [toExportJson] serializes: the Room-backed factor profile (correction factors, time
 * windows, basal rate) plus the calculation-relevant app settings that are persisted separately
 * in DataStore but feed the same bolus/correction calculations, so they travel together in a
 * backup/device-transfer.
 */
data class FactorsExportBundle(
    val factors: FactorsData,
    val breadUnits: Double,
    val periodFactorPercent: Double,
    val correctionSettings: CorrectionSettings,
    val gender: Gender
)

/** Serializes this factor profile and its associated calculation settings to portable JSON. */
fun FactorsExportBundle.toExportJson(): String {
    val factorsArray = factors.factorSlots.joinToString(separator = ",\n", prefix = "[\n", postfix = "\n  ]") { slot ->
        "    {\"name\": ${slot.name.toJsonStringLiteral()}, " +
            "\"factorValue\": ${slot.factorValue.toJsonStringLiteral()}, " +
            "\"startTimeMinutes\": ${slot.startTimeMinutes}}"
    }

    val entries = listOf(
        "schemaVersion" to EXPORT_SCHEMA_VERSION.toString(),
        "isPeriodEnabled" to factors.isPeriodEnabled.toString(),
        "basalReminderEnabled" to factors.basalReminderEnabled.toString(),
        "basalRate" to factors.basalRate.toJsonStringLiteral(),
        "basalTimeMinutes" to factors.basalTimeMinutes.toString(),
        "breadUnits" to breadUnits.toString(),
        "periodFactorPercent" to periodFactorPercent.toString(),
        "correctionThresholdMgDl" to correctionSettings.thresholdMgDl.toString(),
        "correctionLowThresholdMgDl" to correctionSettings.lowThresholdMgDl.toString(),
        "correctionStepMgDl" to correctionSettings.stepMgDl.toString(),
        "glucoseUnit" to correctionSettings.glucoseUnit.name.toJsonStringLiteral(),
        "gender" to gender.name.toJsonStringLiteral(),
        "factors" to factorsArray
    )
    return entries.joinToString(separator = ",\n", prefix = "{\n", postfix = "\n}") { (key, value) ->
        "  \"$key\": $value"
    }
}

/**
 * Parses JSON previously produced by [FactorsExportBundle.toExportJson] back into a [FactorsExportBundle].
 * Returns `null` for anything that isn't a recognizable export of the current [EXPORT_SCHEMA_VERSION]
 * (malformed JSON, missing fields, unreadable numbers) so callers can show one generic import-failed
 * message instead of handling a list of distinct parse-error types.
 */
fun parseFactorsExportJson(json: String): FactorsExportBundle? {
    val extracted = extractFactorsArray(json)
    val fields = extracted?.let { runCatching { parseFlatJsonObject(it.remainingJson) }.getOrNull() }
    if (extracted == null || fields == null || fields["schemaVersion"] != EXPORT_SCHEMA_VERSION.toString()) {
        return null
    }

    return runCatching {
        FactorsExportBundle(
            factors = FactorsData(
                isPeriodEnabled = fields.requireBoolean("isPeriodEnabled"),
                factorSlots = parseFactorSlotArray(extracted.arrayJson),
                basalRate = fields.requireJsonString("basalRate"),
                basalTimeMinutes = fields.requireInt("basalTimeMinutes"),
                basalReminderEnabled = fields.requireBoolean("basalReminderEnabled")
            ),
            breadUnits = fields.requireDouble("breadUnits"),
            periodFactorPercent = fields.requireDouble("periodFactorPercent"),
            correctionSettings = CorrectionSettings(
                thresholdMgDl = fields.requireDouble("correctionThresholdMgDl"),
                lowThresholdMgDl = fields.requireDouble("correctionLowThresholdMgDl"),
                stepMgDl = fields.requireDouble("correctionStepMgDl"),
                glucoseUnit = fields.requireEnum<GlucoseUnit>("glucoseUnit")
            ),
            gender = fields.requireEnum<Gender>("gender")
        )
    }.getOrNull()
}

private fun String.toJsonStringLiteral(): String {
    val escaped = replace("\\", "\\\\").replace("\"", "\\\"")
    return "\"$escaped\""
}

private data class ExtractedFactorsArray(val remainingJson: String, val arrayJson: String)

private val FactorsArrayKeyRegex = Regex("\"factors\"\\s*:\\s*\\[")

/**
 * Splits [json] into its `"factors": [...]` array value and everything else (the flat scalar
 * fields), so each half can go through its own simple parser. The array's matching closing
 * bracket is found with a depth-counting scan that skips bracket-like characters inside quoted
 * strings, so this is robust even if a factor's own name happens to contain `[`/`]` — a naive
 * non-greedy regex would break on that. Returns `null` if no `"factors"` array is found at all.
 */
private fun extractFactorsArray(json: String): ExtractedFactorsArray? {
    fun findMatchingBracket(openIndex: Int): Int? {
        var depth = 0
        var inString = false
        var index = openIndex
        while (index < json.length) {
            val current = json[index]
            when {
                current == '"' && (index == 0 || json[index - 1] != '\\') -> inString = !inString
                !inString && current == '[' -> depth++
                !inString && current == ']' -> {
                    depth--
                    if (depth == 0) return index
                }
            }
            index++
        }
        return null
    }

    val keyMatch = FactorsArrayKeyRegex.find(json) ?: return null
    val arrayStart = keyMatch.range.last
    val arrayEnd = findMatchingBracket(arrayStart)

    return arrayEnd?.let {
        ExtractedFactorsArray(
            remainingJson = json.removeRange(keyMatch.range.first, it + 1),
            arrayJson = json.substring(arrayStart, it + 1)
        )
    }
}

private val FactorObjectRegex = Regex("\\{[^{}]*\\}")

/** Parses a `"factors"` array value (each entry a flat object — never nested) into [FactorSlot]s. */
private fun parseFactorSlotArray(json: String): List<FactorSlot> {
    val trimmed = json.trim()
    require(trimmed.startsWith("[") && trimmed.endsWith("]")) { "Not a JSON array" }

    val objects = FactorObjectRegex.findAll(trimmed).map { it.value }.toList()
    require(objects.isNotEmpty()) { "No factor slots found" }

    return objects.map { objectText ->
        val fields = parseFlatJsonObject(objectText)
        FactorSlot(
            name = fields.requireJsonString("name"),
            factorValue = fields.requireJsonString("factorValue"),
            startTimeMinutes = fields.requireInt("startTimeMinutes")
        )
    }
}

