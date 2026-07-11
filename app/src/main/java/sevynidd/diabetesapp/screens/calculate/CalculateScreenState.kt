package sevynidd.diabetesapp.screens.calculate

import sevynidd.diabetesapp.calculation.ActiveFactorInfo
import sevynidd.diabetesapp.calculation.MINUTES_PER_DAY
import sevynidd.diabetesapp.calculation.SplitBolusResult
import sevynidd.diabetesapp.calculation.activeFactorForTime
import sevynidd.diabetesapp.calculation.applyPeriodMultiplier
import sevynidd.diabetesapp.calculation.calculateSplitBolus
import sevynidd.diabetesapp.data.settings.profile.Gender
import java.time.LocalTime
import kotlin.math.roundToInt

/**
 * The raw text of every editable amount on [CalculateScreen], bundled to keep helper functions'
 * parameter lists short.
 */
internal data class RawBolusInputs(
    val carbohydrates: String,
    val bloodSugar: String,
    val splitCarbohydrates: String,
    val splitBloodSugar: String,
    val splitImmediatePercent: String,
    val splitDurationMinutes: String
)

/** The values derived from [RawBolusInputs], ready to render in either bolus mode's card. */
internal data class CalculateScreenComputed(
    val activeFactorText: String,
    val calculatedUnitsText: String,
    val correctionUnitsText: String,
    val futureFactorText: String,
    val splitBolus: SplitBolusResult?,
    val splitUnits: SplitUnitsResult
)

internal fun computeCalculateScreenState(
    values: CalculateScreenValues,
    now: LocalTime,
    inputs: RawBolusInputs
): CalculateScreenComputed {
    val factors = values.factors
    val isPeriodApplicable = factors.isPeriodEnabled && values.gender == Gender.Female
    val nowMinutes = (now.hour * MINUTES_PER_HOUR) + now.minute
    val activeFactorInfo = activeFactorForTime(factors.factorSlots, nowMinutes)
    val activeFactor = applyPeriodMultiplier(activeFactorInfo.factor, isPeriodApplicable, values.periodFactorPercent)
    val effectiveBreadUnits = values.breadUnits.takeIf { it > 0.0 } ?: DEFAULT_BREAD_UNITS

    val normalUnits = resolveNormalUnits(
        carbohydratesValue = inputs.carbohydrates.replace(',', '.').toDoubleOrNull(),
        activeFactor = activeFactor,
        effectiveBreadUnits = effectiveBreadUnits,
        bloodSugar = inputs.bloodSugar,
        correctionSettings = values.correctionSettings
    )

    val splitCarbohydratesValue = inputs.splitCarbohydrates.replace(',', '.').toDoubleOrNull()
    val splitImmediatePercentValue = inputs.splitImmediatePercent.toIntOrNull()?.coerceIn(0, MAX_PERCENT)
    val splitDurationValue = inputs.splitDurationMinutes.replace(',', '.').toDoubleOrNull()
    val splitDurationOffsetMinutes =
        splitDurationValue?.roundToInt()?.coerceAtLeast(0) ?: DEFAULT_SPLIT_DURATION_MINUTES
    val futureFactorTimeMinutes = (nowMinutes + splitDurationOffsetMinutes) % MINUTES_PER_DAY
    val futureFactorInfo = activeFactorForTime(factors.factorSlots, futureFactorTimeMinutes)
    val futureFactor = applyPeriodMultiplier(futureFactorInfo.factor, isPeriodApplicable, values.periodFactorPercent)

    val splitBolus = splitBolusOrNull(
        carbohydrates = splitCarbohydratesValue,
        immediatePercent = splitImmediatePercentValue,
        breadUnits = effectiveBreadUnits,
        immediateFactor = activeFactor,
        restFactor = futureFactor
    )

    val splitUnits = resolveSplitUnits(
        splitBolus = splitBolus,
        splitBloodSugar = inputs.splitBloodSugar,
        correctionSettings = values.correctionSettings
    )

    return CalculateScreenComputed(
        activeFactorText = activeFactorInfo.toDisplayText(activeFactor),
        calculatedUnitsText = normalUnits.calculatedUnitsText,
        correctionUnitsText = normalUnits.correctionUnitsText,
        futureFactorText = futureFactorInfo.toDisplayText(futureFactor),
        splitBolus = splitBolus,
        splitUnits = splitUnits
    )
}

private fun ActiveFactorInfo.toDisplayText(factorValue: Double? = factor): String {
    val valueText = factorValue.toUiDecimalOrEmpty()
    return if (valueText.isBlank()) factorName else "$factorName · $valueText"
}

private fun splitBolusOrNull(
    carbohydrates: Double?,
    immediatePercent: Int?,
    breadUnits: Double,
    immediateFactor: Double?,
    restFactor: Double?
): SplitBolusResult? {
    val carbsAndPercent = carbohydrates?.let { carbs -> immediatePercent?.let { percent -> carbs to percent } }
    val factors = immediateFactor?.let { immediate -> restFactor?.let { rest -> immediate to rest } }
    return carbsAndPercent?.let { (carbs, percent) ->
        factors?.let { (immediate, rest) ->
            calculateSplitBolus(carbs, percent, breadUnits, immediate, rest)
        }
    }
}

private const val MINUTES_PER_HOUR = 60
private const val DEFAULT_BREAD_UNITS = 12.0
private const val MAX_PERCENT = 100
private const val DEFAULT_SPLIT_DURATION_MINUTES = 120
