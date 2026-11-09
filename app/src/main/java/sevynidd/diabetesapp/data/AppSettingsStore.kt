package sevynidd.diabetesapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.data.settings.appearance.ThemeMode
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode
import sevynidd.diabetesapp.data.settings.profile.Gender
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
    val useDynamicColor: Boolean = false,
    val language: AppLanguage = AppLanguage.System,
    val breadUnits: Double = DEFAULT_BREAD_UNITS,
    val periodFactorPercent: Double = DEFAULT_PERIOD_FACTOR_PERCENT,
    val correctionThresholdMgDl: Double = DEFAULT_CORRECTION_THRESHOLD_MGDL,
    val correctionLowThresholdMgDl: Double = DEFAULT_CORRECTION_LOW_THRESHOLD_MGDL,
    val correctionStepMgDl: Double = DEFAULT_CORRECTION_STEP_MGDL,
    val glucoseUnit: GlucoseUnit = GlucoseUnit.MgDl,
    val doseStepUnits: Double = CorrectionSettings().doseStepUnits,
    val maxDoseUnits: Double = CorrectionSettings().maxDoseUnits,
    val fastCarbsGrams: Double = CorrectionSettings().fastCarbsGrams,
    val maxBloodSugarMgDl: Double = CorrectionSettings().maxBloodSugarMgDl,
    val maxCarbohydratesGrams: Double = CorrectionSettings().maxCarbohydratesGrams,
    val gender: Gender = Gender.PreferNotToSay,
    val therapyMode: TherapyMode = TherapyMode.Pen,
    val hasCompletedOnboarding: Boolean = false
)

