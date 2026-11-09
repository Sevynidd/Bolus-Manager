package sevynidd.diabetesapp.screens.settings

import org.junit.Assert.assertEquals
import org.junit.Test
import sevynidd.diabetesapp.data.settings.appearance.ThemeMode
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.data.settings.profile.Gender
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode
import sevynidd.diabetesapp.localization.AppLanguage

class SettingsSummariesTest {

    @Test
    fun `summaries show the current values in the chosen language`() {
        val inputs = SettingsSummaryInputs(
            therapyMode = TherapyMode.Pump,
            correctionSettings = CorrectionSettings(
                glucoseUnit = GlucoseUnit.MmolL,
                doseStepUnits = 0.5,
                maxDoseUnits = 15.0
            ),
            gender = Gender.Female,
            themeMode = ThemeMode.Dark
        )

        val summaries = buildSettingsSummaries(inputs, AppLanguage.German)

        assertEquals("Insulinpumpe · mmol/l", summaries.factors)
        assertEquals("Dosisschritt 0,5 IE · Maximaldosis 15 IE", summaries.correction)
        assertEquals("Dunkel", summaries.appearance)
        assertEquals("Deutsch", summaries.language)
    }

    @Test
    fun `maximum dose is left out when it is switched off`() {
        val inputs = SettingsSummaryInputs(correctionSettings = CorrectionSettings(maxDoseUnits = 0.0))

        assertEquals("Dose step 0,5 U", buildSettingsSummaries(inputs, AppLanguage.English).correction)
    }

    @Test
    fun `negative maximum dose is treated as off`() {
        val inputs = SettingsSummaryInputs(correctionSettings = CorrectionSettings(maxDoseUnits = -1.0))

        assertEquals("Dose step 0,5 U", buildSettingsSummaries(inputs, AppLanguage.English).correction)
    }
}
