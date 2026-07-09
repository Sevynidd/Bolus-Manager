package sevynidd.diabetesapp.data.settings

/** The blood-sugar correction-dose settings: threshold and step (both mg/dl), and the display unit. */
data class CorrectionSettings(
    val thresholdMgDl: Double = 160.0,
    val stepMgDl: Double = 30.0,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl
)
