package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Test

class FactorInputRoundingTest {

    @Test
    fun `normal case rounds a value up to the nearest quarter step`() {
        assertEquals("1,25", normalizeQuarterStepValue("1,1"))
    }

    @Test
    fun `boundary of a value already on a quarter step is left unchanged`() {
        assertEquals("2,5", normalizeQuarterStepValue("2,5"))
    }

    @Test
    fun `rounding up to a whole number drops the decimal separator`() {
        assertEquals("4", normalizeQuarterStepValue("3,8"))
    }

    @Test
    fun `invalid quarter step input yields an empty string`() {
        assertEquals("", normalizeQuarterStepValue("abc"))
        assertEquals("", normalizeQuarterStepValue(""))
    }

    @Test
    fun `normal case rounds an odd basal rate up to the next even number`() {
        assertEquals("6", normalizeBasalRateValue("5"))
    }

    @Test
    fun `boundary of an already even basal rate is left unchanged`() {
        assertEquals("4", normalizeBasalRateValue("4"))
    }

    @Test
    fun `invalid basal rate input yields an empty string`() {
        assertEquals("", normalizeBasalRateValue("abc"))
        assertEquals("", normalizeBasalRateValue(""))
    }
}
