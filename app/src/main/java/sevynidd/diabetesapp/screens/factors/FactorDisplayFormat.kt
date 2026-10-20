package sevynidd.diabetesapp.screens.factors

import sevynidd.diabetesapp.calculation.factorTextToGramsPerUnitText
import sevynidd.diabetesapp.calculation.gramsPerUnitTextToFactorText
import sevynidd.diabetesapp.calculation.normalizeGramsPerUnitValue
import sevynidd.diabetesapp.calculation.normalizeQuarterStepValue
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode
import sevynidd.diabetesapp.localization.TranslationKey

/**
 * Translates stored factor text (units per bread unit) to and from the notation the user picked
 * in [mode]. [breadUnits] is the grams-per-bread-unit value the pump ratio is derived from.
 */
data class FactorDisplayFormat(
    val mode: TherapyMode = TherapyMode.Pen,
    val breadUnits: Double = DEFAULT_BREAD_UNITS
) {
    val labelKey: TranslationKey
        get() = when (mode) {
            TherapyMode.Pen -> TranslationKey.LabelFactor
            TherapyMode.Pump -> TranslationKey.LabelPumpRatio
        }

    val valueHelpKey: TranslationKey
        get() = when (mode) {
            TherapyMode.Pen -> TranslationKey.FactorValueHelp
            TherapyMode.Pump -> TranslationKey.PumpRatioValueHelp
        }

    val listHelpKey: TranslationKey
        get() = when (mode) {
            TherapyMode.Pen -> TranslationKey.FactorsListHelp
            TherapyMode.Pump -> TranslationKey.FactorsListPumpHelp
        }

    /** The stored [factorText] as it's shown in the current [mode]. */
    fun toDisplayText(factorText: String): String = when (mode) {
        TherapyMode.Pen -> factorText
        TherapyMode.Pump -> factorTextToGramsPerUnitText(factorText, breadUnits)
    }

    /** The factor text to store for [displayText] typed in the current [mode]. */
    fun toFactorText(displayText: String): String = when (mode) {
        TherapyMode.Pen -> displayText
        TherapyMode.Pump -> gramsPerUnitTextToFactorText(displayText, breadUnits)
    }

    /** Rounds freshly typed [displayText] to the current [mode]'s step once editing ends. */
    fun normalizeInput(displayText: String): String = when (mode) {
        TherapyMode.Pen -> normalizeQuarterStepValue(displayText)
        TherapyMode.Pump -> normalizeGramsPerUnitValue(displayText)
    }
}

private const val DEFAULT_BREAD_UNITS = 12.0
