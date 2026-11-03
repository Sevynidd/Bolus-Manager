package sevynidd.diabetesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.calculation.diffFactorChanges
import sevynidd.diabetesapp.calculation.shouldScheduleBasalReminder
import sevynidd.diabetesapp.data.AppSettings
import sevynidd.diabetesapp.data.AppSettingsStore
import sevynidd.diabetesapp.data.database.BolusTemplatesRepository
import sevynidd.diabetesapp.data.database.DiabetesDatabase
import sevynidd.diabetesapp.data.database.FactorAuditLogRepository
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.notifications.BasalReminderScheduler
import sevynidd.diabetesapp.data.settings.appearance.ThemeMode
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.data.database.FactorsRepository
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.screens.BolusManagerMainWindow
import sevynidd.diabetesapp.screens.onboarding.OnboardingCallbacks
import sevynidd.diabetesapp.screens.onboarding.OnboardingScreen
import sevynidd.diabetesapp.screens.onboarding.OnboardingValues
import sevynidd.diabetesapp.screens.settings.correction.CorrectionSettingsCallbacks
import sevynidd.diabetesapp.ui.theme.BolusManagerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appSettingsStore = AppSettingsStore(applicationContext)
        val factorsRepository = FactorsRepository(
            DiabetesDatabase.getInstance(applicationContext).factorProfileDao(),
            DiabetesDatabase.getInstance(applicationContext).factorSlotDao(),
            DiabetesDatabase.getInstance(applicationContext).pumpBasalRateDao()
        )
        val templatesRepository = BolusTemplatesRepository(
            DiabetesDatabase.getInstance(applicationContext).bolusTemplateDao()
        )
        val factorAuditLogRepository = FactorAuditLogRepository(
            DiabetesDatabase.getInstance(applicationContext).factorAuditLogDao()
        )
        val basalReminderScheduler = BasalReminderScheduler(applicationContext)

        setContent {
            val settingsState by appSettingsStore.settingsFlow.collectAsState(initial = null)
            val settings = settingsState ?: AppSettings()
            val correctionSettings = CorrectionSettings(
                thresholdMgDl = settings.correctionThresholdMgDl,
                lowThresholdMgDl = settings.correctionLowThresholdMgDl,
                stepMgDl = settings.correctionStepMgDl,
                glucoseUnit = settings.glucoseUnit
            )
            val factors by factorsRepository.factorsFlow.collectAsState(initial = FactorsData())
            val auditLog by factorAuditLogRepository.auditLogFlow.collectAsState(initial = emptyList())
            val templates by templatesRepository.templatesFlow.collectAsState(initial = emptyList())
            val lastDestination by appSettingsStore.lastDestinationFlow.collectAsState(initial = null)
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                factorsRepository.seedDefaultFactorsIfEmpty(
                    defaultNames = listOf(
                        translate(TranslationKey.FactorMorning, settings.language),
                        translate(TranslationKey.FactorBreakfast, settings.language),
                        translate(TranslationKey.FactorLunch, settings.language),
                        translate(TranslationKey.FactorAfternoon, settings.language),
                        translate(TranslationKey.FactorDinner, settings.language),
                        translate(TranslationKey.FactorLate, settings.language),
                        translate(TranslationKey.FactorNight, settings.language)
                    )
                )
            }

            LaunchedEffect(factors.basalReminderEnabled, factors.basalTimeMinutes, settings.therapyMode) {
                if (shouldScheduleBasalReminder(factors.basalReminderEnabled, settings.therapyMode)) {
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
                when {
                    settingsState == null -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )

                    !settings.hasCompletedOnboarding -> OnboardingScreen(
                        values = OnboardingValues(
                            currentLanguage = settings.language,
                            themeMode = settings.themeMode,
                            contrastLevel = settings.contrastLevel,
                            gender = settings.gender,
                            factors = factors,
                            breadUnits = settings.breadUnits,
                            periodFactorPercent = settings.periodFactorPercent,
                            therapyMode = settings.therapyMode,
                            correctionSettings = correctionSettings,
                            isBasalReminderEnabled = factors.basalReminderEnabled
                        ),
                        callbacks = OnboardingCallbacks(
                            onThemeModeChange = { themeMode ->
                                coroutineScope.launch { appSettingsStore.setThemeMode(themeMode) }
                            },
                            onContrastLevelChange = { contrastLevel ->
                                coroutineScope.launch { appSettingsStore.setContrastLevel(contrastLevel) }
                            },
                            onLanguageChange = { language ->
                                coroutineScope.launch { appSettingsStore.setLanguage(language) }
                            },
                            onGenderChange = { gender ->
                                coroutineScope.launch { appSettingsStore.setGender(gender) }
                            },
                            onFactorsChange = { transform ->
                                coroutineScope.launch { factorsRepository.saveFactors(transform(factors)) }
                            },
                            onBreadUnitsChange = { breadUnits ->
                                coroutineScope.launch { appSettingsStore.setBreadUnits(breadUnits) }
                            },
                            onPeriodFactorPercentChange = { percentage ->
                                coroutineScope.launch { appSettingsStore.setPeriodFactorPercent(percentage) }
                            },
                            onTherapyModeChange = { mode ->
                                coroutineScope.launch { appSettingsStore.setTherapyMode(mode) }
                            },
                            onBasalReminderEnabledChange = { enabled ->
                                coroutineScope.launch {
                                    factorsRepository.saveFactors(factors.copy(basalReminderEnabled = enabled))
                                }
                            },
                            onGlucoseUnitChange = glucoseUnitChangeCallback(
                                coroutineScope,
                                appSettingsStore,
                                correctionSettings
                            ),
                            onCorrectionSettingsChange = correctionSettingsCallbacks(
                                coroutineScope,
                                appSettingsStore,
                                correctionSettings
                            ),
                            onFinished = {
                                coroutineScope.launch { appSettingsStore.setOnboardingCompleted(true) }
                            }
                        )
                    )

                    else -> BolusManagerMainWindow(
                        themeMode = settings.themeMode,
                        contrastLevel = settings.contrastLevel,
                        currentLanguage = settings.language,
                        breadUnits = settings.breadUnits,
                        periodFactorPercent = settings.periodFactorPercent,
                        therapyMode = settings.therapyMode,
                        correctionSettings = correctionSettings,
                        gender = settings.gender,
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
                        onTherapyModeChange = { mode ->
                            coroutineScope.launch { appSettingsStore.setTherapyMode(mode) }
                        },
                        onGlucoseUnitChange = glucoseUnitChangeCallback(
                            coroutineScope,
                            appSettingsStore,
                            correctionSettings
                        ),
                        onCorrectionSettingsChange = correctionSettingsCallbacks(
                            coroutineScope,
                            appSettingsStore,
                            correctionSettings
                        ),
                        onGenderChange = { gender ->
                            coroutineScope.launch { appSettingsStore.setGender(gender) }
                        },
                        onReplayTutorialRequested = {
                            coroutineScope.launch { appSettingsStore.setOnboardingCompleted(false) }
                        },
                        lastDestination = lastDestination,
                        onLastDestinationChange = { destination ->
                            coroutineScope.launch { appSettingsStore.setLastDestination(destination) }
                        },
                        factorData = factors,
                        onFactorSaveRequested = { updatedFactors ->
                            val previousFactors = factors
                            coroutineScope.launch {
                                factorsRepository.saveFactors(updatedFactors)
                                factorAuditLogRepository.logChanges(diffFactorChanges(previousFactors, updatedFactors))
                            }
                        },
                        onImportResult = { imported ->
                            val previousFactors = factors
                            coroutineScope.launch {
                                factorsRepository.saveFactors(imported.factors)
                                val changes = diffFactorChanges(previousFactors, imported.factors)
                                factorAuditLogRepository.logChanges(changes)
                                appSettingsStore.setBreadUnits(imported.breadUnits)
                                appSettingsStore.setPeriodFactorPercent(imported.periodFactorPercent)
                                appSettingsStore.setCorrectionSettings(imported.correctionSettings)
                                appSettingsStore.setGender(imported.gender)
                                appSettingsStore.setTherapyMode(imported.therapyMode)
                            }
                        },
                        auditLog = auditLog,
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

}

/** Builds the correction-settings edit callbacks, shared between the onboarding wizard and Settings. */
private fun correctionSettingsCallbacks(
    coroutineScope: CoroutineScope,
    appSettingsStore: AppSettingsStore,
    correctionSettings: CorrectionSettings
): CorrectionSettingsCallbacks {
    return CorrectionSettingsCallbacks(
        onCorrectionThresholdMgDlChange = { thresholdMgDl ->
            coroutineScope.launch {
                appSettingsStore.setCorrectionSettings(correctionSettings.copy(thresholdMgDl = thresholdMgDl))
            }
        },
        onCorrectionLowThresholdMgDlChange = { lowThresholdMgDl ->
            coroutineScope.launch {
                appSettingsStore.setCorrectionSettings(correctionSettings.copy(lowThresholdMgDl = lowThresholdMgDl))
            }
        },
        onCorrectionStepMgDlChange = { stepMgDl ->
            coroutineScope.launch {
                appSettingsStore.setCorrectionSettings(correctionSettings.copy(stepMgDl = stepMgDl))
            }
        }
    )
}

private fun glucoseUnitChangeCallback(
    coroutineScope: CoroutineScope,
    appSettingsStore: AppSettingsStore,
    correctionSettings: CorrectionSettings
): (GlucoseUnit) -> Unit = { glucoseUnit ->
    coroutineScope.launch {
        appSettingsStore.setCorrectionSettings(correctionSettings.copy(glucoseUnit = glucoseUnit))
    }
}
