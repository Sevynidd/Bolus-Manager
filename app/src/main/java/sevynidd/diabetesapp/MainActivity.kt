package sevynidd.diabetesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.ui.theme.ContrastLevel
import sevynidd.diabetesapp.ui.theme.DiabetesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appSettingsStore = AppSettingsStore(applicationContext)

        setContent {
            val settings by appSettingsStore.settingsFlow.collectAsState(initial = AppSettings())
            val coroutineScope = rememberCoroutineScope()

            val darkTheme = when (settings.themeMode) {
                ThemeMode.System -> isSystemInDarkTheme()
                ThemeMode.Light -> false
                ThemeMode.Dark -> true
            }

            DiabetesAppTheme(
                darkTheme = darkTheme,
                dynamicColor = false,
                contrastLevel = settings.contrastLevel
            ) {
                DiabetesAppMainWindow(
                    themeMode = settings.themeMode,
                    contrastLevel = settings.contrastLevel,
                    currentLanguage = settings.language,
                    onThemeModeChange = { themeMode ->
                        coroutineScope.launch { appSettingsStore.setThemeMode(themeMode) }
                    },
                    onContrastLevelChange = { contrastLevel ->
                        coroutineScope.launch { appSettingsStore.setContrastLevel(contrastLevel) }
                    },
                    onLanguageChange = { language ->
                        coroutineScope.launch { appSettingsStore.setLanguage(language) }
                    }
                )
            }
        }
    }
}

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
                TopAppBar(
                    title = { Text(destinationLabel(currentDestination, currentLanguage)) },
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
                AppDestinations.SETTINGS -> SettingsScreen(
                    modifier = contentModifier,
                    currentThemeMode = themeMode,
                    currentContrastLevel = contrastLevel,
                    currentLanguage = currentLanguage,
                    onThemeModeChange = onThemeModeChange,
                    onContrastLevelChange = onContrastLevelChange,
                    onLanguageChange = onLanguageChange
                )
            }
        }
    }
}

enum class AppDestinations(
    val icon: ImageVector,
) {
    FACTORS(Icons.Filled.Percent),
    CALCULATE(Icons.Filled.Calculate),
    SETTINGS(Icons.Filled.Settings),
}

private fun destinationLabel(destination: AppDestinations, language: AppLanguage): String {
    return when (destination) {
        AppDestinations.FACTORS -> translate(TranslationKey.DestinationFactors, language)
        AppDestinations.CALCULATE -> translate(TranslationKey.DestinationCalculate, language)
        AppDestinations.SETTINGS -> translate(TranslationKey.DestinationSettings, language)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiabetesAppTheme {
        FactorScreen()
    }
}