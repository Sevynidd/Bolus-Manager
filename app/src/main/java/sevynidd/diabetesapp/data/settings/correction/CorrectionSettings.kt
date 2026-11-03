package sevynidd.diabetesapp.data.settings.correction

/**
 * The dose-calculation settings around blood sugar: the high [thresholdMgDl] above which insulin
 * is added, the low [lowThresholdMgDl] below which insulin is subtracted, the shared [stepMgDl]
 * used on both sides, and the display unit. Also the injection [doseStepUnits] the total is
 * rounded to, the [maxDoseUnits] limit (`0` = no limit) and the [fastCarbsGrams] suggested when
 * blood sugar is low.
 */
data class CorrectionSettings(
    val thresholdMgDl: Double = 160.0,
    val lowThresholdMgDl: Double = 80.0,
    val stepMgDl: Double = 30.0,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl,
    val doseStepUnits: Double = DEFAULT_DOSE_STEP_UNITS,
    val maxDoseUnits: Double = 0.0,
    val fastCarbsGrams: Double = DEFAULT_FAST_CARBS_GRAMS
)

/** The dose steps offered in the settings: pump-fine steps up to whole units for pens. */
val DOSE_STEP_OPTIONS_UNITS: List<Double> =
    listOf(PUMP_FINE_STEP_UNITS, PUMP_STEP_UNITS, HALF_UNIT_STEP, WHOLE_UNIT_STEP)

private const val PUMP_FINE_STEP_UNITS = 0.05
private const val PUMP_STEP_UNITS = 0.1
private const val HALF_UNIT_STEP = 0.5
private const val WHOLE_UNIT_STEP = 1.0
private const val DEFAULT_DOSE_STEP_UNITS = HALF_UNIT_STEP
private const val DEFAULT_FAST_CARBS_GRAMS = 15.0
