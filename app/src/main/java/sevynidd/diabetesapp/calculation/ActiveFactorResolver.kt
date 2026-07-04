package sevynidd.diabetesapp.calculation

import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.localization.TranslationKey

/** Number of minutes in a full day, used for time-of-day arithmetic that wraps past midnight. */
const val MINUTES_PER_DAY = 24 * 60

/**
 * The factor active at a point in time: its value (`null` if unparsable), how many minutes it
 * remains in effect, and the label of the time window it belongs to.
 */
data class ActiveFactorInfo(
    val factor: Double?,
    val durationMinutes: Int,
    val factorLabel: TranslationKey
)

/**
 * Resolves which of [factors]' configured time windows (night/morning/breakfast/lunch/afternoon/
 * dinner/late) [nowMinutes] (minutes since midnight) falls into, and returns that window's factor,
 * remaining duration, and label. Windows wrap past midnight: any time outside
 * `[morningTimeMinutes, nightTimeMinutes)` resolves to the night factor.
 */
fun activeFactorForTime(factors: FactorsData, nowMinutes: Int): ActiveFactorInfo {
    val morning = factors.morningTimeMinutes
    val breakfast = factors.breakfastTimeMinutes
    val lunch = factors.lunchTimeMinutes
    val afternoon = factors.afternoonTimeMinutes
    val dinner = factors.dinnerTimeMinutes
    val late = factors.lateTimeMinutes
    val night = factors.nightTimeMinutes

    val (factorText, duration, factorLabel) = when {
        nowMinutes !in morning..<night -> Triple(
            factors.nightFactor,
            (MINUTES_PER_DAY - night) + morning,
            TranslationKey.FactorNight
        )

        nowMinutes < breakfast -> Triple(
            factors.morningFactor,
            breakfast - morning,
            TranslationKey.FactorMorning
        )

        nowMinutes < lunch -> Triple(
            factors.breakfastFactor,
            lunch - breakfast,
            TranslationKey.FactorBreakfast
        )

        nowMinutes < afternoon -> Triple(
            factors.lunchFactor,
            afternoon - lunch,
            TranslationKey.FactorLunch
        )

        nowMinutes < dinner -> Triple(
            factors.afternoonFactor,
            dinner - afternoon,
            TranslationKey.FactorAfternoon
        )

        nowMinutes < late -> Triple(
            factors.dinnerFactor,
            late - dinner,
            TranslationKey.FactorDinner
        )

        else -> Triple(
            factors.lateFactor,
            night - late,
            TranslationKey.FactorLate
        )
    }

    return ActiveFactorInfo(
        factor = factorText.replace(',', '.').toDoubleOrNull(),
        durationMinutes = duration.coerceAtLeast(1),
        factorLabel = factorLabel
    )
}

/**
 * Applies the "Periode" surcharge to [factor]: when [isPeriodeEnabled], scales it up by
 * [periodeFactorPercent] percent (negative percentages are treated as `0`); otherwise returns
 * [factor] unchanged. Returns `null` if [factor] is `null`.
 */
fun applyPeriodeMultiplier(factor: Double?, isPeriodeEnabled: Boolean, periodeFactorPercent: Double): Double? {
    if (!isPeriodeEnabled) return factor
    val sanitizedPercent = periodeFactorPercent.coerceAtLeast(0.0)
    return factor?.times(1.0 + (sanitizedPercent / FULL_PERCENT))
}

private const val FULL_PERCENT = 100.0
