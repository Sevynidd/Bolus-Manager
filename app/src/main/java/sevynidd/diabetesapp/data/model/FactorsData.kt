package sevynidd.diabetesapp.data.model

/** The factor slots shown before any real data has loaded, or in previews/tests. */
private val DEFAULT_FACTOR_SLOTS = listOf(
    FactorSlot(name = "Morning", startTimeMinutes = 5 * 60),
    FactorSlot(name = "Breakfast", startTimeMinutes = 9 * 60),
    FactorSlot(name = "Lunch", startTimeMinutes = 12 * 60),
    FactorSlot(name = "Afternoon", startTimeMinutes = 14 * 60),
    FactorSlot(name = "Dinner", startTimeMinutes = 17 * 60),
    FactorSlot(name = "Late", startTimeMinutes = 20 * 60),
    FactorSlot(name = "Night", startTimeMinutes = 23 * 60)
)

/**
 * UI-friendly model for factor values. Decimal values are represented as strings using comma
 * notation. [factorSlots] holds a variable number of user-defined, freely renamable time windows,
 * always kept sorted by [FactorSlot.startTimeMinutes] ascending with no two slots sharing a
 * minute — see `withUpdatedTime` in `calculation/FactorScheduling.kt`, which enforces this
 * whenever a slot's time is edited or a new one is added.
 */
data class FactorsData(
    val isPeriodEnabled: Boolean = false,
    val factorSlots: List<FactorSlot> = DEFAULT_FACTOR_SLOTS,
    val basalRate: String = "",
    val basalTimeMinutes: Int = 19 * 60,
    val basalReminderEnabled: Boolean = false
)
