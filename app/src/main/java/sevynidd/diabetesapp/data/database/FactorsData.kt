package sevynidd.diabetesapp.data.database

/**
 * UI-friendly model for factor values. Decimal values are represented as strings using comma notation.
 */
data class FactorsData(
    val morningFactor: String = "",
    val breakfastFactor: String = "",
    val lunchFactor: String = "",
    val afternoonFactor: String = "",
    val dinnerFactor: String = "",
    val lateFactor: String = "",
    val nightFactor: String = "",
    val basalRate: String = "",
    val morningTimeMinutes: Int = 5 * 60,
    val breakfastTimeMinutes: Int = 9 * 60,
    val lunchTimeMinutes: Int = 12 * 60,
    val afternoonTimeMinutes: Int = 14 * 60,
    val dinnerTimeMinutes: Int = 17 * 60,
    val lateTimeMinutes: Int = 20 * 60,
    val nightTimeMinutes: Int = 23 * 60,
    val basalTimeMinutes: Int = 19 * 60
)

