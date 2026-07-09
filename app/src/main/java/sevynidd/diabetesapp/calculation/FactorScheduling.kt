package sevynidd.diabetesapp.calculation

import sevynidd.diabetesapp.data.model.FactorSlot
import java.util.Locale

/**
 * Returns [this] list with the slot at [index] moved to [selectedMinutes] (clamped to a single
 * day), then re-sorted by start time and passed through [normalizeAscendingSchedule] so no two
 * slots share a minute. Re-sorting (rather than only nudging neighbors while preserving list
 * order) is what makes this correct for a freely-orderable, variable-length list: display order
 * is always "whichever slot starts earliest", so moving one slot's time earlier than a neighbor
 * naturally reorders them instead of inverting the schedule.
 */
fun List<FactorSlot>.withUpdatedTime(index: Int, selectedMinutes: Int): List<FactorSlot> {
    val clampedMinutes = selectedMinutes.coerceIn(0, MINUTES_PER_DAY - 1)
    val updated = toMutableList().also { it[index] = it[index].copy(startTimeMinutes = clampedMinutes) }
    val sorted = updated.sortedBy { it.startTimeMinutes }
    val normalizedTimes = normalizeAscendingSchedule(sorted.map { it.startTimeMinutes })
    return sorted.mapIndexed { i, slot -> slot.copy(startTimeMinutes = normalizedTimes[i]) }
}

/**
 * Forces [times] into strictly ascending order (each at least 1 minute after the previous),
 * clamped within a single day. Used after editing (or adding) a schedule time so it doesn't
 * invert or collide with its neighbors. Works for any list size, including empty/single-element.
 */
fun normalizeAscendingSchedule(times: List<Int>): IntArray {
    val result = times.toIntArray()
    if (result.isEmpty()) return result

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

/**
 * Suggests a start time for a new factor slot: the midpoint of the largest gap between
 * consecutive (wrapping) existing slots, so a newly added factor lands somewhere plausible
 * rather than colliding with — or sitting right next to — an existing one. Returns midday
 * (`12:00`) if [this] is empty.
 */
fun List<FactorSlot>.suggestedNewSlotTimeMinutes(): Int {
    if (isEmpty()) return MINUTES_PER_DAY / 2

    val sorted = sortedBy { it.startTimeMinutes }
    var bestGapStart = sorted.first().startTimeMinutes
    var bestGapSize = -1

    for (index in sorted.indices) {
        val start = sorted[index].startTimeMinutes
        val end = sorted[(index + 1) % sorted.size].startTimeMinutes
        val gapSize = if (index == sorted.lastIndex) (MINUTES_PER_DAY - start) + end else end - start

        if (gapSize > bestGapSize) {
            bestGapSize = gapSize
            bestGapStart = start
        }
    }

    return (bestGapStart + bestGapSize / 2) % MINUTES_PER_DAY
}

private const val MINUTES_PER_HOUR = 60

/** Formats [totalMinutes] (minutes since midnight, wrapped into a single day even if negative) as `HH:mm`. */
fun formatTimeOfDay(totalMinutes: Int): String {
    val normalized = ((totalMinutes % MINUTES_PER_DAY) + MINUTES_PER_DAY) % MINUTES_PER_DAY
    val hours = normalized / MINUTES_PER_HOUR
    val minutes = normalized % MINUTES_PER_HOUR
    return String.format(Locale.ROOT, "%02d:%02d", hours, minutes)
}
