package sevynidd.diabetesapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.data.AppSettings
import sevynidd.diabetesapp.data.AppSettingsStore
import sevynidd.diabetesapp.data.database.BolusTemplatesRepository
import sevynidd.diabetesapp.data.database.DiabetesDatabase
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.notifications.AppUpdateNotifier
import sevynidd.diabetesapp.data.notifications.BasalReminderScheduler
import sevynidd.diabetesapp.data.notifications.EXTRA_OPEN_APP_UPDATE
import sevynidd.diabetesapp.data.settings.CorrectionSettings
import sevynidd.diabetesapp.data.settings.ThemeMode
import sevynidd.diabetesapp.data.database.FactorsRepository
import sevynidd.diabetesapp.data.update.GitHubRelease
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.screens.BolusManagerMainWindow
import sevynidd.diabetesapp.screens.onboarding.OnboardingCallbacks
import sevynidd.diabetesapp.screens.onboarding.OnboardingScreen
import sevynidd.diabetesapp.screens.onboarding.OnboardingValues
import sevynidd.diabetesapp.screens.settings.CorrectionSettingsCallbacks
import sevynidd.diabetesapp.screens.settings.UpdateCheckPhase
import sevynidd.diabetesapp.screens.settings.UpdateCheckViewModel
import sevynidd.diabetesapp.ui.theme.BolusManagerTheme

class MainActivity : ComponentActivity() {
    private var openAppUpdateOnLaunch by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        openAppUpdateOnLaunch = intent.consumeOpenAppUpdateExtra()

        val appSettingsStore = AppSettingsStore(applicationContext)
        val factorsRepository = FactorsRepository(
            DiabetesDatabase.getInstance(applicationContext).factorProfileDao(),
            DiabetesDatabase.getInstance(applicationContext).factorSlotDao()
        )
        val templatesRepository = BolusTemplatesRepository(
            DiabetesDatabase.getInstance(applicationContext).bolusTemplateDao()
        )
        val basalReminderScheduler = BasalReminderScheduler(applicationContext)

        setContent {
            val settingsState by appSettingsStore.settingsFlow.collectAsState(initial = null)
            val settings = settingsState ?: AppSettings()
            val correctionSettings = CorrectionSettings(
                thresholdMgDl = settings.correctionThresholdMgDl,
                stepMgDl = settings.correctionStepMgDl,
                glucoseUnit = settings.glucoseUnit
            )
            val factors by factorsRepository.factorsFlow.collectAsState(initial = FactorsData())
            val templates by templatesRepository.templatesFlow.collectAsState(initial = emptyList())
            val lastDestination by appSettingsStore.lastDestinationFlow.collectAsState(initial = null)
            val coroutineScope = rememberCoroutineScope()
            val context = LocalContext.current
            val updateCheckViewModel: UpdateCheckViewModel = viewModel()

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

            LaunchedEffect(factors.basalReminderEnabled, factors.basalTimeMinutes) {
                if (factors.basalReminderEnabled) {
                    basalReminderScheduler.scheduleDaily(factors.basalTimeMinutes)
                } else {
                    basalReminderScheduler.cancel()
                }
            }

            LaunchedEffect(Unit) {
                if (updateCheckViewModel.uiState.phase == UpdateCheckPhase.Idle) {
                    updateCheckViewModel.checkForUpdate()
                }
            }

            val showAppUpdateNotification by rememberUpdatedState { release: GitHubRelease ->
                AppUpdateNotifier.ensureChannel(
                    context,
                    translate(TranslationKey.AppUpdateChannelName, settings.language)
                )
                AppUpdateNotifier.show(
                    context = context,
                    title = translate(TranslationKey.AppUpdateAvailable, settings.language),
                    body = translate(TranslationKey.AppUpdateNotificationBody, settings.language),
                    versionTag = release.tagName,
                    actionLabel = translate(TranslationKey.AppUpdateDownloadButton, settings.language)
                )
            }

            val notificationPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (granted) {
                    updateCheckViewModel.uiState.latestRelease?.let(showAppUpdateNotification)
                }
            }

            LaunchedEffect(updateCheckViewModel.uiState.phase, updateCheckViewModel.uiState.latestRelease) {
                val updateState = updateCheckViewModel.uiState
                val latestRelease = updateState.latestRelease
                if (updateState.phase == UpdateCheckPhase.UpdateAvailable && latestRelease != null) {
                    if (context.hasNotificationPermission()) {
                        showAppUpdateNotification(latestRelease)
                    } else {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
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
                            onFactorsChange = { updatedFactors ->
                                coroutineScope.launch { factorsRepository.saveFactors(updatedFactors) }
                            },
                            onPeriodEnabledChange = { enabled ->
                                coroutineScope.launch {
                                    factorsRepository.saveFactors(factors.copy(isPeriodEnabled = enabled))
                                }
                            },
                            onBreadUnitsChange = { breadUnits ->
                                coroutineScope.launch { appSettingsStore.setBreadUnits(breadUnits) }
                            },
                            onPeriodFactorPercentChange = { percentage ->
                                coroutineScope.launch { appSettingsStore.setPeriodFactorPercent(percentage) }
                            },
                            onBasalReminderEnabledChange = { enabled ->
                                coroutineScope.launch {
                                    factorsRepository.saveFactors(factors.copy(basalReminderEnabled = enabled))
                                }
                            },
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
                        openAppUpdateOnLaunch = openAppUpdateOnLaunch,
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        openAppUpdateOnLaunch = intent.consumeOpenAppUpdateExtra()
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
        onCorrectionStepMgDlChange = { stepMgDl ->
            coroutineScope.launch {
                appSettingsStore.setCorrectionSettings(correctionSettings.copy(stepMgDl = stepMgDl))
            }
        },
        onGlucoseUnitChange = { glucoseUnit ->
            coroutineScope.launch {
                appSettingsStore.setCorrectionSettings(correctionSettings.copy(glucoseUnit = glucoseUnit))
            }
        }
    )
}

/** Reads and clears [EXTRA_OPEN_APP_UPDATE] so a retained intent can't re-trigger navigation later. */
private fun Intent.consumeOpenAppUpdateExtra(): Boolean {
    val shouldOpen = getBooleanExtra(EXTRA_OPEN_APP_UPDATE, false)
    removeExtra(EXTRA_OPEN_APP_UPDATE)
    return shouldOpen
}

private fun Context.hasNotificationPermission(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true

    return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
}