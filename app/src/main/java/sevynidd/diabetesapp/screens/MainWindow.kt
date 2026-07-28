package sevynidd.diabetesapp.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import sevynidd.diabetesapp.data.database.BolusTemplateEntity
import sevynidd.diabetesapp.data.export.FactorsExportBundle
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.appearance.ThemeMode
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.profile.Gender
import sevynidd.diabetesapp.navigation.AppDestinations
import sevynidd.diabetesapp.navigation.destinationLabel
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.navigation.CalculateDestination
import sevynidd.diabetesapp.navigation.FactorsDestination
import sevynidd.diabetesapp.navigation.SettingsDestination
import sevynidd.diabetesapp.navigation.calculateDestinationTransition
import sevynidd.diabetesapp.navigation.factorsDestinationTransition
import sevynidd.diabetesapp.navigation.settingsDestinationTransition
import sevynidd.diabetesapp.screens.calculate.BolusMode
import sevynidd.diabetesapp.screens.calculate.CalculateScreen
import sevynidd.diabetesapp.screens.calculate.CalculateScreenCallbacks
import sevynidd.diabetesapp.screens.calculate.CalculateScreenValues
import sevynidd.diabetesapp.screens.calculate.TemplateEditorScreen
import sevynidd.diabetesapp.screens.calculate.TemplateManagerScreen
import sevynidd.diabetesapp.screens.factors.FactorEditSessionViewModel
import sevynidd.diabetesapp.screens.factors.FactorScreen
import sevynidd.diabetesapp.screens.factors.FactorScreenState
import sevynidd.diabetesapp.screens.factors.ScheduleFactorScreen
import sevynidd.diabetesapp.screens.settings.SettingsNavigationCallbacks
import sevynidd.diabetesapp.screens.settings.SettingsScreen
import sevynidd.diabetesapp.screens.settings.appearance.ThemeSettingsScreen
import sevynidd.diabetesapp.screens.settings.correction.CorrectionSettingsCallbacks
import sevynidd.diabetesapp.screens.settings.correction.CorrectionSettingsScreen
import sevynidd.diabetesapp.screens.settings.correction.toCorrectionSettingsValues
import sevynidd.diabetesapp.screens.settings.datamanagement.ImportExportSettingsScreen
import sevynidd.diabetesapp.screens.settings.factor.FactorSettingsScreen
import sevynidd.diabetesapp.screens.settings.factor.FactorSettingsValues
import sevynidd.diabetesapp.screens.settings.language.LanguageSettingsScreen
import sevynidd.diabetesapp.screens.settings.notifications.NotificationSettingsScreen
import sevynidd.diabetesapp.screens.settings.profile.GenderSettingsScreen
import sevynidd.diabetesapp.screens.settings.update.UpdateCheckViewModel
import sevynidd.diabetesapp.screens.settings.update.UpdateSettingsScreen
import sevynidd.diabetesapp.ui.theme.ContrastLevel

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun BolusManagerMainWindow(
    themeMode: ThemeMode = ThemeMode.System,
    contrastLevel: ContrastLevel = ContrastLevel.Normal,
    currentLanguage: AppLanguage = AppLanguage.System,
    breadUnits: Double = 12.0,
    periodFactorPercent: Double = 0.0,
    correctionSettings: CorrectionSettings = CorrectionSettings(),
    gender: Gender = Gender.PreferNotToSay,
    factorData: FactorsData = FactorsData(),
    onThemeModeChange: (ThemeMode) -> Unit = {},
    onContrastLevelChange: (ContrastLevel) -> Unit = {},
    onLanguageChange: (AppLanguage) -> Unit = {},
    onBreadUnitsChange: (Double) -> Unit = {},
    onPeriodFactorPercentChange: (Double) -> Unit = {},
    onCorrectionSettingsChange: CorrectionSettingsCallbacks = CorrectionSettingsCallbacks(),
    onGenderChange: (Gender) -> Unit = {},
    onReplayTutorialRequested: () -> Unit = {},
    onFactorSaveRequested: (FactorsData) -> Unit = {},
    onImportResult: (FactorsExportBundle) -> Unit = {},
    lastDestination: AppDestinations? = null,
    onLastDestinationChange: (AppDestinations) -> Unit = {},
    openAppUpdateOnLaunch: Boolean = false,
    templates: List<BolusTemplateEntity> = emptyList(),
    onTemplateAddRequested: suspend (name: String, emoji: String?, carbohydrates: Double) -> Boolean = { _, _, _ -> false },
    onTemplateUpdateRequested: suspend (BolusTemplateEntity) -> Boolean = { false },
    onTemplateDeleteRequested: (BolusTemplateEntity) -> Unit = {},
    onTemplateMarkUsedRequested: (Long) -> Unit = {}
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.FACTORS) }
    var settingsDestination by rememberSaveable { mutableStateOf(SettingsDestination.Main) }
    var factorsDestination by rememberSaveable { mutableStateOf(FactorsDestination.Main) }
    var calculateDestination by rememberSaveable { mutableStateOf(CalculateDestination.Main) }
    var calculateBolusMode by rememberSaveable { mutableStateOf(BolusMode.Normal) }
    val factorEditorViewModel: FactorEditSessionViewModel = viewModel()
    val factorEditorState = factorEditorViewModel.uiState
    val updateCheckViewModel: UpdateCheckViewModel = viewModel()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val lifecycleOwner = LocalLifecycleOwner.current
    var templatePrefillCarbohydrates by rememberSaveable { mutableStateOf<Double?>(null) }
    var templatePrefillToken by rememberSaveable { mutableIntStateOf(0) }
    var editingTemplateId by rememberSaveable { mutableStateOf<Long?>(null) }
    val editingTemplate = templates.firstOrNull { it.id == editingTemplateId }

    fun leaveFactorsEditMode(shouldSave: Boolean) {
        factorEditorViewModel.leaveEditMode(shouldSave)
    }

    val canNavigateUpWithinDestination = (
        currentDestination == AppDestinations.SETTINGS && settingsDestination != SettingsDestination.Main
        ) ||
        (currentDestination == AppDestinations.FACTORS && factorsDestination == FactorsDestination.EditSchedule) ||
        (currentDestination == AppDestinations.CALCULATE && calculateDestination != CalculateDestination.Main)

    fun navigateUpWithinDestination() {
        when (currentDestination) {
            AppDestinations.SETTINGS -> settingsDestination = SettingsDestination.Main
            AppDestinations.FACTORS -> factorsDestination = FactorsDestination.Main
            AppDestinations.CALCULATE -> {
                calculateDestination = when (calculateDestination) {
                    CalculateDestination.TemplateEditor -> CalculateDestination.Templates
                    else -> CalculateDestination.Main
                }
            }
        }
    }

    BackHandler(enabled = canNavigateUpWithinDestination) { navigateUpWithinDestination() }

    fun navigateTo(destination: AppDestinations) {
        if (destination == currentDestination) return

        // Clearing focus first commits any in-progress text field edit via its
        // onFocusChanged handler before the destination switch removes the field
        // from composition, which would otherwise silently drop the edit.
        focusManager.clearFocus(force = true)

        if (currentDestination == AppDestinations.FACTORS) {
            leaveFactorsEditMode(shouldSave = true)
        }

        if (currentDestination == AppDestinations.CALCULATE) {
            calculateDestination = CalculateDestination.Main
            editingTemplateId = null
        }

        currentDestination = destination
        onLastDestinationChange(destination)
    }

    // `lastDestination` is null until the persisted value loads; applying it only once it
    // resolves (rather than seeding the rememberSaveable default above) avoids briefly snapping
    // back to FACTORS before the real value arrives.
    LaunchedEffect(lastDestination) {
        lastDestination?.let { currentDestination = it }
    }

    LaunchedEffect(openAppUpdateOnLaunch) {
        if (openAppUpdateOnLaunch) {
            currentDestination = AppDestinations.SETTINGS
            settingsDestination = SettingsDestination.Updates
        }
    }

    val requestBackgroundSave by rememberUpdatedState {
        if (activity?.isChangingConfigurations == true) {
            return@rememberUpdatedState
        }

        if (currentDestination == AppDestinations.FACTORS) {
            focusManager.clearFocus(force = true)
            leaveFactorsEditMode(shouldSave = true)
        }
    }

    LaunchedEffect(factorData, factorEditorState.isEditMode, factorEditorState.pendingSave) {
        factorEditorViewModel.syncPersistedFactors(factorData)
    }

    LaunchedEffect(
        factorEditorState.isEditMode,
        factorEditorState.pendingSave,
        factorEditorState.factors
    ) {
        if (!factorEditorState.isEditMode && factorEditorState.pendingSave) {
            withFrameNanos { }
            factorEditorViewModel.consumePendingSave()?.let(onFactorSaveRequested)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                requestBackgroundSave()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val navigationLayoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())

    val mainContent: @Composable (bottomContentPadding: Dp, contentWindowInsets: WindowInsets) -> Unit =
        { bottomContentPadding, contentWindowInsets ->
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentWindowInsets = contentWindowInsets,
            topBar = {
                val topBarTitle = when (currentDestination) {
                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.Theme ->
                        translate(TranslationKey.Appearance, currentLanguage)

                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.Language ->
                        translate(TranslationKey.Language, currentLanguage)

                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.Gender ->
                        translate(TranslationKey.GenderSettingsTitle, currentLanguage)

                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.FactorSettings ->
                        translate(TranslationKey.FactorSettingsTitle, currentLanguage)

                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.Correction ->
                        translate(TranslationKey.CorrectionSettingsTitle, currentLanguage)

                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.Notifications ->
                        translate(TranslationKey.NotificationSettingsTitle, currentLanguage)

                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.DataManagement ->
                        translate(TranslationKey.DataManagementTitle, currentLanguage)

                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.Updates ->
                        translate(TranslationKey.AppUpdateTitle, currentLanguage)

                    AppDestinations.FACTORS if factorsDestination == FactorsDestination.EditSchedule ->
                        translate(TranslationKey.ActionSchedule, currentLanguage)

                    AppDestinations.CALCULATE if calculateDestination == CalculateDestination.Templates ->
                        translate(TranslationKey.TemplatesTitle, currentLanguage)

                    AppDestinations.CALCULATE if calculateDestination == CalculateDestination.TemplateEditor ->
                        translate(
                            if (editingTemplate != null) TranslationKey.TemplateEdit else TranslationKey.TemplateAdd,
                            currentLanguage
                        )

                    else -> destinationLabel(currentDestination, currentLanguage)
                }

                TopAppBar(
                    title = { Text(topBarTitle) },
                    navigationIcon = {
                        if (canNavigateUpWithinDestination) {
                            IconButton(onClick = { navigateUpWithinDestination() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = translate(TranslationKey.ActionBack, currentLanguage)
                                )
                            }
                        }
                    },
                    actions = {
                        if ((currentDestination == AppDestinations.FACTORS) &&
                                (factorsDestination == FactorsDestination.Main))
                        {
                            IconButton(onClick = {
                                factorsDestination = if (factorsDestination == FactorsDestination.EditSchedule) {
                                    FactorsDestination.Main
                                } else {
                                    FactorsDestination.EditSchedule
                                }
                            }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ChangeCircle,
                                    contentDescription = translate(TranslationKey.ActionSchedule, currentLanguage)
                                )
                            }

                            IconButton(onClick = {
                                if (factorEditorState.isEditMode) {
                                    leaveFactorsEditMode(shouldSave = true)
                                } else {
                                    factorEditorViewModel.startEditing()
                                }
                            }) {
                                Icon(
                                    imageVector = if (factorEditorState.isEditMode) Icons.Filled.Check else Icons.Filled.Edit,
                                    contentDescription = if (factorEditorState.isEditMode) {
                                        translate(TranslationKey.ActionSave, currentLanguage)
                                    } else {
                                        translate(TranslationKey.ActionEdit, currentLanguage)
                                    }
                                )
                            }
                        }

                        if (currentDestination == AppDestinations.CALCULATE && calculateDestination == CalculateDestination.Main) {
                            IconButton(onClick = { calculateDestination = CalculateDestination.Templates }) {
                                Icon(
                                    imageVector = Icons.Filled.Bookmark,
                                    contentDescription = translate(TranslationKey.ActionTemplates, currentLanguage)
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            val contentModifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = bottomContentPadding)

            when (currentDestination) {
                AppDestinations.FACTORS -> {
                    AnimatedContent(
                        targetState = factorsDestination,
                        label = "factors_navigation_animation",
                        transitionSpec = {
                            factorsDestinationTransition(initialState, targetState)
                        }
                    ) { destination ->
                        when (destination) {
                            FactorsDestination.Main -> FactorScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                state = FactorScreenState(
                                    factors = factorEditorState.factors,
                                    isEditMode = factorEditorState.isEditMode,
                                    onFactorsChange = factorEditorViewModel::updateDraft
                                )
                            )

                            FactorsDestination.EditSchedule -> ScheduleFactorScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                factors = factorEditorState.factors,
                                onFactorsChange = factorEditorViewModel::updateDraft
                            )
                        }
                    }
                }
                AppDestinations.CALCULATE -> {
                    AnimatedContent(
                        targetState = calculateDestination,
                        label = "calculate_navigation_animation",
                        transitionSpec = {
                            calculateDestinationTransition(initialState, targetState)
                        }
                    ) { destination ->
                        when (destination) {
                            CalculateDestination.Main -> CalculateScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                values = CalculateScreenValues(
                                    factors = factorEditorState.factors,
                                    breadUnits = breadUnits,
                                    periodFactorPercent = periodFactorPercent,
                                    correctionSettings = correctionSettings,
                                    gender = gender,
                                    templatePrefillCarbohydrates = templatePrefillCarbohydrates,
                                    templatePrefillToken = templatePrefillToken,
                                    selectedMode = calculateBolusMode
                                ),
                                callbacks = CalculateScreenCallbacks(
                                    onSelectedModeChange = { calculateBolusMode = it },
                                    onPeriodEnabledChange = factorEditorViewModel::updatePeriodEnabled
                                )
                            )

                            CalculateDestination.Templates -> TemplateManagerScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                templates = templates,
                                onTemplateSelected = { selectedTemplate ->
                                    templatePrefillCarbohydrates = selectedTemplate.carbohydrates
                                    templatePrefillToken += 1
                                    onTemplateMarkUsedRequested(selectedTemplate.id)
                                    calculateDestination = CalculateDestination.Main
                                },
                                onAddTemplateRequested = {
                                    editingTemplateId = null
                                    calculateDestination = CalculateDestination.TemplateEditor
                                },
                                onEditTemplateRequested = { template ->
                                    editingTemplateId = template.id
                                    calculateDestination = CalculateDestination.TemplateEditor
                                },
                                onTemplateDeleteRequested = onTemplateDeleteRequested
                            )

                            CalculateDestination.TemplateEditor -> TemplateEditorScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                template = editingTemplate,
                                templates = templates,
                                onAddRequested = onTemplateAddRequested,
                                onUpdateRequested = onTemplateUpdateRequested,
                                onSaved = { calculateDestination = CalculateDestination.Templates },
                                onCancel = { calculateDestination = CalculateDestination.Templates }
                            )
                        }
                    }
                }
                AppDestinations.SETTINGS -> {
                    AnimatedContent(
                        targetState = settingsDestination,
                        label = "settings_navigation_animation",
                        transitionSpec = {
                            settingsDestinationTransition(initialState, targetState)
                        }
                    ) { destination ->
                        when (destination) {
                            SettingsDestination.Main -> SettingsScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                navigation = SettingsNavigationCallbacks(
                                    onNavigateToTheme = { settingsDestination = SettingsDestination.Theme },
                                    onNavigateToLanguage = { settingsDestination = SettingsDestination.Language },
                                    onNavigateToGender = { settingsDestination = SettingsDestination.Gender },
                                    onNavigateToFactorSettings = {
                                        settingsDestination = SettingsDestination.FactorSettings
                                    },
                                    onNavigateToCorrection = {
                                        settingsDestination = SettingsDestination.Correction
                                    },
                                    onNavigateToNotifications = {
                                        settingsDestination = SettingsDestination.Notifications
                                    },
                                    onNavigateToDataManagement = {
                                        settingsDestination = SettingsDestination.DataManagement
                                    },
                                    onNavigateToUpdates = { settingsDestination = SettingsDestination.Updates },
                                    onReplayTutorial = onReplayTutorialRequested
                                )
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

                            SettingsDestination.Gender -> GenderSettingsScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                currentGender = gender,
                                onGenderChange = onGenderChange
                            )

                            SettingsDestination.FactorSettings -> FactorSettingsScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                values = FactorSettingsValues(
                                    breadUnits = breadUnits,
                                    periodFactorPercent = periodFactorPercent,
                                    gender = gender
                                ),
                                onBreadUnitsChange = onBreadUnitsChange,
                                onPeriodFactorPercentChange = onPeriodFactorPercentChange
                            )

                            SettingsDestination.Correction -> CorrectionSettingsScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                values = correctionSettings.toCorrectionSettingsValues(),
                                callbacks = onCorrectionSettingsChange
                            )

                            SettingsDestination.Notifications -> NotificationSettingsScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                isBasalReminderEnabled = factorEditorState.factors.basalReminderEnabled,
                                onBasalReminderEnabledChange = factorEditorViewModel::updateBasalReminderEnabled
                            )

                            SettingsDestination.DataManagement -> ImportExportSettingsScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                currentValues = FactorsExportBundle(
                                    factors = factorEditorState.factors,
                                    breadUnits = breadUnits,
                                    periodFactorPercent = periodFactorPercent,
                                    correctionSettings = correctionSettings,
                                    gender = gender
                                ),
                                onImportResult = onImportResult
                            )

                            SettingsDestination.Updates -> UpdateSettingsScreen(
                                modifier = contentModifier,
                                currentLanguage = currentLanguage,
                                uiState = updateCheckViewModel.uiState,
                                canRequestPackageInstalls = updateCheckViewModel.canRequestPackageInstalls(),
                                onCheckForUpdateRequested = updateCheckViewModel::checkForUpdate,
                                onDownloadAndInstallRequested = updateCheckViewModel::downloadAndInstall,
                                onRequestInstallPermission = updateCheckViewModel::requestInstallPermission
                            )
                        }
                    }
                }
            }
        }
    }

    if (navigationLayoutType == NavigationSuiteType.NavigationBar) {
        Box(modifier = Modifier.fillMaxSize()) {
            mainContent(floatingNavigationBarReservedHeight(), WindowInsets(0, 0, 0, 0))
            FloatingNavigationBar(
                currentDestination = currentDestination,
                currentLanguage = currentLanguage,
                onDestinationSelected = { navigateTo(it) },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    } else {
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
                        onClick = { navigateTo(it) }
                    )
                }
            },
            layoutType = navigationLayoutType
        ) {
            mainContent(0.dp, ScaffoldDefaults.contentWindowInsets)
        }
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
