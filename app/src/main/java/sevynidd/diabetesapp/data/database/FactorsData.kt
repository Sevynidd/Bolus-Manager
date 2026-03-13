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
    val basalRate: String = ""
)

