package sevynidd.diabetesapp.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * One persisted audit-log row: a single detected change to a factor slot or the basal rate,
 * timestamped when it was saved. Mirrors [sevynidd.diabetesapp.data.model.FactorAuditLogEntry]
 * plus the row's own [id]; see that type for what the nullable columns mean. Indexed by
 * [timestampMillis] since every read orders/filters by it (see `FactorAuditLogDao`); the index is
 * created explicitly in `DiabetesDatabase.MIGRATION_7_8` and must be declared here too so Room's
 * expected schema matches what that migration actually creates.
 */
@Entity(tableName = "factor_audit_log", indices = [Index("timestampMillis")])
data class FactorAuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampMillis: Long,
    val changeType: String,
    val factorName: String?,
    val oldValue: Double?,
    val newValue: Double?,
    val oldTimeMinutes: Int?,
    val newTimeMinutes: Int?
)
