package sevynidd.diabetesapp.calculation

import sevynidd.diabetesapp.data.model.FactorsData

private const val WAKING_SLOT_COUNT = 7

/** Which time-of-day schedule field a schedule edit applies to. */
enum class ScheduleTimeSlot {
    Morning,
    Breakfast,
    Lunch,
    Afternoon,
    Dinner,
    Late,
    Night,
    Basal
}

/**
 * Returns [this] with [slot]'s time updated to [selectedMinutes] (clamped to a single day).
 * Updating [ScheduleTimeSlot.Basal] only changes its own field; updating any other slot
 * re-normalizes all seven waking-hour slots into ascending order via
 * [normalizeAscendingSchedule] so schedule windows never overlap or invert.
 */
fun FactorsData.withUpdatedTime(slot: ScheduleTimeSlot, selectedMinutes: Int): FactorsData {
    val clampedMinutes = selectedMinutes.coerceIn(0, MINUTES_PER_DAY - 1)

    if (slot == ScheduleTimeSlot.Basal) {
        return copy(basalTimeMinutes = clampedMinutes)
    }

    val wakingTimes = mutableListOf(
        morningTimeMinutes,
        breakfastTimeMinutes,
        lunchTimeMinutes,
        afternoonTimeMinutes,
        dinnerTimeMinutes,
        lateTimeMinutes,
        nightTimeMinutes
    )
    val slotIndex = slot.ordinal
    wakingTimes[slotIndex] = clampedMinutes

    val normalized = normalizeAscendingSchedule(wakingTimes)

    return copy(
        morningTimeMinutes = normalized[0],
        breakfastTimeMinutes = normalized[1],
        lunchTimeMinutes = normalized[2],
        afternoonTimeMinutes = normalized[3],
        dinnerTimeMinutes = normalized[4],
        lateTimeMinutes = normalized[5],
        nightTimeMinutes = normalized[6]
    )
}

/**
 * Forces [times] (morning, breakfast, lunch, afternoon, dinner, late, night, in that order) into
 * strictly ascending order (each at least 1 minute after the previous), clamped within a single
 * day. Used after editing one schedule time so it doesn't invert the boundary with its neighbors.
 */
fun normalizeAscendingSchedule(times: List<Int>): IntArray {
    require(times.size == WAKING_SLOT_COUNT) { "Expected $WAKING_SLOT_COUNT schedule times" }
    val result = times.toIntArray()

    result[0] = result[0].coerceIn(0, MINUTES_PER_DAY - 1)
    for (index in 1 until result.size) {
        result[index] = maxOf(result[index], result[index - 1] + 1)
    }

    if (result.last() >= MINUTES_PER_DAY) {
        result[result.lastIndex] = MINUTES_PER_DAY - 1
        for (index in result.lastIndex - 1 downTo 0) {
            result[index] = minOf(result[index], result[index + 1] - 1)
        }

        result[0] = maxOf(result[0], 0)
        for (index in 1 until result.size) {
            result[index] = maxOf(result[index], result[index - 1] + 1)
        }
    }

    return result
}
