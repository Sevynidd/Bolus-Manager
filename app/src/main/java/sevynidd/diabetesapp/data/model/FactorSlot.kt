package sevynidd.diabetesapp.data.model

/**
 * One user-defined correction-factor time window: a free-form [name], the factor value
 * (comma-decimal UI text, same convention as [FactorsData]'s other decimal fields), and the
 * minute-of-day it starts at. The window's end is implicit — it runs until the next slot
 * (by [startTimeMinutes]) in the same [FactorsData.factorSlots] list, wrapping past midnight.
 */
data class FactorSlot(
    val name: String,
    val factorValue: String = "",
    val startTimeMinutes: Int
)
