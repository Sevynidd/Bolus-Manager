package sevynidd.diabetesapp.screens.onboarding

import sevynidd.diabetesapp.calculation.isBasalReminderApplicable
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode

/**
 * A step of the first-run onboarding wizard, in the order it's presented ([entries] order).
 * [Notifications] comes after [FactorSettings] (where the therapy is chosen) because it's skipped
 * for pump therapy — see [onboardingSteps] — and dropping a step only after the current one keeps
 * the wizard's position stable when the therapy changes.
 */
enum class OnboardingStep {
    Appearance, Language, Gender, FactorSettings, Notifications, Factors, Correction
}

/** The onboarding steps shown for [therapyMode]: all of them, minus the basal reminder for pump therapy. */
fun onboardingSteps(therapyMode: TherapyMode): List<OnboardingStep> =
    OnboardingStep.entries.filter { it != OnboardingStep.Notifications || isBasalReminderApplicable(therapyMode) }
