package sevynidd.diabetesapp.calculation

import kotlin.math.roundToInt

/**
 * Whole units of correction insulin for [bloodSugarMgDl]: [bloodSugarMgDl] minus [thresholdMgDl],
 * divided by [stepMgDl], rounded to the nearest whole unit. Below the threshold, or with a
 * non-positive [stepMgDl], this is `0`.
 */
fun calculateCorrectionUnits(bloodSugarMgDl: Double, thresholdMgDl: Double, stepMgDl: Double): Int {
    val excess = bloodSugarMgDl - thresholdMgDl
    return if (stepMgDl <= 0.0 || excess <= 0.0) 0 else (excess / stepMgDl).roundToInt()
}
