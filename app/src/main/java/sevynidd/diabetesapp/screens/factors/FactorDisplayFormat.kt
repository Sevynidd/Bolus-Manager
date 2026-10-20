package sevynidd.diabetesapp.screens.factors

import sevynidd.diabetesapp.calculation.factorTextToGramsPerUnitText
import sevynidd.diabetesapp.calculation.gramsPerUnitTextToFactorText
import sevynidd.diabetesapp.calculation.normalizeGramsPerUnitValue
import sevynidd.diabetesapp.calculation.normalizeQuarterStepValue
import sevynidd.diabetesapp.data.settings.factor.FactorDisplayMode
import sevynidd.diabetesapp.localization.TranslationKey

/**
 * Translates stored factor text (units per bread unit) to and from the notation the user picked
 * in [mode]. [breadUnits] is the grams-per-bread-unit value the pump ratio is derived from.
 */
data class FactorDisplayFormat(
    val mode: FactorDisplayMode = FactorDisplayMode.UnitsPerBreadUnit,
    val breadUnits: Double = DEFAULT_BREAD_UNITS
) {
    val labelKey: TranslationKey
        get() = when (mode) {
            FactorDisplayMode.UnitsPerBreadUnit -> TranslationKey.LabelFactor
            FactorDisplayMode.GramsPerUnit -> TranslationKey.LabelPumpRatio
        }

    val valueHelpKey: TranslationKey
        get() = when (mode) {
            FactorDisplayMode.UnitsPerBreadUnit -> TranslationKey.FactorValueHelp
            FactorDisplayMode.GramsPerUnit -> TranslationKey.PumpRatioValueHelp
        }

    val listHelpKey: TranslationKey
        get() = when (mode) {
            FactorDisplayMode.UnitsPerBreadUnit -> TranslationKey.FactorsListHelp
            FactorDisplayMode.GramsPerUnit -> TranslationKey.FactorsListPumpHelp
        }

    /** The stored [factorText] as it's shown in the current [mode]. */
    fun toDisplayText(factorText: String): String = when (mode) {
        FactorDisplayMode.UnitsPerBreadUnit -> factorText
        FactorDisplayMode.GramsPerUnit -> factorTextToGramsPerUnitText(factorText, breadUnits)
    }

    /** The factor text to store for [displayText] typed in the current [mode]. */
    fun toFactorText(displayText: String): String = when (mode) {
        FactorDisplayMode.UnitsPerBreadUnit -> displayText
        FactorDisplayMode.GramsPerUnit -> gramsPerUnitTextToFactorText(displayText, breadUnits)
    }

    /** Rounds freshly typed [displayText] to the current [mode]'s step once editing ends. */
    fun normalizeInput(displayText: String): String = when (mode) {
        FactorDisplayMode.UnitsPerBreadUnit -> normalizeQuarterStepValue(displayText)
        FactorDisplayMode.GramsPerUnit -> normalizeGramsPerUnitValue(displayText)
    }
}

private const val DEFAULT_BREAD_UNITS = 12.0
