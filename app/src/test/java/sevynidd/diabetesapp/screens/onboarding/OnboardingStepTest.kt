package sevynidd.diabetesapp.screens.onboarding

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode

class OnboardingStepTest {

    @Test
    fun `pen therapy shows every onboarding step`() {
        assertEquals(OnboardingStep.entries, onboardingSteps(TherapyMode.Pen))
    }

    @Test
    fun `pump therapy skips the basal reminder step`() {
        val steps = onboardingSteps(TherapyMode.Pump)

        assertFalse(OnboardingStep.Notifications in steps)
        assertEquals(OnboardingStep.entries.size - 1, steps.size)
    }

    @Test
    fun `boundary the therapy choice comes before the skipped step so earlier positions never shift`() {
        val therapyIndex = OnboardingStep.entries.indexOf(OnboardingStep.FactorSettings)

        assertEquals(
            onboardingSteps(TherapyMode.Pen).take(therapyIndex + 1),
            onboardingSteps(TherapyMode.Pump).take(therapyIndex + 1)
        )
    }
}
