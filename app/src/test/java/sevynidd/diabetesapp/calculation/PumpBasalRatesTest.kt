package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PumpBasalRatesTest {

    @Test
    fun `normal case rounds a typed hourly rate to two decimals`() {
        assertEquals("0,85", normalizeHourlyBasalRateValue("0,849"))
        assertEquals("1,2", normalizeHourlyBasalRateValue("1.2"))
    }

    @Test
    fun `boundary of an exact midpoint rounds half up`() {
        assertEquals("0,13", normalizeHourlyBasalRateValue("0,125"))
        assertEquals("1,01", normalizeHourlyBasalRateValue("1,005"))
    }

    @Test
    fun `zero is a valid hourly rate`() {
        assertEquals("0", normalizeHourlyBasalRateValue("0"))
        assertEquals("0", normalizeHourlyBasalRateValue("0,004"))
    }

    @Test
    fun `invalid or negative hourly rates yield an empty string`() {
        assertEquals("", normalizeHourlyBasalRateValue(""))
        assertEquals("", normalizeHourlyBasalRateValue("abc"))
        assertEquals("", normalizeHourlyBasalRateValue("-0,5"))
    }

    @Test
    fun `normal case daily total sums every set hour`() {
        val rates = List(24) { "0,5" }

        assertEquals(12.0, requireNotNull(pumpBasalDailyTotal(rates)), DELTA)
    }

    @Test
    fun `unset hours contribute nothing to the daily total`() {
        val rates = listOf("0,75", "", "1,25") + List(21) { "" }

        assertEquals(2.0, requireNotNull(pumpBasalDailyTotal(rates)), DELTA)
    }

    @Test
    fun `a profile with no set hour has no daily total`() {
        assertNull(pumpBasalDailyTotal(List(24) { "" }))
    }

    @Test
    fun `units are formatted with comma decimals and no trailing zeros`() {
        assertEquals("18,4", formatUnits(18.4))
        assertEquals("19", formatUnits(19.0))
    }

    @Test
    fun `hour range labels cover one hour and wrap at midnight`() {
        assertEquals("05:00 - 06:00", pumpBasalHourRangeLabel(5))
        assertEquals("00:00 - 01:00", pumpBasalHourRangeLabel(0))
        assertEquals("23:00 - 00:00", pumpBasalHourRangeLabel(23))
        assertEquals("01:00 - 02:00", pumpBasalHourRangeLabel(25))
    }

    private companion object {
        const val DELTA = 1e-9
    }
}
