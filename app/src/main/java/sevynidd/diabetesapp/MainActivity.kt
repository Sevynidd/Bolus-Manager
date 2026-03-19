package sevynidd.diabetesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.data.AppSettings
import sevynidd.diabetesapp.data.AppSettingsStore
import sevynidd.diabetesapp.data.database.DiabetesDatabase
import sevynidd.diabetesapp.data.database.FactorsData
import sevynidd.diabetesapp.data.database.FactorsRepository
import sevynidd.diabetesapp.navigation.ThemeMode
import sevynidd.diabetesapp.screens.DiabetesAppMainWindow
import sevynidd.diabetesapp.screens.factors.FactorScreen
import sevynidd.diabetesapp.ui.theme.DiabetesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appSettingsStore = AppSettingsStore(applicationContext)
        val factorsRepository = FactorsRepository(
            DiabetesDatabase.getInstance(applicationContext).factorProfileDao()
        )

        setContent {
            val settings by appSettingsStore.settingsFlow.collectAsState(initial = AppSettings())
            val factors by factorsRepository.factorsFlow.collectAsState(initial = FactorsData())
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
                    },
                    factorData = factors,
                    onFactorSaveRequested = { updatedFactors ->
                        coroutineScope.launch { factorsRepository.saveFactors(updatedFactors) }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiabetesAppTheme {
        FactorScreen()
    }
}