package sevynidd.diabetesapp.calculation

import sevynidd.diabetesapp.data.model.PUMP_BASAL_HOURS
import java.math.BigDecimal
import java.math.RoundingMode

private const val UNITS_DECIMALS = 2
private const val MINUTES_PER_HOUR = 60

/**
 * Rounds typed hourly basal-rate text (comma or dot decimal) half-up to the nearest `0.01` U/h,
 * formatted with comma decimal notation and no trailing zeros. `0` is a valid rate (a pump can
 * deliver no basal in an hour); `""` when the text is empty, doesn't parse, or is negative.
 */
fun normalizeHourlyBasalRateValue(value: String): String =
    value.replace(',', '.').toDoubleOrNull()
        ?.takeIf { it >= 0.0 }
        ?.let(::formatUnits)
        .orEmpty()

/**
 * The total basal insulin per day, in units: the sum of every hourly rate in [hourlyRates]
 * (each covering exactly one hour, so its U/h value is also its units for that hour). Unset or
 * unparseable hours contribute nothing; `null` when no hour is set at all.
 */
fun pumpBasalDailyTotal(hourlyRates: List<String>): Double? {
    val rates = hourlyRates.mapNotNull { it.replace(',', '.').toDoubleOrNull() }
    return if (rates.isEmpty()) null else rates.sum()
}

/**
 * [units] rounded half-up to `0.01` and formatted with comma decimal notation and no trailing
 * zeros, e.g. `18,4`. Rounds the decimal value (via [BigDecimal]) so a midpoint like `0.125`
 * reliably becomes `0,13` rather than being skewed by its binary floating-point representation.
 */
fun formatUnits(units: Double): String =
    BigDecimal.valueOf(units)
        .setScale(UNITS_DECIMALS, RoundingMode.HALF_UP)
        .stripTrailingZeros()
        .toPlainString()
        .replace('.', ',')

/** The time range an hourly basal rate covers, e.g. `05:00 - 06:00`; [hour] wraps into `0..23`. */
fun pumpBasalHourRangeLabel(hour: Int): String {
    val startHour = Math.floorMod(hour, PUMP_BASAL_HOURS)
    val endHour = (startHour + 1) % PUMP_BASAL_HOURS
    return "${formatTimeOfDay(startHour * MINUTES_PER_HOUR)} - ${formatTimeOfDay(endHour * MINUTES_PER_HOUR)}"
}
