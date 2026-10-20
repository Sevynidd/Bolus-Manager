package sevynidd.diabetesapp.calculation

import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

private const val GRAMS_PER_UNIT_DECIMALS = 1
private const val FACTOR_TEXT_DECIMALS = 5

/**
 * Grams of carbohydrates covered by one insulin unit (the pump notation of a factor):
 * `GramsPerUnit = BreadUnits / Factor`. `null` when [factor] or [breadUnits] isn't positive,
 * since no finite ratio exists then.
 */
fun gramsPerUnitFromFactor(factor: Double, breadUnits: Double): Double? =
    if (factor > 0.0 && breadUnits > 0.0) breadUnits / factor else null

/**
 * The factor (units per bread unit) matching a pump ratio of [gramsPerUnit] grams per unit:
 * `Factor = BreadUnits / GramsPerUnit`. `null` when [gramsPerUnit] or [breadUnits] isn't positive.
 */
fun factorFromGramsPerUnit(gramsPerUnit: Double, breadUnits: Double): Double? =
    if (gramsPerUnit > 0.0 && breadUnits > 0.0) breadUnits / gramsPerUnit else null

/**
 * [gramsPerUnit] rounded half-up to the nearest `0.1` g, the resolution pump ratios are shown in.
 * Rounds the decimal value (via [BigDecimal]) so a midpoint like `9.25` reliably becomes `9.3`
 * instead of being skewed by its binary floating-point representation.
 */
fun roundToTenthGram(gramsPerUnit: Double): Double =
    BigDecimal.valueOf(gramsPerUnit).setScale(GRAMS_PER_UNIT_DECIMALS, RoundingMode.HALF_UP).toDouble()

/**
 * Rounds typed pump-ratio text (comma or dot decimal) to the nearest `0.1` g, formatted with
 * comma decimal notation. `""` when it doesn't parse or rounds to `0`, since a ratio of `0` g
 * per unit has no matching factor.
 */
fun normalizeGramsPerUnitValue(value: String): String {
    val rounded = value.replace(',', '.').toDoubleOrNull()?.let(::roundToTenthGram)
    return if (rounded != null && rounded > 0.0) formatCommaDecimal(rounded, GRAMS_PER_UNIT_DECIMALS) else ""
}

/**
 * The stored factor text, shown as a pump ratio in grams per unit rounded to `0.1` g.
 * `""` when [factorText] is empty, unparseable, or not positive.
 */
fun factorTextToGramsPerUnitText(factorText: String, breadUnits: Double): String =
    factorText.replace(',', '.').toDoubleOrNull()
        ?.let { gramsPerUnitFromFactor(it, breadUnits) }
        ?.let { formatCommaDecimal(roundToTenthGram(it), GRAMS_PER_UNIT_DECIMALS) }
        .orEmpty()

/**
 * The factor text to store for a pump ratio of [gramsPerUnitText] grams per unit. Unlike factors
 * typed directly, it isn't snapped to the `0.25` factor grid — it keeps 5 decimals, enough for
 * the ratio to round-trip back to the same `0.1` g value. `""` when the ratio is empty or invalid.
 */
fun gramsPerUnitTextToFactorText(gramsPerUnitText: String, breadUnits: Double): String =
    gramsPerUnitText.replace(',', '.').toDoubleOrNull()
        ?.let { factorFromGramsPerUnit(it, breadUnits) }
        ?.let { formatCommaDecimal(it, FACTOR_TEXT_DECIMALS) }
        .orEmpty()

private fun formatCommaDecimal(value: Double, decimals: Int): String =
    String.format(Locale.ROOT, "%.${decimals}f", value)
        .replace('.', ',')
        .trimEnd('0')
        .trimEnd(',')
