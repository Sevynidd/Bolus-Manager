package sevynidd.diabetesapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/** Room access to the [PumpBasalRateEntity] table. */
@Dao
interface PumpBasalRateDao {
    /** Emits every stored hourly basal rate, ordered by hour. */
    @Query("SELECT * FROM pump_basal_rate ORDER BY hour ASC")
    fun observeAll(): Flow<List<PumpBasalRateEntity>>

    @Insert
    suspend fun insertAll(rates: List<PumpBasalRateEntity>)

    @Query("DELETE FROM pump_basal_rate")
    suspend fun deleteAll()

    /** Atomically replaces every hourly basal rate with [rates] — the day's profile is one persisted unit. */
    @Transaction
    suspend fun replaceAll(rates: List<PumpBasalRateEntity>) {
        deleteAll()
        insertAll(rates)
    }
}