/** Reads and writes [AppSettings] to DataStore, migrating them from the legacy SharedPreferences store. */
class AppSettingsStore(private val context: Context) {
    private object Keys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val CONTRAST_LEVEL = stringPreferencesKey("contrast_level")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        // Keep legacy key name so existing language value migrates seamlessly.
        val LANGUAGE = stringPreferencesKey(LEGACY_LANGUAGE_KEY)
        val BREAD_UNITS = stringPreferencesKey("bread_units")
        // Keep legacy key name so existing period-factor values migrate seamlessly.
        val PERIOD_FACTOR_PERCENT = stringPreferencesKey("periode_factor_percent")
        val CORRECTION_THRESHOLD_MGDL = stringPreferencesKey("correction_threshold_mgdl")
        val CORRECTION_LOW_THRESHOLD_MGDL = stringPreferencesKey("correction_low_threshold_mgdl")
        val CORRECTION_STEP_MGDL = stringPreferencesKey("correction_step_mgdl")
        val GLUCOSE_UNIT = stringPreferencesKey("glucose_unit")
        val DOSE_STEP_UNITS = stringPreferencesKey("dose_step_units")
        val MAX_DOSE_UNITS = stringPreferencesKey("max_dose_units")
        val FAST_CARBS_GRAMS = stringPreferencesKey("fast_carbs_grams")
        val MAX_BLOOD_SUGAR_MGDL = stringPreferencesKey("max_blood_sugar_mgdl")
        val MAX_CARBOHYDRATES_GRAMS = stringPreferencesKey("max_carbohydrates_grams")
        val GENDER = stringPreferencesKey("gender")
        val THERAPY_MODE = stringPreferencesKey("therapy_mode")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    /** The current [AppSettings], updating whenever a stored value changes. */
    val settingsFlow: Flow<AppSettings> = context.appSettingsDataStore.data.map { preferences ->
        AppSettings(
            themeMode = preferences[Keys.THEME_MODE].toEnumOrDefault(ThemeMode.System),
            contrastLevel = preferences[Keys.CONTRAST_LEVEL].toEnumOrDefault(ContrastLevel.Normal),
            useDynamicColor = preferences[Keys.USE_DYNAMIC_COLOR] ?: false,
            language = preferences[Keys.LANGUAGE].toEnumOrDefault(AppLanguage.System),
            breadUnits = preferences[Keys.BREAD_UNITS].toDoubleOrDefault(DEFAULT_BREAD_UNITS),
            periodFactorPercent = preferences[Keys.PERIOD_FACTOR_PERCENT]
                .toDoubleOrDefault(DEFAULT_PERIOD_FACTOR_PERCENT),
            correctionThresholdMgDl = preferences[Keys.CORRECTION_THRESHOLD_MGDL]
                .toDoubleOrDefault(DEFAULT_CORRECTION_THRESHOLD_MGDL),
            correctionLowThresholdMgDl = preferences[Keys.CORRECTION_LOW_THRESHOLD_MGDL]
                .toDoubleOrDefault(DEFAULT_CORRECTION_LOW_THRESHOLD_MGDL),
            correctionStepMgDl = preferences[Keys.CORRECTION_STEP_MGDL]
                .toDoubleOrDefault(DEFAULT_CORRECTION_STEP_MGDL),
            glucoseUnit = preferences[Keys.GLUCOSE_UNIT].toEnumOrDefault(GlucoseUnit.MgDl),
            doseStepUnits = preferences[Keys.DOSE_STEP_UNITS].toDoubleOrDefault(CorrectionSettings().doseStepUnits),
            maxDoseUnits = preferences[Keys.MAX_DOSE_UNITS].toDoubleOrDefault(CorrectionSettings().maxDoseUnits),
            fastCarbsGrams = preferences[Keys.FAST_CARBS_GRAMS].toDoubleOrDefault(CorrectionSettings().fastCarbsGrams),
            maxBloodSugarMgDl = preferences[Keys.MAX_BLOOD_SUGAR_MGDL]
                .toDoubleOrDefault(CorrectionSettings().maxBloodSugarMgDl),
            maxCarbohydratesGrams = preferences[Keys.MAX_CARBOHYDRATES_GRAMS]
                .toDoubleOrDefault(CorrectionSettings().maxCarbohydratesGrams),
            gender = preferences[Keys.GENDER].toEnumOrDefault(Gender.PreferNotToSay),
            therapyMode = preferences[Keys.THERAPY_MODE]
                .toEnumOrDefault(TherapyMode.Pen),
            hasCompletedOnboarding = preferences[Keys.ONBOARDING_COMPLETED] ?: false
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

    /** Persists whether the theme takes its colors from the wallpaper instead of the app's own palette. */
    suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.USE_DYNAMIC_COLOR] = useDynamicColor
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

    /**
     * Persists the correction thresholds, step, glucose unit, dose step, max dose and fast-carb amount together, in one
     * atomic write.
     */
    suspend fun setCorrectionSettings(correctionSettings: CorrectionSettings) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.CORRECTION_THRESHOLD_MGDL] = correctionSettings.thresholdMgDl.toString()
            preferences[Keys.CORRECTION_LOW_THRESHOLD_MGDL] = correctionSettings.lowThresholdMgDl.toString()
            preferences[Keys.CORRECTION_STEP_MGDL] = correctionSettings.stepMgDl.toString()
            preferences[Keys.GLUCOSE_UNIT] = correctionSettings.glucoseUnit.name
            preferences[Keys.DOSE_STEP_UNITS] = correctionSettings.doseStepUnits.toString()
            preferences[Keys.MAX_DOSE_UNITS] = correctionSettings.maxDoseUnits.toString()
            preferences[Keys.FAST_CARBS_GRAMS] = correctionSettings.fastCarbsGrams.toString()
            preferences[Keys.MAX_BLOOD_SUGAR_MGDL] = correctionSettings.maxBloodSugarMgDl.toString()
            preferences[Keys.MAX_CARBOHYDRATES_GRAMS] = correctionSettings.maxCarbohydratesGrams.toString()
        }
    }

    /** Persists the selected [gender], used to gate whether Period-related settings apply. */
    suspend fun setGender(gender: Gender) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.GENDER] = gender.name
        }
    }

    /** Persists whether factors are shown per bread unit or as an insulin-pump ratio. */
    suspend fun setTherapyMode(mode: TherapyMode) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.THERAPY_MODE] = mode.name
        }
    }

    /** Persists whether the first-run onboarding tutorial has been completed. */
    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.appSettingsDataStore.edit { preferences ->
            preferences[Keys.ONBOARDING_COMPLETED] = completed
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
private const val DEFAULT_CORRECTION_LOW_THRESHOLD_MGDL = 100.0
private const val DEFAULT_CORRECTION_STEP_MGDL = 30.0

