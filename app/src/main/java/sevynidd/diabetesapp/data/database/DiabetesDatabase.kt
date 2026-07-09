package sevynidd.diabetesapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private const val SCHEMA_VERSION_5 = 5
private const val SCHEMA_VERSION_6 = 6
private const val SCHEMA_VERSION_7 = 7
private const val DEFAULT_FACTOR_COLUMN_COUNT = 7

// The 7 default time-of-day boundaries (minutes since midnight) MIGRATION_1_2 originally
// backfilled onto factor_profile — frozen here for MIGRATION_6_7's historical re-seed into
// factor_slot. Intentionally not shared with FactorsRepository's own (separately named, currently
// identical) defaults for brand-new installs: a migration's behavior must stay reproducible even
// if a future change tweaks what a fresh install seeds.
private const val DEFAULT_MORNING_MINUTES = 300
private const val DEFAULT_BREAKFAST_MINUTES = 540
private const val DEFAULT_LUNCH_MINUTES = 720
private const val DEFAULT_AFTERNOON_MINUTES = 840
private const val DEFAULT_DINNER_MINUTES = 1020
private const val DEFAULT_LATE_MINUTES = 1200
private const val DEFAULT_NIGHT_MINUTES = 1380

/** The app's Room database: the factor profile, its factor slots, and saved bolus templates. */
@Database(
    entities = [FactorProfileEntity::class, FactorSlotEntity::class, BolusTemplateEntity::class],
    version = 7,
    exportSchema = false
)
abstract class DiabetesDatabase : RoomDatabase() {
    abstract fun factorProfileDao(): FactorProfileDao
    abstract fun factorSlotDao(): FactorSlotDao
    abstract fun bolusTemplateDao(): BolusTemplateDao

    companion object {
        @Volatile
        private var instance: DiabetesDatabase? = null

        /** Returns the process-wide singleton database instance, creating it on first access. */
        fun getInstance(context: Context): DiabetesDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DiabetesDatabase::class.java,
                    "diabetes_app.db"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3,
                        MIGRATION_3_4,
                        MIGRATION_4_5,
                        MIGRATION_5_6,
                        MIGRATION_6_7
                    )
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

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN isPeriodeEnabled INTEGER NOT NULL DEFAULT 0")
            }
        }

        private val MIGRATION_5_6 = object : Migration(SCHEMA_VERSION_5, SCHEMA_VERSION_6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE factor_profile ADD COLUMN basalReminderEnabled INTEGER NOT NULL DEFAULT 0")
            }
        }

        // Replaces the 7 hardcoded named factor+time columns on factor_profile with a variable-
        // length factor_slot child table, so the app can support any number of freely named
        // factors. Migrations can't call the app's translate() function, so the 7 rows seeded
        // here from existing data use fixed English names; they're immediately renamable
        // afterward. A genuinely new install (no factor_profile row at all yet) seeds its factors
        // separately in Kotlin via FactorsRepository.seedDefaultFactorsIfEmpty, which can localize.
        private val MIGRATION_6_7 = object : Migration(SCHEMA_VERSION_6, SCHEMA_VERSION_7) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS factor_slot (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        factorValue REAL,
                        startTimeMinutes INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                seedFactorSlotsFromOldColumns(db)

                db.execSQL(
                    """
                    CREATE TABLE factor_profile_new (
                        id INTEGER PRIMARY KEY NOT NULL,
                        isPeriodeEnabled INTEGER NOT NULL DEFAULT 0,
                        basalRate INTEGER,
                        basalTimeMinutes INTEGER,
                        basalReminderEnabled INTEGER NOT NULL DEFAULT 0
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO factor_profile_new (id, isPeriodeEnabled, basalRate, basalTimeMinutes, basalReminderEnabled)
                    SELECT id, isPeriodeEnabled, basalRate, basalTimeMinutes, basalReminderEnabled FROM factor_profile
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE factor_profile")
                db.execSQL("ALTER TABLE factor_profile_new RENAME TO factor_profile")
            }

            private fun seedFactorSlotsFromOldColumns(db: SupportSQLiteDatabase) {
                val defaultNames = listOf("Morning", "Breakfast", "Lunch", "Afternoon", "Dinner", "Late", "Night")
                val defaultTimes = listOf(
                    DEFAULT_MORNING_MINUTES,
                    DEFAULT_BREAKFAST_MINUTES,
                    DEFAULT_LUNCH_MINUTES,
                    DEFAULT_AFTERNOON_MINUTES,
                    DEFAULT_DINNER_MINUTES,
                    DEFAULT_LATE_MINUTES,
                    DEFAULT_NIGHT_MINUTES
                )

                db.query(
                    "SELECT morningFactor, breakfastFactor, lunchFactor, afternoonFactor, dinnerFactor, " +
                        "lateFactor, nightFactor, morningTimeMinutes, breakfastTimeMinutes, lunchTimeMinutes, " +
                        "afternoonTimeMinutes, dinnerTimeMinutes, lateTimeMinutes, nightTimeMinutes " +
                        "FROM factor_profile WHERE id = ${FactorProfileEntity.SINGLE_PROFILE_ID} LIMIT 1"
                ).use { cursor ->
                    if (!cursor.moveToFirst()) return

                    for (index in defaultNames.indices) {
                        val factorValue: Double? = if (cursor.isNull(index)) null else cursor.getDouble(index)
                        val timeColumn = index + DEFAULT_FACTOR_COLUMN_COUNT
                        val startTimeMinutes = if (cursor.isNull(timeColumn)) {
                            defaultTimes[index]
                        } else {
                            cursor.getInt(timeColumn)
                        }

                        db.execSQL(
                            "INSERT INTO factor_slot (name, factorValue, startTimeMinutes) VALUES (?, ?, ?)",
                            arrayOf<Any?>(defaultNames[index], factorValue, startTimeMinutes)
                        )
                    }
                }
            }
        }
    }
}
