package sevynidd.diabetesapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The persisted, single-row factor profile: per-time-window correction factors, the time
 * boundaries between them, and the basal rate/time. `null` numeric fields mean "not yet set".
 */
@Entity(tableName = "factor_profile")
data class FactorProfileEntity(
    @PrimaryKey val id: Int = SINGLE_PROFILE_ID,
    // Column name kept as the original "isPeriodeEnabled" from migration 4->5 (see
    // DiabetesDatabase.MIGRATION_4_5) so existing installs don't need a new migration just to
    // rename the Kotlin-side property to the correct English spelling.
    @ColumnInfo(name = "isPeriodeEnabled")
    val isPeriodEnabled: Boolean = false,
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

