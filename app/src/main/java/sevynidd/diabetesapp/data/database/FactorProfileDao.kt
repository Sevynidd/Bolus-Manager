package sevynidd.diabetesapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Room access to the single-row [FactorProfileEntity] table. */
@Dao
interface FactorProfileDao {
    /** Emits the current factor profile, or `null` if it hasn't been saved yet. */
    @Query("SELECT * FROM factor_profile WHERE id = :profileId LIMIT 1")
    fun observeProfile(profileId: Int = FactorProfileEntity.SINGLE_PROFILE_ID): Flow<FactorProfileEntity?>

    /** Inserts or replaces the factor profile row. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: FactorProfileEntity)
}

