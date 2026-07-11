package sevynidd.diabetesapp.screens.settings.correction

import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit

/**
 * The current values [CorrectionSettingsScreen] displays: high threshold, low threshold, step,
 * and display unit.
 */
data class CorrectionSettingsValues(
    val correctionThresholdMgDl: Double = 160.0,
    val correctionLowThresholdMgDl: Double = 80.0,
    val correctionStepMgDl: Double = 30.0,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl
)

/** Maps the persisted [CorrectionSettings] to the display values [CorrectionSettingsScreen] expects. */
fun CorrectionSettings.toCorrectionSettingsValues(): CorrectionSettingsValues = CorrectionSettingsValues(
    correctionThresholdMgDl = thresholdMgDl,
    correctionLowThresholdMgDl = lowThresholdMgDl,
    correctionStepMgDl = stepMgDl,
    glucoseUnit = glucoseUnit
)
