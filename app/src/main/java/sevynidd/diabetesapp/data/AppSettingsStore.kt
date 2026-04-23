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
import sevynidd.diabetesapp.data.settings.ThemeMode
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

data class AppSettings(
    val themeMode: ThemeMode = ThemeMode.System,
    val contrastLevel: ContrastLevel = ContrastLevel.Normal,
    val language: AppLanguage = AppLanguage.System,
    val breadUnits: Double = DEFAULT_BREAD_UNITS
)

class AppSettingsStore(private val context: Context) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CONTRAST_LEVEL = stringPreferencesKey("contrast_level")
        // Keep legacy key name so existing language value migrates seamlessly.
        val LANGUAGE = stringPreferencesKey(LEGACY_LANGUAGE_KEY)
        val BREAD_UNITS = stringPreferencesKey("bread_units")
    }

    val settingsFlow: Flow<AppSettings> = context.appSettingsDataStore.data.map { preferences ->
        AppSettings(
            themeMode = preferences[Keys.THEME_MODE].toEnumOrDefault(ThemeMode.System),
            contrastLevel = preferences[Keys.CONTRAST_LEVEL].toEnumOrDefault(ContrastLevel.Normal),
            language = preferences[Keys.LANGUAGE].toEnumOrDefault(AppLanguage.System),
            breadUnits = preferences[Keys.BREAD_UNITS].toDoubleOrDefault(DEFAULT_BREAD_UNITS)
        )
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.THEME_MODE] = themeMode.name
        }
    }

    suspend fun setContrastLevel(contrastLevel: ContrastLevel) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.CONTRAST_LEVEL] = contrastLevel.name
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.LANGUAGE] = language.name
        }
    }

    suspend fun setBreadUnits(breadUnits: Double) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.BREAD_UNITS] = breadUnits.toString()
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

