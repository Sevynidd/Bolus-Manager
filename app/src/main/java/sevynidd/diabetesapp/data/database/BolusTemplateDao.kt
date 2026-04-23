package sevynidd.diabetesapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BolusTemplateDao {
    @Query("SELECT * FROM bolus_template")
    fun observeAll(): Flow<List<BolusTemplateEntity>>

    @Query("SELECT COUNT(*) FROM bolus_template WHERE nameNormalized = :normalizedName")
    suspend fun countByNormalizedName(normalizedName: String): Int

    @Query("SELECT COUNT(*) FROM bolus_template WHERE nameNormalized = :normalizedName AND id != :excludeId")
    suspend fun countByNormalizedNameExcludingId(normalizedName: String, excludeId: Long): Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(template: BolusTemplateEntity)

    @Update
    suspend fun update(template: BolusTemplateEntity)

    @Delete
    suspend fun delete(template: BolusTemplateEntity)

    @Query("UPDATE bolus_template SET lastUsedAtEpochMillis = :usedAt WHERE id = :templateId")
    suspend fun markUsed(templateId: Long, usedAt: Long)
}

