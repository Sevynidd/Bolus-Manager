package sevynidd.diabetesapp.data.settings.therapy

/**
 * How the user administers insulin, which decides how factors and the basal rate are shown.
 * [Pen]: factors in units per bread unit and one daily basal dose at a set time. [Pump]: factors
 * as an insulin-pump ratio ("1 unit per x grams of carbohydrates", derived from the stored
 * factor) and the basal rate as 24 hourly rates in units per hour. Both basal kinds stay stored
 * independently, so switching modes never discards data.
 */
enum class TherapyMode {
    Pen, Pump
}
