package sevynidd.diabetesapp.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import sevynidd.diabetesapp.data.database.BolusTemplateEntity
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.ThemeMode
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
import sevynidd.diabetesapp.screens.calculate.TemplateManagerScreen
import sevynidd.diabetesapp.screens.factors.FactorEditSessionViewModel
import sevynidd.diabetesapp.screens.factors.FactorScreen
import sevynidd.diabetesapp.screens.factors.ScheduleFactorScreen
import sevynidd.diabetesapp.screens.settings.BreadUnitsSettingsScreen
import sevynidd.diabetesapp.screens.settings.SettingsScreen
import sevynidd.diabetesapp.screens.settings.ThemeSettingsScreen
import sevynidd.diabetesapp.screens.settings.LanguageSettingsScreen
import sevynidd.diabetesapp.ui.theme.ContrastLevel

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun BolusManagerMainWindow(
    themeMode: ThemeMode = ThemeMode.System,
    contrastLevel: ContrastLevel = ContrastLevel.Normal,
    currentLanguage: AppLanguage = AppLanguage.System,
    breadUnits: Double = 12.0,
    factorData: FactorsData = FactorsData(),
    onThemeModeChange: (ThemeMode) -> Unit = {},
    onContrastLevelChange: (ContrastLevel) -> Unit = {},
    onLanguageChange: (AppLanguage) -> Unit = {},
    onBreadUnitsChange: (Double) -> Unit = {},
    onFactorSaveRequested: (FactorsData) -> Unit = {},
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
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val lifecycleOwner = LocalLifecycleOwner.current
    var templatePrefillCarbohydrates by rememberSaveable { mutableStateOf<Double?>(null) }
    var templatePrefillToken by rememberSaveable { mutableIntStateOf(0) }

    fun leaveFactorsEditMode(shouldSave: Boolean) {
        factorEditorViewModel.leaveEditMode(shouldSave)
    }

    fun navigateTo(destination: AppDestinations) {
        if (destination == currentDestination) return

        if (currentDestination == AppDestinations.FACTORS) {
            leaveFactorsEditMode(shouldSave = true)
        }

        if (currentDestination == AppDestinations.CALCULATE) {
            calculateDestination = CalculateDestination.Main
        }

        currentDestination = destination
    }

    val requestBackgroundSave by rememberUpdatedState {
        if (activity?.isChangingConfigurations == true) {
            return@rememberUpdatedState
        }

        if (currentDestination == AppDestinations.FACTORS) {
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
                        navigateTo(it)
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                val topBarTitle = when (currentDestination) {
                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.Theme ->
                        translate(TranslationKey.Appearance, currentLanguage)

                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.Language ->
                        translate(TranslationKey.Language, currentLanguage)

                    AppDestinations.SETTINGS if settingsDestination == SettingsDestination.BreadUnits ->
                        translate(TranslationKey.BreadUnits, currentLanguage)

                    AppDestinations.FACTORS if factorsDestination == FactorsDestination.EditSchedule ->
                        translate(TranslationKey.ActionSchedule, currentLanguage)

                    AppDestinations.CALCULATE if calculateDestination == CalculateDestination.Templates ->
                        translate(TranslationKey.TemplatesTitle, currentLanguage)

                    else -> destinationLabel(currentDestination, currentLanguage)
                }

                TopAppBar(
                    title = { Text(topBarTitle) },
                    navigationIcon = {
                        if (currentDestination == AppDestinations.SETTINGS && settingsDestination != SettingsDestination.Main){
                            IconButton(onClick = { settingsDestination = SettingsDestination.Main }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        } else if (currentDestination == AppDestinations.FACTORS && factorsDestination == FactorsDestination.EditSchedule) {
                            IconButton(onClick = { factorsDestination = FactorsDestination.Main }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        } else if (currentDestination == AppDestinations.CALCULATE && calculateDestination == CalculateDestination.Templates) {
                            IconButton(onClick = { calculateDestination = CalculateDestination.Main }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                    }
                )
            }
        ) { innerPadding ->
            val contentModifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)

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
                                isEditMode = factorEditorState.isEditMode,
                                currentLanguage = currentLanguage,
                                factors = factorEditorState.factors,
                                onFactorsChange = factorEditorViewModel::updateDraft
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
                                factors = factorEditorState.factors,
                                breadUnits = breadUnits,
                                templatePrefillCarbohydrates = templatePrefillCarbohydrates,
                                templatePrefillToken = templatePrefillToken,
                                selectedMode = calculateBolusMode,
                                onSelectedModeChange = { calculateBolusMode = it }
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
                                onTemplateAddRequested = onTemplateAddRequested,
                                onTemplateUpdateRequested = onTemplateUpdateRequested,
                                onTemplateDeleteRequested = onTemplateDeleteRequested
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
                                onNavigateToTheme = { settingsDestination = SettingsDestination.Theme },
                                onNavigateToLanguage = { settingsDestination = SettingsDestination.Language },
                                onNavigateToBreadUnits = { settingsDestination = SettingsDestination.BreadUnits }
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

                            SettingsDestination.BreadUnits -> BreadUnitsSettingsScreen(
                                modifier = contentModifier,
                                currentBreadUnits = breadUnits,
                                currentLanguage = currentLanguage,
                                onBreadUnitsChange = onBreadUnitsChange,
                                onBackClick = { settingsDestination = SettingsDestination.Main }
                            )
                        }
                    }
                }
            }

            // Template manager now lives as a dedicated calculate sub-screen.
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
