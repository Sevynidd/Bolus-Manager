package sevynidd.diabetesapp.screens.calculate

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.ui.theme.BolusManagerTheme
import sevynidd.diabetesapp.ui.theme.ContrastLevel

private const val PREVIEW_MAX_DOSE_UNITS = 5.0
private const val PREVIEW_MEAL_UNITS = 8.0

@Composable
private fun DoseResultAreaPreviewContent(darkTheme: Boolean, contrastLevel: ContrastLevel) {
    val settings = CorrectionSettings(maxDoseUnits = PREVIEW_MAX_DOSE_UNITS)
    BolusManagerTheme(darkTheme = darkTheme, dynamicColor = false, contrastLevel = contrastLevel) {
        Surface(color = MaterialTheme.colorScheme.background) {
            DoseResultArea(
                dose = resolveDose(mealUnits = PREVIEW_MEAL_UNITS, rawBloodSugar = "62", correctionSettings = settings),
                rawBloodSugar = "62",
                activeFactorText = "Breakfast · 1,5",
                correctionSettings = settings,
                currentLanguage = AppLanguage.System
            )
        }
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun DoseResultAreaLightPreview() {
    DoseResultAreaPreviewContent(darkTheme = false, contrastLevel = ContrastLevel.Normal)
}

@Preview(name = "Dark", showBackground = true)
@Composable
private fun DoseResultAreaDarkPreview() {
    DoseResultAreaPreviewContent(darkTheme = true, contrastLevel = ContrastLevel.Normal)
}

@Preview(name = "High contrast", showBackground = true)
@Composable
private fun DoseResultAreaHighContrastPreview() {
    DoseResultAreaPreviewContent(darkTheme = false, contrastLevel = ContrastLevel.HighContrast)
}
