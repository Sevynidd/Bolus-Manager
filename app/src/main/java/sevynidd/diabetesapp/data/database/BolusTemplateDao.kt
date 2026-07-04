package sevynidd.diabetesapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/** Room access to the [BolusTemplateEntity] table. */
@Dao
interface BolusTemplateDao {
    /** Emits every saved template. */
    @Query("SELECT * FROM bolus_template")
    fun observeAll(): Flow<List<BolusTemplateEntity>>

    /** How many templates already use [normalizedName], for uniqueness checks before inserting. */
    @Query("SELECT COUNT(*) FROM bolus_template WHERE nameNormalized = :normalizedName")
    suspend fun countByNormalizedName(normalizedName: String): Int

    /** Like [countByNormalizedName], excluding [excludeId] itself when updating that template. */
    @Query("SELECT COUNT(*) FROM bolus_template WHERE nameNormalized = :normalizedName AND id != :excludeId")
    suspend fun countByNormalizedNameExcludingId(normalizedName: String, excludeId: Long): Int

    /** Inserts a new template; aborts if a conflicting row already exists. */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(template: BolusTemplateEntity)

    /** Overwrites an existing template's row. */
    @Update
    suspend fun update(template: BolusTemplateEntity)

    /** Removes a template's row. */
    @Delete
    suspend fun delete(template: BolusTemplateEntity)

    /** Records that the template with [templateId] was last used at [usedAt] (epoch millis). */
    @Query("UPDATE bolus_template SET lastUsedAtEpochMillis = :usedAt WHERE id = :templateId")
    suspend fun markUsed(templateId: Long, usedAt: Long)
}

