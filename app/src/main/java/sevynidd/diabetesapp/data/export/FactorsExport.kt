package sevynidd.diabetesapp.data.export

import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Schema version of the JSON produced by [toExportJson]. Bumped whenever the exported field set
 * changes in a way that isn't backward compatible; [parseFactorsExportJson] rejects any other
 * version rather than guessing at a migration. Version 2 replaced the 7 fixed named factor
 * fields with a variable-length `"factors"` array (see [FactorSlot]) — v1 exports are rejected
 * outright rather than upgraded.
 */
private const val EXPORT_SCHEMA_VERSION = 2

private val EXPORT_FILE_NAME_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

/** Builds the suggested export file name, embedding [date] so repeated exports don't overwrite each other. */
fun factorsExportFileName(date: LocalDate): String =
    "bolus-manager-factors-${date.format(EXPORT_FILE_NAME_DATE_FORMATTER)}.json"

/** Serializes this factor profile (correction factors, time windows, basal rate) to portable JSON. */
fun FactorsData.toExportJson(): String {
    val factorsArray = factorSlots.joinToString(separator = ",\n", prefix = "[\n", postfix = "\n  ]") { slot ->
        "    {\"name\": ${slot.name.toJsonStringLiteral()}, " +
            "\"factorValue\": ${slot.factorValue.toJsonStringLiteral()}, " +
            "\"startTimeMinutes\": ${slot.startTimeMinutes}}"
    }

    val entries = listOf(
        "schemaVersion" to EXPORT_SCHEMA_VERSION.toString(),
        "isPeriodEnabled" to isPeriodEnabled.toString(),
        "basalReminderEnabled" to basalReminderEnabled.toString(),
        "basalRate" to basalRate.toJsonStringLiteral(),
        "basalTimeMinutes" to basalTimeMinutes.toString(),
        "factors" to factorsArray
    )
    return entries.joinToString(separator = ",\n", prefix = "{\n", postfix = "\n}") { (key, value) ->
        "  \"$key\": $value"
    }
}

/**
 * Parses JSON previously produced by [FactorsData.toExportJson] back into a [FactorsData].
 * Returns `null` for anything that isn't a recognizable export of the current [EXPORT_SCHEMA_VERSION]
 * (malformed JSON, missing fields, unreadable numbers) so callers can show one generic import-failed
 * message instead of handling a list of distinct parse-error types.
 */
fun parseFactorsExportJson(json: String): FactorsData? {
    val extracted = extractFactorsArray(json)
    val fields = extracted?.let { runCatching { parseFlatJsonObject(it.remainingJson) }.getOrNull() }
    if (extracted == null || fields == null || fields["schemaVersion"] != EXPORT_SCHEMA_VERSION.toString()) {
        return null
    }

    return runCatching {
        FactorsData(
            isPeriodEnabled = fields.requireBoolean("isPeriodEnabled"),
            factorSlots = parseFactorSlotArray(extracted.arrayJson),
            basalRate = fields.requireJsonString("basalRate"),
            basalTimeMinutes = fields.requireInt("basalTimeMinutes"),
            basalReminderEnabled = fields.requireBoolean("basalReminderEnabled")
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

private val JsonEntryRegex = Regex("\"(\\w+)\"\\s*:\\s*(\"(?:[^\"\\\\]|\\\\.)*\"|[^,}\\s][^,}]*)")

/** Extracts top-level `"key": value` pairs from a flat (non-nested) JSON object. */
private fun parseFlatJsonObject(json: String): Map<String, String> {
    val trimmed = json.trim()
    require(trimmed.startsWith("{") && trimmed.endsWith("}")) { "Not a JSON object" }

    val matches = JsonEntryRegex.findAll(trimmed).associate { match ->
        match.groupValues[1] to match.groupValues[2].trim()
    }
    require(matches.isNotEmpty()) { "No fields found" }
    return matches
}

private fun Map<String, String>.requireJsonString(key: String): String {
    val raw = getValue(key)
    require(raw.length >= 2 && raw.startsWith("\"") && raw.endsWith("\"")) { "Expected string for $key" }

    val unescaped = raw.substring(1, raw.length - 1)
    val builder = StringBuilder(unescaped.length)
    var index = 0
    while (index < unescaped.length) {
        val current = unescaped[index]
        if (current == '\\' && index + 1 < unescaped.length) {
            when (unescaped[index + 1]) {
                '"' -> builder.append('"')
                '\\' -> builder.append('\\')
                else -> builder.append(current).append(unescaped[index + 1])
            }
            index += 2
        } else {
            builder.append(current)
            index++
        }
    }
    return builder.toString()
}

private fun Map<String, String>.requireBoolean(key: String): Boolean {
    return when (getValue(key)) {
        "true" -> true
        "false" -> false
        else -> error("Expected boolean for $key")
    }
}

private fun Map<String, String>.requireInt(key: String): Int {
    return getValue(key).toInt()
}
