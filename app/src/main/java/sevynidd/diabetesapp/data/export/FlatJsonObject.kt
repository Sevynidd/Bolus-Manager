package sevynidd.diabetesapp.data.export

private val JsonEntryRegex = Regex("\"(\\w+)\"\\s*:\\s*(\"(?:[^\"\\\\]|\\\\.)*\"|[^,}\\s][^,}]*)")

/** Extracts top-level `"key": value` pairs from a flat (non-nested) JSON object. */
internal fun parseFlatJsonObject(json: String): Map<String, String> {
    val trimmed = json.trim()
    require(trimmed.startsWith("{") && trimmed.endsWith("}")) { "Not a JSON object" }

    val matches = JsonEntryRegex.findAll(trimmed).associate { match ->
        match.groupValues[1] to match.groupValues[2].trim()
    }
    require(matches.isNotEmpty()) { "No fields found" }
    return matches
}

internal fun Map<String, String>.requireJsonString(key: String): String {
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

internal fun Map<String, String>.requireBoolean(key: String): Boolean {
    return when (getValue(key)) {
        "true" -> true
        "false" -> false
        else -> error("Expected boolean for $key")
    }
}

internal fun Map<String, String>.requireInt(key: String): Int {
    return getValue(key).toInt()
}

internal fun Map<String, String>.requireDouble(key: String): Double {
    return getValue(key).toDouble()
}

internal inline fun <reified T : Enum<T>> Map<String, String>.requireEnum(key: String): T {
    val name = requireJsonString(key)
    return enumValues<T>().firstOrNull { it.name == name } ?: error("Unknown $key: $name")
}
