package sevynidd.diabetesapp.calculation

import sevynidd.diabetesapp.data.model.FactorSlot

/** Number of minutes in a full day, used for time-of-day arithmetic that wraps past midnight. */
const val MINUTES_PER_DAY = 24 * 60

/**
 * The factor active at a point in time: its value (`null` if unparsable), how many minutes its
 * time window lasts in total, its name, and its position within the (time-sorted) input list —
 * `-1` if the input list was empty, which should only be transiently observable before a fresh
 * install's default factors have been seeded.
 */
data class ActiveFactorInfo(
    val factor: Double?,
    val durationMinutes: Int,
    val factorName: String,
    val activeIndex: Int
)

/**
 * Resolves which of [factorSlots]' time windows [nowMinutes] (minutes since midnight) falls
 * into, and returns that window's factor, total duration, name, and index. [factorSlots] is
 * sorted by [FactorSlot.startTimeMinutes] defensively before resolving. The active window is the
 * last slot whose start is at or before [nowMinutes]; if [nowMinutes] precedes every slot's
 * start, it wraps to the last slot (the previous day's final window carrying over past
 * midnight). [ActiveFactorInfo.durationMinutes] is the matched window's own total length (next
 * slot's start minus this slot's start, wrapping) — not "time remaining from now".
 */
fun activeFactorForTime(factorSlots: List<FactorSlot>, nowMinutes: Int): ActiveFactorInfo {
    if (factorSlots.isEmpty()) {
        return ActiveFactorInfo(factor = null, durationMinutes = MINUTES_PER_DAY, factorName = "", activeIndex = -1)
    }

    val sorted = factorSlots.sortedBy { it.startTimeMinutes }
    val activeIndex = sorted.indexOfLast { it.startTimeMinutes <= nowMinutes }.takeIf { it >= 0 } ?: sorted.lastIndex
    val nextIndex = (activeIndex + 1) % sorted.size
    val active = sorted[activeIndex]
    val next = sorted[nextIndex]

    val duration = if (nextIndex <= activeIndex) {
        (MINUTES_PER_DAY - active.startTimeMinutes) + next.startTimeMinutes
    } else {
        next.startTimeMinutes - active.startTimeMinutes
    }

    return ActiveFactorInfo(
        factor = active.factorValue.replace(',', '.').toDoubleOrNull(),
        durationMinutes = duration.coerceAtLeast(1),
        factorName = active.name,
        activeIndex = activeIndex
    )
}

/**
 * Applies the "Period" surcharge to [factor]: when [isPeriodEnabled], scales it up by
 * [periodFactorPercent] percent (negative percentages are treated as `0`); otherwise returns
 * [factor] unchanged. Returns `null` if [factor] is `null`.
 */
fun applyPeriodMultiplier(factor: Double?, isPeriodEnabled: Boolean, periodFactorPercent: Double): Double? {
    if (!isPeriodEnabled) return factor
    val sanitizedPercent = periodFactorPercent.coerceAtLeast(0.0)
    return factor?.times(1.0 + (sanitizedPercent / FULL_PERCENT))
}

private const val FULL_PERCENT = 100.0
