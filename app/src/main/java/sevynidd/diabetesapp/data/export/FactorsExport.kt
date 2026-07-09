package sevynidd.diabetesapp.data.export

import sevynidd.diabetesapp.data.model.FactorsData

/**
 * Schema version of the JSON produced by [toExportJson]. Bumped whenever the exported field set
 * changes in a way that isn't backward compatible; [parseFactorsExportJson] rejects any other
 * version rather than guessing at a migration.
 */
private const val EXPORT_SCHEMA_VERSION = 1

/** Serializes this factor profile (correction factors, time windows, basal rate) to portable JSON. */
fun FactorsData.toExportJson(): String {
    val entries = listOf(
        "schemaVersion" to EXPORT_SCHEMA_VERSION.toString(),
        "isPeriodEnabled" to isPeriodEnabled.toString(),
        "basalReminderEnabled" to basalReminderEnabled.toString(),
        "morningFactor" to morningFactor.toJsonStringLiteral(),
        "breakfastFactor" to breakfastFactor.toJsonStringLiteral(),
        "lunchFactor" to lunchFactor.toJsonStringLiteral(),
        "afternoonFactor" to afternoonFactor.toJsonStringLiteral(),
        "dinnerFactor" to dinnerFactor.toJsonStringLiteral(),
        "lateFactor" to lateFactor.toJsonStringLiteral(),
        "nightFactor" to nightFactor.toJsonStringLiteral(),
        "basalRate" to basalRate.toJsonStringLiteral(),
        "morningTimeMinutes" to morningTimeMinutes.toString(),
        "breakfastTimeMinutes" to breakfastTimeMinutes.toString(),
        "lunchTimeMinutes" to lunchTimeMinutes.toString(),
        "afternoonTimeMinutes" to afternoonTimeMinutes.toString(),
        "dinnerTimeMinutes" to dinnerTimeMinutes.toString(),
        "lateTimeMinutes" to lateTimeMinutes.toString(),
        "nightTimeMinutes" to nightTimeMinutes.toString(),
        "basalTimeMinutes" to basalTimeMinutes.toString()
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
    val fields = runCatching { parseFlatJsonObject(json) }.getOrNull()
    if (fields == null || fields["schemaVersion"] != EXPORT_SCHEMA_VERSION.toString()) return null

    return runCatching {
        FactorsData(
            isPeriodEnabled = fields.requireBoolean("isPeriodEnabled"),
            morningFactor = fields.requireJsonString("morningFactor"),
            breakfastFactor = fields.requireJsonString("breakfastFactor"),
            lunchFactor = fields.requireJsonString("lunchFactor"),
            afternoonFactor = fields.requireJsonString("afternoonFactor"),
            dinnerFactor = fields.requireJsonString("dinnerFactor"),
            lateFactor = fields.requireJsonString("lateFactor"),
            nightFactor = fields.requireJsonString("nightFactor"),
            basalRate = fields.requireJsonString("basalRate"),
            morningTimeMinutes = fields.requireInt("morningTimeMinutes"),
            breakfastTimeMinutes = fields.requireInt("breakfastTimeMinutes"),
            lunchTimeMinutes = fields.requireInt("lunchTimeMinutes"),
            afternoonTimeMinutes = fields.requireInt("afternoonTimeMinutes"),
            dinnerTimeMinutes = fields.requireInt("dinnerTimeMinutes"),
            lateTimeMinutes = fields.requireInt("lateTimeMinutes"),
            nightTimeMinutes = fields.requireInt("nightTimeMinutes"),
            basalTimeMinutes = fields.requireInt("basalTimeMinutes"),
            basalReminderEnabled = fields.requireBoolean("basalReminderEnabled")
        )
    }.getOrNull()
}

private fun String.toJsonStringLiteral(): String {
    val escaped = replace("\\", "\\\\").replace("\"", "\\\"")
    return "\"$escaped\""
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
    return raw.substring(1, raw.length - 1).unescapeJsonString()
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

private fun String.unescapeJsonString(): String {
    val builder = StringBuilder(length)
    var index = 0
    while (index < length) {
        val current = this[index]
        if (current == '\\' && index + 1 < length) {
            when (this[index + 1]) {
                '"' -> builder.append('"')
                '\\' -> builder.append('\\')
                else -> builder.append(current).append(this[index + 1])
            }
            index += 2
        } else {
            builder.append(current)
            index++
        }
    }
    return builder.toString()
}
