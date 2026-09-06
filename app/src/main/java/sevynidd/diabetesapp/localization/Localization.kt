package sevynidd.diabetesapp.localization

import java.util.Locale

/** A language the UI can be displayed in, or [System] to follow the device locale. */
enum class AppLanguage {
    System,
    English,
    German,
    French,
    Polish
}

/** A user-facing string that [translate] resolves to localized text. */
enum class TranslationKey {
    // Appearance & theme
    Appearance,
    ThemeMode,
    ThemeModeHelp,
    ContrastLevel,
    ContrastLevelHelp,
    Language,
    LanguageHelp,
    ThemeSystem,
    ThemeLight,
    ThemeDark,
    ContrastNormal,
    ContrastMedium,
    ContrastHigh,
    LanguageEnglish,
    LanguageGerman,
    LanguageFrench,
    LanguagePolish,
    LanguageSystem,

    // Navigation
    DestinationFactors,
    DestinationCalculate,
    DestinationSettings,

    // Common actions
    ActionEdit,
    ActionSave,
    ActionCancel,
    ActionClose,
    ActionBack,
    ActionTemplates,
    HelpIconContentDescription,
    ActionDelete,
    ActionMoreOptions,
    ActionNext,
    ActionFinish,

    // Factors & schedule
    // FactorMorning..FactorNight are no longer "the" factor labels — factors are now a free-form,
    // user-editable list (see FactorSlot). These 7 keys are kept only as localized seed names for
    // a brand new install (see FactorsRepository.seedDefaultFactorsIfEmpty), used nowhere else.
    FactorMorning,
    FactorBreakfast,
    FactorLunch,
    FactorAfternoon,
    FactorDinner,
    FactorLate,
    FactorNight,
    FactorsListTitle,
    FactorsListHelp,
    BasalRate,
    BasalRateHelp,
    LabelFactor,
    FactorValueHelp,
    FactorNameLabel,
    FactorNameHelp,
    FactorStartTime,
    FactorStartTimeHelp,
    ActionSchedule,
    ActionAddFactor,
    FactorNameDuplicateError,
    ScheduleAutoOrderHint,

    // Bolus calculation
    BolusType,
    BolusTypeHelp,
    BolusNormal,
    BolusSplit,
    BolusUnits,
    BolusImmediatePercent,
    BolusImmediatePercentHelp,
    BolusExtendedPercent,
    BolusDurationMinutes,
    BolusDurationMinutesHelp,
    Carbohydrates,
    CarbohydratesHelp,
    ActiveFactor,
    Calculated,
    CalculatedUnits,
    BolusImmediateUnits,
    BolusExtendedUnits,
    FutureFactor,
    PeriodLabel,
    PeriodHelp,
    PeriodFactorPercent,
    PeriodFactorPercentHelp,
    BreadUnits,
    BreadUnitsHelp,
    FactorSettingsTitle,
    BloodSugar,
    BloodSugarHelp,
    CorrectionUnits,
    CorrectionSettingsTitle,
    CorrectionThreshold,
    CorrectionThresholdHelp,
    CorrectionLowThreshold,
    CorrectionLowThresholdHelp,
    CorrectionStep,
    CorrectionStepHelp,
    GlucoseUnitLabel,
    GlucoseUnitHelp,
    GenderSettingsTitle,
    GenderSettingsHelp,
    GenderMale,
    GenderFemale,
    GenderPreferNotToSay,
    OnboardingStepLabel,
    ReplayTutorial,

    // Templates
    TemplatesTitle,
    TemplateAdd,
    TemplateEdit,
    TemplateDelete,
    TemplateName,
    TemplateNameHelp,
    TemplateEmojiOptional,
    TemplateEmojiHelp,
    TemplateEmoji,
    TemplateSortTitle,
    TemplateSortHelp,
    TemplateEmpty,
    TemplateSortRecent,
    TemplateSortAlphabetical,
    TemplateDuplicateNameError,
    ActiveNowBadge,

    // App update
    AppUpdateTitle,
    AppUpdateCurrentVersion,
    AppUpdateCheckButton,
    AppUpdateChecking,
    AppUpdateUpToDate,
    AppUpdateAvailable,
    AppUpdateDownloadButton,
    AppUpdateDownloading,
    AppUpdateReadyToInstall,
    AppUpdatePermissionNeeded,
    AppUpdateOpenSettingsButton,
    AppUpdateError,
    AppUpdateRetryButton,
    AppUpdateViewOnGitHub,
    AppUpdateChannelName,
    AppUpdateNotificationBody,

    // Notifications
    BasalReminderEnabled,
    BasalReminderHelp,
    BasalReminderNotificationTitle,
    BasalReminderNotificationBody,
    BasalReminderChannelName,
    BasalReminderExactAlarmHint,
    NotificationSettingsTitle,

    // Import & export
    DataManagementTitle,
    DataManagementDescription,
    ActionExport,
    ActionImport,
    ExportSuccessMessage,
    ExportErrorMessage,
    ImportSuccessMessage,
    ImportErrorMessage,

    // Statistics & documentation
    StatisticsSettingsTitle,
    StatisticsSettingsHelp,
    StatisticsSectionTitle,
    StatisticsSectionHelp,
    StatisticsEmptyState,
    StatisticsSeriesNotEnoughData,
    DocumentationSectionTitle,
    DocumentationSectionHelp,
    DocumentationEmptyState,
    ActionExportCsv,
    ActionExportPdf,
    LogExportSuccessMessage,
    LogExportErrorMessage,
    AuditLogCsvDateHeader,
    AuditLogCsvDescriptionHeader,
    PdfExportSuccessMessage,
    PdfExportErrorMessage,
    PdfReportTitle,
    PdfGeneratedOnLabel,
    PdfFactorTimeWindowHeader,
    PdfBasalRateSummary,
    AuditFactorAdded,
    AuditFactorValueChanged,
    AuditFactorTimeChanged,
    AuditFactorDeleted,
    AuditBasalRateChanged,
    AuditBasalTimeChanged,
    AuditUnknownChange,
    AuditValueNotSet
}

