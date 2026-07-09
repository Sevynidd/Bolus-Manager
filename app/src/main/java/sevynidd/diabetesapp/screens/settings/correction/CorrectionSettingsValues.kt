package sevynidd.diabetesapp.screens.settings.correction

import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit

/** The current values [CorrectionSettingsScreen] displays: threshold, step, and display unit. */
data class CorrectionSettingsValues(
    val correctionThresholdMgDl: Double = 160.0,
    val correctionStepMgDl: Double = 30.0,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl
)
