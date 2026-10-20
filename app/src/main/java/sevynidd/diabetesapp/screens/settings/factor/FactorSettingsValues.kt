package sevynidd.diabetesapp.screens.settings.factor

import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode
import sevynidd.diabetesapp.data.settings.profile.Gender

/**
 * The current values [FactorSettingsScreen] displays: bread units, the period surcharge
 * percentage, gender (which gates whether the period surcharge card is shown at all), how
 * factors are displayed, and the unit blood-sugar values are entered in.
 */
data class FactorSettingsValues(
    val breadUnits: Double = 12.0,
    val periodFactorPercent: Double = 0.0,
    val gender: Gender = Gender.PreferNotToSay,
    val therapyMode: TherapyMode = TherapyMode.Pen,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl
)

/** Edit callbacks for [FactorSettingsScreen], bundled to keep its parameter list short. */
data class FactorSettingsCallbacks(
    val onBreadUnitsChange: (Double) -> Unit = {},
    val onPeriodFactorPercentChange: (Double) -> Unit = {},
    val onTherapyModeChange: (TherapyMode) -> Unit = {},
    val onGlucoseUnitChange: (GlucoseUnit) -> Unit = {}
)
