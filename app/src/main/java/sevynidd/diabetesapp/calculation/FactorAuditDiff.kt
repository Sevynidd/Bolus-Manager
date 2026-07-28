package sevynidd.diabetesapp.calculation

import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData

/** What kind of change to a factor slot or the basal rate a [FactorChangeEvent] records. */
enum class FactorChangeType {
    FACTOR_ADDED,
    FACTOR_VALUE_CHANGED,
    FACTOR_TIME_CHANGED,
    FACTOR_DELETED,
    BASAL_RATE_CHANGED,
    BASAL_TIME_CHANGED
}

/**
 * One detected change between two [FactorsData] snapshots, as computed by [diffFactorChanges].
 * Which of [oldValue]/[newValue] (the factor value or basal rate, unitless/units-per-hour) and
 * [oldTimeMinutes]/[newTimeMinutes] (minutes since midnight) are set depends on [changeType]:
 * value fields for `*_VALUE_CHANGED`/`BASAL_RATE_CHANGED`, time fields for `*_TIME_CHANGED`, both
 * "new" fields for `FACTOR_ADDED`, both "old" fields for `FACTOR_DELETED`. [factorName] is `null`
 * for basal-rate/-time events.
 */
data class FactorChangeEvent(
    val changeType: FactorChangeType,
    val factorName: String?,
    val oldValue: Double? = null,
    val newValue: Double? = null,
    val oldTimeMinutes: Int? = null,
    val newTimeMinutes: Int? = null
)

/**
 * Computes the audit-log-worthy differences between [previous] and [updated]: factor slots added,
 * removed, or changed in value/time (matched between the two snapshots by name, since a slot has
 * no persisted identity beyond its name), plus basal-rate/basal-time changes.
 */
fun diffFactorChanges(previous: FactorsData, updated: FactorsData): List<FactorChangeEvent> {
    return diffFactorSlots(previous.factorSlots, updated.factorSlots) + diffBasal(previous, updated)
}

private fun diffFactorSlots(previous: List<FactorSlot>, updated: List<FactorSlot>): List<FactorChangeEvent> {
    val previousByName = previous.associateBy { it.name }
    val updatedByName = updated.associateBy { it.name }
    val events = mutableListOf<FactorChangeEvent>()

    for ((name, old) in previousByName) {
        val new = updatedByName[name]
        if (new == null) {
            events += FactorChangeEvent(
                changeType = FactorChangeType.FACTOR_DELETED,
                factorName = name,
                oldValue = old.factorValue.toAuditDouble(),
                oldTimeMinutes = old.startTimeMinutes
            )
            continue
        }
        events += diffMatchedSlot(name, old, new)
    }

    for ((name, new) in updatedByName) {
        if (name !in previousByName) {
            events += FactorChangeEvent(
                changeType = FactorChangeType.FACTOR_ADDED,
                factorName = name,
                newValue = new.factorValue.toAuditDouble(),
                newTimeMinutes = new.startTimeMinutes
            )
        }
    }

    return events
}

private fun diffMatchedSlot(name: String, old: FactorSlot, new: FactorSlot): List<FactorChangeEvent> {
    val events = mutableListOf<FactorChangeEvent>()
    val oldValue = old.factorValue.toAuditDouble()
    val newValue = new.factorValue.toAuditDouble()
    if (oldValue != newValue) {
        events += FactorChangeEvent(
            changeType = FactorChangeType.FACTOR_VALUE_CHANGED,
            factorName = name,
            oldValue = oldValue,
            newValue = newValue
        )
    }
    if (old.startTimeMinutes != new.startTimeMinutes) {
        events += FactorChangeEvent(
            changeType = FactorChangeType.FACTOR_TIME_CHANGED,
            factorName = name,
            oldTimeMinutes = old.startTimeMinutes,
            newTimeMinutes = new.startTimeMinutes
        )
    }
    return events
}

private fun diffBasal(previous: FactorsData, updated: FactorsData): List<FactorChangeEvent> {
    val events = mutableListOf<FactorChangeEvent>()
    val oldRate = previous.basalRate.toAuditDouble()
    val newRate = updated.basalRate.toAuditDouble()
    if (oldRate != newRate) {
        events += FactorChangeEvent(
            changeType = FactorChangeType.BASAL_RATE_CHANGED,
            factorName = null,
            oldValue = oldRate,
            newValue = newRate
        )
    }
    if (previous.basalTimeMinutes != updated.basalTimeMinutes) {
        events += FactorChangeEvent(
            changeType = FactorChangeType.BASAL_TIME_CHANGED,
            factorName = null,
            oldTimeMinutes = previous.basalTimeMinutes,
            newTimeMinutes = updated.basalTimeMinutes
        )
    }
    return events
}

/** Parses a UI comma-decimal factor/basal string (see [FactorsData]) into a [Double], or `null` if unset/invalid. */
private fun String.toAuditDouble(): Double? = replace(',', '.').toDoubleOrNull()
