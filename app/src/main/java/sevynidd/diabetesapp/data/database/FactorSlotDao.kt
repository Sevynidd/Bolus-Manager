package sevynidd.diabetesapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/** Room access to the [FactorSlotEntity] table. */
@Dao
interface FactorSlotDao {
    /** Emits every factor slot, ordered chronologically by start time. */
    @Query("SELECT * FROM factor_slot ORDER BY startTimeMinutes ASC")
    fun observeAll(): Flow<List<FactorSlotEntity>>

    /** How many factor slots are currently saved, used to decide whether to seed defaults. */
    @Query("SELECT COUNT(*) FROM factor_slot")
    suspend fun count(): Int

    @Insert
    suspend fun insertAll(slots: List<FactorSlotEntity>)

    @Query("DELETE FROM factor_slot")
    suspend fun deleteAll()

    /** Atomically replaces every factor slot with [slots] — the whole list is one persisted unit. */
    @Transaction
    suspend fun replaceAll(slots: List<FactorSlotEntity>) {
        deleteAll()
        insertAll(slots)
    }
}
