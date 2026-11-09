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
        assertFalse(result.isLowBloodSugar)
    }

    @Test
    fun `meal and correction are summed and rounded to the dose step`() {
        val result = resolveDose(mealUnits = 3.2, rawBloodSugar = "190", correctionSettings = settings)

        assertEquals(4.2, result.breakdown!!.exactTotalUnits, 1e-9)
        assertEquals(4.0, result.breakdown!!.roundedTotalUnits, 0.0)
    }

    @Test
    fun `low blood sugar is flagged and the dose is clamped to zero`() {
        val result = resolveDose(mealUnits = 1.2, rawBloodSugar = "50", correctionSettings = settings)

        assertTrue(result.isLowBloodSugar)
        assertEquals(0.0, result.breakdown!!.roundedTotalUnits, 0.0)
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
    fun `normal dose with carbs but without a factor is blocked`() {
        val result = resolveNormalDose(
            carbohydratesValue = 48.0,
            activeFactor = null,
            effectiveBreadUnits = 12.0,
            bloodSugar = "",
            correctionSettings = settings
        )

        assertNull(result.breakdown)
        assertEquals(DoseBlocker.NoFactor, result.blocker)
    }

    @Test
    fun `blood sugar without carbs and without a factor still gives a correction dose`() {
        val result = resolveNormalDose(
            carbohydratesValue = null,
            activeFactor = null,
            effectiveBreadUnits = 12.0,
            bloodSugar = "190",
            correctionSettings = settings
        )

        assertNull(result.blocker)
        assertEquals(1.0, result.breakdown!!.exactTotalUnits, 1e-9)
    }

    @Test
    fun `blood sugar above the plausible limit blocks the dose`() {
        val result = resolveNormalDose(48.0, 1.5, 12.0, "601", settings.copy(maxBloodSugarMgDl = 600.0))

        assertNull(result.breakdown)
        assertEquals(DoseBlocker.BloodSugarTooHigh, result.blocker)
    }

    @Test
    fun `blood sugar exactly at the plausible limit is accepted`() {
        val result = resolveNormalDose(48.0, 1.5, 12.0, "600", settings.copy(maxBloodSugarMgDl = 600.0))

        assertNull(result.blocker)
    }

    @Test
    fun `mmol per litre blood sugar is compared with the limit in mg per dl`() {
        val mmolSettings = settings.copy(glucoseUnit = GlucoseUnit.MmolL, maxBloodSugarMgDl = 600.0)

        assertEquals(DoseBlocker.BloodSugarTooHigh, resolveNormalDose(null, 1.5, 12.0, "34", mmolSettings).blocker)
        assertNull(resolveNormalDose(null, 1.5, 12.0, "33", mmolSettings).blocker)
    }

    @Test
    fun `carbohydrates above the plausible limit block the dose`() {
        val result = resolveNormalDose(301.0, 1.5, 12.0, "", settings.copy(maxCarbohydratesGrams = 300.0))

        assertNull(result.breakdown)
        assertEquals(DoseBlocker.CarbohydratesTooHigh, result.blocker)
    }

    @Test
    fun `a limit of zero switches the plausibility checks off`() {
        val unlimited = settings.copy(maxBloodSugarMgDl = 0.0, maxCarbohydratesGrams = 0.0)

        assertNull(resolveNormalDose(5000.0, 1.5, 12.0, "5000", unlimited).blocker)
    }

    @Test
    fun `a blocked dose still reports low blood sugar`() {
        val result = resolveNormalDose(301.0, 1.5, 12.0, "50", settings.copy(maxCarbohydratesGrams = 300.0))

        assertEquals(DoseBlocker.CarbohydratesTooHigh, result.blocker)
        assertTrue(result.isLowBloodSugar)
    }

    @Test
    fun `split dose is blocked when a factor is missing`() {
        val inputs = SplitDoseInputs(splitBolus = null, carbohydratesGrams = 40.0, bloodSugar = "", hasFactors = false)

        assertEquals(DoseBlocker.NoFactor, resolveSplitDose(inputs, settings).blocker)
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