/** The localized text for [key] in [language]; [AppLanguage.System] resolves to the device locale. */
fun translate(key: TranslationKey, language: AppLanguage): String {
    val effectiveLanguage = resolveAppLanguage(language)
    return when (effectiveLanguage) {
        AppLanguage.English -> when (key) {
            // Appearance & theme
            TranslationKey.Appearance -> "Appearance"
            TranslationKey.ThemeMode -> "Theme mode"
            TranslationKey.ThemeModeHelp ->
                "Choose System to follow your device's light/dark setting, or force Light or " +
                    "Dark regardless of the device."
            TranslationKey.ContrastLevel -> "Contrast level"
            TranslationKey.ContrastLevelHelp ->
                "Increases the contrast between text, icons, and their background for better " +
                    "readability. Higher contrast can help in bright light or with low vision."
            TranslationKey.Language -> "Language"
            TranslationKey.LanguageHelp ->
                "The language used throughout the app. Choose System to follow your device's " +
                    "language setting."
            TranslationKey.ThemeSystem -> "System"
            TranslationKey.ThemeLight -> "Light"
            TranslationKey.ThemeDark -> "Dark"
            TranslationKey.ContrastNormal -> "Normal"
            TranslationKey.ContrastMedium -> "Medium"
            TranslationKey.ContrastHigh -> "High"
            TranslationKey.LanguageEnglish -> "English"
            TranslationKey.LanguageGerman -> "German"
            TranslationKey.LanguageFrench -> "French"
            TranslationKey.LanguagePolish -> "Polish"
            TranslationKey.LanguageSystem -> "System"

            // Navigation
            TranslationKey.DestinationFactors -> "Factors"
            TranslationKey.DestinationCalculate -> "Calculate"
            TranslationKey.DestinationSettings -> "Settings"

            // Common actions
            TranslationKey.ActionEdit -> "Edit"
            TranslationKey.ActionSave -> "Save"
            TranslationKey.ActionCancel -> "Cancel"
            TranslationKey.ActionClose -> "Close"
            TranslationKey.ActionBack -> "Back"
            TranslationKey.ActionTemplates -> "Templates"
            TranslationKey.HelpIconContentDescription -> "Help"
            TranslationKey.ActionDelete -> "Delete"
            TranslationKey.ActionMoreOptions -> "More options"
            TranslationKey.ActionNext -> "Next"
            TranslationKey.ActionFinish -> "Finish"

            // Factors & schedule
            TranslationKey.FactorMorning -> "Morning"
            TranslationKey.FactorBreakfast -> "Breakfast"
            TranslationKey.FactorLunch -> "Lunch"
            TranslationKey.FactorAfternoon -> "Afternoon"
            TranslationKey.FactorDinner -> "Dinner"
            TranslationKey.FactorLate -> "Late"
            TranslationKey.FactorNight -> "Night"
            TranslationKey.FactorsListTitle -> "Factors"
            TranslationKey.FactorsListHelp ->
                "Each row is a time window: name it (e.g. \"Breakfast\") and set its factor — " +
                    "the units of insulin per bread unit used whenever the current time falls in " +
                    "that window. Windows are ordered automatically by start time."
            TranslationKey.BasalRate -> "Basal rate"
            TranslationKey.BasalRateHelp ->
                "Your basal insulin dose, shown for reference alongside the time it's " +
                    "administered. Editing it here does not change any calculation — it's just a record."
            TranslationKey.LabelFactor -> "Factor"
            TranslationKey.FactorValueHelp ->
                "Units of insulin per bread unit during this time window. A higher factor means " +
                    "more insulin for the same amount of carbohydrates."
            TranslationKey.FactorNameLabel -> "Name"
            TranslationKey.FactorNameHelp ->
                "The name for this time window, e.g. \"Breakfast\" or \"Lunch\" — shown " +
                    "throughout the app wherever this factor is active."
            TranslationKey.FactorStartTime -> "Start time"
            TranslationKey.FactorStartTimeHelp ->
                "The time of day this factor (or your basal dose) becomes active. Factors are " +
                    "automatically ordered by start time, and each window ends where the next one begins."
            TranslationKey.ActionSchedule -> "Schedule"
            TranslationKey.ActionAddFactor -> "Add factor"
            TranslationKey.FactorNameDuplicateError -> "A factor with this name already exists"
            TranslationKey.ScheduleAutoOrderHint -> "Times are auto-corrected to keep the daily order."

            // Bolus calculation
            TranslationKey.BolusType -> "Bolus type"
            TranslationKey.BolusTypeHelp ->
                "Normal calculates one combined dose right away. Split divides the dose into an " +
                    "immediate part now and an extended part later, useful for meals that digest slowly."
            TranslationKey.BolusNormal -> "Normal"
            TranslationKey.BolusSplit -> "Split bolus"
            TranslationKey.BolusUnits -> "Bolus units"
            TranslationKey.BolusImmediatePercent -> "Immediate share (%)"
            TranslationKey.BolusImmediatePercentHelp ->
                "The percentage of the split dose given immediately; the rest (shown as Extended " +
                    "share) is given later, using the factor active at that later time."
            TranslationKey.BolusExtendedPercent -> "Extended share (%)"
            TranslationKey.BolusDurationMinutes -> "Duration (minutes)"
            TranslationKey.BolusDurationMinutesHelp ->
                "How many minutes after now the extended part of a split dose is due, using the " +
                    "factor active at that later time."
            TranslationKey.Carbohydrates -> "Carbohydrates"
            TranslationKey.CarbohydratesHelp ->
                "The grams of carbohydrates in this meal. Combined with your active factor and " +
                    "bread-unit setting, this determines the carb-based part of your dose."
            TranslationKey.ActiveFactor -> "Active factor"
            TranslationKey.Calculated -> "Calculated"
            TranslationKey.CalculatedUnits -> "Calculated units"
            TranslationKey.BolusImmediateUnits -> "Immediate units"
            TranslationKey.BolusExtendedUnits -> "Extended units"
            TranslationKey.FutureFactor -> "Future factor"
            TranslationKey.PeriodLabel -> "Period?"
            TranslationKey.PeriodHelp ->
                "When enabled, every active factor is scaled up by the Period surcharge " +
                    "percentage configured in Factor settings, to account for higher insulin " +
                    "needs during your period."
            TranslationKey.PeriodFactorPercent -> "Period increase (%)"
            TranslationKey.PeriodFactorPercentHelp ->
                "How much your active factor is increased, as a percentage, while Period is " +
                    "enabled — to cover the higher insulin needs many people experience during their period."
            TranslationKey.BreadUnits -> "Bread units"
            TranslationKey.BreadUnitsHelp ->
                "Grams of carbohydrates that count as one bread unit in your calculations, e.g. " +
                    "12 g. Your active factor is applied per bread unit, not per gram."
            TranslationKey.FactorSettingsTitle -> "Factor settings"
            TranslationKey.BloodSugar -> "Blood sugar"
            TranslationKey.BloodSugarHelp ->
                "Enter your current blood sugar to apply a correction to your dose: units are " +
                    "added if it's above the high correction threshold and subtracted if it's " +
                    "below the low threshold (configured in Correction settings). Your total " +
                    "dose is never suggested below zero."
            TranslationKey.CorrectionUnits -> "Correction units"
            TranslationKey.CorrectionSettingsTitle -> "Correction"
            TranslationKey.CorrectionThreshold -> "Correction threshold"
            TranslationKey.CorrectionThresholdHelp ->
                "If your blood sugar is above this value, extra insulin is added to your dose: " +
                    "one unit for every correction step above the threshold, rounded to the " +
                    "nearest whole unit."
            TranslationKey.CorrectionLowThreshold -> "Low correction threshold"
            TranslationKey.CorrectionLowThresholdHelp ->
                "If your blood sugar is below this value, insulin is subtracted from your dose: " +
                    "one unit for every correction step below the threshold, rounded to the " +
                    "nearest whole unit. Your total dose is never reduced below zero."
            TranslationKey.CorrectionStep -> "Correction step"
            TranslationKey.CorrectionStepHelp ->
                "How many mg/dl (or mmol/l) of blood sugar correspond to one unit of correction " +
                    "insulin, used for both the high and low threshold."
            TranslationKey.GlucoseUnitLabel -> "Blood glucose unit"
            TranslationKey.GlucoseUnitHelp -> "The unit your blood sugar values are entered and displayed in."
            TranslationKey.GenderSettingsTitle -> "Gender"
            TranslationKey.GenderSettingsHelp ->
                "Selecting Female enables the optional Period feature, which lets you " +
                    "temporarily increase your active factor. This setting only affects what's shown in the app."
            TranslationKey.GenderMale -> "Male"
            TranslationKey.GenderFemale -> "Female"
            TranslationKey.GenderPreferNotToSay -> "Prefer not to say"
            TranslationKey.OnboardingStepLabel -> "Step"
            TranslationKey.ReplayTutorial -> "Replay tutorial"

            // Templates
            TranslationKey.TemplatesTitle -> "Templates"
            TranslationKey.TemplateAdd -> "Add template"
            TranslationKey.TemplateEdit -> "Edit template"
            TranslationKey.TemplateDelete -> "Delete template"
            TranslationKey.TemplateName -> "Name"
            TranslationKey.TemplateNameHelp ->
                "The name shown for this template in your list. Must be unique among your saved templates."
            TranslationKey.TemplateEmojiOptional -> "Emoji (optional)"
            TranslationKey.TemplateEmojiHelp ->
                "An optional emoji shown as the template's icon in your list, to help you spot it quickly."
            TranslationKey.TemplateEmpty -> "No templates yet"
            TranslationKey.TemplateEmoji -> "Emoji"
            TranslationKey.TemplateSortTitle -> "Order by"
            TranslationKey.TemplateSortHelp ->
                "Choose whether your templates list is ordered by most recently used or " +
                    "alphabetically by name."
            TranslationKey.TemplateSortRecent -> "Recently used"
            TranslationKey.TemplateSortAlphabetical -> "Alphabetical"
            TranslationKey.TemplateDuplicateNameError -> "A template with this name already exists"
            TranslationKey.ActiveNowBadge -> "Now"

            // App update
            TranslationKey.AppUpdateTitle -> "App update"
            TranslationKey.AppUpdateCurrentVersion -> "Current version"
            TranslationKey.AppUpdateCheckButton -> "Check for updates"
            TranslationKey.AppUpdateChecking -> "Checking for updates…"
            TranslationKey.AppUpdateUpToDate -> "You're up to date"
            TranslationKey.AppUpdateAvailable -> "Update available"
            TranslationKey.AppUpdateDownloadButton -> "Download & install"
            TranslationKey.AppUpdateDownloading -> "Downloading update…"
            TranslationKey.AppUpdateReadyToInstall -> "Follow the prompts to finish installing"
            TranslationKey.AppUpdatePermissionNeeded -> "Allow installing apps from this source to continue"
            TranslationKey.AppUpdateOpenSettingsButton -> "Open settings"
            TranslationKey.AppUpdateError -> "Couldn't check for updates. Please try again."
            TranslationKey.AppUpdateRetryButton -> "Retry"
            TranslationKey.AppUpdateViewOnGitHub -> "View source on GitHub"
            TranslationKey.AppUpdateChannelName -> "App updates"
            TranslationKey.AppUpdateNotificationBody -> "A new version is ready to install:"

            // Notifications
            TranslationKey.BasalReminderEnabled -> "Basal rate reminder"
            TranslationKey.BasalReminderHelp ->
                "Sends a daily notification at your configured basal time, so you don't forget " +
                    "to take your basal dose."
            TranslationKey.BasalReminderNotificationTitle -> "Basal rate reminder"
            TranslationKey.BasalReminderNotificationBody -> "It's time for your basal rate."
            TranslationKey.BasalReminderChannelName -> "Basal rate reminders"
            TranslationKey.BasalReminderExactAlarmHint -> "Allow exact alarms in system settings"
            TranslationKey.NotificationSettingsTitle -> "Notifications"

            // Import & export
            TranslationKey.DataManagementTitle -> "Import & Export"
            TranslationKey.DataManagementDescription ->
                "Save your factors and time windows to a file, or load them from a file you exported earlier."
            TranslationKey.ActionExport -> "Export"
            TranslationKey.ActionImport -> "Import"
            TranslationKey.ExportSuccessMessage -> "Factors exported successfully."
            TranslationKey.ExportErrorMessage -> "Couldn't export factors."
            TranslationKey.ImportSuccessMessage -> "Factors imported successfully."
            TranslationKey.ImportErrorMessage -> "Couldn't import factors. Make sure the file is a valid export."

            // Statistics & documentation
            TranslationKey.StatisticsSettingsTitle -> "Statistics & Documentation"
            TranslationKey.StatisticsSettingsHelp ->
                "Shows how your factors and basal rate have changed over time, and keeps a written " +
                    "log of every change. You can export this history as a CSV file, or export your " +
                    "current factors as a printable PDF report to share with your endocrinologist."
            TranslationKey.StatisticsSectionTitle -> "Statistics"
            TranslationKey.StatisticsSectionHelp ->
                "Charts how each factor's value and your basal rate developed over time, based on your edit history."
            TranslationKey.StatisticsEmptyState -> "No value history recorded yet."
            TranslationKey.StatisticsSeriesNotEnoughData -> "Not enough history yet"
            TranslationKey.DocumentationSectionTitle -> "Change log"
            TranslationKey.DocumentationSectionHelp ->
                "A chronological record of every time a factor or your basal rate was added, edited, " +
                    "or deleted, with the old and new values."
            TranslationKey.DocumentationEmptyState -> "No changes recorded yet."
            TranslationKey.ActionExportCsv -> "Export CSV"
            TranslationKey.ActionExportPdf -> "Export PDF"
            TranslationKey.LogExportSuccessMessage -> "Change log exported successfully."
            TranslationKey.LogExportErrorMessage -> "Couldn't export the change log."
            TranslationKey.AuditLogCsvDateHeader -> "Date"
            TranslationKey.AuditLogCsvDescriptionHeader -> "Description"
            TranslationKey.PdfExportSuccessMessage -> "Factor report exported successfully."
            TranslationKey.PdfExportErrorMessage -> "Couldn't export the factor report."
            TranslationKey.PdfReportTitle -> "Factor Report"
            TranslationKey.PdfGeneratedOnLabel -> $$"Generated on %1$s"
            TranslationKey.PdfFactorTimeWindowHeader -> "Time window"
            TranslationKey.PdfBasalRateSummary -> $$"%1$s units/day at %2$s"
            TranslationKey.AuditFactorAdded -> $$"Factor \"%1$s\" was added with value %2$s."
            TranslationKey.AuditFactorValueChanged -> $$"Factor \"%1$s\" value changed from %2$s to %3$s."
            TranslationKey.AuditFactorTimeChanged -> $$"Factor \"%1$s\" start time changed from %2$s to %3$s."
            TranslationKey.AuditFactorDeleted -> $$"Factor \"%1$s\" (value %2$s) was deleted."
            TranslationKey.AuditBasalRateChanged -> $$"Basal rate changed from %1$s to %2$s units/day."
            TranslationKey.AuditBasalTimeChanged -> $$"Basal rate time changed from %1$s to %2$s."
            TranslationKey.AuditUnknownChange -> "Unknown change."
            TranslationKey.AuditValueNotSet -> "not set"
        }

        AppLanguage.German -> when (key) {
            // Appearance & theme
            TranslationKey.Appearance -> "Darstellung"
            TranslationKey.ThemeMode -> "Thema"
            TranslationKey.ThemeModeHelp ->
                "Wähle System, um die Hell/Dunkel-Einstellung deines Geräts zu übernehmen, oder " +
                    "erzwinge Hell oder Dunkel unabhängig vom Gerät."
            TranslationKey.ContrastLevel -> "Kontrast"
            TranslationKey.ContrastLevelHelp ->
                "Erhöht den Kontrast zwischen Text, Symbolen und ihrem Hintergrund für bessere " +
                    "Lesbarkeit. Höherer Kontrast kann bei hellem Licht oder Sehschwäche helfen."
            TranslationKey.Language -> "Sprache"
            TranslationKey.LanguageHelp ->
                "Die in der gesamten App verwendete Sprache. Wähle System, um die " +
                    "Spracheinstellung deines Geräts zu übernehmen."
            TranslationKey.ThemeSystem -> "System"
            TranslationKey.ThemeLight -> "Hell"
            TranslationKey.ThemeDark -> "Dunkel"
            TranslationKey.ContrastNormal -> "Normal"
            TranslationKey.ContrastMedium -> "Mittel"
            TranslationKey.ContrastHigh -> "Hoch"
            TranslationKey.LanguageEnglish -> "Englisch"
            TranslationKey.LanguageGerman -> "Deutsch"
            TranslationKey.LanguageFrench -> "Französisch"
            TranslationKey.LanguagePolish -> "Polnisch"
            TranslationKey.LanguageSystem -> "System"

            // Navigation
            TranslationKey.DestinationFactors -> "Faktoren"
            TranslationKey.DestinationCalculate -> "Berechnen"
            TranslationKey.DestinationSettings -> "Einstellungen"

            // Common actions
            TranslationKey.ActionEdit -> "Bearbeiten"
            TranslationKey.ActionSave -> "Speichern"
            TranslationKey.ActionCancel -> "Abbrechen"
            TranslationKey.ActionClose -> "Schließen"
            TranslationKey.ActionBack -> "Zurück"
            TranslationKey.ActionTemplates -> "Vorlagen"
            TranslationKey.HelpIconContentDescription -> "Hilfe"
            TranslationKey.ActionDelete -> "Löschen"
            TranslationKey.ActionMoreOptions -> "Weitere Optionen"
            TranslationKey.ActionNext -> "Weiter"
            TranslationKey.ActionFinish -> "Fertigstellen"

            // Factors & schedule
            TranslationKey.FactorMorning -> "Morgen"
            TranslationKey.FactorBreakfast -> "Frühstück"
            TranslationKey.FactorLunch -> "Mittagessen"
            TranslationKey.FactorAfternoon -> "Nachmittag"
            TranslationKey.FactorDinner -> "Abendessen"
            TranslationKey.FactorLate -> "Spätmahlzeit"
            TranslationKey.FactorNight -> "Nacht"
            TranslationKey.FactorsListTitle -> "Faktoren"
            TranslationKey.FactorsListHelp ->
                "Jede Zeile ist ein Zeitfenster: benenne es (z. B. \"Frühstück\") und lege seinen " +
                    "Faktor fest — die Einheiten Insulin pro Broteinheit, die verwendet werden, " +
                    "wenn die aktuelle Zeit in dieses Fenster fällt. Fenster werden automatisch " +
                    "nach Startzeit sortiert."
            TranslationKey.BasalRate -> "Basisrate"
            TranslationKey.BasalRateHelp ->
                "Deine Basalinsulindosis, zur Referenz zusammen mit der Uhrzeit der Verabreichung " +
                    "angezeigt. Das Bearbeiten hier ändert keine Berechnung — es ist nur ein Eintrag."
            TranslationKey.LabelFactor -> "Faktor"
            TranslationKey.FactorValueHelp ->
                "Einheiten Insulin pro Broteinheit in diesem Zeitfenster. Ein höherer Faktor " +
                    "bedeutet mehr Insulin für dieselbe Menge Kohlenhydrate."
            TranslationKey.FactorNameLabel -> "Name"
            TranslationKey.FactorNameHelp ->
                "Der Name für dieses Zeitfenster, z. B. \"Frühstück\" oder \"Mittagessen\" — wird " +
                    "überall in der App angezeigt, wo dieser Faktor aktiv ist."
            TranslationKey.FactorStartTime -> "Startzeit"
            TranslationKey.FactorStartTimeHelp ->
                "Die Tageszeit, zu der dieser Faktor (oder deine Basaldosis) aktiv wird. Faktoren " +
                    "werden automatisch nach Startzeit sortiert, und jedes Fenster endet dort, wo das nächste beginnt."
            TranslationKey.ActionSchedule -> "Zeitplanung"
            TranslationKey.ActionAddFactor -> "Faktor hinzufügen"
            TranslationKey.FactorNameDuplicateError -> "Ein Faktor mit diesem Namen existiert bereits"
            TranslationKey.ScheduleAutoOrderHint -> "Zeiten werden automatisch angepasst, damit die Tagesreihenfolge erhalten bleibt."

            // Bolus calculation
            TranslationKey.BolusType -> "Bolus-Typ"
            TranslationKey.BolusTypeHelp ->
                "Normal berechnet sofort eine kombinierte Dosis. Aufgeteilt teilt die Dosis in " +
                    "einen sofortigen Teil und einen späteren, erweiterten Teil auf — nützlich bei " +
                    "langsam verdaulichen Mahlzeiten."
            TranslationKey.BolusNormal -> "Normal"
            TranslationKey.BolusSplit -> "Gesplitteter Bolus"
            TranslationKey.BolusUnits -> "Bolus-Einheiten"
            TranslationKey.BolusImmediatePercent -> "Sofortanteil (%)"
            TranslationKey.BolusImmediatePercentHelp ->
                "Der Prozentsatz der aufgeteilten Dosis, der sofort verabreicht wird; der Rest " +
                    "(als erweiterter Anteil angezeigt) folgt später, mit dem dann aktiven Faktor."
            TranslationKey.BolusExtendedPercent -> "Verzögerter Anteil (%)"
            TranslationKey.BolusDurationMinutes -> "Dauer (Minuten)"
            TranslationKey.BolusDurationMinutesHelp ->
                "Wie viele Minuten nach jetzt der erweiterte Teil einer aufgeteilten Dosis fällig " +
                    "ist, mit dem dann aktiven Faktor."
            TranslationKey.Carbohydrates -> "Kohlenhydrate"
            TranslationKey.CarbohydratesHelp ->
                "Die Gramm Kohlenhydrate in dieser Mahlzeit. Zusammen mit deinem aktiven Faktor " +
                    "und der Broteinheiten-Einstellung ergibt sich daraus der kohlenhydratbasierte Teil deiner Dosis."
            TranslationKey.ActiveFactor -> "Aktiver Faktor"
            TranslationKey.Calculated -> "Berechnet"
            TranslationKey.CalculatedUnits -> "Berechnete Einheiten"
            TranslationKey.BolusImmediateUnits -> "Sofort-Einheiten"
            TranslationKey.BolusExtendedUnits -> "Verzögerte Einheiten"
            TranslationKey.FutureFactor -> "Zukünftiger Faktor"
            TranslationKey.PeriodLabel -> "Periode?"
            TranslationKey.PeriodHelp ->
                "Wenn aktiviert, wird jeder aktive Faktor um den in den Faktor-Einstellungen " +
                    "konfigurierten Periode-Zuschlag erhöht, um den höheren Insulinbedarf während " +
                    "der Periode auszugleichen."
            TranslationKey.PeriodFactorPercent -> "Periode-Erhöhung (%)"
            TranslationKey.PeriodFactorPercentHelp ->
                "Um wie viel Prozent dein aktiver Faktor erhöht wird, solange Periode aktiviert " +
                    "ist — um den höheren Insulinbedarf vieler Menschen während der Periode zu decken."
            TranslationKey.BreadUnits -> "Broteinheiten"
            TranslationKey.BreadUnitsHelp ->
                "Gramm Kohlenhydrate, die in deinen Berechnungen als eine Broteinheit zählen, z. " +
                    "B. 12 g. Dein aktiver Faktor wird pro Broteinheit angewendet, nicht pro Gramm."
            TranslationKey.FactorSettingsTitle -> "Faktor-Einstellungen"
            TranslationKey.BloodSugar -> "Blutzucker"
            TranslationKey.BloodSugarHelp ->
                "Gib deinen aktuellen Blutzucker ein, um eine Korrektur auf deine Dosis " +
                    "anzuwenden: Über der oberen Korrekturschwelle werden Einheiten addiert, " +
                    "unter der unteren Schwelle werden Einheiten abgezogen (einstellbar unter " +
                    "Korrektur). Die Gesamtdosis wird nie unter null vorgeschlagen."
            TranslationKey.CorrectionUnits -> "Korrektureinheiten"
            TranslationKey.CorrectionSettingsTitle -> "Korrektur"
            TranslationKey.CorrectionThreshold -> "Korrekturschwelle"
            TranslationKey.CorrectionThresholdHelp ->
                "Liegt dein Blutzucker über diesem Wert, wird zusätzliches Insulin zur Dosis " +
                    "addiert: eine Einheit pro Korrekturschritt über der Schwelle, auf die " +
                    "nächste ganze Einheit gerundet."
            TranslationKey.CorrectionLowThreshold -> "Niedrige Korrekturschwelle"
            TranslationKey.CorrectionLowThresholdHelp ->
                "Liegt dein Blutzucker unter diesem Wert, wird Insulin von der Dosis abgezogen: " +
                    "eine Einheit pro Korrekturschritt unter der Schwelle, auf die nächste ganze " +
                    "Einheit gerundet. Die Gesamtdosis wird nie unter null reduziert."
            TranslationKey.CorrectionStep -> "Korrekturschritt"
            TranslationKey.CorrectionStepHelp ->
                "Wie viele mg/dl (bzw. mmol/l) Blutzucker einer Einheit Korrekturinsulin " +
                    "entsprechen, gilt gleichermaßen für die obere und untere Schwelle."
            TranslationKey.GlucoseUnitLabel -> "Blutzucker-Einheit"
            TranslationKey.GlucoseUnitHelp ->
                "Die Einheit, in der deine Blutzuckerwerte eingegeben und angezeigt werden."
            TranslationKey.GenderSettingsTitle -> "Geschlecht"
            TranslationKey.GenderSettingsHelp ->
                "Die Auswahl von Weiblich aktiviert die optionale Periode-Funktion, mit der du " +
                    "deinen aktiven Faktor vorübergehend erhöhen kannst. Diese Einstellung wirkt " +
                    "sich nur auf die Anzeige in der App aus."
            TranslationKey.GenderMale -> "Männlich"
            TranslationKey.GenderFemale -> "Weiblich"
            TranslationKey.GenderPreferNotToSay -> "Keine Angabe"
            TranslationKey.OnboardingStepLabel -> "Schritt"
            TranslationKey.ReplayTutorial -> "Tutorial erneut anzeigen"

            // Templates
            TranslationKey.TemplatesTitle -> "Vorlagen"
            TranslationKey.TemplateAdd -> "Vorlage hinzufügen"
            TranslationKey.TemplateEdit -> "Vorlage bearbeiten"
            TranslationKey.TemplateDelete -> "Vorlage löschen"
            TranslationKey.TemplateName -> "Name"
            TranslationKey.TemplateNameHelp ->
                "Der Name dieser Vorlage in deiner Liste. Muss unter deinen gespeicherten " +
                    "Vorlagen eindeutig sein."
            TranslationKey.TemplateEmojiOptional -> "Emoji (optional)"
            TranslationKey.TemplateEmojiHelp ->
                "Ein optionales Emoji, das als Symbol der Vorlage in deiner Liste angezeigt wird, " +
                    "damit du sie schneller wiedererkennst."
            TranslationKey.TemplateEmpty -> "Noch keine Vorlagen"
            TranslationKey.TemplateEmoji -> "Emoji"
            TranslationKey.TemplateSortTitle -> "Sortierung"
            TranslationKey.TemplateSortHelp ->
                "Wähle, ob deine Vorlagenliste nach zuletzt verwendet oder alphabetisch nach " +
                    "Namen sortiert wird."
            TranslationKey.TemplateSortRecent -> "Zuletzt verwendet"
            TranslationKey.TemplateSortAlphabetical -> "Alphabetisch"
            TranslationKey.TemplateDuplicateNameError -> "Eine Vorlage mit diesem Namen existiert bereits"
            TranslationKey.ActiveNowBadge -> "Jetzt"

            // App update
            TranslationKey.AppUpdateTitle -> "App-Update"
            TranslationKey.AppUpdateCurrentVersion -> "Aktuelle Version"
            TranslationKey.AppUpdateCheckButton -> "Nach Updates suchen"
            TranslationKey.AppUpdateChecking -> "Suche nach Updates…"
            TranslationKey.AppUpdateUpToDate -> "Du bist auf dem neuesten Stand"
            TranslationKey.AppUpdateAvailable -> "Update verfügbar"
            TranslationKey.AppUpdateDownloadButton -> "Herunterladen & installieren"
            TranslationKey.AppUpdateDownloading -> "Update wird heruntergeladen…"
            TranslationKey.AppUpdateReadyToInstall -> "Folge den Anweisungen, um die Installation abzuschließen"
            TranslationKey.AppUpdatePermissionNeeded -> "Erlaube die Installation von Apps aus dieser Quelle, um fortzufahren"
            TranslationKey.AppUpdateOpenSettingsButton -> "Einstellungen öffnen"
            TranslationKey.AppUpdateError -> "Update-Suche fehlgeschlagen. Bitte versuche es erneut."
            TranslationKey.AppUpdateRetryButton -> "Erneut versuchen"
            TranslationKey.AppUpdateViewOnGitHub -> "Quellcode auf GitHub ansehen"
            TranslationKey.AppUpdateChannelName -> "App-Updates"
            TranslationKey.AppUpdateNotificationBody -> "Eine neue Version ist bereit zur Installation:"

            // Notifications
            TranslationKey.BasalReminderEnabled -> "Basisraten-Erinnerung"
            TranslationKey.BasalReminderHelp ->
                "Sendet täglich zur konfigurierten Basalzeit eine Benachrichtigung, damit du " +
                    "deine Basaldosis nicht vergisst."
            TranslationKey.BasalReminderNotificationTitle -> "Basisraten-Erinnerung"
            TranslationKey.BasalReminderNotificationBody -> "Zeit für deine Basisrate."
            TranslationKey.BasalReminderChannelName -> "Basisraten-Erinnerungen"
            TranslationKey.BasalReminderExactAlarmHint -> "Exakte Alarme in den Systemeinstellungen erlauben"
            TranslationKey.NotificationSettingsTitle -> "Benachrichtigungen"

            // Import & export
            TranslationKey.DataManagementTitle -> "Import & Export"
            TranslationKey.DataManagementDescription ->
                "Speichere deine Faktoren und Zeitfenster in einer Datei oder lade sie aus einer " +
                    "zuvor exportierten Datei."
            TranslationKey.ActionExport -> "Exportieren"
            TranslationKey.ActionImport -> "Importieren"
            TranslationKey.ExportSuccessMessage -> "Faktoren erfolgreich exportiert."
            TranslationKey.ExportErrorMessage -> "Faktoren konnten nicht exportiert werden."
            TranslationKey.ImportSuccessMessage -> "Faktoren erfolgreich importiert."
            TranslationKey.ImportErrorMessage ->
                "Faktoren konnten nicht importiert werden. Stelle sicher, dass die Datei ein gültiger Export ist."

            // Statistics & documentation
            TranslationKey.StatisticsSettingsTitle -> "Statistik & Dokumentation"
            TranslationKey.StatisticsSettingsHelp ->
                "Zeigt, wie sich deine Faktoren und deine Basalrate im Laufe der Zeit verändert haben, und " +
                    "führt ein schriftliches Protokoll jeder Änderung. Du kannst diesen Verlauf als CSV-Datei " +
                    "exportieren oder deine aktuellen Faktoren als druckbaren PDF-Bericht, um ihn mit deiner " +
                    "Diabetologin oder deinem Diabetologen zu teilen."
            TranslationKey.StatisticsSectionTitle -> "Statistik"
            TranslationKey.StatisticsSectionHelp ->
                "Zeigt anhand deines Änderungsverlaufs in Diagrammen, wie sich der Wert jedes Faktors und " +
                    "deine Basalrate über die Zeit entwickelt haben."
            TranslationKey.StatisticsEmptyState -> "Noch kein Wertverlauf aufgezeichnet."
            TranslationKey.StatisticsSeriesNotEnoughData -> "Noch nicht genug Verlaufsdaten"
            TranslationKey.DocumentationSectionTitle -> "Änderungsprotokoll"
            TranslationKey.DocumentationSectionHelp ->
                "Eine chronologische Aufzeichnung jeder Hinzufügung, Änderung oder Löschung eines Faktors " +
                    "oder deiner Basalrate, mit altem und neuem Wert."
            TranslationKey.DocumentationEmptyState -> "Noch keine Änderungen aufgezeichnet."
            TranslationKey.ActionExportCsv -> "CSV exportieren"
            TranslationKey.ActionExportPdf -> "PDF exportieren"
            TranslationKey.LogExportSuccessMessage -> "Änderungsprotokoll erfolgreich exportiert."
            TranslationKey.LogExportErrorMessage -> "Änderungsprotokoll konnte nicht exportiert werden."
            TranslationKey.AuditLogCsvDateHeader -> "Datum"
            TranslationKey.AuditLogCsvDescriptionHeader -> "Beschreibung"
            TranslationKey.PdfExportSuccessMessage -> "Faktorenbericht erfolgreich exportiert."
            TranslationKey.PdfExportErrorMessage -> "Faktorenbericht konnte nicht exportiert werden."
            TranslationKey.PdfReportTitle -> "Faktorenbericht"
            TranslationKey.PdfGeneratedOnLabel -> $$"Erstellt am %1$s"
            TranslationKey.PdfFactorTimeWindowHeader -> "Zeitfenster"
            TranslationKey.PdfBasalRateSummary -> $$"%1$s Einheiten/Tag um %2$s"
            TranslationKey.AuditFactorAdded -> $$"Faktor \"%1$s\" wurde mit dem Wert %2$s hinzugefügt."
            TranslationKey.AuditFactorValueChanged -> $$"Wert von Faktor \"%1$s\" wurde von %2$s auf %3$s geändert."
            TranslationKey.AuditFactorTimeChanged ->
                $$"Startzeit von Faktor \"%1$s\" wurde von %2$s auf %3$s geändert."
            TranslationKey.AuditFactorDeleted -> $$"Faktor \"%1$s\" (Wert %2$s) wurde gelöscht."
            TranslationKey.AuditBasalRateChanged -> $$"Basalrate wurde von %1$s auf %2$s Einheiten/Tag geändert."
            TranslationKey.AuditBasalTimeChanged -> $$"Basalrate-Uhrzeit wurde von %1$s auf %2$s geändert."
            TranslationKey.AuditUnknownChange -> "Unbekannte Änderung."
            TranslationKey.AuditValueNotSet -> "nicht festgelegt"
        }

        AppLanguage.French -> when (key) {
            // Appearance & theme
            TranslationKey.Appearance -> "Apparence"
            TranslationKey.ThemeMode -> "Mode Thème"
            TranslationKey.ThemeModeHelp ->
                "Choisissez Système pour suivre le réglage clair/sombre de votre appareil, ou " +
                    "forcez Lumière ou Sombre quel que soit l'appareil."
            TranslationKey.ContrastLevel -> "Niveau de contraste"
            TranslationKey.ContrastLevelHelp ->
                "Augmente le contraste entre le texte, les icônes et leur arrière-plan pour une " +
                    "meilleure lisibilité. Un contraste plus élevé peut aider en lumière vive ou " +
                    "en cas de basse vision."
            TranslationKey.Language -> "Langue"
            TranslationKey.LanguageHelp ->
                "La langue utilisée dans toute l'application. Choisissez Système pour suivre la " +
                    "langue de votre appareil."
            TranslationKey.ThemeSystem -> "Système"
            TranslationKey.ThemeLight -> "Lumière"
            TranslationKey.ThemeDark -> "Sombre"
            TranslationKey.ContrastNormal -> "Normal"
            TranslationKey.ContrastMedium -> "Moyen"
            TranslationKey.ContrastHigh -> "Élevé"
            TranslationKey.LanguageEnglish -> "Anglais"
            TranslationKey.LanguageGerman -> "Allemand"
            TranslationKey.LanguageFrench -> "Français"
            TranslationKey.LanguagePolish -> "Polonais"
            TranslationKey.LanguageSystem -> "Système"

            // Navigation
            TranslationKey.DestinationFactors -> "Facteurs"
            TranslationKey.DestinationCalculate -> "Calculer"
            TranslationKey.DestinationSettings -> "Paramètres"

            // Common actions
            TranslationKey.ActionEdit -> "Modifier"
            TranslationKey.ActionSave -> "Enregistrer"
            TranslationKey.ActionCancel -> "Annuler"
            TranslationKey.ActionClose -> "Fermer"
            TranslationKey.ActionBack -> "Retour"
            TranslationKey.ActionTemplates -> "Modeles"
            TranslationKey.HelpIconContentDescription -> "Aide"
            TranslationKey.ActionDelete -> "Supprimer"
            TranslationKey.ActionMoreOptions -> "Plus d'options"
            TranslationKey.ActionNext -> "Suivant"
            TranslationKey.ActionFinish -> "Terminer"

            // Factors & schedule
            TranslationKey.FactorMorning -> "Matin"
            TranslationKey.FactorBreakfast -> "Petit-déjeuner"
            TranslationKey.FactorLunch -> "Dejeuner"
            TranslationKey.FactorAfternoon -> "Après-midi"
            TranslationKey.FactorDinner -> "Diner"
            TranslationKey.FactorLate -> "Tard"
            TranslationKey.FactorNight -> "Nuit"
            TranslationKey.FactorsListTitle -> "Facteurs"
            TranslationKey.FactorsListHelp ->
                "Chaque ligne est une plage horaire : nommez-la (par ex. \"Petit-déjeuner\") et " +
                    "définissez son facteur — les unités d'insuline par unité de pain utilisées " +
                    "lorsque l'heure actuelle se situe dans cette plage. Les plages sont triées " +
                    "automatiquement par heure de début."
            TranslationKey.BasalRate -> "Débit de base"
            TranslationKey.BasalRateHelp ->
                "Votre dose d'insuline basale, affichée à titre de référence avec l'heure " +
                    "d'administration. La modifier ici ne change aucun calcul — c'est juste un enregistrement."
            TranslationKey.LabelFactor -> "Facteur"
            TranslationKey.FactorValueHelp ->
                "Unités d'insuline par unité de pain pendant cette plage horaire. Un facteur plus " +
                    "élevé signifie plus d'insuline pour la même quantité de glucides."
            TranslationKey.FactorNameLabel -> "Nom"
            TranslationKey.FactorNameHelp ->
                "Le nom de cette plage horaire, par ex. \"Petit-déjeuner\" ou \"Déjeuner\" — " +
                    "affiché partout dans l'application là où ce facteur est actif."
            TranslationKey.FactorStartTime -> "Heure de début"
            TranslationKey.FactorStartTimeHelp ->
                "L'heure de la journée à laquelle ce facteur (ou votre dose basale) devient actif. " +
                    "Les facteurs sont automatiquement triés par heure de début, et chaque plage " +
                    "se termine là où la suivante commence."
            TranslationKey.ActionSchedule -> "Calendrier"
            TranslationKey.ActionAddFactor -> "Ajouter un facteur"
            TranslationKey.FactorNameDuplicateError -> "Un facteur avec ce nom existe déjà"
            TranslationKey.ScheduleAutoOrderHint -> "Les heures sont corrigées automatiquement pour conserver l'ordre de la journée."

            // Bolus calculation
            TranslationKey.BolusType -> "Type de bolus"
            TranslationKey.BolusTypeHelp ->
                "Normal calcule immédiatement une dose combinée unique. Fractionné divise la " +
                    "dose en une part immédiate et une part différée, utile pour les repas à " +
                    "digestion lente."
            TranslationKey.BolusNormal -> "Normal"
            TranslationKey.BolusSplit -> "Bolus fractionné"
            TranslationKey.BolusUnits -> "Unités de bolus"
            TranslationKey.BolusImmediatePercent -> "Part immédiate (%)"
            TranslationKey.BolusImmediatePercentHelp ->
                "Le pourcentage de la dose fractionnée administré immédiatement ; le reste (part " +
                    "différée) est administré plus tard, avec le facteur actif à ce moment-là."
            TranslationKey.BolusExtendedPercent -> "Part prolongée (%)"
            TranslationKey.BolusDurationMinutes -> "Durée (minutes)"
            TranslationKey.BolusDurationMinutesHelp ->
                "Le nombre de minutes après maintenant où la part différée d'une dose fractionnée " +
                    "est due, avec le facteur actif à ce moment-là."
            TranslationKey.Carbohydrates -> "Glucides"
            TranslationKey.CarbohydratesHelp ->
                "Les grammes de glucides de ce repas. Combiné à votre facteur actif et au réglage " +
                    "des unités de pain, cela détermine la part glucidique de votre dose."
            TranslationKey.ActiveFactor -> "Facteur actif"
            TranslationKey.Calculated -> "Calcule"
            TranslationKey.CalculatedUnits -> "Unités calculées"
            TranslationKey.BolusImmediateUnits -> "Unités immédiates"
            TranslationKey.BolusExtendedUnits -> "Unités prolongées"
            TranslationKey.FutureFactor -> "Facteur futur"
            TranslationKey.PeriodLabel -> "Période?"
            TranslationKey.PeriodHelp ->
                "Lorsqu'activé, chaque facteur actif est augmenté du pourcentage de majoration " +
                    "Période configuré dans les paramètres des facteurs, pour couvrir les besoins " +
                    "accrus en insuline pendant vos règles."
            TranslationKey.PeriodFactorPercent -> "Augmentation Période (%)"
            TranslationKey.PeriodFactorPercentHelp ->
                "De combien votre facteur actif est augmenté, en pourcentage, tant que Période " +
                    "est activé — pour couvrir les besoins accrus en insuline que beaucoup " +
                    "connaissent pendant leurs règles."
            TranslationKey.BreadUnits -> "Unités de pain"
            TranslationKey.BreadUnitsHelp ->
                "Grammes de glucides comptant pour une unité de pain dans vos calculs, par ex. " +
                    "12 g. Votre facteur actif est appliqué par unité de pain, pas par gramme."
            TranslationKey.FactorSettingsTitle -> "Paramètres des facteurs"
            TranslationKey.BloodSugar -> "Glycémie"
            TranslationKey.BloodSugarHelp ->
                "Saisissez votre glycémie actuelle pour appliquer une correction à votre dose : " +
                    "des unités sont ajoutées si elle est supérieure au seuil haut de correction " +
                    "et soustraites si elle est inférieure au seuil bas (réglables dans les " +
                    "paramètres de correction). La dose totale n'est jamais suggérée en dessous " +
                    "de zéro."
            TranslationKey.CorrectionUnits -> "Unités de correction"
            TranslationKey.CorrectionSettingsTitle -> "Correction"
            TranslationKey.CorrectionThreshold -> "Seuil de correction"
            TranslationKey.CorrectionThresholdHelp ->
                "Si votre glycémie est supérieure à cette valeur, de l'insuline supplémentaire " +
                    "est ajoutée à votre dose : une unité par palier de correction au-dessus du " +
                    "seuil, arrondie à l'unité la plus proche."
            TranslationKey.CorrectionLowThreshold -> "Seuil bas de correction"
            TranslationKey.CorrectionLowThresholdHelp ->
                "Si votre glycémie est inférieure à cette valeur, de l'insuline est soustraite " +
                    "de votre dose : une unité par palier de correction en dessous du seuil, " +
                    "arrondie à l'unité la plus proche. La dose totale n'est jamais réduite en " +
                    "dessous de zéro."
            TranslationKey.CorrectionStep -> "Palier de correction"
            TranslationKey.CorrectionStepHelp ->
                "Le nombre de mg/dl (ou mmol/l) de glycémie correspondant à une unité d'insuline " +
                    "de correction, utilisé pour les deux seuils."
            TranslationKey.GlucoseUnitLabel -> "Unité de glycémie"
            TranslationKey.GlucoseUnitHelp -> "L'unité dans laquelle vos valeurs de glycémie sont saisies et affichées."
            TranslationKey.GenderSettingsTitle -> "Genre"
            TranslationKey.GenderSettingsHelp ->
                "Sélectionner Femme active la fonction optionnelle Période, qui vous permet " +
                    "d'augmenter temporairement votre facteur actif. Ce réglage n'affecte que ce " +
                    "qui est affiché dans l'application."
            TranslationKey.GenderMale -> "Homme"
            TranslationKey.GenderFemale -> "Femme"
            TranslationKey.GenderPreferNotToSay -> "Préfère ne pas répondre"
            TranslationKey.OnboardingStepLabel -> "Étape"
            TranslationKey.ReplayTutorial -> "Revoir le tutoriel"

            // Templates
            TranslationKey.TemplatesTitle -> "Modeles"
            TranslationKey.TemplateAdd -> "Ajouter un modele"
            TranslationKey.TemplateEdit -> "Modifier le modele"
            TranslationKey.TemplateDelete -> "Supprimer le modele"
            TranslationKey.TemplateName -> "Nom"
            TranslationKey.TemplateNameHelp ->
                "Le nom affiché pour ce modèle dans votre liste. Doit être unique parmi vos " +
                    "modèles enregistrés."
            TranslationKey.TemplateEmojiOptional -> "Emoji (optionnel)"
            TranslationKey.TemplateEmojiHelp ->
                "Un emoji facultatif affiché comme icône du modèle dans votre liste, pour vous " +
                    "aider à le repérer rapidement."
            TranslationKey.TemplateEmpty -> "Aucun modele"
            TranslationKey.TemplateEmoji -> "Emoji"
            TranslationKey.TemplateSortTitle -> "Trier par"
            TranslationKey.TemplateSortHelp ->
                "Choisissez si votre liste de modèles est triée par utilisation récente ou par " +
                    "ordre alphabétique."
            TranslationKey.TemplateSortRecent -> "Recemment utilises"
            TranslationKey.TemplateSortAlphabetical -> "Alphabetique"
            TranslationKey.TemplateDuplicateNameError -> "Un modele avec ce nom existe deja"
            TranslationKey.ActiveNowBadge -> "Actuel"

            // App update
            TranslationKey.AppUpdateTitle -> "Mise à jour de l'application"
            TranslationKey.AppUpdateCurrentVersion -> "Version actuelle"
            TranslationKey.AppUpdateCheckButton -> "Rechercher des mises à jour"
            TranslationKey.AppUpdateChecking -> "Recherche de mises à jour…"
            TranslationKey.AppUpdateUpToDate -> "Vous êtes à jour"
            TranslationKey.AppUpdateAvailable -> "Mise à jour disponible"
            TranslationKey.AppUpdateDownloadButton -> "Télécharger et installer"
            TranslationKey.AppUpdateDownloading -> "Téléchargement de la mise à jour…"
            TranslationKey.AppUpdateReadyToInstall -> "Suivez les instructions pour terminer l'installation"
            TranslationKey.AppUpdatePermissionNeeded -> "Autorisez l'installation d'applications depuis cette source pour continuer"
            TranslationKey.AppUpdateOpenSettingsButton -> "Ouvrir les paramètres"
            TranslationKey.AppUpdateError -> "Impossible de rechercher des mises à jour. Veuillez réessayer."
            TranslationKey.AppUpdateRetryButton -> "Réessayer"
            TranslationKey.AppUpdateViewOnGitHub -> "Voir le code source sur GitHub"
            TranslationKey.AppUpdateChannelName -> "Mises à jour de l'application"
            TranslationKey.AppUpdateNotificationBody -> "Une nouvelle version est prête à être installée :"

            // Notifications
            TranslationKey.BasalReminderEnabled -> "Rappel du débit de base"
            TranslationKey.BasalReminderHelp ->
                "Envoie une notification quotidienne à l'heure basale configurée, pour que vous " +
                    "n'oubliiez pas de prendre votre dose basale."
            TranslationKey.BasalReminderNotificationTitle -> "Rappel du débit de base"
            TranslationKey.BasalReminderNotificationBody -> "C'est l'heure de votre débit de base."
            TranslationKey.BasalReminderChannelName -> "Rappels du débit de base"
            TranslationKey.BasalReminderExactAlarmHint -> "Autoriser les alarmes exactes dans les paramètres système"
            TranslationKey.NotificationSettingsTitle -> "Notifications"

            // Import & export
            TranslationKey.DataManagementTitle -> "Importer et exporter"
            TranslationKey.DataManagementDescription ->
                "Enregistrez vos facteurs et vos plages horaires dans un fichier, ou chargez-les " +
                    "depuis un fichier exporté précédemment."
            TranslationKey.ActionExport -> "Exporter"
            TranslationKey.ActionImport -> "Importer"
            TranslationKey.ExportSuccessMessage -> "Facteurs exportés avec succès."
            TranslationKey.ExportErrorMessage -> "Impossible d'exporter les facteurs."
            TranslationKey.ImportSuccessMessage -> "Facteurs importés avec succès."
            TranslationKey.ImportErrorMessage ->
                "Impossible d'importer les facteurs. Vérifiez que le fichier est un export valide."

            // Statistics & documentation
            TranslationKey.StatisticsSettingsTitle -> "Statistiques et documentation"
            TranslationKey.StatisticsSettingsHelp ->
                "Montre l'évolution de vos facteurs et de votre débit basal au fil du temps, et conserve un " +
                    "journal écrit de chaque modification. Vous pouvez exporter cet historique au format CSV, " +
                    "ou exporter vos facteurs actuels sous forme de rapport PDF imprimable à partager avec " +
                    "votre endocrinologue."
            TranslationKey.StatisticsSectionTitle -> "Statistiques"
            TranslationKey.StatisticsSectionHelp ->
                "Affiche sous forme de graphiques l'évolution de la valeur de chaque facteur et de votre " +
                    "débit basal, d'après votre historique de modifications."
            TranslationKey.StatisticsEmptyState -> "Aucun historique de valeurs enregistré pour le moment."
            TranslationKey.StatisticsSeriesNotEnoughData -> "Pas encore assez d'historique"
            TranslationKey.DocumentationSectionTitle -> "Journal des modifications"
            TranslationKey.DocumentationSectionHelp ->
                "Un enregistrement chronologique de chaque ajout, modification ou suppression d'un facteur " +
                    "ou de votre débit basal, avec les anciennes et nouvelles valeurs."
            TranslationKey.DocumentationEmptyState -> "Aucune modification enregistrée pour le moment."
            TranslationKey.ActionExportCsv -> "Exporter en CSV"
            TranslationKey.ActionExportPdf -> "Exporter en PDF"
            TranslationKey.LogExportSuccessMessage -> "Journal des modifications exporté avec succès."
            TranslationKey.LogExportErrorMessage -> "Impossible d'exporter le journal des modifications."
            TranslationKey.AuditLogCsvDateHeader -> "Date"
            TranslationKey.AuditLogCsvDescriptionHeader -> "Description"
            TranslationKey.PdfExportSuccessMessage -> "Rapport des facteurs exporté avec succès."
            TranslationKey.PdfExportErrorMessage -> "Impossible d'exporter le rapport des facteurs."
            TranslationKey.PdfReportTitle -> "Rapport des facteurs"
            TranslationKey.PdfGeneratedOnLabel -> $$"Généré le %1$s"
            TranslationKey.PdfFactorTimeWindowHeader -> "Plage horaire"
            TranslationKey.PdfBasalRateSummary -> $$"%1$s unités/jour à %2$s"
            TranslationKey.AuditFactorAdded -> $$"Le facteur « %1$s » a été ajouté avec la valeur %2$s."
            TranslationKey.AuditFactorValueChanged -> $$"La valeur du facteur « %1$s » est passée de %2$s à %3$s."
            TranslationKey.AuditFactorTimeChanged ->
                $$"L'heure de début du facteur « %1$s » est passée de %2$s à %3$s."
            TranslationKey.AuditFactorDeleted -> $$"Le facteur « %1$s » (valeur %2$s) a été supprimé."
            TranslationKey.AuditBasalRateChanged -> $$"Le débit basal est passé de %1$s à %2$s unités/jour."
            TranslationKey.AuditBasalTimeChanged -> $$"L'heure du débit basal est passée de %1$s à %2$s."
            TranslationKey.AuditUnknownChange -> "Modification inconnue."
            TranslationKey.AuditValueNotSet -> "non défini"
        }

        AppLanguage.Polish -> when (key) {
            // Appearance & theme
            TranslationKey.Appearance -> "Wygląd"
            TranslationKey.ThemeMode -> "Tryb motywu"
            TranslationKey.ThemeModeHelp ->
                "Wybierz System, aby podążać za ustawieniem jasny/ciemny urządzenia, albo wymuś " +
                    "Jasny lub Ciemny niezależnie od urządzenia."
            TranslationKey.ContrastLevel -> "Poziom kontrastu"
            TranslationKey.ContrastLevelHelp ->
                "Zwiększa kontrast między tekstem, ikonami a ich tłem dla lepszej czytelności. " +
                    "Wyższy kontrast może pomóc przy jasnym świetle lub słabym wzroku."
            TranslationKey.Language -> "Język"
            TranslationKey.LanguageHelp ->
                "Język używany w całej aplikacji. Wybierz System, aby podążać za ustawieniem " +
                    "języka urządzenia."
            TranslationKey.ThemeSystem -> "System"
            TranslationKey.ThemeLight -> "Światło"
            TranslationKey.ThemeDark -> "Ciemny"
            TranslationKey.ContrastNormal -> "Normalny"
            TranslationKey.ContrastMedium -> "Średni"
            TranslationKey.ContrastHigh -> "Wysoki"
            TranslationKey.LanguageEnglish -> "Angielski"
            TranslationKey.LanguageGerman -> "Niemiecki"
            TranslationKey.LanguageFrench -> "Francuski"
            TranslationKey.LanguagePolish -> "Polski"
            TranslationKey.LanguageSystem -> "System"

            // Navigation
            TranslationKey.DestinationFactors -> "Czynniki"
            TranslationKey.DestinationCalculate -> "Oblicz"
            TranslationKey.DestinationSettings -> "Ustawienia"

            // Common actions
            TranslationKey.ActionEdit -> "Edytuj"
            TranslationKey.ActionSave -> "Zapisz"
            TranslationKey.ActionCancel -> "Anuluj"
            TranslationKey.ActionClose -> "Zamknij"
            TranslationKey.ActionBack -> "Wstecz"
            TranslationKey.ActionTemplates -> "Szablony"
            TranslationKey.HelpIconContentDescription -> "Pomoc"
            TranslationKey.ActionDelete -> "Usuń"
            TranslationKey.ActionMoreOptions -> "Więcej opcji"
            TranslationKey.ActionNext -> "Dalej"
            TranslationKey.ActionFinish -> "Zakończ"

            // Factors & schedule
            TranslationKey.FactorMorning -> "Rano"
            TranslationKey.FactorBreakfast -> "Śniadanie"
            TranslationKey.FactorLunch -> "Obiad"
            TranslationKey.FactorAfternoon -> "Popołudnie"
            TranslationKey.FactorDinner -> "Kolacja"
            TranslationKey.FactorLate -> "Późno"
            TranslationKey.FactorNight -> "Noc"
            TranslationKey.FactorsListTitle -> "Czynniki"
            TranslationKey.FactorsListHelp ->
                "Każdy wiersz to przedział czasowy: nadaj mu nazwę (np. \"Śniadanie\") i ustaw " +
                    "jego czynnik — jednostki insuliny na wymiennik chlebowy używane, gdy " +
                    "aktualny czas mieści się w tym przedziale. Przedziały są automatycznie " +
                    "porządkowane według godziny rozpoczęcia."
            TranslationKey.BasalRate -> "Wartość podstawowa"
            TranslationKey.BasalRateHelp ->
                "Twoja dawka insuliny podstawowej, pokazana informacyjnie razem z godziną " +
                    "podania. Edycja tutaj nie zmienia żadnych obliczeń — to tylko zapis."
            TranslationKey.LabelFactor -> "Czynnik"
            TranslationKey.FactorValueHelp ->
                "Jednostki insuliny na wymiennik chlebowy w tym przedziale czasowym. Wyższy " +
                    "czynnik oznacza więcej insuliny dla tej samej ilości węglowodanów."
            TranslationKey.FactorNameLabel -> "Nazwa"
            TranslationKey.FactorNameHelp ->
                "Nazwa tego przedziału czasowego, np. \"Śniadanie\" lub \"Obiad\" — wyświetlana " +
                    "wszędzie w aplikacji, gdzie ten czynnik jest aktywny."
            TranslationKey.FactorStartTime -> "Godzina rozpoczęcia"
            TranslationKey.FactorStartTimeHelp ->
                "Pora dnia, o której ten czynnik (lub twoja dawka podstawowa) staje się aktywny. " +
                    "Czynniki są automatycznie porządkowane według godziny rozpoczęcia, a każdy " +
                    "przedział kończy się tam, gdzie zaczyna się kolejny."
            TranslationKey.ActionSchedule -> "Planuj"
            TranslationKey.ActionAddFactor -> "Dodaj czynnik"
            TranslationKey.FactorNameDuplicateError -> "Czynnik o tej nazwie już istnieje"
            TranslationKey.ScheduleAutoOrderHint -> "Godziny są automatycznie korygowane, aby zachować kolejność w ciągu dnia."

            // Bolus calculation
            TranslationKey.BolusType -> "Typ bolusa"
            TranslationKey.BolusTypeHelp ->
                "Normalny od razu oblicza jedną łączną dawkę. Podzielony dzieli dawkę na część " +
                    "natychmiastową i przedłużoną podawaną później, przydatne przy posiłkach " +
                    "trawionych wolno."
            TranslationKey.BolusNormal -> "Normalny"
            TranslationKey.BolusSplit -> "Bolus złożony"
            TranslationKey.BolusUnits -> "Jednostki bolusa"
            TranslationKey.BolusImmediatePercent -> "Część natychmiastowa (%)"
            TranslationKey.BolusImmediatePercentHelp ->
                "Procent podzielonej dawki podawany natychmiast; reszta (część przedłużona) jest " +
                    "podawana później, z czynnikiem aktywnym w tamtym momencie."
            TranslationKey.BolusExtendedPercent -> "Część przedłużona (%)"
            TranslationKey.BolusDurationMinutes -> "Czas trwania (minuty)"
            TranslationKey.BolusDurationMinutesHelp ->
                "Ile minut od teraz przypada część przedłużona podzielonej dawki, z czynnikiem " +
                    "aktywnym w tamtym momencie."
            TranslationKey.Carbohydrates -> "Węglowodany"
            TranslationKey.CarbohydratesHelp ->
                "Gramy węglowodanów w tym posiłku. W połączeniu z aktywnym czynnikiem i " +
                    "ustawieniem wymiennika chlebowego określa to węglowodanową część dawki."
            TranslationKey.ActiveFactor -> "Aktywny współczynnik"
            TranslationKey.Calculated -> "Obliczone"
            TranslationKey.CalculatedUnits -> "Obliczone jednostki"
            TranslationKey.BolusImmediateUnits -> "Jednostki natychmiastowe"
            TranslationKey.BolusExtendedUnits -> "Jednostki przedłużone"
            TranslationKey.FutureFactor -> "Przyszły współczynnik"
            TranslationKey.PeriodLabel -> "Okres?"
            TranslationKey.PeriodHelp ->
                "Gdy włączone, każdy aktywny czynnik jest zwiększany o procent dopłaty Okres " +
                    "ustawiony w Ustawieniach współczynnika, aby uwzględnić wyższe zapotrzebowanie " +
                    "na insulinę podczas okresu."
            TranslationKey.PeriodFactorPercent -> "Zwiększenie Okres (%)"
            TranslationKey.PeriodFactorPercentHelp ->
                "O ile procent zwiększany jest twój aktywny czynnik, gdy Okres jest włączony — " +
                    "aby pokryć wyższe zapotrzebowanie na insulinę, jakiego wiele osób doświadcza " +
                    "podczas okresu."
            TranslationKey.BreadUnits -> "Wymienniki chlebowe"
            TranslationKey.BreadUnitsHelp ->
                "Gramy węglowodanów liczone jako jeden wymiennik chlebowy w obliczeniach, np. 12 " +
                    "g. Aktywny czynnik jest stosowany na wymiennik chlebowy, nie na gram."
            TranslationKey.FactorSettingsTitle -> "Ustawienia współczynnika"
            TranslationKey.BloodSugar -> "Poziom cukru we krwi"
            TranslationKey.BloodSugarHelp ->
                "Wprowadź aktualny poziom cukru we krwi, aby zastosować korektę dawki: jednostki " +
                    "są dodawane, gdy poziom jest powyżej górnego progu korekty, i odejmowane, " +
                    "gdy jest poniżej dolnego progu (ustawianych w Korekcie). Dawka całkowita " +
                    "nigdy nie jest sugerowana poniżej zera."
            TranslationKey.CorrectionUnits -> "Jednostki korekcyjne"
            TranslationKey.CorrectionSettingsTitle -> "Korekta"
            TranslationKey.CorrectionThreshold -> "Próg korekty"
            TranslationKey.CorrectionThresholdHelp ->
                "Jeśli poziom cukru we krwi jest wyższy niż ta wartość, do dawki dodawana jest " +
                    "dodatkowa insulina: jedna jednostka za każdy krok korekty powyżej progu, " +
                    "zaokrąglona do najbliższej całej jednostki."
            TranslationKey.CorrectionLowThreshold -> "Niski próg korekty"
            TranslationKey.CorrectionLowThresholdHelp ->
                "Jeśli poziom cukru we krwi jest niższy niż ta wartość, insulina jest odejmowana " +
                    "od dawki: jedna jednostka za każdy krok korekty poniżej progu, zaokrąglona " +
                    "do najbliższej całej jednostki. Dawka całkowita nigdy nie jest zmniejszana " +
                    "poniżej zera."
            TranslationKey.CorrectionStep -> "Krok korekty"
            TranslationKey.CorrectionStepHelp ->
                "Ile mg/dl (lub mmol/l) poziomu cukru we krwi odpowiada jednej jednostce " +
                    "insuliny korekcyjnej, dotyczy zarówno górnego, jak i dolnego progu."
            TranslationKey.GlucoseUnitLabel -> "Jednostka glukozy we krwi"
            TranslationKey.GlucoseUnitHelp ->
                "Jednostka, w której wprowadzane i wyświetlane są wartości poziomu cukru we krwi."
            TranslationKey.GenderSettingsTitle -> "Płeć"
            TranslationKey.GenderSettingsHelp ->
                "Wybranie Kobieta włącza opcjonalną funkcję Okres, która pozwala tymczasowo " +
                    "zwiększyć aktywny czynnik. To ustawienie wpływa tylko na to, co jest " +
                    "wyświetlane w aplikacji."
            TranslationKey.GenderMale -> "Mężczyzna"
            TranslationKey.GenderFemale -> "Kobieta"
            TranslationKey.GenderPreferNotToSay -> "Wolę nie podawać"
            TranslationKey.OnboardingStepLabel -> "Krok"
            TranslationKey.ReplayTutorial -> "Powtórz samouczek"

            // Templates
            TranslationKey.TemplatesTitle -> "Szablony"
            TranslationKey.TemplateAdd -> "Dodaj szablon"
            TranslationKey.TemplateEdit -> "Edytuj szablon"
            TranslationKey.TemplateDelete -> "Usuń szablon"
            TranslationKey.TemplateName -> "Nazwa"
            TranslationKey.TemplateNameHelp ->
                "Nazwa wyświetlana dla tego szablonu na liście. Musi być unikalna wśród " +
                    "zapisanych szablonów."
            TranslationKey.TemplateEmojiOptional -> "Emoji (opcjonalnie)"
            TranslationKey.TemplateEmojiHelp ->
                "Opcjonalne emoji wyświetlane jako ikona szablonu na liście, ułatwiające jego " +
                    "szybkie rozpoznanie."
            TranslationKey.TemplateEmpty -> "Brak szablonów"
            TranslationKey.TemplateEmoji -> "Emoji"
            TranslationKey.TemplateSortTitle -> "Sortowanie"
            TranslationKey.TemplateSortHelp ->
                "Wybierz, czy lista szablonów ma być sortowana według ostatniego użycia, czy " +
                    "alfabetycznie według nazwy."
            TranslationKey.TemplateSortRecent -> "Ostatnio używane"
            TranslationKey.TemplateSortAlphabetical -> "Alfabetycznie"
            TranslationKey.TemplateDuplicateNameError -> "Szablon o tej nazwie już istnieje"
            TranslationKey.ActiveNowBadge -> "Teraz"

            // App update
            TranslationKey.AppUpdateTitle -> "Aktualizacja aplikacji"
            TranslationKey.AppUpdateCurrentVersion -> "Bieżąca wersja"
            TranslationKey.AppUpdateCheckButton -> "Sprawdź aktualizacje"
            TranslationKey.AppUpdateChecking -> "Sprawdzanie aktualizacji…"
            TranslationKey.AppUpdateUpToDate -> "Masz najnowszą wersję"
            TranslationKey.AppUpdateAvailable -> "Dostępna aktualizacja"
            TranslationKey.AppUpdateDownloadButton -> "Pobierz i zainstaluj"
            TranslationKey.AppUpdateDownloading -> "Pobieranie aktualizacji…"
            TranslationKey.AppUpdateReadyToInstall -> "Postępuj zgodnie z instrukcjami, aby zakończyć instalację"
            TranslationKey.AppUpdatePermissionNeeded -> "Zezwól na instalowanie aplikacji z tego źródła, aby kontynuować"
            TranslationKey.AppUpdateOpenSettingsButton -> "Otwórz ustawienia"
            TranslationKey.AppUpdateError -> "Nie udało się sprawdzić aktualizacji. Spróbuj ponownie."
            TranslationKey.AppUpdateRetryButton -> "Spróbuj ponownie"
            TranslationKey.AppUpdateViewOnGitHub -> "Zobacz kod źródłowy na GitHub"
            TranslationKey.AppUpdateChannelName -> "Aktualizacje aplikacji"
            TranslationKey.AppUpdateNotificationBody -> "Nowa wersja jest gotowa do zainstalowania:"

            // Notifications
            TranslationKey.BasalReminderEnabled -> "Przypomnienie o wartości podstawowej"
            TranslationKey.BasalReminderHelp ->
                "Wysyła codzienne powiadomienie o ustawionej porze wartości podstawowej, abyś nie " +
                    "zapomniał/a przyjąć dawki podstawowej."
            TranslationKey.BasalReminderNotificationTitle -> "Przypomnienie o wartości podstawowej"
            TranslationKey.BasalReminderNotificationBody -> "Czas na Twoją wartość podstawową."
            TranslationKey.BasalReminderChannelName -> "Przypomnienia o wartości podstawowej"
            TranslationKey.BasalReminderExactAlarmHint -> "Zezwól na dokładne alarmy w ustawieniach systemowych"
            TranslationKey.NotificationSettingsTitle -> "Powiadomienia"

            // Import & export
            TranslationKey.DataManagementTitle -> "Import i eksport"
            TranslationKey.DataManagementDescription ->
                "Zapisz swoje współczynniki i przedziały czasowe do pliku lub wczytaj je z " +
                    "wcześniej wyeksportowanego pliku."
            TranslationKey.ActionExport -> "Eksportuj"
            TranslationKey.ActionImport -> "Importuj"
            TranslationKey.ExportSuccessMessage -> "Współczynniki wyeksportowane pomyślnie."
            TranslationKey.ExportErrorMessage -> "Nie udało się wyeksportować współczynników."
            TranslationKey.ImportSuccessMessage -> "Współczynniki zaimportowane pomyślnie."
            TranslationKey.ImportErrorMessage ->
                "Nie udało się zaimportować współczynników. Upewnij się, że plik jest prawidłowym eksportem."

            // Statistics & documentation
            TranslationKey.StatisticsSettingsTitle -> "Statystyki i dokumentacja"
            TranslationKey.StatisticsSettingsHelp ->
                "Pokazuje, jak zmieniały się Twoje współczynniki i dawka podstawowa w czasie, oraz prowadzi " +
                    "pisemny dziennik każdej zmiany. Możesz wyeksportować tę historię jako plik CSV lub " +
                    "wyeksportować bieżące współczynniki jako gotowy do druku raport PDF, aby podzielić się " +
                    "nim z diabetologiem."
            TranslationKey.StatisticsSectionTitle -> "Statystyki"
            TranslationKey.StatisticsSectionHelp ->
                "Pokazuje na wykresach, jak zmieniała się wartość każdego współczynnika i dawka podstawowa " +
                    "w czasie, na podstawie historii zmian."
            TranslationKey.StatisticsEmptyState -> "Nie zarejestrowano jeszcze historii wartości."
            TranslationKey.StatisticsSeriesNotEnoughData -> "Za mało danych historycznych"
            TranslationKey.DocumentationSectionTitle -> "Dziennik zmian"
            TranslationKey.DocumentationSectionHelp ->
                "Chronologiczny zapis każdego dodania, zmiany lub usunięcia współczynnika lub dawki " +
                    "podstawowej, wraz ze starą i nową wartością."
            TranslationKey.DocumentationEmptyState -> "Nie zarejestrowano jeszcze żadnych zmian."
            TranslationKey.ActionExportCsv -> "Eksportuj CSV"
            TranslationKey.ActionExportPdf -> "Eksportuj PDF"
            TranslationKey.LogExportSuccessMessage -> "Dziennik zmian wyeksportowany pomyślnie."
            TranslationKey.LogExportErrorMessage -> "Nie udało się wyeksportować dziennika zmian."
            TranslationKey.AuditLogCsvDateHeader -> "Data"
            TranslationKey.AuditLogCsvDescriptionHeader -> "Opis"
            TranslationKey.PdfExportSuccessMessage -> "Raport współczynników wyeksportowany pomyślnie."
            TranslationKey.PdfExportErrorMessage -> "Nie udało się wyeksportować raportu współczynników."
            TranslationKey.PdfReportTitle -> "Raport współczynników"
            TranslationKey.PdfGeneratedOnLabel -> $$"Wygenerowano dnia %1$s"
            TranslationKey.PdfFactorTimeWindowHeader -> "Przedział czasowy"
            TranslationKey.PdfBasalRateSummary -> $$"%1$s jednostek/dobę o %2$s"
            TranslationKey.AuditFactorAdded -> $$"Współczynnik „%1$s” został dodany z wartością %2$s."
            TranslationKey.AuditFactorValueChanged -> $$"Wartość współczynnika „%1$s” zmieniła się z %2$s na %3$s."
            TranslationKey.AuditFactorTimeChanged ->
                $$"Godzina rozpoczęcia współczynnika „%1$s” zmieniła się z %2$s na %3$s."
            TranslationKey.AuditFactorDeleted -> $$"Współczynnik „%1$s” (wartość %2$s) został usunięty."
            TranslationKey.AuditBasalRateChanged -> $$"Dawka podstawowa zmieniła się z %1$s na %2$s jednostek/dzień."
            TranslationKey.AuditBasalTimeChanged -> $$"Godzina dawki podstawowej zmieniła się z %1$s na %2$s."
            TranslationKey.AuditUnknownChange -> "Nieznana zmiana."
            TranslationKey.AuditValueNotSet -> "nie ustawiono"
        }

        AppLanguage.System -> error("SystemDefault must be resolved before translating")
    }
}

private fun resolveAppLanguage(language: AppLanguage): AppLanguage {
    if (language != AppLanguage.System) {
        return language
    }

    return when (Locale.getDefault().language.lowercase(Locale.ROOT)) {
        "de" -> AppLanguage.German
        "fr" -> AppLanguage.French
        "pl" -> AppLanguage.Polish
        else -> AppLanguage.English
    }
}
