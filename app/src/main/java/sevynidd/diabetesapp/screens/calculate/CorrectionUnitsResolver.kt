package sevynidd.diabetesapp.screens.calculate

import sevynidd.diabetesapp.calculation.SplitBolusResult
import sevynidd.diabetesapp.calculation.calculateBolusUnits
import sevynidd.diabetesapp.calculation.calculateCorrectionUnits
import sevynidd.diabetesapp.calculation.mmolLToMgDl
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit

internal fun correctionUnitsFor(rawBloodSugar: String, correctionSettings: CorrectionSettings): Int {
    val enteredValue = rawBloodSugar.replace(',', '.').toDoubleOrNull() ?: return 0
    val bloodSugarMgDl = if (correctionSettings.glucoseUnit == GlucoseUnit.MmolL) {
        mmolLToMgDl(enteredValue)
    } else {
        enteredValue
    }
    return calculateCorrectionUnits(
        bloodSugarMgDl = bloodSugarMgDl,
        thresholdMgDl = correctionSettings.thresholdMgDl,
        lowThresholdMgDl = correctionSettings.lowThresholdMgDl,
        stepMgDl = correctionSettings.stepMgDl
    )
}

internal fun hasNumericInput(raw: String): Boolean = raw.replace(',', '.').toDoubleOrNull() != null

/** The normal-mode total/correction unit texts, blank whenever neither carbs nor blood sugar is entered. */
internal data class NormalUnitsResult(val calculatedUnitsText: String, val correctionUnitsText: String)

internal fun resolveNormalUnits(
    carbohydratesValue: Double?,
    activeFactor: Double?,
    effectiveBreadUnits: Double,
    bloodSugar: String,
    correctionSettings: CorrectionSettings
): NormalUnitsResult {
    val hasBloodSugarInput = hasNumericInput(bloodSugar)
    val correctionUnits = correctionUnitsFor(bloodSugar, correctionSettings)
    val baseUnits = if (carbohydratesValue != null && activeFactor != null) {
        calculateBolusUnits(carbohydratesValue, effectiveBreadUnits, activeFactor)
    } else {
        0.0
    }
    val calculatedUnits = if (carbohydratesValue != null || hasBloodSugarInput) {
        (baseUnits + correctionUnits).coerceAtLeast(0.0)
    } else {
        null
    }
    return NormalUnitsResult(
        calculatedUnitsText = calculatedUnits.toUiDecimalOrEmpty(),
        correctionUnitsText = if (hasBloodSugarInput) correctionUnits.toString() else ""
    )
}

/** The split-mode total/correction unit texts, blank whenever neither carbs nor blood sugar is entered. */
internal data class SplitUnitsResult(val totalUnitsText: String, val correctionUnitsText: String)

internal fun resolveSplitUnits(
    splitBolus: SplitBolusResult?,
    splitBloodSugar: String,
    correctionSettings: CorrectionSettings
): SplitUnitsResult {
    val hasBloodSugarInput = hasNumericInput(splitBloodSugar)
    val correctionUnits = correctionUnitsFor(splitBloodSugar, correctionSettings)
    val totalUnits = if (splitBolus != null || hasBloodSugarInput) {
        ((splitBolus?.totalUnits ?: 0.0) + correctionUnits).coerceAtLeast(0.0)
    } else {
        null
    }
    return SplitUnitsResult(
        totalUnitsText = totalUnits.toUiDecimalOrEmpty(),
        correctionUnitsText = if (hasBloodSugarInput) correctionUnits.toString() else ""
    )
}
