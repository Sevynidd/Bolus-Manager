package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DoseBreakdownTest {
    @Test
    fun `dose is rounded to the nearest step`() {
        assertEquals(4.0, roundDoseToStep(4.2, 0.5), 0.0)
        assertEquals(4.5, roundDoseToStep(4.3, 0.5), 0.0)
        assertEquals(3.45, roundDoseToStep(3.47, 0.05), 0.0)
    }

    @Test
    fun `an exact half step rounds up`() {
        assertEquals(4.5, roundDoseToStep(4.25, 0.5), 0.0)
        assertEquals(0.2, roundDoseToStep(0.15, 0.1), 0.0)
    }

    @Test
    fun `a non-positive step leaves the dose unchanged`() {
        assertEquals(3.47, roundDoseToStep(3.47, 0.0), 0.0)
        assertEquals(3.47, roundDoseToStep(3.47, -0.5), 0.0)
    }

    @Test
    fun `breakdown sums meal and correction and rounds the total`() {
        val breakdown = buildDoseBreakdown(mealUnits = 3.2, correctionUnits = 1, stepUnits = 0.5)

        assertEquals(4.2, breakdown.exactTotalUnits, 1e-9)
        assertEquals(4.0, breakdown.roundedTotalUnits, 0.0)
    }

    @Test
    fun `a negative correction cannot make the total negative`() {
        val breakdown = buildDoseBreakdown(mealUnits = 1.2, correctionUnits = -2, stepUnits = 0.5)

        assertEquals(0.0, breakdown.exactTotalUnits, 0.0)
        assertEquals(0.0, breakdown.roundedTotalUnits, 0.0)
    }

    @Test
    fun `zero meal and zero correction give a zero dose`() {
        assertEquals(0.0, buildDoseBreakdown(0.0, 0, 0.5).roundedTotalUnits, 0.0)
    }

    @Test
    fun `blood sugar exactly at the low threshold is not low`() {
        assertTrue(isBelowLowThreshold(bloodSugarMgDl = 79.9, lowThresholdMgDl = 80.0))
        assertFalse(isBelowLowThreshold(bloodSugarMgDl = 80.0, lowThresholdMgDl = 80.0))
    }

    @Test
    fun `max dose is exceeded only strictly above the limit`() {
        assertTrue(isAboveMaxDose(units = 15.5, maxDoseUnits = 15.0))
        assertFalse(isAboveMaxDose(units = 15.0, maxDoseUnits = 15.0))
    }

    @Test
    fun `a max dose of zero switches the limit off`() {
        assertFalse(isAboveMaxDose(units = 500.0, maxDoseUnits = 0.0))
    }
}
