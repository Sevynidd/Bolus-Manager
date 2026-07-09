package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorSlot

class ActiveFactorResolverTest {

    private val slots = listOf(
        FactorSlot(name = "Morning", factorValue = "1", startTimeMinutes = 300),
        FactorSlot(name = "Breakfast", factorValue = "2", startTimeMinutes = 540),
        FactorSlot(name = "Lunch", factorValue = "3", startTimeMinutes = 720),
        FactorSlot(name = "Afternoon", factorValue = "4", startTimeMinutes = 840),
        FactorSlot(name = "Dinner", factorValue = "5", startTimeMinutes = 1020),
        FactorSlot(name = "Late", factorValue = "6", startTimeMinutes = 1200),
        FactorSlot(name = "Night", factorValue = "0,5", startTimeMinutes = 1380)
    )

    @Test
    fun `normal case resolves the factor for the middle of a window`() {
        // 10:00, between breakfast (09:00) and lunch (12:00)
        val info = activeFactorForTime(slots, nowMinutes = 10 * 60)

        assertEquals(2.0, info.factor)
        assertEquals("Breakfast", info.factorName)
        assertEquals(180, info.durationMinutes)
        assertEquals(1, info.activeIndex)
    }

    @Test
    fun `boundary at exact window start resolves to that window, not the previous one`() {
        val info = activeFactorForTime(slots, nowMinutes = 540)

        assertEquals(2.0, info.factor)
        assertEquals("Breakfast", info.factorName)
    }

    @Test
    fun `boundary at night wraps past midnight to the morning window`() {
        val info = activeFactorForTime(slots, nowMinutes = 1380)

        assertEquals(0.5, info.factor)
        assertEquals("Night", info.factorName)
        assertEquals((MINUTES_PER_DAY - 1380) + 300, info.durationMinutes)
        assertEquals(6, info.activeIndex)
    }

    @Test
    fun `unparsable factor value resolves to a null factor`() {
        val slotsWithBlankBreakfast = slots.map { if (it.name == "Breakfast") it.copy(factorValue = "") else it }
        val info = activeFactorForTime(slotsWithBlankBreakfast, nowMinutes = 10 * 60)

        assertNull(info.factor)
        assertEquals("Breakfast", info.factorName)
    }

    @Test
    fun `works with a factor count other than seven`() {
        val threeSlots = listOf(
            FactorSlot(name = "Day", factorValue = "1", startTimeMinutes = 6 * 60),
            FactorSlot(name = "Evening", factorValue = "2", startTimeMinutes = 18 * 60),
            FactorSlot(name = "Night", factorValue = "3", startTimeMinutes = 22 * 60)
        )

        val info = activeFactorForTime(threeSlots, nowMinutes = 19 * 60)

        assertEquals(2.0, info.factor)
        assertEquals("Evening", info.factorName)
        assertEquals(1, info.activeIndex)
    }

    @Test
    fun `a single factor slot is active for the entire day`() {
        val singleSlot = listOf(FactorSlot(name = "AllDay", factorValue = "1,5", startTimeMinutes = 0))

        val info = activeFactorForTime(singleSlot, nowMinutes = 12 * 60)

        assertEquals(1.5, info.factor)
        assertEquals(MINUTES_PER_DAY, info.durationMinutes)
        assertEquals(0, info.activeIndex)
    }

    @Test
    fun `an empty factor list resolves to a null factor without throwing`() {
        val info = activeFactorForTime(emptyList(), nowMinutes = 12 * 60)

        assertNull(info.factor)
        assertEquals(-1, info.activeIndex)
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
