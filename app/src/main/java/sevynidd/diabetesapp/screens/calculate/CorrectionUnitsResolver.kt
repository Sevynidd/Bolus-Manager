package sevynidd.diabetesapp.screens.calculate

import sevynidd.diabetesapp.calculation.DoseBreakdown
import sevynidd.diabetesapp.calculation.SplitBolusResult
import sevynidd.diabetesapp.calculation.buildDoseBreakdown
import sevynidd.diabetesapp.calculation.calculateBolusUnits
import sevynidd.diabetesapp.calculation.calculateCorrectionUnits
import sevynidd.diabetesapp.calculation.exceedsPlausibleLimit
import sevynidd.diabetesapp.calculation.isAboveMaxDose
import sevynidd.diabetesapp.calculation.isBelowLowThreshold
import sevynidd.diabetesapp.calculation.mmolLToMgDl
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit

internal fun bloodSugarMgDlOrNull(rawBloodSugar: String, correctionSettings: CorrectionSettings): Double? {
    val enteredValue = rawBloodSugar.replace(',', '.').toDoubleOrNull() ?: return null
    return if (correctionSettings.glucoseUnit == GlucoseUnit.MmolL) mmolLToMgDl(enteredValue) else enteredValue
}

/** Why no dose is shown even though something was entered. */
internal enum class DoseBlocker {
    BloodSugarTooHigh,
    CarbohydratesTooHigh,
    NoFactor
}

/**
 * What the result card shows for one bolus mode: the dose [breakdown] (`null` until carbs or
 * blood sugar are entered, or while a [blocker] applies) and the safety flags that drive the
 * warning banners. [isLowBloodSugar] stays set while a [blocker] applies so the low-sugar warning
 * is never hidden by an unrelated mistake.
 */
internal data class DoseResult(
    val breakdown: DoseBreakdown?,
    val isLowBloodSugar: Boolean,
    val isOverMaxDose: Boolean,
    val blocker: DoseBlocker? = null
)

/**
 * The reason the entries cannot be turned into a dose, or `null` when they can. A blood sugar or
 * carb amount above its plausibility limit is treated as a typing mistake; carbs without any
 * factor for the time window would silently give a meal dose of zero.
 */
internal fun findDoseBlocker(
    carbohydratesGrams: Double?,
    bloodSugarMgDl: Double?,
    hasFactor: Boolean,
    correctionSettings: CorrectionSettings
): DoseBlocker? = when {
    bloodSugarMgDl != null && exceedsPlausibleLimit(bloodSugarMgDl, correctionSettings.maxBloodSugarMgDl) ->
        DoseBlocker.BloodSugarTooHigh
    carbohydratesGrams != null &&
        exceedsPlausibleLimit(carbohydratesGrams, correctionSettings.maxCarbohydratesGrams) ->
        DoseBlocker.CarbohydratesTooHigh
    carbohydratesGrams != null && !hasFactor -> DoseBlocker.NoFactor
    else -> null
}

/**
 * Combines [mealUnits] (`null` when no carbs are entered) with the blood-sugar correction for
 * [rawBloodSugar]. The result is blank until either input exists or while a [blocker] applies; the
 * total is never below zero units and is rounded to the configured dose step.
 */
internal fun resolveDose(
    mealUnits: Double?,
    rawBloodSugar: String,
    correctionSettings: CorrectionSettings,
    blocker: DoseBlocker? = null
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
    val breakdown = if (blocker == null && (mealUnits != null || bloodSugarMgDl != null)) {
        buildDoseBreakdown(mealUnits ?: 0.0, correctionUnits, correctionSettings.doseStepUnits)
    } else {
        null
    }
    return DoseResult(
        breakdown = breakdown,
        isLowBloodSugar = bloodSugarMgDl?.let { isBelowLowThreshold(it, correctionSettings.lowThresholdMgDl) } == true,
        isOverMaxDose = breakdown?.let {
            isAboveMaxDose(it.roundedTotalUnits, correctionSettings.maxDoseUnits)
        } == true,
        blocker = blocker
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
    val blocker = findDoseBlocker(
        carbohydratesGrams = carbohydratesValue,
        bloodSugarMgDl = bloodSugarMgDlOrNull(bloodSugar, correctionSettings),
        hasFactor = activeFactor != null,
        correctionSettings = correctionSettings
    )
    return resolveDose(mealUnits, bloodSugar, correctionSettings, blocker)
}

/** The entries and factor availability behind a split-mode dose. */
internal data class SplitDoseInputs(
    val splitBolus: SplitBolusResult?,
    val carbohydratesGrams: Double?,
    val bloodSugar: String,
    val hasFactors: Boolean
)

/** The split-mode [DoseResult]: the split bolus total plus the blood-sugar correction. */
internal fun resolveSplitDose(inputs: SplitDoseInputs, correctionSettings: CorrectionSettings): DoseResult {
    val blocker = findDoseBlocker(
        carbohydratesGrams = inputs.carbohydratesGrams,
        bloodSugarMgDl = bloodSugarMgDlOrNull(inputs.bloodSugar, correctionSettings),
        hasFactor = inputs.hasFactors,
        correctionSettings = correctionSettings
    )
    return resolveDose(inputs.splitBolus?.totalUnits, inputs.bloodSugar, correctionSettings, blocker)
}
