package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Test

class CorrectionDoseCalculatorTest {

    @Test
    fun `normal case rounds the number of steps over the threshold to the nearest whole unit`() {
        assertEquals(0, calculateCorrectionUnits(bloodSugarMgDl = 160.0, thresholdMgDl = 160.0, stepMgDl = 30.0))
        assertEquals(0, calculateCorrectionUnits(bloodSugarMgDl = 170.0, thresholdMgDl = 160.0, stepMgDl = 30.0))
        assertEquals(1, calculateCorrectionUnits(bloodSugarMgDl = 180.0, thresholdMgDl = 160.0, stepMgDl = 30.0))
        assertEquals(1, calculateCorrectionUnits(bloodSugarMgDl = 190.0, thresholdMgDl = 160.0, stepMgDl = 30.0))
        assertEquals(2, calculateCorrectionUnits(bloodSugarMgDl = 220.0, thresholdMgDl = 160.0, stepMgDl = 30.0))
    }

    @Test
    fun `boundary of blood sugar exactly at the threshold yields zero units`() {
        val units = calculateCorrectionUnits(bloodSugarMgDl = 160.0, thresholdMgDl = 160.0, stepMgDl = 30.0)

        assertEquals(0, units)
    }

    @Test
    fun `boundary of exactly half a step rounds up to the next whole unit`() {
        val units = calculateCorrectionUnits(bloodSugarMgDl = 205.0, thresholdMgDl = 160.0, stepMgDl = 30.0)

        assertEquals(2, units)
    }

    @Test
    fun `edge case of blood sugar below the threshold yields zero units`() {
        val units = calculateCorrectionUnits(bloodSugarMgDl = 100.0, thresholdMgDl = 160.0, stepMgDl = 30.0)

        assertEquals(0, units)
    }

    @Test
    fun `edge case of a zero step yields zero units regardless of blood sugar`() {
        val units = calculateCorrectionUnits(bloodSugarMgDl = 300.0, thresholdMgDl = 160.0, stepMgDl = 0.0)

        assertEquals(0, units)
    }

    @Test
    fun `edge case of a negative step yields zero units regardless of blood sugar`() {
        val units = calculateCorrectionUnits(bloodSugarMgDl = 300.0, thresholdMgDl = 160.0, stepMgDl = -30.0)

        assertEquals(0, units)
    }
}
