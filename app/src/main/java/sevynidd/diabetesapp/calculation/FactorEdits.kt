package sevynidd.diabetesapp.calculation

import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import java.util.Locale

/** The fewest factor windows a schedule can have; the last one can't be deleted. */
const val MIN_FACTOR_COUNT = 1

/**
 * Whether [name] may be used for a factor window next to [otherNames]: it must not be blank and,
 * ignoring case and surrounding spaces, must not match another window's name.
 */
fun isFactorNameValid(name: String, otherNames: List<String>): Boolean {
    val normalized = name.trim().lowercase(Locale.ROOT)
    return normalized.isNotEmpty() && otherNames.none { it.trim().lowercase(Locale.ROOT) == normalized }
}

/** The slots with the one at [index] renamed to the trimmed [name] and given [factorValue]; its time is kept. */
fun List<FactorSlot>.withSlotEdited(index: Int, name: String, factorValue: String): List<FactorSlot> =
    mapIndexed { slotIndex, slot ->
        if (slotIndex == index) slot.copy(name = name.trim(), factorValue = factorValue) else slot
    }

/** The slots without the one at [index]; unchanged if that would leave fewer than [MIN_FACTOR_COUNT]. */
fun List<FactorSlot>.withoutSlot(index: Int): List<FactorSlot> =
    if (size <= MIN_FACTOR_COUNT || index !in indices) this else filterIndexed { slotIndex, _ -> slotIndex != index }

/** The hourly rates with the one for [hour] replaced by [rate]; unchanged for an hour outside the list. */
fun List<String>.withRateAt(hour: Int, rate: String): List<String> =
    mapIndexed { index, existing -> if (index == hour) rate else existing }

/** Which kind of value the factor editor is changing. */
enum class FactorEditKind {
    /** The factor (or pump ratio) and name of one time window. */
    Slot,

    /** The pen therapy's daily basal dose. */
    Basal,

    /** One hour's pump basal rate. */
    PumpHour
}

/**
 * These factors with the edit of [kind] applied. [index] is the window (for [FactorEditKind.Slot]) or
 * the hour (for [FactorEditKind.PumpHour]); [name] only matters for a window. [value] is the already
 * normalized text to store.
 */
fun FactorsData.withEdit(kind: FactorEditKind, index: Int, name: String, value: String): FactorsData =
    when (kind) {
        FactorEditKind.Slot -> copy(factorSlots = factorSlots.withSlotEdited(index, name, value))
        FactorEditKind.Basal -> copy(basalRate = value)
        FactorEditKind.PumpHour -> copy(pumpBasalRates = pumpBasalRates.withRateAt(index, value))
    }
