package sevynidd.diabetesapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "factor_profile")
data class FactorProfileEntity(
    @PrimaryKey val id: Int = SINGLE_PROFILE_ID,
    val morningFactor: Double? = null,
    val breakfastFactor: Double? = null,
    val lunchFactor: Double? = null,
    val afternoonFactor: Double? = null,
    val dinnerFactor: Double? = null,
    val lateFactor: Double? = null,
    val nightFactor: Double? = null,
    val basalRate: Int? = null,
    val morningTimeMinutes: Int? = null,
    val breakfastTimeMinutes: Int? = null,
    val lunchTimeMinutes: Int? = null,
    val afternoonTimeMinutes: Int? = null,
    val dinnerTimeMinutes: Int? = null,
    val lateTimeMinutes: Int? = null,
    val nightTimeMinutes: Int? = null,
    val basalTimeMinutes: Int? = null
) {
    companion object {
        const val SINGLE_PROFILE_ID = 1
    }
}

