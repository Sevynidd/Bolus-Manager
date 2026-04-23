package sevynidd.diabetesapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [FactorProfileEntity::class, BolusTemplateEntity::class],
    version = 4,
    exportSchema = false
)
abstract class DiabetesDatabase : RoomDatabase() {
    abstract fun factorProfileDao(): FactorProfileDao
    abstract fun bolusTemplateDao(): BolusTemplateDao

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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
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

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS bolus_template (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        emoji TEXT,
                        carbohydrates REAL NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE bolus_template ADD COLUMN nameNormalized TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE bolus_template ADD COLUMN lastUsedAtEpochMillis INTEGER NOT NULL DEFAULT 0")
                db.execSQL("UPDATE bolus_template SET nameNormalized = LOWER(TRIM(name)) WHERE nameNormalized = ''")
                db.execSQL("DELETE FROM bolus_template WHERE id NOT IN (SELECT MAX(id) FROM bolus_template GROUP BY nameNormalized)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_bolus_template_nameNormalized ON bolus_template(nameNormalized)")
            }
        }
    }
}

