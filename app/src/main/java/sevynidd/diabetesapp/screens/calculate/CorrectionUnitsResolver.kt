package sevynidd.diabetesapp.screens.calculate

import sevynidd.diabetesapp.calculation.DoseBreakdown
import sevynidd.diabetesapp.calculation.SplitBolusResult
import sevynidd.diabetesapp.calculation.buildDoseBreakdown
import sevynidd.diabetesapp.calculation.calculateBolusUnits
import sevynidd.diabetesapp.calculation.calculateCorrectionUnits
import sevynidd.diabetesapp.calculation.isAboveMaxDose
import sevynidd.diabetesapp.calculation.isBelowLowThreshold
import sevynidd.diabetesapp.calculation.mmolLToMgDl
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit

internal fun bloodSugarMgDlOrNull(rawBloodSugar: String, correctionSettings: CorrectionSettings): Double? {
    val enteredValue = rawBloodSugar.replace(',', '.').toDoubleOrNull() ?: return null
    return if (correctionSettings.glucoseUnit == GlucoseUnit.MmolL) mmolLToMgDl(enteredValue) else enteredValue
}

/**
 * What the result card shows for one bolus mode: the dose [breakdown] (`null` until carbs or
 * blood sugar are entered), the signed [correctionUnitsText] (blank without blood sugar), and the
 * two safety flags that drive the warning banners.
 */
internal data class DoseResult(
    val breakdown: DoseBreakdown?,
    val correctionUnitsText: String,
    val isLowBloodSugar: Boolean,
    val isOverMaxDose: Boolean
)

/**
 * Combines [mealUnits] (`null` when no carbs are entered) with the blood-sugar correction for
 * [rawBloodSugar]. The result is blank until either input exists; the total is never below zero
 * units and is rounded to the configured dose step.
 */
internal fun resolveDose(
    mealUnits: Double?,
    rawBloodSugar: String,
    correctionSettings: CorrectionSettings
): DoseResult {
    val bloodSugarMgDl = bloodSugarMgDlOrNull(rawBloodSugar, correctionSettings)
    val correctionUnits = bloodSugarMgDl?.let { mgDl ->
        calculateCorrectionUnits(
            bloodSugarMgDl = mgDl,
            thresholdMgDl = correctionSettings.thresholdMgDl,
            lowThresholdMgDl = correctionSettings.lowThresholdMgDl,
            stepMgDl = correctionSettings.stepMgDl
        )
    } ?: 0
    val breakdown = if (mealUnits != null || bloodSugarMgDl != null) {
        buildDoseBreakdown(mealUnits ?: 0.0, correctionUnits, correctionSettings.doseStepUnits)
    } else {
        null
    }
    return DoseResult(
        breakdown = breakdown,
        correctionUnitsText = if (bloodSugarMgDl != null) correctionUnits.toString() else "",
        isLowBloodSugar = bloodSugarMgDl?.let { isBelowLowThreshold(it, correctionSettings.lowThresholdMgDl) } == true,
        isOverMaxDose = breakdown?.let { isAboveMaxDose(it.roundedTotalUnits, correctionSettings.maxDoseUnits) } == true
    )
}

/** The normal-mode [DoseResult]: carbs at the active factor plus the blood-sugar correction. */
internal fun resolveNormalDose(
    carbohydratesValue: Double?,
    activeFactor: Double?,
    effectiveBreadUnits: Double,
    bloodSugar: String,
    correctionSettings: CorrectionSettings
): DoseResult {
    val mealUnits = carbohydratesValue?.let { carbs ->
        activeFactor?.let { factor -> calculateBolusUnits(carbs, effectiveBreadUnits, factor) } ?: 0.0
    }
    return resolveDose(mealUnits, bloodSugar, correctionSettings)
}

/** The split-mode [DoseResult]: the split bolus total plus the blood-sugar correction. */
internal fun resolveSplitDose(
    splitBolus: SplitBolusResult?,
    splitBloodSugar: String,
    correctionSettings: CorrectionSettings
): DoseResult = resolveDose(splitBolus?.totalUnits, splitBloodSugar, correctionSettings)
