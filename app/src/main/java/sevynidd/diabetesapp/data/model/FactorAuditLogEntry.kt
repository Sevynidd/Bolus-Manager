package sevynidd.diabetesapp.data.model

/**
 * One logged change to a factor slot or the basal rate, as persisted by
 * `FactorAuditLogRepository`. [changeType] is the [sevynidd.diabetesapp.calculation.FactorChangeType]
 * name it was created from; see that type for what [oldValue]/[newValue]/[oldTimeMinutes]/
 * [newTimeMinutes] mean for each kind of change.
 */
data class FactorAuditLogEntry(
    val timestampMillis: Long,
    val changeType: String,
    val factorName: String?,
    val oldValue: Double?,
    val newValue: Double?,
    val oldTimeMinutes: Int?,
    val newTimeMinutes: Int?
)
