package sevynidd.diabetesapp.calculation

import kotlin.math.ceil

private const val QUARTER_STEP = 0.25
private const val EVEN_STEP = 2

/**
 * Rounds [value] (comma- or dot-decimal text) up to the nearest `0.25` step, formatted back with
 * comma decimal notation and no trailing zeros. Returns `""` if [value] doesn't parse as a number.
 */
fun normalizeQuarterStepValue(value: String): String {
    return value
        .replace(',', '.')
        .toDoubleOrNull()
        ?.let { raw ->
            val rounded = ceil(raw / QUARTER_STEP) * QUARTER_STEP
            if (rounded % 1.0 == 0.0) {
                rounded.toInt().toString()
            } else {
                rounded
                    .toString()
                    .replace('.', ',')
                    .trimEnd('0')
                    .trimEnd(',')
            }
        }
        ?: ""
}

/**
 * Rounds [value] (integer text) up to the nearest even number. Returns `""` if [value] doesn't
 * parse as an integer.
 */
fun normalizeBasalRateValue(value: String): String {
    return value.toIntOrNull()?.let { raw ->
        if (raw % EVEN_STEP == 0) raw else raw + 1
    }?.toString() ?: ""
}
