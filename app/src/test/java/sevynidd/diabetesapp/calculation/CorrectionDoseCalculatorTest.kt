package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Test

class CorrectionDoseCalculatorTest {

    @Test
    fun `normal case rounds the number of steps over the threshold to the nearest whole unit`() {
        assertEquals(0, correctionUnits(bloodSugarMgDl = 160.0))
        assertEquals(0, correctionUnits(bloodSugarMgDl = 170.0))
        assertEquals(1, correctionUnits(bloodSugarMgDl = 180.0))
        assertEquals(1, correctionUnits(bloodSugarMgDl = 190.0))
        assertEquals(2, correctionUnits(bloodSugarMgDl = 220.0))
    }

    @Test
    fun `normal case rounds the number of steps under the low threshold to a negative whole unit`() {
        assertEquals(0, correctionUnits(bloodSugarMgDl = 80.0))
        assertEquals(0, correctionUnits(bloodSugarMgDl = 70.0))
        assertEquals(-1, correctionUnits(bloodSugarMgDl = 60.0))
        assertEquals(-1, correctionUnits(bloodSugarMgDl = 50.0))
        assertEquals(-2, correctionUnits(bloodSugarMgDl = 20.0))
    }

    @Test
    fun `boundary of blood sugar exactly at the threshold yields zero units`() {
        val units = correctionUnits(bloodSugarMgDl = 160.0)

        assertEquals(0, units)
    }

    @Test
    fun `boundary of blood sugar exactly at the low threshold yields zero units`() {
        val units = correctionUnits(bloodSugarMgDl = 80.0)

        assertEquals(0, units)
    }

    @Test
    fun `boundary of exactly half a step above the threshold rounds up to the next whole unit`() {
        val units = correctionUnits(bloodSugarMgDl = 205.0)

        assertEquals(2, units)
    }

    @Test
    fun `boundary of exactly half a step below the low threshold rounds toward positive infinity`() {
        val units = correctionUnits(bloodSugarMgDl = 35.0)

        assertEquals(-1, units)
    }

    @Test
    fun `edge case of blood sugar between the two thresholds yields zero units`() {
        val units = correctionUnits(bloodSugarMgDl = 100.0)

        assertEquals(0, units)
    }

    @Test
    fun `edge case of a zero step yields zero units regardless of blood sugar`() {
        val above = calculateCorrectionUnits(
            bloodSugarMgDl = 300.0,
            thresholdMgDl = 160.0,
            lowThresholdMgDl = 80.0,
            stepMgDl = 0.0
        )
        val below = calculateCorrectionUnits(
            bloodSugarMgDl = 20.0,
            thresholdMgDl = 160.0,
            lowThresholdMgDl = 80.0,
            stepMgDl = 0.0
        )

        assertEquals(0, above)
        assertEquals(0, below)
    }

    @Test
    fun `edge case of a negative step yields zero units regardless of blood sugar`() {
        val above = calculateCorrectionUnits(
            bloodSugarMgDl = 300.0,
            thresholdMgDl = 160.0,
            lowThresholdMgDl = 80.0,
            stepMgDl = -30.0
        )
        val below = calculateCorrectionUnits(
            bloodSugarMgDl = 20.0,
            thresholdMgDl = 160.0,
            lowThresholdMgDl = 80.0,
            stepMgDl = -30.0
        )

        assertEquals(0, above)
        assertEquals(0, below)
    }

    private fun correctionUnits(bloodSugarMgDl: Double): Int = calculateCorrectionUnits(
        bloodSugarMgDl = bloodSugarMgDl,
        thresholdMgDl = 160.0,
        lowThresholdMgDl = 80.0,
        stepMgDl = 30.0
    )
}
