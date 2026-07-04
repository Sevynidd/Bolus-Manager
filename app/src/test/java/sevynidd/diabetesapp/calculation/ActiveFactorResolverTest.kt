package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.localization.TranslationKey

class ActiveFactorResolverTest {

    private val factors = FactorsData(
        morningFactor = "1",
        breakfastFactor = "2",
        lunchFactor = "3",
        afternoonFactor = "4",
        dinnerFactor = "5",
        lateFactor = "6",
        nightFactor = "0,5"
    )

    @Test
    fun `normal case resolves the factor for the middle of a window`() {
        // 10:00, between breakfast (09:00) and lunch (12:00)
        val info = activeFactorForTime(factors, nowMinutes = 10 * 60)

        assertEquals(2.0, info.factor)
        assertEquals(TranslationKey.FactorBreakfast, info.factorLabel)
        assertEquals(180, info.durationMinutes)
    }

    @Test
    fun `boundary at exact window start resolves to that window, not the previous one`() {
        // Exactly 09:00 (breakfastTimeMinutes) is no longer "before breakfast" (morning window)
        val info = activeFactorForTime(factors, nowMinutes = factors.breakfastTimeMinutes)

        assertEquals(2.0, info.factor)
        assertEquals(TranslationKey.FactorBreakfast, info.factorLabel)
    }

    @Test
    fun `boundary at night wraps past midnight to the morning window`() {
        // Exactly 23:00 (nightTimeMinutes) is excluded from the day range, so night factor applies
        val info = activeFactorForTime(factors, nowMinutes = factors.nightTimeMinutes)

        assertEquals(0.5, info.factor)
        assertEquals(TranslationKey.FactorNight, info.factorLabel)
        assertEquals((MINUTES_PER_DAY - factors.nightTimeMinutes) + factors.morningTimeMinutes, info.durationMinutes)
    }

    @Test
    fun `unparsable factor string resolves to a null factor`() {
        val info = activeFactorForTime(FactorsData(breakfastFactor = ""), nowMinutes = 10 * 60)

        assertNull(info.factor)
        assertEquals(TranslationKey.FactorBreakfast, info.factorLabel)
    }

    @Test
    fun `period multiplier scales the factor up by the given percent`() {
        val result = applyPeriodMultiplier(factor = 2.0, isPeriodEnabled = true, periodFactorPercent = 20.0)

        assertEquals(2.4, result!!, 0.0001)
    }

    @Test
    fun `period multiplier is a no-op when disabled`() {
        val result = applyPeriodMultiplier(factor = 2.0, isPeriodEnabled = false, periodFactorPercent = 50.0)

        assertEquals(2.0, result!!, 0.0001)
    }

    @Test
    fun `period multiplier treats a negative percent as zero`() {
        val result = applyPeriodMultiplier(factor = 2.0, isPeriodEnabled = true, periodFactorPercent = -50.0)

        assertEquals(2.0, result!!, 0.0001)
    }

    @Test
    fun `period multiplier passes through a null factor`() {
        val result = applyPeriodMultiplier(factor = null, isPeriodEnabled = true, periodFactorPercent = 20.0)

        assertNull(result)
    }
}
