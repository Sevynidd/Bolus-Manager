package sevynidd.diabetesapp.calculation

private const val FULL_PERCENT = 100.0

/**
 * Bolus units required for [carbohydrates] grams of carbs, given [breadUnits] grams per bread
 * unit and a correction [factor] (units per bread unit).
 */
fun calculateBolusUnits(carbohydrates: Double, breadUnits: Double, factor: Double): Double =
    (carbohydrates / breadUnits) * factor

/** The immediate and delayed portions of a split bolus, in both carbohydrates and units. */
data class SplitBolusResult(
    val immediateCarbohydrates: Double,
    val restCarbohydrates: Double,
    val immediateUnits: Double,
    val restUnits: Double,
    val totalUnits: Double
)

/**
 * Splits [carbohydrates] into an immediate portion ([immediatePercent]% of [carbohydrates], dosed
 * at [immediateFactor]) and a delayed rest portion (the remaining percent, dosed at [restFactor]
 * to account for the factor active later once the split's duration elapses).
 */
fun calculateSplitBolus(
    carbohydrates: Double,
    immediatePercent: Int,
    breadUnits: Double,
    immediateFactor: Double,
    restFactor: Double
): SplitBolusResult {
    val restPercent = FULL_PERCENT - immediatePercent
    val immediateCarbohydrates = carbohydrates * immediatePercent / FULL_PERCENT
    val restCarbohydrates = carbohydrates * restPercent / FULL_PERCENT
    val immediateUnits = calculateBolusUnits(immediateCarbohydrates, breadUnits, immediateFactor)
    val restUnits = calculateBolusUnits(restCarbohydrates, breadUnits, restFactor)

    return SplitBolusResult(
        immediateCarbohydrates = immediateCarbohydrates,
        restCarbohydrates = restCarbohydrates,
        immediateUnits = immediateUnits,
        restUnits = restUnits,
        totalUnits = immediateUnits + restUnits
    )
}
