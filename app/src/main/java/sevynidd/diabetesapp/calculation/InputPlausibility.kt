package sevynidd.diabetesapp.calculation

/**
 * Whether an entered [value] is above the user's plausibility [limit] and therefore likely a
 * typing mistake. A [limit] of `0` or less switches the check off; a value exactly at the limit
 * is still accepted.
 */
fun exceedsPlausibleLimit(value: Double, limit: Double): Boolean = limit > 0.0 && value > limit
