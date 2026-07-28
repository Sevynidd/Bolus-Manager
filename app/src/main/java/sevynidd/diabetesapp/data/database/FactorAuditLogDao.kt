package sevynidd.diabetesapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Room access to the [FactorAuditLogEntity] table. */
@Dao
interface FactorAuditLogDao {
    /** Emits every audit-log entry, most recent first. */
    @Query("SELECT * FROM factor_audit_log ORDER BY timestampMillis DESC, id DESC")
    fun observeAll(): Flow<List<FactorAuditLogEntity>>

    @Insert
    suspend fun insertAll(entries: List<FactorAuditLogEntity>)
}
