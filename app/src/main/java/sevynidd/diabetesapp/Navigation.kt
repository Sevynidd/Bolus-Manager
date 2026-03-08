package sevynidd.diabetesapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppDestinations(
    val icon: ImageVector,
) {
    FACTORS(Icons.Filled.Percent),
    CALCULATE(Icons.Filled.Calculate),
    SETTINGS(Icons.Filled.Settings),
}

fun destinationLabel(destination: AppDestinations, language: AppLanguage): String {
    return when (destination) {
        AppDestinations.FACTORS -> translate(TranslationKey.DestinationFactors, language)
        AppDestinations.CALCULATE -> translate(TranslationKey.DestinationCalculate, language)
        AppDestinations.SETTINGS -> translate(TranslationKey.DestinationSettings, language)
    }
}

