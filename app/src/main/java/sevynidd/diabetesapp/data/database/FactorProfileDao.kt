package sevynidd.diabetesapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FactorProfileDao {
    @Query("SELECT * FROM factor_profile WHERE id = :profileId LIMIT 1")
    fun observeProfile(profileId: Int = FactorProfileEntity.SINGLE_PROFILE_ID): Flow<FactorProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: FactorProfileEntity)
}

