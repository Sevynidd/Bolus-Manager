package sevynidd.diabetesapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.data.settings.GlucoseUnit
import sevynidd.diabetesapp.data.settings.ThemeMode
import sevynidd.diabetesapp.navigation.AppDestinations
import sevynidd.diabetesapp.ui.theme.ContrastLevel

private const val DATASTORE_NAME = "app_settings"
private const val LEGACY_PREFS_NAME = "diabetes_app_settings"
private const val LEGACY_LANGUAGE_KEY = "pref_language"

private val Context.appSettingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATASTORE_NAME,
    produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context, LEGACY_PREFS_NAME))
    }
)

/** The user's persisted app-wide preferences, with defaults applied for any unset value. */
data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.System,
    val contrastLevel: ContrastLevel = ContrastLevel.Normal,
    val language: AppLanguage = AppLanguage.System,
    val breadUnits: Double = DEFAULT_BREAD_UNITS,
    val periodFactorPercent: Double = DEFAULT_PERIOD_FACTOR_PERCENT,
    val correctionThresholdMgDl: Double = DEFAULT_CORRECTION_THRESHOLD_MGDL,
    val correctionStepMgDl: Double = DEFAULT_CORRECTION_STEP_MGDL,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl
)

/** Reads and writes [AppSettings] to DataStore, migrating them from the legacy SharedPreferences store. */
class AppSettingsStore(private val context: Context) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CONTRAST_LEVEL = stringPreferencesKey("contrast_level")
        // Keep legacy key name so existing language value migrates seamlessly.
        val LANGUAGE = stringPreferencesKey(LEGACY_LANGUAGE_KEY)
        val BREAD_UNITS = stringPreferencesKey("bread_units")
        // Keep legacy key name so existing period-factor values migrate seamlessly.
        val PERIOD_FACTOR_PERCENT = stringPreferencesKey("periode_factor_percent")
        val LAST_DESTINATION = stringPreferencesKey("last_destination")
        val CORRECTION_THRESHOLD_MGDL = stringPreferencesKey("correction_threshold_mgdl")
        val CORRECTION_STEP_MGDL = stringPreferencesKey("correction_step_mgdl")
        val GLUCOSE_UNIT = stringPreferencesKey("glucose_unit")
    }

    /** The current [AppSettings], updating whenever a stored value changes. */
    val settingsFlow: Flow<AppSettings> = context.appSettingsDataStore.data.map { preferences ->
        AppSettings(
            themeMode = preferences[Keys.THEME_MODE].toEnumOrDefault(ThemeMode.System),
            contrastLevel = preferences[Keys.CONTRAST_LEVEL].toEnumOrDefault(ContrastLevel.Normal),
            language = preferences[Keys.LANGUAGE].toEnumOrDefault(AppLanguage.System),
            breadUnits = preferences[Keys.BREAD_UNITS].toDoubleOrDefault(DEFAULT_BREAD_UNITS),
            periodFactorPercent = preferences[Keys.PERIOD_FACTOR_PERCENT]
                .toDoubleOrDefault(DEFAULT_PERIOD_FACTOR_PERCENT),
            correctionThresholdMgDl = preferences[Keys.CORRECTION_THRESHOLD_MGDL]
                .toDoubleOrDefault(DEFAULT_CORRECTION_THRESHOLD_MGDL),
            correctionStepMgDl = preferences[Keys.CORRECTION_STEP_MGDL]
                .toDoubleOrDefault(DEFAULT_CORRECTION_STEP_MGDL),
            glucoseUnit = preferences[Keys.GLUCOSE_UNIT].toEnumOrDefault(GlucoseUnit.MgDl)
        )
    }

    /** Persists the selected [themeMode]. */
    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.THEME_MODE] = themeMode.name
        }
    }

    /** Persists the selected [contrastLevel]. */
    suspend fun setContrastLevel(contrastLevel: ContrastLevel) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.CONTRAST_LEVEL] = contrastLevel.name
        }
    }

    /** Persists the selected [language]. */
    suspend fun setLanguage(language: AppLanguage) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.LANGUAGE] = language.name
        }
    }

    /** Persists the configured grams-per-bread-unit value used by bolus calculations. */
    suspend fun setBreadUnits(breadUnits: Double) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.BREAD_UNITS] = breadUnits.toString()
        }
    }

    /** Persists the configured "Period" factor surcharge, as a percentage. */
    suspend fun setPeriodFactorPercent(percentage: Double) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.PERIOD_FACTOR_PERCENT] = percentage.toString()
        }
    }

    /** Persists the configured blood-sugar correction-dose threshold, in mg/dl. */
    suspend fun setCorrectionThresholdMgDl(thresholdMgDl: Double) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.CORRECTION_THRESHOLD_MGDL] = thresholdMgDl.toString()
        }
    }

    /** Persists the configured mg/dl step that adds one whole correction unit. */
    suspend fun setCorrectionStepMgDl(stepMgDl: Double) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.CORRECTION_STEP_MGDL] = stepMgDl.toString()
        }
    }

    /** Persists the selected [glucoseUnit] used to enter and display blood-sugar values. */
    suspend fun setGlucoseUnit(glucoseUnit: GlucoseUnit) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.GLUCOSE_UNIT] = glucoseUnit.name
        }
    }

    /**
     * The top-level screen that was open when the app was last closed. `null` means it hasn't
     * been read from disk yet (kept separate from [settingsFlow] so callers can tell "not loaded
     * yet" apart from "loaded, and it's [AppDestinations.FACTORS]" instead of silently defaulting).
     */
    val lastDestinationFlow: Flow<AppDestinations?> = context.appSettingsDataStore.data.map { preferences ->
        preferences[Keys.LAST_DESTINATION]?.let { name ->
            AppDestinations.entries.firstOrNull { it.name == name }
        }
    }

    /** Persists [destination] as the screen to reopen to on the next app start. */
    suspend fun setLastDestination(destination: AppDestinations) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.LAST_DESTINATION] = destination.name
        }
    }
}

private inline fun <reified T : Enum<T>> String?.toEnumOrDefault(defaultValue: T): T {
    if (this == null) {
        return defaultValue
    }
    return enumValues<T>().firstOrNull { it.name == this } ?: defaultValue
}

private fun String?.toDoubleOrDefault(defaultValue: Double): Double {
    return this?.toDoubleOrNull() ?: defaultValue
}

private const val DEFAULT_BREAD_UNITS = 12.0
private const val DEFAULT_PERIOD_FACTOR_PERCENT = 0.0
private const val DEFAULT_CORRECTION_THRESHOLD_MGDL = 160.0
private const val DEFAULT_CORRECTION_STEP_MGDL = 30.0

