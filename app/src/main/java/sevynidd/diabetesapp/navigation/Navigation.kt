package sevynidd.diabetesapp.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.localization.TranslationKey

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

enum class SettingsDestination {
    Main,
    Theme,
    Language,
    BreadUnits
}

enum class FactorsDestination {
    Main,
    EditSchedule
}

fun settingsDestinationTransition(
    initialState: SettingsDestination,
    targetState: SettingsDestination
): ContentTransform {
    val isForward = initialState == SettingsDestination.Main && targetState != SettingsDestination.Main
    return if (isForward) {
        (slideInHorizontally { fullWidth -> fullWidth } + fadeIn()) togetherWith
            (slideOutHorizontally { fullWidth -> -fullWidth / 4 } + fadeOut())
    } else {
        (slideInHorizontally { fullWidth -> -fullWidth / 4 } + fadeIn()) togetherWith
            (slideOutHorizontally { fullWidth -> fullWidth } + fadeOut())
    }
}

fun factorsDestinationTransition(
    initialState: FactorsDestination,
    targetState: FactorsDestination
): ContentTransform {
    val isForward = initialState == FactorsDestination.Main && targetState != FactorsDestination.Main
    return if (isForward) {
        (slideInHorizontally { fullWidth -> fullWidth } + fadeIn()) togetherWith
                (slideOutHorizontally { fullWidth -> -fullWidth / 4 } + fadeOut())
    } else {
        (slideInHorizontally { fullWidth -> -fullWidth / 4 } + fadeIn()) togetherWith
                (slideOutHorizontally { fullWidth -> fullWidth } + fadeOut())
    }
}