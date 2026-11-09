package sevynidd.diabetesapp.screens.settings.correction

import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit

/**
 * The current values [CorrectionSettingsScreen] displays: high threshold, low threshold, step,
 * display unit, injection dose step, maximum dose, suggested fast-carb amount and the
 * plausibility limits for blood sugar and carbohydrates.
 */
data class CorrectionSettingsValues(
    val correctionThresholdMgDl: Double = 160.0,
    val correctionLowThresholdMgDl: Double = 80.0,
    val correctionStepMgDl: Double = 30.0,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl,
    val doseStepUnits: Double = CorrectionSettings().doseStepUnits,
    val maxDoseUnits: Double = CorrectionSettings().maxDoseUnits,
    val fastCarbsGrams: Double = CorrectionSettings().fastCarbsGrams,
    val maxBloodSugarMgDl: Double = CorrectionSettings().maxBloodSugarMgDl,
    val maxCarbohydratesGrams: Double = CorrectionSettings().maxCarbohydratesGrams
)

/** Maps the persisted [CorrectionSettings] to the display values [CorrectionSettingsScreen] expects. */
fun CorrectionSettings.toCorrectionSettingsValues(): CorrectionSettingsValues = CorrectionSettingsValues(
    correctionThresholdMgDl = thresholdMgDl,
    correctionLowThresholdMgDl = lowThresholdMgDl,
    correctionStepMgDl = stepMgDl,
    glucoseUnit = glucoseUnit,
    doseStepUnits = doseStepUnits,
    maxDoseUnits = maxDoseUnits,
    fastCarbsGrams = fastCarbsGrams,
    maxBloodSugarMgDl = maxBloodSugarMgDl,
    maxCarbohydratesGrams = maxCarbohydratesGrams
)
