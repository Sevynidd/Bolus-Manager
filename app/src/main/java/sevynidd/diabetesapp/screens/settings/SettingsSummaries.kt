package sevynidd.diabetesapp.screens.settings

import sevynidd.diabetesapp.data.settings.appearance.ThemeMode
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.profile.Gender
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.screens.calculate.toUiDecimalOrEmpty
import sevynidd.diabetesapp.ui.glucoseUnitLabel

/** The current settings values that the "More" screen shows as one-line subtitles. */
data class SettingsSummaryInputs(
    val therapyMode: TherapyMode = TherapyMode.Pen,
    val correctionSettings: CorrectionSettings = CorrectionSettings(),
    val gender: Gender = Gender.PreferNotToSay,
    val themeMode: ThemeMode = ThemeMode.System
)

/** One-line subtitles for the "More" rows that have a current value worth showing at a glance. */
data class SettingsSummaries(
    val factors: String,
    val correction: String,
    val gender: String,
    val appearance: String,
    val language: String
)

private const val SUMMARY_SEPARATOR = " · "

/**
 * Builds the subtitles of the "More" rows in [language]. The maximum dose is only mentioned when
 * it is switched on (a value above 0), so "off" never has to be spelled out.
 */
fun buildSettingsSummaries(
    inputs: SettingsSummaryInputs,
    language: AppLanguage
): SettingsSummaries {
    val correction = inputs.correctionSettings
    val unit = translate(TranslationKey.DoseUnitSymbol, language)
    fun doseText(label: TranslationKey, units: Double) =
        "${translate(label, language)} ${units.toUiDecimalOrEmpty()} $unit"
    val doseStep = doseText(TranslationKey.DoseStep, correction.doseStepUnits)
    val maxDose = doseText(TranslationKey.MaxDose, correction.maxDoseUnits).takeIf { correction.maxDoseUnits > 0.0 }

    return SettingsSummaries(
        factors = listOf(
            translate(inputs.therapyMode.labelKey(), language),
            glucoseUnitLabel(correction.glucoseUnit)
        ).joinToString(SUMMARY_SEPARATOR),
        correction = listOfNotNull(doseStep, maxDose).joinToString(SUMMARY_SEPARATOR),
        gender = translate(inputs.gender.labelKey(), language),
        appearance = translate(inputs.themeMode.labelKey(), language),
        language = translate(language.labelKey(), language)
    )
}

private fun TherapyMode.labelKey(): TranslationKey = when (this) {
    TherapyMode.Pen -> TranslationKey.TherapyModePen
    TherapyMode.Pump -> TranslationKey.TherapyModePump
}

private fun Gender.labelKey(): TranslationKey = when (this) {
    Gender.Male -> TranslationKey.GenderMale
    Gender.Female -> TranslationKey.GenderFemale
    Gender.PreferNotToSay -> TranslationKey.GenderPreferNotToSay
}

private fun ThemeMode.labelKey(): TranslationKey = when (this) {
    ThemeMode.System -> TranslationKey.ThemeSystem
    ThemeMode.Light -> TranslationKey.ThemeLight
    ThemeMode.Dark -> TranslationKey.ThemeDark
}

private fun AppLanguage.labelKey(): TranslationKey = when (this) {
    AppLanguage.System -> TranslationKey.LanguageSystem
    AppLanguage.English -> TranslationKey.LanguageEnglish
    AppLanguage.German -> TranslationKey.LanguageGerman
    AppLanguage.French -> TranslationKey.LanguageFrench
    AppLanguage.Polish -> TranslationKey.LanguagePolish
}
