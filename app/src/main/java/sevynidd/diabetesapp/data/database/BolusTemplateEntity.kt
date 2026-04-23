package sevynidd.diabetesapp.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bolus_template",
    indices = [Index(value = ["nameNormalized"], unique = true)]
)
data class BolusTemplateEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val nameNormalized: String,
    val emoji: String?,
    val carbohydrates: Double,
    val lastUsedAtEpochMillis: Long = 0L
)

