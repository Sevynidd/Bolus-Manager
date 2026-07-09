package sevynidd.diabetesapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The persisted, single-row factor profile: the period-surcharge toggle and the basal rate/time/
 * reminder. The correction-factor time windows themselves live in [FactorSlotEntity] — a separate,
 * variable-length child table — since (unlike this profile row) there can be any number of them.
 * `null` numeric fields mean "not yet set".
 */
@Entity(tableName = "factor_profile")
data class FactorProfileEntity(
    @PrimaryKey val id: Int = SINGLE_PROFILE_ID,
    // Column name kept as the original "isPeriodeEnabled" from migration 4->5 (see
    // DiabetesDatabase.MIGRATION_4_5) so existing installs don't need a new migration just to
    // rename the Kotlin-side property to the correct English spelling.
    @ColumnInfo(name = "isPeriodeEnabled")
    val isPeriodEnabled: Boolean = false,
    val basalRate: Int? = null,
    val basalTimeMinutes: Int? = null,
    val basalReminderEnabled: Boolean = false
) {
    companion object {
        const val SINGLE_PROFILE_ID = 1
    }
}
