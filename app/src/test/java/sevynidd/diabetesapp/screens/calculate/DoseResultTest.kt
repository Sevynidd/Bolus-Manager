package sevynidd.diabetesapp.screens.calculate

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit

class DoseResultTest {
    private val settings = CorrectionSettings(
        thresholdMgDl = 160.0,
        lowThresholdMgDl = 80.0,
        stepMgDl = 30.0,
        doseStepUnits = 0.5
    )

    @Test
    fun `nothing entered gives no breakdown`() {
        val result = resolveDose(mealUnits = null, rawBloodSugar = "", correctionSettings = settings)

        assertNull(result.breakdown)
        assertEquals("", result.correctionUnitsText)
        assertFalse(result.isLowBloodSugar)
    }

    @Test
    fun `meal and correction are summed and rounded to the dose step`() {
        val result = resolveDose(mealUnits = 3.2, rawBloodSugar = "190", correctionSettings = settings)

        assertEquals(4.2, result.breakdown!!.exactTotalUnits, 1e-9)
        assertEquals(4.0, result.breakdown!!.roundedTotalUnits, 0.0)
        assertEquals("1", result.correctionUnitsText)
    }

    @Test
    fun `low blood sugar is flagged and the dose is clamped to zero`() {
        val result = resolveDose(mealUnits = 1.2, rawBloodSugar = "50", correctionSettings = settings)

        assertTrue(result.isLowBloodSugar)
        assertEquals(0.0, result.breakdown!!.roundedTotalUnits, 0.0)
        assertEquals("-1", result.correctionUnitsText)
    }

    @Test
    fun `blood sugar exactly at the low threshold is not flagged`() {
        val result = resolveDose(mealUnits = 1.0, rawBloodSugar = "80", correctionSettings = settings)

        assertFalse(result.isLowBloodSugar)
    }

    @Test
    fun `mmol per litre input is converted before checking the low threshold`() {
        val mmolSettings = settings.copy(glucoseUnit = GlucoseUnit.MmolL)

        assertTrue(resolveDose(null, "3,4", mmolSettings).isLowBloodSugar)
        assertFalse(resolveDose(null, "5,5", mmolSettings).isLowBloodSugar)
    }

    @Test
    fun `a dose above the maximum is flagged but the limit is off at zero`() {
        val limited = settings.copy(maxDoseUnits = 10.0)

        assertTrue(resolveDose(mealUnits = 12.0, rawBloodSugar = "", correctionSettings = limited).isOverMaxDose)
        assertFalse(resolveDose(mealUnits = 10.0, rawBloodSugar = "", correctionSettings = limited).isOverMaxDose)
        assertFalse(resolveDose(mealUnits = 500.0, rawBloodSugar = "", correctionSettings = settings).isOverMaxDose)
    }

    @Test
    fun `normal dose without a factor treats the meal as zero units`() {
        val result = resolveNormalDose(
            carbohydratesValue = 48.0,
            activeFactor = null,
            effectiveBreadUnits = 12.0,
            bloodSugar = "",
            correctionSettings = settings
        )

        assertEquals(0.0, result.breakdown!!.roundedTotalUnits, 0.0)
    }

    @Test
    fun `normal dose uses carbs bread units and factor`() {
        val result = resolveNormalDose(
            carbohydratesValue = 48.0,
            activeFactor = 1.5,
            effectiveBreadUnits = 12.0,
            bloodSugar = "",
            correctionSettings = settings
        )

        assertEquals(6.0, result.breakdown!!.exactTotalUnits, 1e-9)
    }
}
