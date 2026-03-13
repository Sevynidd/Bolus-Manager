package sevynidd.diabetesapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FactorProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DiabetesDatabase : RoomDatabase() {
    abstract fun factorProfileDao(): FactorProfileDao

    companion object {
        @Volatile
        private var instance: DiabetesDatabase? = null

        fun getInstance(context: Context): DiabetesDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DiabetesDatabase::class.java,
                    "diabetes_app.db"
                ).build().also { created ->
                    instance = created
                }
            }
        }
    }
}

