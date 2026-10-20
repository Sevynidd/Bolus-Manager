package sevynidd.diabetesapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * One persisted hourly insulin-pump basal rate: [unitsPerHour] delivered during the hour starting
 * at [hour] (`0..23`). Only hours that have a rate are stored; a missing row means "not set".
 */
@Entity(tableName = "pump_basal_rate")
data class PumpBasalRateEntity(
    @PrimaryKey val hour: Int,
    val unitsPerHour: Double
)
