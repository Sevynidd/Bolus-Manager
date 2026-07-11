package sevynidd.diabetesapp.calculation

import kotlin.math.roundToInt

/**
 * Whole units of correction insulin for [bloodSugarMgDl]. Above [thresholdMgDl], this is the
 * excess over the threshold divided by [stepMgDl], rounded to the nearest whole unit (extra
 * insulin to bring blood sugar down). Below [lowThresholdMgDl], this is the deficit under that
 * lower threshold divided by [stepMgDl], again rounded to the nearest whole unit, but negative
 * (insulin to subtract so the dose doesn't push blood sugar down further). Between the two
 * thresholds, or with a non-positive [stepMgDl], this is `0`.
 */
fun calculateCorrectionUnits(
    bloodSugarMgDl: Double,
    thresholdMgDl: Double,
    lowThresholdMgDl: Double,
    stepMgDl: Double
): Int {
    if (stepMgDl <= 0.0) return 0
    return when {
        bloodSugarMgDl > thresholdMgDl -> ((bloodSugarMgDl - thresholdMgDl) / stepMgDl).roundToInt()
        bloodSugarMgDl < lowThresholdMgDl -> ((bloodSugarMgDl - lowThresholdMgDl) / stepMgDl).roundToInt()
        else -> 0
    }
}
