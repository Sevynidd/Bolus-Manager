package sevynidd.diabetesapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One persisted, user-defined correction-factor time window. There's only ever one implicit
 * owner (the single [FactorProfileEntity] row), so — like that entity's own single-row design —
 * this table has no explicit profile foreign key.
 */
@Entity(tableName = "factor_slot")
data class FactorSlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val factorValue: Double?,
    val startTimeMinutes: Int
)
