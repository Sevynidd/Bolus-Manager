package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorSlot

class FactorSchedulingTest {

    private val defaultSlots = listOf(
        FactorSlot(name = "Morning", startTimeMinutes = 300),
        FactorSlot(name = "Breakfast", startTimeMinutes = 540),
        FactorSlot(name = "Lunch", startTimeMinutes = 720),
        FactorSlot(name = "Afternoon", startTimeMinutes = 840),
        FactorSlot(name = "Dinner", startTimeMinutes = 1020),
        FactorSlot(name = "Late", startTimeMinutes = 1200),
        FactorSlot(name = "Night", startTimeMinutes = 1380)
    )

    @Test
    fun `normal case moves a slot's time without touching unrelated neighbors`() {
        val updated = defaultSlots.withUpdatedTime(index = 2, selectedMinutes = 700) // Lunch

        assertEquals(700, updated.first { it.name == "Lunch" }.startTimeMinutes)
        assertEquals(540, updated.first { it.name == "Breakfast" }.startTimeMinutes)
        assertEquals(840, updated.first { it.name == "Afternoon" }.startTimeMinutes)
    }

    @Test
    fun `boundary of colliding with the next slot pushes it forward by a minute`() {
        // Default lunch is 720; moving breakfast onto it must not create a tie.
        val updated = defaultSlots.withUpdatedTime(index = 1, selectedMinutes = 720) // Breakfast

        assertEquals(720, updated.first { it.name == "Breakfast" }.startTimeMinutes)
        assertEquals(721, updated.first { it.name == "Lunch" }.startTimeMinutes)
        assertEquals(840, updated.first { it.name == "Afternoon" }.startTimeMinutes)
    }

    @Test
    fun `moving a slot earlier than a previous neighbor reorders the list`() {
        // Moving Lunch to 06:00 should place it right after Morning (05:00), before Breakfast (09:00).
        val updated = defaultSlots.withUpdatedTime(index = 2, selectedMinutes = 6 * 60)

        assertEquals(
            listOf("Morning", "Lunch", "Breakfast", "Afternoon", "Dinner", "Late", "Night"),
            updated.map { it.name }
        )
    }

    @Test
    fun `invalid out-of-range minutes are clamped into a single day`() {
        val clampedLow = listOf(FactorSlot(name = "Only", startTimeMinutes = 0))
            .withUpdatedTime(index = 0, selectedMinutes = -10)
        assertEquals(0, clampedLow[0].startTimeMinutes)

        val clampedHigh = defaultSlots.withUpdatedTime(index = 6, selectedMinutes = 1500) // Night
        assertEquals(MINUTES_PER_DAY - 1, clampedHigh.last().startTimeMinutes)
    }

    @Test
    fun `normal case leaves an already ascending schedule unchanged`() {
        val result = normalizeAscendingSchedule(listOf(300, 540, 720, 840, 1020, 1200, 1380))

        assertEquals(listOf(300, 540, 720, 840, 1020, 1200, 1380), result.toList())
    }

    @Test
    fun `boundary of every slot colliding at end of day cascades corrections backward then forward`() {
        val result = normalizeAscendingSchedule(listOf(1439, 1439, 1439, 1439, 1439, 1439, 1439))

        assertEquals(listOf(1433, 1434, 1435, 1436, 1437, 1438, 1439), result.toList())
    }

    @Test
    fun `normalizeAscendingSchedule handles a single-element and an empty list`() {
        assertEquals(listOf(1439), normalizeAscendingSchedule(listOf(1439)).toList())
        assertEquals(emptyList<Int>(), normalizeAscendingSchedule(emptyList()).toList())
    }

    @Test
    fun `suggested new slot time lands in the largest gap`() {
        // Largest wrapping gap is Night (23:00) -> Morning (05:00): 360 minutes, midpoint 02:00 (120).
        val suggested = defaultSlots.suggestedNewSlotTimeMinutes()

        assertEquals(120, suggested)
    }

    @Test
    fun `suggested new slot time for an empty list is midday`() {
        assertEquals(MINUTES_PER_DAY / 2, emptyList<FactorSlot>().suggestedNewSlotTimeMinutes())
    }
}
