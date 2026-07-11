package sevynidd.diabetesapp.data.settings.correction

/**
 * The blood-sugar correction-dose settings: the high [thresholdMgDl] above which insulin is
 * added, the low [lowThresholdMgDl] below which insulin is subtracted, the shared [stepMgDl] used
 * on both sides, and the display unit.
 */
data class CorrectionSettings(
    val thresholdMgDl: Double = 160.0,
    val lowThresholdMgDl: Double = 80.0,
    val stepMgDl: Double = 30.0,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl
)
