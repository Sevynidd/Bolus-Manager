package sevynidd.diabetesapp.screens.onboarding

import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.appearance.ThemeMode
import sevynidd.diabetesapp.data.settings.profile.Gender
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.screens.settings.correction.CorrectionSettingsCallbacks
import sevynidd.diabetesapp.ui.theme.ContrastLevel

/** Edit callbacks for [OnboardingScreen]'s steps, bundled to keep its parameter list short. */
data class OnboardingCallbacks(
    val onThemeModeChange: (ThemeMode) -> Unit = {},
    val onContrastLevelChange: (ContrastLevel) -> Unit = {},
    val onLanguageChange: (AppLanguage) -> Unit = {},
    val onGenderChange: (Gender) -> Unit = {},
    val onFactorsChange: ((FactorsData) -> FactorsData) -> Unit = {},
    val onBreadUnitsChange: (Double) -> Unit = {},
    val onPeriodFactorPercentChange: (Double) -> Unit = {},
    val onBasalReminderEnabledChange: (Boolean) -> Unit = {},
    val onCorrectionSettingsChange: CorrectionSettingsCallbacks = CorrectionSettingsCallbacks(),
    val onFinished: () -> Unit = {}
)
