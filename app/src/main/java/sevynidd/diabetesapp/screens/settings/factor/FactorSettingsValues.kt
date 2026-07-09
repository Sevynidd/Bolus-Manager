package sevynidd.diabetesapp.screens.settings.factor

import sevynidd.diabetesapp.data.settings.profile.Gender

/**
 * The current values [FactorSettingsScreen] displays: bread units, the period surcharge
 * percentage, and gender (which gates whether the period surcharge card is shown at all).
 */
data class FactorSettingsValues(
    val breadUnits: Double = 12.0,
    val periodFactorPercent: Double = 0.0,
    val gender: Gender = Gender.PreferNotToSay
)
