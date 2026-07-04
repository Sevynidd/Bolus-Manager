package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorsData

class ScheduleTimeSlotTest {

    @Test
    fun `normal case moves a slot's time without touching its neighbors`() {
        val updated = FactorsData().withUpdatedTime(ScheduleTimeSlot.Lunch, selectedMinutes = 700)

        assertEquals(700, updated.lunchTimeMinutes)
        assertEquals(540, updated.breakfastTimeMinutes)
        assertEquals(840, updated.afternoonTimeMinutes)
    }

    @Test
    fun `boundary of colliding with the next slot pushes it forward by a minute`() {
        // Default lunch is 720; moving breakfast onto it must not create a tie.
        val updated = FactorsData().withUpdatedTime(ScheduleTimeSlot.Breakfast, selectedMinutes = 720)

        assertEquals(720, updated.breakfastTimeMinutes)
        assertEquals(721, updated.lunchTimeMinutes)
        assertEquals(840, updated.afternoonTimeMinutes)
    }

    @Test
    fun `invalid out-of-range minutes are clamped into a single day`() {
        val updatedBasal = FactorsData().withUpdatedTime(ScheduleTimeSlot.Basal, selectedMinutes = -10)
        assertEquals(0, updatedBasal.basalTimeMinutes)

        val updatedNight = FactorsData().withUpdatedTime(ScheduleTimeSlot.Night, selectedMinutes = 1500)
        assertEquals(MINUTES_PER_DAY - 1, updatedNight.nightTimeMinutes)
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
}
