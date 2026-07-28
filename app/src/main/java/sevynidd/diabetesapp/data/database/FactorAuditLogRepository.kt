package sevynidd.diabetesapp.data.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sevynidd.diabetesapp.calculation.FactorChangeEvent
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import java.time.Clock

/**
 * Bridges [FactorChangeEvent]s produced by `diffFactorChanges`, the persisted [FactorAuditLogEntity]
 * rows, and the UI-facing [FactorAuditLogEntry] model.
 */
class FactorAuditLogRepository(
    private val dao: FactorAuditLogDao,
    private val clock: Clock = Clock.systemDefaultZone()
) {
    /** Every logged change, most recent first. */
    val auditLogFlow: Flow<List<FactorAuditLogEntry>> = dao.observeAll().map { rows -> rows.map { it.toEntry() } }

    /** Persists [events] as new audit-log rows, all timestamped with the current moment. No-op if empty. */
    suspend fun logChanges(events: List<FactorChangeEvent>) {
        if (events.isEmpty()) return

        val timestampMillis = clock.millis()
        dao.insertAll(events.map { it.toEntity(timestampMillis) })
    }
}

private fun FactorChangeEvent.toEntity(timestampMillis: Long): FactorAuditLogEntity {
    return FactorAuditLogEntity(
        timestampMillis = timestampMillis,
        changeType = changeType.name,
        factorName = factorName,
        oldValue = oldValue,
        newValue = newValue,
        oldTimeMinutes = oldTimeMinutes,
        newTimeMinutes = newTimeMinutes
    )
}

private fun FactorAuditLogEntity.toEntry(): FactorAuditLogEntry {
    return FactorAuditLogEntry(
        timestampMillis = timestampMillis,
        changeType = changeType,
        factorName = factorName,
        oldValue = oldValue,
        newValue = newValue,
        oldTimeMinutes = oldTimeMinutes,
        newTimeMinutes = newTimeMinutes
    )
}
