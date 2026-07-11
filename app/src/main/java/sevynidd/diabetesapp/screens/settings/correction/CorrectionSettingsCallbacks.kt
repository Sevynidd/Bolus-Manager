package sevynidd.diabetesapp.screens.settings.correction

import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit

/** Callbacks for [CorrectionSettingsScreen], grouped to keep the screen's own parameter list short. */
data class CorrectionSettingsCallbacks(
    val onCorrectionThresholdMgDlChange: (Double) -> Unit = {},
    val onCorrectionLowThresholdMgDlChange: (Double) -> Unit = {},
    val onCorrectionStepMgDlChange: (Double) -> Unit = {},
    val onGlucoseUnitChange: (GlucoseUnit) -> Unit = {}
)
