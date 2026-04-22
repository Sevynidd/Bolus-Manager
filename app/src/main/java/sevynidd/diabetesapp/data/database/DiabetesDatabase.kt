package sevynidd.diabetesapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [FactorProfileEntity::class],
    version = 2,
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
                )
                    .addMigrations(MIGRATION_1_2)
                    .build().also { created ->
                    instance = created
                }
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN morningTimeMinutes INTEGER")
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN breakfastTimeMinutes INTEGER")
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN lunchTimeMinutes INTEGER")
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN afternoonTimeMinutes INTEGER")
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN dinnerTimeMinutes INTEGER")
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN lateTimeMinutes INTEGER")
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN nightTimeMinutes INTEGER")
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN basalTimeMinutes INTEGER")

                db.execSQL("UPDATE factor_profile SET morningTimeMinutes = 300 WHERE morningTimeMinutes IS NULL")
                db.execSQL("UPDATE factor_profile SET breakfastTimeMinutes = 540 WHERE breakfastTimeMinutes IS NULL")
                db.execSQL("UPDATE factor_profile SET lunchTimeMinutes = 720 WHERE lunchTimeMinutes IS NULL")
                db.execSQL("UPDATE factor_profile SET afternoonTimeMinutes = 840 WHERE afternoonTimeMinutes IS NULL")
                db.execSQL("UPDATE factor_profile SET dinnerTimeMinutes = 1020 WHERE dinnerTimeMinutes IS NULL")
                db.execSQL("UPDATE factor_profile SET lateTimeMinutes = 1200 WHERE lateTimeMinutes IS NULL")
                db.execSQL("UPDATE factor_profile SET nightTimeMinutes = 1380 WHERE nightTimeMinutes IS NULL")
                db.execSQL("UPDATE factor_profile SET basalTimeMinutes = 1140 WHERE basalTimeMinutes IS NULL")
            }
        }
    }
}

