package sevynidd.diabetesapp.calculation

import java.time.ZonedDateTime

private const val MINUTES_PER_HOUR = 60

/**
 * Returns the epoch-millis instant of the next occurrence of [targetMinutes] (minutes since
 * midnight, `0..1439`) at or after [now]: today's occurrence if [now]'s time-of-day is strictly
 * before [targetMinutes], otherwise tomorrow's. Firing exactly at [targetMinutes] therefore rolls
 * over to the next day rather than being treated as "still due" and re-fired immediately.
 */
fun nextDailyTriggerEpochMillis(targetMinutes: Int, now: ZonedDateTime): Long {
    val nowMinutesOfDay = now.hour * MINUTES_PER_HOUR + now.minute
    val daysToAdd = if (nowMinutesOfDay < targetMinutes) 0L else 1L

    return now
        .plusDays(daysToAdd)
        .withHour(targetMinutes / MINUTES_PER_HOUR)
        .withMinute(targetMinutes % MINUTES_PER_HOUR)
        .withSecond(0)
        .withNano(0)
        .toInstant()
        .toEpochMilli()
}
