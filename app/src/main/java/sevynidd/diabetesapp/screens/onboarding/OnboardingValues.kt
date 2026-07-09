package sevynidd.diabetesapp.screens.onboarding

import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.appearance.ThemeMode
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.profile.Gender
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.ui.theme.ContrastLevel

/** The current values shown across [OnboardingScreen]'s steps, bundled to keep its parameter list short. */
data class OnboardingValues(
    val currentLanguage: AppLanguage = AppLanguage.System,
    val themeMode: ThemeMode = ThemeMode.System,
    val contrastLevel: ContrastLevel = ContrastLevel.Normal,
    val gender: Gender = Gender.PreferNotToSay,
    val factors: FactorsData = FactorsData(),
    val breadUnits: Double = 12.0,
    val periodFactorPercent: Double = 0.0,
    val correctionSettings: CorrectionSettings = CorrectionSettings(),
    val isBasalReminderEnabled: Boolean = false
)
