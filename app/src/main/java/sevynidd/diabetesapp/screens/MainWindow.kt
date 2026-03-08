package sevynidd.diabetesapp.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.navigation.AppDestinations
import sevynidd.diabetesapp.navigation.destinationLabel
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.navigation.ThemeMode
import sevynidd.diabetesapp.settings.SettingsDestination
import sevynidd.diabetesapp.settings.SettingsScreen
import sevynidd.diabetesapp.settings.ThemeSettingsScreen
import sevynidd.diabetesapp.settings.LanguageSettingsScreen
import sevynidd.diabetesapp.ui.theme.ContrastLevel
import sevynidd.diabetesapp.ui.theme.DiabetesAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun DiabetesAppMainWindow(
    themeMode: ThemeMode = ThemeMode.System,
    contrastLevel: ContrastLevel = ContrastLevel.Normal,
    currentLanguage: AppLanguage = AppLanguage.System,
    onThemeModeChange: (ThemeMode) -> Unit = {},
    onContrastLevelChange: (ContrastLevel) -> Unit = {},
    onLanguageChange: (AppLanguage) -> Unit = {}
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.FACTORS) }
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var settingsDestination by rememberSaveable { mutableStateOf(SettingsDestination.Main) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = null
                        )
                    },
                    label = { Text(destinationLabel(it, currentLanguage)) },
                    selected = it == currentDestination,
                    onClick = {
                        currentDestination = it
                        isEditMode = false
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val topBarTitle = when {
                    currentDestination == AppDestinations.SETTINGS && settingsDestination == SettingsDestination.Theme ->
                        translate(TranslationKey.Appearance, currentLanguage)
                    currentDestination == AppDestinations.SETTINGS && settingsDestination == SettingsDestination.Language ->
                        translate(TranslationKey.Language, currentLanguage)
                    else -> destinationLabel(currentDestination, currentLanguage)
                }

                TopAppBar(
                    title = { Text(topBarTitle) },
                    navigationIcon = {
                        if (currentDestination == AppDestinations.SETTINGS && settingsDestination != SettingsDestination.Main) {
                            IconButton(onClick = { settingsDestination = SettingsDestination.Main }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    },
                    actions = {
                        if (currentDestination == AppDestinations.FACTORS) {
                            IconButton(onClick = { isEditMode = !isEditMode }) {
                                Icon(
                                    imageVector = if (isEditMode) Icons.Filled.Check else Icons.Filled.Edit,
                                    contentDescription = if (isEditMode) {
                                        translate(TranslationKey.ActionSave, currentLanguage)
                                    } else {
                                        translate(TranslationKey.ActionEdit, currentLanguage)
                                    }
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            val contentModifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)

            when (currentDestination) {
                AppDestinations.FACTORS -> FactorScreen(
                    modifier = contentModifier,
                    isEditMode = isEditMode,
                    currentLanguage = currentLanguage
                )

                AppDestinations.CALCULATE -> CalculateScreen(contentModifier)
                AppDestinations.SETTINGS -> {
                    when (settingsDestination) {
                        SettingsDestination.Main -> SettingsScreen(
                            modifier = contentModifier,
                            currentLanguage = currentLanguage,
                            onNavigateToTheme = { settingsDestination = SettingsDestination.Theme },
                            onNavigateToLanguage = { settingsDestination = SettingsDestination.Language }
                        )
                        SettingsDestination.Theme -> ThemeSettingsScreen(
                            modifier = contentModifier,
                            currentThemeMode = themeMode,
                            currentContrastLevel = contrastLevel,
                            currentLanguage = currentLanguage,
                            onThemeModeChange = onThemeModeChange,
                            onContrastLevelChange = onContrastLevelChange,
                            onBackClick = { settingsDestination = SettingsDestination.Main }
                        )
                        SettingsDestination.Language -> LanguageSettingsScreen(
                            modifier = contentModifier,
                            currentLanguage = currentLanguage,
                            onLanguageChange = onLanguageChange,
                            onBackClick = { settingsDestination = SettingsDestination.Main }
                        )
                    }
                }
            }
        }
    }
}

