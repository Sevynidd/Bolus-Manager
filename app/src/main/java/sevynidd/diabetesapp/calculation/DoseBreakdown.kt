package sevynidd.diabetesapp.calculation

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * A dose split into its parts: the [mealUnits] for the carbs, the whole-unit [correctionUnits]
 * for blood sugar, the [exactTotalUnits] (their sum, never below zero) and the
 * [roundedTotalUnits] actually to inject.
 */
data class DoseBreakdown(
    val mealUnits: Double,
    val correctionUnits: Int,
    val exactTotalUnits: Double,
    val roundedTotalUnits: Double
)

/**
 * Rounds [units] to the nearest multiple of [stepUnits], with exact halves rounding up. A
 * non-positive [stepUnits] means "no rounding" and returns [units] unchanged. Decimal arithmetic
 * avoids binary-float artifacts such as `0.15` landing on the wrong side of a `0.1` step.
 */
fun roundDoseToStep(units: Double, stepUnits: Double): Double {
    if (stepUnits <= 0.0) return units
    val step = BigDecimal.valueOf(stepUnits)
    return BigDecimal.valueOf(units)
        .divide(step, 0, RoundingMode.HALF_UP)
        .multiply(step)
        .toDouble()
}

/**
 * Combines [mealUnits] and [correctionUnits] into a [DoseBreakdown]. The exact total is clamped
 * to `0` because a negative correction can cancel the meal dose but never turn into a negative
 * injection; the rounded total is that exact total rounded to [stepUnits].
 */
fun buildDoseBreakdown(mealUnits: Double, correctionUnits: Int, stepUnits: Double): DoseBreakdown {
    val exactTotal = (mealUnits + correctionUnits).coerceAtLeast(0.0)
    return DoseBreakdown(
        mealUnits = mealUnits,
        correctionUnits = correctionUnits,
        exactTotalUnits = exactTotal,
        roundedTotalUnits = roundDoseToStep(exactTotal, stepUnits)
    )
}

/** Whether [bloodSugarMgDl] is strictly below [lowThresholdMgDl]; exactly at the threshold is not low. */
fun isBelowLowThreshold(bloodSugarMgDl: Double, lowThresholdMgDl: Double): Boolean =
    bloodSugarMgDl < lowThresholdMgDl

/** Whether [units] exceeds [maxDoseUnits]; a non-positive [maxDoseUnits] means the limit is switched off. */
fun isAboveMaxDose(units: Double, maxDoseUnits: Double): Boolean =
    maxDoseUnits > 0.0 && units > maxDoseUnits
