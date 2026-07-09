package sevynidd.diabetesapp.screens.settings

import sevynidd.diabetesapp.data.settings.GlucoseUnit

/** The current values [CorrectionSettingsScreen] displays: threshold, step, and display unit. */
data class CorrectionSettingsValues(
    val correctionThresholdMgDl: Double = 160.0,
    val correctionStepMgDl: Double = 30.0,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl
)
