package sevynidd.diabetesapp.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.screens.factors.FactorScreen
import sevynidd.diabetesapp.screens.factors.FactorScreenState
import sevynidd.diabetesapp.screens.settings.appearance.ThemeSettingsScreen
import sevynidd.diabetesapp.screens.settings.correction.CorrectionSettingsScreen
import sevynidd.diabetesapp.screens.settings.correction.toCorrectionSettingsValues
import sevynidd.diabetesapp.screens.settings.factor.FactorSettingsScreen
import sevynidd.diabetesapp.screens.settings.factor.FactorSettingsValues
import sevynidd.diabetesapp.screens.settings.language.LanguageSettingsScreen
import sevynidd.diabetesapp.screens.settings.notifications.NotificationSettingsScreen
import sevynidd.diabetesapp.screens.settings.profile.GenderSettingsScreen

/**
 * The first-run onboarding wizard: walks a new user through [OnboardingStep.entries] in order,
 * embedding the real settings/factor screens as steps rather than duplicating their UI.
 */
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    values: OnboardingValues = OnboardingValues(),
    callbacks: OnboardingCallbacks = OnboardingCallbacks()
) {
    var stepIndex by rememberSaveable { mutableIntStateOf(0) }
    val steps = OnboardingStep.entries
    val step = steps[stepIndex]

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        OnboardingHeader(
            stepIndex = stepIndex,
            totalSteps = steps.size,
            title = onboardingStepTitle(step, values.currentLanguage),
            currentLanguage = values.currentLanguage
        )

        OnboardingStepContent(
            step = step,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            values = values,
            callbacks = callbacks
        )

        OnboardingNavigationBar(
            currentLanguage = values.currentLanguage,
            isFirstStep = stepIndex == 0,
            isLastStep = stepIndex == steps.lastIndex,
            onBack = { stepIndex-- },
            onNext = {
                if (stepIndex == steps.lastIndex) {
                    callbacks.onFinished()
                } else {
                    stepIndex++
                }
            }
        )
    }
}

@Composable
private fun OnboardingHeader(
    stepIndex: Int,
    totalSteps: Int,
    title: String,
    currentLanguage: AppLanguage
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "${translate(TranslationKey.OnboardingStepLabel, currentLanguage)} ${stepIndex + 1}/$totalSteps",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 4.dp)
        )
        LinearProgressIndicator(
            progress = { (stepIndex + 1) / totalSteps.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
    }
}

@Composable
private fun OnboardingStepContent(
    step: OnboardingStep,
    modifier: Modifier,
    values: OnboardingValues,
    callbacks: OnboardingCallbacks
) {
    when (step) {
        OnboardingStep.Factors -> FactorScreen(
            modifier = modifier,
            currentLanguage = values.currentLanguage,
            state = FactorScreenState(
                factors = values.factors,
                isEditMode = true,
                onFactorsChange = callbacks.onFactorsChange
            )
        )

        OnboardingStep.Gender -> GenderSettingsScreen(
            modifier = modifier,
            currentLanguage = values.currentLanguage,
            currentGender = values.gender,
            onGenderChange = callbacks.onGenderChange
        )

        OnboardingStep.Appearance -> ThemeSettingsScreen(
            modifier = modifier,
            currentThemeMode = values.themeMode,
            currentContrastLevel = values.contrastLevel,
            currentLanguage = values.currentLanguage,
            onThemeModeChange = callbacks.onThemeModeChange,
            onContrastLevelChange = callbacks.onContrastLevelChange
        )

        OnboardingStep.Language -> LanguageSettingsScreen(
            modifier = modifier,
            currentLanguage = values.currentLanguage,
            onLanguageChange = callbacks.onLanguageChange
        )

        OnboardingStep.Notifications -> NotificationSettingsScreen(
            modifier = modifier,
            currentLanguage = values.currentLanguage,
            isBasalReminderEnabled = values.isBasalReminderEnabled,
            onBasalReminderEnabledChange = callbacks.onBasalReminderEnabledChange
        )

        OnboardingStep.FactorSettings -> FactorSettingsScreen(
            modifier = modifier,
            currentLanguage = values.currentLanguage,
            values = FactorSettingsValues(
                breadUnits = values.breadUnits,
                periodFactorPercent = values.periodFactorPercent,
                gender = values.gender
            ),
            onBreadUnitsChange = callbacks.onBreadUnitsChange,
            onPeriodFactorPercentChange = callbacks.onPeriodFactorPercentChange
        )

        OnboardingStep.Correction -> CorrectionSettingsScreen(
            modifier = modifier,
            currentLanguage = values.currentLanguage,
            values = values.correctionSettings.toCorrectionSettingsValues(),
            callbacks = callbacks.onCorrectionSettingsChange
        )
    }
}

private fun onboardingStepTitle(step: OnboardingStep, currentLanguage: AppLanguage): String {
    return when (step) {
        OnboardingStep.Factors -> translate(TranslationKey.DestinationFactors, currentLanguage)
        OnboardingStep.Gender -> translate(TranslationKey.GenderSettingsTitle, currentLanguage)
        OnboardingStep.Appearance -> translate(TranslationKey.Appearance, currentLanguage)
        OnboardingStep.Language -> translate(TranslationKey.Language, currentLanguage)
        OnboardingStep.Notifications -> translate(TranslationKey.NotificationSettingsTitle, currentLanguage)
        OnboardingStep.FactorSettings -> translate(TranslationKey.FactorSettingsTitle, currentLanguage)
        OnboardingStep.Correction -> translate(TranslationKey.CorrectionSettingsTitle, currentLanguage)
    }
}

@Composable
private fun OnboardingNavigationBar(
    currentLanguage: AppLanguage,
    isFirstStep: Boolean,
    isLastStep: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isFirstStep) {
            Spacer(modifier = Modifier)
        } else {
            OutlinedButton(onClick = onBack) {
                Text(translate(TranslationKey.ActionBack, currentLanguage))
            }
        }

        Button(onClick = onNext) {
            Text(
                text = if (isLastStep) {
                    translate(TranslationKey.ActionFinish, currentLanguage)
                } else {
                    translate(TranslationKey.ActionNext, currentLanguage)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenPreview() {
    OnboardingScreen()
}
