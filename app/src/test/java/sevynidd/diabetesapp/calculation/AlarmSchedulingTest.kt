package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZoneOffset
import java.time.ZonedDateTime

private val ZONE = ZoneOffset.UTC

class AlarmSchedulingTest {

    @Test
    fun `normal case schedules later today when target time hasn't passed yet`() {
        val now = ZonedDateTime.of(2026, 7, 8, 10, 0, 0, 0, ZONE)

        val result = nextDailyTriggerEpochMillis(targetMinutes = 12 * 60, now = now)

        val expected = ZonedDateTime.of(2026, 7, 8, 12, 0, 0, 0, ZONE)
        assertEquals(expected.toInstant().toEpochMilli(), result)
    }

    @Test
    fun `boundary of now exactly matching the target time rolls over to tomorrow`() {
        val now = ZonedDateTime.of(2026, 7, 8, 12, 0, 0, 0, ZONE)

        val result = nextDailyTriggerEpochMillis(targetMinutes = 12 * 60, now = now)

        val expected = ZonedDateTime.of(2026, 7, 9, 12, 0, 0, 0, ZONE)
        assertEquals(expected.toInstant().toEpochMilli(), result)
    }

    @Test
    fun `edge minutes of a full day range schedule at midnight and one minute before it`() {
        val now = ZonedDateTime.of(2026, 7, 8, 10, 30, 0, 0, ZONE)

        val midnight = nextDailyTriggerEpochMillis(targetMinutes = 0, now = now)
        val justBeforeMidnight = nextDailyTriggerEpochMillis(targetMinutes = 23 * 60 + 59, now = now)

        assertEquals(
            ZonedDateTime.of(2026, 7, 9, 0, 0, 0, 0, ZONE).toInstant().toEpochMilli(),
            midnight
        )
        assertEquals(
            ZonedDateTime.of(2026, 7, 8, 23, 59, 0, 0, ZONE).toInstant().toEpochMilli(),
            justBeforeMidnight
        )
    }
}
