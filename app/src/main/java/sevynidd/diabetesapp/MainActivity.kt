package sevynidd.diabetesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.data.AppSettings
import sevynidd.diabetesapp.data.AppSettingsStore
import sevynidd.diabetesapp.data.database.BolusTemplatesRepository
import sevynidd.diabetesapp.data.database.DiabetesDatabase
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.notifications.BasalReminderScheduler
import sevynidd.diabetesapp.data.settings.ThemeMode
import sevynidd.diabetesapp.data.database.FactorsRepository
import sevynidd.diabetesapp.screens.BolusManagerMainWindow
import sevynidd.diabetesapp.ui.theme.BolusManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appSettingsStore = AppSettingsStore(applicationContext)
        val factorsRepository = FactorsRepository(
            DiabetesDatabase.getInstance(applicationContext).factorProfileDao()
        )
        val templatesRepository = BolusTemplatesRepository(
            DiabetesDatabase.getInstance(applicationContext).bolusTemplateDao()
        )
        val basalReminderScheduler = BasalReminderScheduler(applicationContext)

        setContent {
            val settings by appSettingsStore.settingsFlow.collectAsState(initial = AppSettings())
            val factors by factorsRepository.factorsFlow.collectAsState(initial = FactorsData())
            val templates by templatesRepository.templatesFlow.collectAsState(initial = emptyList())
            val lastDestination by appSettingsStore.lastDestinationFlow.collectAsState(initial = null)
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(factors.basalReminderEnabled, factors.basalTimeMinutes) {
                if (factors.basalReminderEnabled) {
                    basalReminderScheduler.scheduleDaily(factors.basalTimeMinutes)
                } else {
                    basalReminderScheduler.cancel()
                }
            }

            val darkTheme = when (settings.themeMode) {
                ThemeMode.System -> isSystemInDarkTheme()
                ThemeMode.Light -> false
                ThemeMode.Dark -> true
            }

            BolusManagerTheme(
                darkTheme = darkTheme,
                dynamicColor = true,
                contrastLevel = settings.contrastLevel
            ) {
                BolusManagerMainWindow(
                    themeMode = settings.themeMode,
                    contrastLevel = settings.contrastLevel,
                    currentLanguage = settings.language,
                    breadUnits = settings.breadUnits,
                    periodFactorPercent = settings.periodFactorPercent,
                    onThemeModeChange = { themeMode ->
                        coroutineScope.launch { appSettingsStore.setThemeMode(themeMode) }
                    },
                    onContrastLevelChange = { contrastLevel ->
                        coroutineScope.launch { appSettingsStore.setContrastLevel(contrastLevel) }
                    },
                    onLanguageChange = { language ->
                        coroutineScope.launch { appSettingsStore.setLanguage(language) }
                    },
                    onBreadUnitsChange = { breadUnits ->
                        coroutineScope.launch { appSettingsStore.setBreadUnits(breadUnits) }
                    },
                    onPeriodFactorPercentChange = { percentage ->
                        coroutineScope.launch { appSettingsStore.setPeriodFactorPercent(percentage) }
                    },
                    lastDestination = lastDestination,
                    onLastDestinationChange = { destination ->
                        coroutineScope.launch { appSettingsStore.setLastDestination(destination) }
                    },
                    factorData = factors,
                    onFactorSaveRequested = { updatedFactors ->
                        coroutineScope.launch { factorsRepository.saveFactors(updatedFactors) }
                    },
                    templates = templates,
                    onTemplateAddRequested = { name, emoji, carbohydrates ->
                        templatesRepository.addTemplate(name, emoji, carbohydrates)
                    },
                    onTemplateUpdateRequested = { template ->
                        templatesRepository.updateTemplate(template)
                    },
                    onTemplateDeleteRequested = { template ->
                        coroutineScope.launch {
                            templatesRepository.deleteTemplate(template)
                        }
                    },
                    onTemplateMarkUsedRequested = { templateId ->
                        coroutineScope.launch {
                            templatesRepository.markTemplateUsed(templateId)
                        }
                    }
                )
            }
        }
    }
}