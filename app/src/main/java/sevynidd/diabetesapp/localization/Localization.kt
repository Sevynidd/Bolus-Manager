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
    // More screen groups
    SettingsGroupTherapy,
    SettingsGroupData,
    SettingsGroupApp,

    // Appearance & theme
    Appearance,
    ThemeMode,
    ThemeModeHelp,
    ContrastLevel,
    ContrastLevelHelp,
    DynamicColor,
    DynamicColorHelp,
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
    ActionUndo,
    FactorsSaved,
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
    FactorsListPumpHelp,
    LabelPumpRatio,
    PumpRatioValueHelp,
    PumpRatioUnit,
    TherapyModeTitle,
    TherapyModeHelp,
    TherapyModePen,
    TherapyModePump,
    PumpBasalRatesTitle,
    PumpBasalRatesHelp,
    PumpBasalRateUnit,
    PumpBasalDailyTotal,
    ActionShowHours,
    ActionHideHours,
    FactorNameLabel,
    FactorNameHelp,
    FactorStartTime,
    FactorStartTimeHelp,
    ActionSchedule,
    ActionAddFactor,
    FactorNameDuplicateError,
    ScheduleAutoOrderHint,

    // Bolus calculation
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
    CorrectionSettingsTitle,
    CorrectionThreshold,
    CorrectionThresholdHelp,
    CorrectionLowThreshold,
    CorrectionLowThresholdHelp,
    CorrectionStep,
    CorrectionStepHelp,
    DoseStep,
    DoseStepHelp,
    MaxDose,
    MaxDoseHelp,
    FastCarbs,
    FastCarbsHelp,
    DoseUnitSymbol,
    GramsUnit,
    DoseResultTitle,
    DoseResultHelp,
    DoseBreakdown,
    DoseRoundedTo,
    LowBloodSugarTitle,
    LowBloodSugarBody,
    FastCarbsSuggestion,
    MaxDoseExceeded,
    MaxBloodSugar,
    MaxBloodSugarHelp,
    MaxCarbohydrates,
    MaxCarbohydratesHelp,
    DoseBlockedBloodSugar,
    DoseBlockedCarbohydrates,
    DoseBlockedNoFactor,
    ResetInputs,
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
    AppUpdateViewOnGitHub,
    AppUpdateObtainiumDescription,
    AppUpdateObtainiumAddButton,
    AppUpdateObtainiumGetButton,

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
    AuditPumpBasalRateChanged,
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
            TranslationKey.DynamicColor -> "Wallpaper colors"
            TranslationKey.DynamicColorHelp ->
                "Uses colors taken from your wallpaper instead of the app's own warm palette. Warnings for " +
                    "a low blood sugar or a high dose always stay red or amber."
            TranslationKey.Language -> "Language"
            TranslationKey.LanguageHelp ->
                "The language used throughout the app. Choose System to follow your device's " +
                    "language setting."
            TranslationKey.SettingsGroupTherapy -> "Therapy and dose"
            TranslationKey.SettingsGroupData -> "Data and report"
            TranslationKey.SettingsGroupApp -> "App"
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
            TranslationKey.DestinationSettings -> "More"

            // Common actions
            TranslationKey.ActionEdit -> "Edit"
            TranslationKey.ActionUndo -> "Undo"
            TranslationKey.FactorsSaved -> "Changes saved"
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
                    "that window. Windows are ordered automatically by start time. " +
                    "Tap a row to change it. Changes can be undone right after saving."
            TranslationKey.BasalRate -> "Basal rate"
            TranslationKey.BasalRateHelp ->
                "Your basal insulin dose, shown for reference alongside the time it's " +
                    "administered. Editing it here does not change any calculation — it's just a record."
            TranslationKey.LabelFactor -> "Factor"
            TranslationKey.FactorValueHelp ->
                "Units of insulin per bread unit during this time window. A higher factor means " +
                    "more insulin for the same amount of carbohydrates."
            TranslationKey.FactorsListPumpHelp ->
                "Each row is a time window: name it (e.g. \"Breakfast\") and set its pump ratio — " +
                    "how many grams of carbohydrates one unit of insulin covers whenever the current " +
                    "time falls in that window. Windows are ordered automatically by start time. " +
                    "Tap a row to change it. Changes can be undone right after saving."
            TranslationKey.LabelPumpRatio -> "1 U per … g carbs"
            TranslationKey.PumpRatioValueHelp ->
                "The insulin-to-carb ratio as your pump shows it: one unit of insulin per this many " +
                    "grams of carbohydrates. It's converted from your factor using your bread-unit " +
                    "value (grams ÷ factor) and rounded to 0.1 g. A lower number means more insulin " +
                    "for the same amount of carbohydrates."
            TranslationKey.PumpRatioUnit -> "g/U"
            TranslationKey.DoseStep -> "Dose step"
            TranslationKey.DoseStepHelp ->
                "The smallest amount your pen or pump can deliver. The calculated dose is rounded to the " +
                    "nearest multiple of this step, so the result is something you can actually inject."
            TranslationKey.MaxDose -> "Maximum dose"
            TranslationKey.MaxDoseHelp ->
                "A safety limit for a single dose. If a result is higher, a warning is shown so a typing " +
                    "mistake can't go unnoticed. Set 0 to switch the limit off."
            TranslationKey.FastCarbs -> "Fast carbs for a low"
            TranslationKey.FastCarbsHelp ->
                "How many grams of fast-acting carbohydrates are suggested when your blood sugar is below " +
                    "the low threshold. Agree on this amount with your diabetes care team."
            TranslationKey.DoseUnitSymbol -> "U"
            TranslationKey.GramsUnit -> "g"
            TranslationKey.DoseResultTitle -> "Dose to inject"
            TranslationKey.DoseResultHelp ->
                "The total dose is the meal dose plus the correction for your blood sugar, never below zero, " +
                    "rounded to your dose step. The line below shows the exact sum before rounding. " +
                    "If a value looks like a typing mistake, no dose is shown until you correct it."
            TranslationKey.DoseBreakdown -> $$"Meal %1$s + correction %2$s = %3$s %4$s"
            TranslationKey.DoseRoundedTo -> $$"Rounded to %1$s %2$s steps"
            TranslationKey.LowBloodSugarTitle -> "Below your low threshold"
            TranslationKey.LowBloodSugarBody ->
                $$"%1$s is under %2$s. Treat the low first, then recalculate."
            TranslationKey.FastCarbsSuggestion -> "Suggested fast carbs"
            TranslationKey.MaxDoseExceeded ->
                $$"Above your maximum dose of %1$s %2$s. Check the amounts before injecting."
            TranslationKey.MaxBloodSugar -> "Highest plausible blood sugar"
            TranslationKey.MaxBloodSugarHelp ->
                "Blood sugar entries above this value are treated as a typing mistake: no dose is shown until " +
                    "you correct them. Set 0 to switch the check off."
            TranslationKey.MaxCarbohydrates -> "Highest plausible carbs"
            TranslationKey.MaxCarbohydratesHelp ->
                "Carbohydrate entries above this amount are treated as a typing mistake: no dose is shown " +
                    "until you correct them. Set 0 to switch the check off."
            TranslationKey.DoseBlockedBloodSugar ->
                $$"Blood sugar is above %1$s %2$s. Check the value."
            TranslationKey.DoseBlockedCarbohydrates ->
                $$"Carbohydrates are above %1$s %2$s. Check the value."
            TranslationKey.DoseBlockedNoFactor -> "No factor is set for this time of day. Add one under Factors."
            TranslationKey.ResetInputs -> "Reset"
            TranslationKey.TherapyModeTitle -> "Therapy"
            TranslationKey.TherapyModeHelp ->
                "Choose how you take insulin. \"Pen\" shows factors as units of insulin per bread " +
                    "unit (e.g. 1.5) and the basal rate as one daily dose at a set time. \"Insulin pump\" " +
                    "shows factors the way pumps such as the YpsoPump do — one unit per x grams of " +
                    "carbohydrates (e.g. 8 g/U with 12 g per bread unit) — and the basal rate as 24 " +
                    "hourly rates in units per hour. Factors mean the same in both, so bolus " +
                    "calculations don't change. Your pen dose and pump rates are kept separately." +
                    " The daily basal reminder is only available with a pen, since a pump delivers basal " +
                    "insulin on its own."
            TranslationKey.TherapyModePen -> "Pen"
            TranslationKey.TherapyModePump -> "Insulin pump"
            TranslationKey.PumpBasalRatesTitle -> "Basal rate (pump)"
            TranslationKey.PumpBasalRatesHelp ->
                "The basal rate programmed in your pump, one value per hour of the day in units " +
                    "per hour, rounded to 0.01 U/h. The daily total is the sum of all 24 hours. It's a " +
                    "record for you and your diabetes team — it doesn't change any bolus calculation." +
                    " Tap the header to show or hide the 24 hours; the daily total always stays visible. " +
                    "Tap an hour to change its rate."
            TranslationKey.PumpBasalRateUnit -> "U/h"
            TranslationKey.ActionShowHours -> "Show hours"
            TranslationKey.ActionHideHours -> "Hide hours"
            TranslationKey.PumpBasalDailyTotal -> $$"Daily total: %1$s U"
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
            TranslationKey.AppUpdateViewOnGitHub -> "View source on GitHub"
            TranslationKey.AppUpdateObtainiumDescription ->
                "Updates are delivered through Obtainium, which watches this app's GitHub releases and " +
                    "installs new versions for you. Add the app to Obtainium once and it takes care of the rest."
            TranslationKey.AppUpdateObtainiumAddButton -> "Add to Obtainium"
            TranslationKey.AppUpdateObtainiumGetButton -> "Get Obtainium"

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
            TranslationKey.AuditPumpBasalRateChanged ->
                $$"Pump basal rate %1$s changed from %2$s to %3$s U/h."
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
            TranslationKey.DynamicColor -> "Farben vom Hintergrundbild"
            TranslationKey.DynamicColorHelp ->
                "Übernimmt Farben aus deinem Hintergrundbild statt der eigenen warmen Farbpalette der App. " +
                    "Warnungen bei Unterzucker oder hoher Dosis bleiben immer rot oder bernsteinfarben."
            TranslationKey.Language -> "Sprache"
            TranslationKey.LanguageHelp ->
                "Die in der gesamten App verwendete Sprache. Wähle System, um die " +
                    "Spracheinstellung deines Geräts zu übernehmen."
            TranslationKey.SettingsGroupTherapy -> "Therapie und Dosis"
            TranslationKey.SettingsGroupData -> "Daten und Bericht"
            TranslationKey.SettingsGroupApp -> "App"
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
            TranslationKey.DestinationSettings -> "Mehr"

            // Common actions
            TranslationKey.ActionEdit -> "Bearbeiten"
            TranslationKey.ActionUndo -> "Rückgängig"
            TranslationKey.FactorsSaved -> "Änderungen gespeichert"
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
                    "nach Startzeit sortiert. " +
                    "Tippe auf eine Zeile, um sie zu ändern. " +
                    "Änderungen lassen sich direkt nach dem Speichern rückgängig machen."
            TranslationKey.BasalRate -> "Basisrate"
            TranslationKey.BasalRateHelp ->
                "Deine Basalinsulindosis, zur Referenz zusammen mit der Uhrzeit der Verabreichung " +
                    "angezeigt. Das Bearbeiten hier ändert keine Berechnung — es ist nur ein Eintrag."
            TranslationKey.LabelFactor -> "Faktor"
            TranslationKey.FactorValueHelp ->
                "Einheiten Insulin pro Broteinheit in diesem Zeitfenster. Ein höherer Faktor " +
                    "bedeutet mehr Insulin für dieselbe Menge Kohlenhydrate."
            TranslationKey.FactorsListPumpHelp ->
                "Jede Zeile ist ein Zeitfenster: benenne es (z. B. \"Frühstück\") und lege sein " +
                    "Pumpenverhältnis fest — wie viel Gramm Kohlenhydrate eine Einheit Insulin abdeckt, " +
                    "wenn die aktuelle Zeit in dieses Fenster fällt. Fenster werden automatisch " +
                    "nach Startzeit sortiert. " +
                    "Tippe auf eine Zeile, um sie zu ändern. " +
                    "Änderungen lassen sich direkt nach dem Speichern rückgängig machen."
            TranslationKey.LabelPumpRatio -> "1 IE pro … g KH"
            TranslationKey.PumpRatioValueHelp ->
                "Das Insulin-Kohlenhydrat-Verhältnis, wie es deine Pumpe anzeigt: eine Einheit " +
                    "Insulin pro so viel Gramm Kohlenhydrate. Es wird mit deinem Broteinheiten-Wert " +
                    "aus deinem Faktor umgerechnet (Gramm ÷ Faktor) und auf 0,1 g gerundet. Eine " +
                    "kleinere Zahl bedeutet mehr Insulin für dieselbe Menge Kohlenhydrate."
            TranslationKey.PumpRatioUnit -> "g/IE"
            TranslationKey.DoseStep -> "Dosisschritt"
            TranslationKey.DoseStepHelp ->
                "Die kleinste Menge, die dein Pen oder deine Pumpe abgeben kann. Die berechnete Dosis wird " +
                    "auf das nächste Vielfache dieses Schritts gerundet, sodass du sie tatsächlich spritzen kannst."
            TranslationKey.MaxDose -> "Maximaldosis"
            TranslationKey.MaxDoseHelp ->
                "Eine Sicherheitsgrenze für eine einzelne Dosis. Ist ein Ergebnis höher, erscheint eine " +
                    "Warnung, damit ein Tippfehler nicht unbemerkt bleibt. Mit 0 schaltest du die Grenze aus."
            TranslationKey.FastCarbs -> "Schnelle KH bei Unterzucker"
            TranslationKey.FastCarbsHelp ->
                "Wie viele Gramm schnell wirkende Kohlenhydrate vorgeschlagen werden, wenn dein Blutzucker " +
                    "unter der unteren Schwelle liegt. Stimme diese Menge mit deinem Diabetes-Team ab."
            TranslationKey.DoseUnitSymbol -> "IE"
            TranslationKey.GramsUnit -> "g"
            TranslationKey.DoseResultTitle -> "Zu spritzende Dosis"
            TranslationKey.DoseResultHelp ->
                "Die Gesamtdosis ist die Mahlzeitendosis plus die Korrektur für deinen Blutzucker, nie unter " +
                    "null, gerundet auf deinen Dosisschritt. Die Zeile darunter zeigt die genaue Summe " +
                    "vor dem Runden. Sieht ein Wert nach einem Tippfehler aus, wird keine Dosis angezeigt, " +
                    "bis du ihn korrigierst."
            TranslationKey.DoseBreakdown -> $$"Mahlzeit %1$s + Korrektur %2$s = %3$s %4$s"
            TranslationKey.DoseRoundedTo -> $$"Gerundet auf %1$s-%2$s-Schritte"
            TranslationKey.LowBloodSugarTitle -> "Unter deiner unteren Schwelle"
            TranslationKey.LowBloodSugarBody ->
                $$"%1$s liegt unter %2$s. Behandle zuerst den Unterzucker und rechne dann neu."
            TranslationKey.FastCarbsSuggestion -> "Empfohlene schnelle KH"
            TranslationKey.MaxDoseExceeded ->
                $$"Über deiner Maximaldosis von %1$s %2$s. Prüfe die Eingaben vor dem Spritzen."
            TranslationKey.MaxBloodSugar -> "Höchster plausibler Blutzucker"
            TranslationKey.MaxBloodSugarHelp ->
                "Blutzuckerwerte über dieser Grenze gelten als Tippfehler: Es wird keine Dosis angezeigt, " +
                    "bis du sie korrigierst. Mit 0 schaltest du die Prüfung aus."
            TranslationKey.MaxCarbohydrates -> "Höchste plausible Kohlenhydrate"
            TranslationKey.MaxCarbohydratesHelp ->
                "Kohlenhydratmengen über dieser Grenze gelten als Tippfehler: Es wird keine Dosis angezeigt, " +
                    "bis du sie korrigierst. Mit 0 schaltest du die Prüfung aus."
            TranslationKey.DoseBlockedBloodSugar ->
                $$"Der Blutzucker liegt über %1$s %2$s. Prüfe den Wert."
            TranslationKey.DoseBlockedCarbohydrates ->
                $$"Die Kohlenhydrate liegen über %1$s %2$s. Prüfe den Wert."
            TranslationKey.DoseBlockedNoFactor ->
                "Für diese Tageszeit ist kein Faktor eingetragen. " +
                    "Trage unter Faktoren einen ein."
            TranslationKey.ResetInputs -> "Zurücksetzen"
            TranslationKey.TherapyModeTitle -> "Therapie"
            TranslationKey.TherapyModeHelp ->
                "Wähle, wie du Insulin verabreichst. \"Pen\" zeigt Faktoren als Einheiten Insulin pro " +
                    "Broteinheit (z. B. 1,5) und die Basalrate als eine tägliche Dosis zu einer festen " +
                    "Uhrzeit. \"Insulinpumpe\" zeigt Faktoren so, wie Pumpen wie die YpsoPump es tun — " +
                    "eine Einheit pro x Gramm Kohlenhydrate (z. B. 8 g/IE bei 12 g pro Broteinheit) — und " +
                    "die Basalrate als 24 stündliche Raten in Einheiten pro Stunde. Faktoren bedeuten in " +
                    "beiden dasselbe, die Bolusberechnung ändert sich also nicht. Pen-Dosis und " +
                    "Pumpenraten werden getrennt gespeichert." +
                    " Die Basal-Erinnerung gibt es nur beim Pen, da eine Pumpe das Basalinsulin selbst abgibt."
            TranslationKey.TherapyModePen -> "Pen"
            TranslationKey.TherapyModePump -> "Insulinpumpe"
            TranslationKey.PumpBasalRatesTitle -> "Basalrate (Pumpe)"
            TranslationKey.PumpBasalRatesHelp ->
                "Die in deiner Pumpe programmierte Basalrate, ein Wert pro Stunde des Tages in " +
                    "Einheiten pro Stunde, gerundet auf 0,01 IE/h. Die Tagessumme ist die Summe aller 24 " +
                    "Stunden. Sie dient als Eintrag für dich und dein Diabetesteam — sie ändert keine " +
                    "Bolusberechnung." +
                    " Tippe auf die Überschrift, um die 24 Stunden ein- oder auszublenden; die Tagessumme " +
                    "bleibt immer sichtbar. " +
                    "Tippe auf eine Stunde, um ihren Wert zu ändern."
            TranslationKey.PumpBasalRateUnit -> "IE/h"
            TranslationKey.ActionShowHours -> "Stunden anzeigen"
            TranslationKey.ActionHideHours -> "Stunden ausblenden"
            TranslationKey.PumpBasalDailyTotal -> $$"Tagessumme: %1$s IE"
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
            TranslationKey.BolusTypeHelp ->
                "Normal berechnet sofort eine kombinierte Dosis. Aufgeteilt teilt die Dosis in " +
                    "einen sofortigen Teil und einen späteren, erweiterten Teil auf — nützlich bei " +
                    "langsam verdaulichen Mahlzeiten."
            TranslationKey.BolusNormal -> "Normal"
            TranslationKey.BolusSplit -> "Gesplittet"
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
            TranslationKey.AppUpdateViewOnGitHub -> "Quellcode auf GitHub ansehen"
            TranslationKey.AppUpdateObtainiumDescription ->
                "Updates werden über Obtainium bereitgestellt. Obtainium beobachtet die GitHub-Releases dieser App " +
                    "und installiert neue Versionen für dich. " +
                    "Füge die App einmal zu Obtainium hinzu, den Rest erledigt es selbst."
            TranslationKey.AppUpdateObtainiumAddButton -> "Zu Obtainium hinzufügen"
            TranslationKey.AppUpdateObtainiumGetButton -> "Obtainium herunterladen"

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
            TranslationKey.AuditPumpBasalRateChanged ->
                $$"Pumpen-Basalrate %1$s wurde von %2$s auf %3$s IE/h geändert."
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
            TranslationKey.DynamicColor -> "Couleurs du fond d'écran"
            TranslationKey.DynamicColorHelp ->
                "Utilise les couleurs de votre fond d'écran au lieu de la palette chaude de l'application. " +
                    "Les avertissements pour une hypoglycémie ou une dose élevée restent toujours rouges ou ambrés."
            TranslationKey.Language -> "Langue"
            TranslationKey.LanguageHelp ->
                "La langue utilisée dans toute l'application. Choisissez Système pour suivre la " +
                    "langue de votre appareil."
            TranslationKey.SettingsGroupTherapy -> "Thérapie et dose"
            TranslationKey.SettingsGroupData -> "Données et rapport"
            TranslationKey.SettingsGroupApp -> "Application"
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
            TranslationKey.DestinationSettings -> "Plus"

            // Common actions
            TranslationKey.ActionEdit -> "Modifier"
            TranslationKey.ActionUndo -> "Annuler"
            TranslationKey.FactorsSaved -> "Modifications enregistrées"
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
                    "automatiquement par heure de début. " +
                    "Touchez une ligne pour la modifier. " +
                    "Les modifications peuvent être annulées juste après l’enregistrement."
            TranslationKey.BasalRate -> "Débit de base"
            TranslationKey.BasalRateHelp ->
                "Votre dose d'insuline basale, affichée à titre de référence avec l'heure " +
                    "d'administration. La modifier ici ne change aucun calcul — c'est juste un enregistrement."
            TranslationKey.LabelFactor -> "Facteur"
            TranslationKey.FactorValueHelp ->
                "Unités d'insuline par unité de pain pendant cette plage horaire. Un facteur plus " +
                    "élevé signifie plus d'insuline pour la même quantité de glucides."
            TranslationKey.FactorsListPumpHelp ->
                "Chaque ligne est une plage horaire : nommez-la (par ex. \"Petit-déjeuner\") et " +
                    "définissez son ratio de pompe — combien de grammes de glucides une unité " +
                    "d'insuline couvre lorsque l'heure actuelle se situe dans cette plage. Les plages " +
                    "sont triées automatiquement par heure de début. " +
                    "Touchez une ligne pour la modifier. " +
                    "Les modifications peuvent être annulées juste après l’enregistrement."
            TranslationKey.LabelPumpRatio -> "1 U pour … g de glucides"
            TranslationKey.PumpRatioValueHelp ->
                "Le ratio insuline/glucides tel que votre pompe l'affiche : une unité d'insuline " +
                    "pour ce nombre de grammes de glucides. Il est converti depuis votre facteur à " +
                    "l'aide de votre valeur d'unité de pain (grammes ÷ facteur) et arrondi à 0,1 g. " +
                    "Un nombre plus petit signifie plus d'insuline pour la même quantité de glucides."
            TranslationKey.PumpRatioUnit -> "g/U"
            TranslationKey.DoseStep -> "Pas de dose"
            TranslationKey.DoseStepHelp ->
                "La plus petite quantité que votre stylo ou pompe peut délivrer. La dose calculée est " +
                    "arrondie au multiple le plus proche de ce pas, pour obtenir une dose réellement injectable."
            TranslationKey.MaxDose -> "Dose maximale"
            TranslationKey.MaxDoseHelp ->
                "Une limite de sécurité pour une dose unique. Si un résultat la dépasse, un avertissement " +
                    "s'affiche pour qu'une erreur de saisie ne passe pas inaperçue. Mettez 0 pour la désactiver."
            TranslationKey.FastCarbs -> "Sucres rapides en cas d'hypo"
            TranslationKey.FastCarbsHelp ->
                "Le nombre de grammes de glucides rapides suggéré quand votre glycémie est sous le seuil " +
                    "bas. Convenez de cette quantité avec votre équipe soignante."
            TranslationKey.DoseUnitSymbol -> "U"
            TranslationKey.GramsUnit -> "g"
            TranslationKey.DoseResultTitle -> "Dose à injecter"
            TranslationKey.DoseResultHelp ->
                "La dose totale est la dose du repas plus la correction de votre glycémie, jamais sous " +
                    "zéro, arrondie à votre pas de dose. La ligne dessous montre la somme exacte avant arrondi. " +
                    "Si une valeur ressemble à une erreur de saisie, aucune dose n'est affichée tant que " +
                    "vous ne la corrigez pas."
            TranslationKey.DoseBreakdown -> $$"Repas %1$s + correction %2$s = %3$s %4$s"
            TranslationKey.DoseRoundedTo -> $$"Arrondi par pas de %1$s %2$s"
            TranslationKey.LowBloodSugarTitle -> "Sous votre seuil bas"
            TranslationKey.LowBloodSugarBody ->
                $$"%1$s est sous %2$s. Traitez d'abord l'hypo, puis recalculez."
            TranslationKey.FastCarbsSuggestion -> "Sucres rapides conseillés"
            TranslationKey.MaxDoseExceeded ->
                $$"Au-dessus de votre dose maximale de %1$s %2$s. Vérifiez les valeurs avant d'injecter."
            TranslationKey.MaxBloodSugar -> "Glycémie plausible maximale"
            TranslationKey.MaxBloodSugarHelp ->
                "Les glycémies au-dessus de cette limite sont considérées comme une erreur de saisie : " +
                    "aucune dose n'est affichée tant que vous ne les corrigez pas. " +
                    "Mettez 0 pour désactiver la vérification."
            TranslationKey.MaxCarbohydrates -> "Glucides plausibles maximum"
            TranslationKey.MaxCarbohydratesHelp ->
                "Les quantités de glucides au-dessus de cette limite sont considérées comme une erreur de saisie : " +
                    "aucune dose n'est affichée tant que vous ne les corrigez pas. " +
                    "Mettez 0 pour désactiver la vérification."
            TranslationKey.DoseBlockedBloodSugar ->
                $$"La glycémie dépasse %1$s %2$s. Vérifiez la valeur."
            TranslationKey.DoseBlockedCarbohydrates ->
                $$"Les glucides dépassent %1$s %2$s. Vérifiez la valeur."
            TranslationKey.DoseBlockedNoFactor ->
                "Aucun facteur n'est défini pour cette heure. " +
                    "Ajoutez-en un sous Facteurs."
            TranslationKey.ResetInputs -> "Réinitialiser"
            TranslationKey.TherapyModeTitle -> "Traitement"
            TranslationKey.TherapyModeHelp ->
                "Choisissez comment vous administrez l'insuline. \"Stylo\" affiche les facteurs en " +
                    "unités d'insuline par unité de pain (par ex. 1,5) et le débit basal comme une dose " +
                    "quotidienne à une heure fixe. \"Pompe à insuline\" affiche les facteurs comme les " +
                    "pompes telles que la YpsoPump — une unité pour x grammes de glucides (par ex. 8 g/U " +
                    "avec 12 g par unité de pain) — et le débit basal comme 24 débits horaires en unités " +
                    "par heure. Les facteurs ont le même sens dans les deux cas, le calcul du bolus ne " +
                    "change donc pas. La dose au stylo et les débits de la pompe sont conservés séparément." +
                    " Le rappel de basal n'est disponible qu'avec un stylo, car une pompe délivre " +
                    "l'insuline basale elle-même."
            TranslationKey.TherapyModePen -> "Stylo"
            TranslationKey.TherapyModePump -> "Pompe à insuline"
            TranslationKey.PumpBasalRatesTitle -> "Débit basal (pompe)"
            TranslationKey.PumpBasalRatesHelp ->
                "Le débit basal programmé dans votre pompe, une valeur par heure de la journée en " +
                    "unités par heure, arrondie à 0,01 U/h. Le total journalier est la somme des 24 " +
                    "heures. C'est un relevé pour vous et votre équipe soignante — il ne modifie aucun " +
                    "calcul de bolus." +
                    " Touchez l'en-tête pour afficher ou masquer les 24 heures ; le total journalier reste " +
                    "toujours visible. " +
                    "Touchez une heure pour modifier son débit."
            TranslationKey.PumpBasalRateUnit -> "U/h"
            TranslationKey.ActionShowHours -> "Afficher les heures"
            TranslationKey.ActionHideHours -> "Masquer les heures"
            TranslationKey.PumpBasalDailyTotal -> $$"Total journalier : %1$s U"
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
            TranslationKey.BolusTypeHelp ->
                "Normal calcule immédiatement une dose combinée unique. Fractionné divise la " +
                    "dose en une part immédiate et une part différée, utile pour les repas à " +
                    "digestion lente."
            TranslationKey.BolusNormal -> "Normal"
            TranslationKey.BolusSplit -> "Fractionné"
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
            TranslationKey.AppUpdateViewOnGitHub -> "Voir le code source sur GitHub"
            TranslationKey.AppUpdateObtainiumDescription ->
                "Les mises à jour sont fournies par Obtainium, qui surveille les versions GitHub de " +
                    "cette application " +
                    "et installe les nouvelles versions pour vous. " +
                    "Ajoutez l'application une seule fois à Obtainium, le reste est automatique."
            TranslationKey.AppUpdateObtainiumAddButton -> "Ajouter à Obtainium"
            TranslationKey.AppUpdateObtainiumGetButton -> "Obtenir Obtainium"

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
            TranslationKey.AuditPumpBasalRateChanged ->
                $$"Le débit basal de la pompe %1$s est passé de %2$s à %3$s U/h."
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
            TranslationKey.DynamicColor -> "Kolory z tapety"
            TranslationKey.DynamicColorHelp ->
                "Używa kolorów z Twojej tapety zamiast własnej, ciepłej palety aplikacji. " +
                    "Ostrzeżenia przy niskim cukrze lub wysokiej dawce zawsze pozostają czerwone lub bursztynowe."
            TranslationKey.Language -> "Język"
            TranslationKey.LanguageHelp ->
                "Język używany w całej aplikacji. Wybierz System, aby podążać za ustawieniem " +
                    "języka urządzenia."
            TranslationKey.SettingsGroupTherapy -> "Terapia i dawka"
            TranslationKey.SettingsGroupData -> "Dane i raport"
            TranslationKey.SettingsGroupApp -> "Aplikacja"
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
            TranslationKey.DestinationSettings -> "Więcej"

            // Common actions
            TranslationKey.ActionEdit -> "Edytuj"
            TranslationKey.ActionUndo -> "Cofnij"
            TranslationKey.FactorsSaved -> "Zapisano zmiany"
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
                    "porządkowane według godziny rozpoczęcia. " +
                    "Dotknij wiersza, aby go zmienić. Zmiany można cofnąć zaraz po zapisaniu."
            TranslationKey.BasalRate -> "Wartość podstawowa"
            TranslationKey.BasalRateHelp ->
                "Twoja dawka insuliny podstawowej, pokazana informacyjnie razem z godziną " +
                    "podania. Edycja tutaj nie zmienia żadnych obliczeń — to tylko zapis."
            TranslationKey.LabelFactor -> "Czynnik"
            TranslationKey.FactorValueHelp ->
                "Jednostki insuliny na wymiennik chlebowy w tym przedziale czasowym. Wyższy " +
                    "czynnik oznacza więcej insuliny dla tej samej ilości węglowodanów."
            TranslationKey.FactorsListPumpHelp ->
                "Każdy wiersz to przedział czasu: nazwij go (np. \"Śniadanie\") i ustaw jego " +
                    "przelicznik pompy — ile gramów węglowodanów pokrywa jedna jednostka insuliny, " +
                    "gdy bieżąca godzina mieści się w tym przedziale. Przedziały są sortowane " +
                    "automatycznie według godziny rozpoczęcia. " +
                    "Dotknij wiersza, aby go zmienić. Zmiany można cofnąć zaraz po zapisaniu."
            TranslationKey.LabelPumpRatio -> "1 j. na … g węglowodanów"
            TranslationKey.PumpRatioValueHelp ->
                "Przelicznik insuliny do węglowodanów tak, jak pokazuje go pompa: jedna jednostka " +
                    "insuliny na tyle gramów węglowodanów. Jest przeliczany z Twojego czynnika przy " +
                    "użyciu wartości wymiennika chlebowego (gramy ÷ czynnik) i zaokrąglany do 0,1 g. " +
                    "Mniejsza liczba oznacza więcej insuliny na tę samą ilość węglowodanów."
            TranslationKey.PumpRatioUnit -> "g/j."
            TranslationKey.DoseStep -> "Krok dawki"
            TranslationKey.DoseStepHelp ->
                "Najmniejsza ilość, jaką może podać Twój pen lub pompa. Obliczona dawka jest zaokrąglana " +
                    "do najbliższej wielokrotności tego kroku, aby wynik dało się faktycznie wstrzyknąć."
            TranslationKey.MaxDose -> "Dawka maksymalna"
            TranslationKey.MaxDoseHelp ->
                "Limit bezpieczeństwa dla pojedynczej dawki. Jeśli wynik jest wyższy, pojawi się " +
                    "ostrzeżenie, by literówka nie przeszła niezauważona. Ustaw 0, aby wyłączyć limit."
            TranslationKey.FastCarbs -> "Szybkie węglowodany przy niskim cukrze"
            TranslationKey.FastCarbsHelp ->
                "Ile gramów szybkich węglowodanów jest sugerowane, gdy glukoza jest poniżej dolnego progu. " +
                    "Ustal tę ilość z zespołem diabetologicznym."
            TranslationKey.DoseUnitSymbol -> "j."
            TranslationKey.GramsUnit -> "g"
            TranslationKey.DoseResultTitle -> "Dawka do wstrzyknięcia"
            TranslationKey.DoseResultHelp ->
                "Dawka całkowita to dawka na posiłek plus korekta glukozy, nigdy poniżej zera, " +
                    "zaokrąglona do kroku dawki. Wiersz poniżej pokazuje dokładną sumę przed zaokrągleniem. " +
                    "Jeśli wartość wygląda na literówkę, dawka nie jest pokazywana, dopóki jej nie poprawisz."
            TranslationKey.DoseBreakdown -> $$"Posiłek %1$s + korekta %2$s = %3$s %4$s"
            TranslationKey.DoseRoundedTo -> $$"Zaokrąglone do kroków %1$s %2$s"
            TranslationKey.LowBloodSugarTitle -> "Poniżej dolnego progu"
            TranslationKey.LowBloodSugarBody ->
                $$"%1$s jest poniżej %2$s. Najpierw lecz niski cukier, potem policz ponownie."
            TranslationKey.FastCarbsSuggestion -> "Sugerowane szybkie węglowodany"
            TranslationKey.MaxDoseExceeded ->
                $$"Powyżej Twojej dawki maksymalnej %1$s %2$s. Sprawdź wartości przed wstrzyknięciem."
            TranslationKey.MaxBloodSugar -> "Maksymalna wiarygodna glikemia"
            TranslationKey.MaxBloodSugarHelp ->
                "Wartości glikemii powyżej tej granicy uznawane są za literówkę: dawka nie jest pokazywana, " +
                    "dopóki ich nie poprawisz. Ustaw 0, aby wyłączyć kontrolę."
            TranslationKey.MaxCarbohydrates -> "Maksymalna wiarygodna ilość węglowodanów"
            TranslationKey.MaxCarbohydratesHelp ->
                "Ilości węglowodanów powyżej tej granicy uznawane są za literówkę: dawka nie jest pokazywana, " +
                    "dopóki ich nie poprawisz. Ustaw 0, aby wyłączyć kontrolę."
            TranslationKey.DoseBlockedBloodSugar ->
                $$"Glikemia przekracza %1$s %2$s. Sprawdź wartość."
            TranslationKey.DoseBlockedCarbohydrates ->
                $$"Węglowodany przekraczają %1$s %2$s. Sprawdź wartość."
            TranslationKey.DoseBlockedNoFactor ->
                "Brak współczynnika dla tej pory dnia. " +
                    "Dodaj go w zakładce Współczynniki."
            TranslationKey.ResetInputs -> "Wyczyść"
            TranslationKey.TherapyModeTitle -> "Terapia"
            TranslationKey.TherapyModeHelp ->
                "Wybierz, jak podajesz insulinę. \"Pen\" pokazuje czynniki jako jednostki insuliny " +
                    "na wymiennik chlebowy (np. 1,5), a dawkę podstawową jako jedną dawkę dzienną o " +
                    "stałej godzinie. \"Pompa insulinowa\" pokazuje czynniki tak jak pompy, np. " +
                    "YpsoPump — jedna jednostka na x gramów węglowodanów (np. 8 g/j. przy 12 g na " +
                    "wymiennik) — a dawkę podstawową jako 24 godzinowe przepływy w jednostkach na " +
                    "godzinę. Czynniki znaczą w obu trybach to samo, więc obliczenia bolusa się nie " +
                    "zmieniają. Dawka z pena i przepływy pompy są zapisywane osobno." +
                    " Przypomnienie o dawce podstawowej jest dostępne tylko przy penie, bo pompa podaje " +
                    "insulinę podstawową sama."
            TranslationKey.TherapyModePen -> "Pen"
            TranslationKey.TherapyModePump -> "Pompa insulinowa"
            TranslationKey.PumpBasalRatesTitle -> "Dawka podstawowa (pompa)"
            TranslationKey.PumpBasalRatesHelp ->
                "Dawka podstawowa zaprogramowana w pompie, jedna wartość na każdą godzinę doby w " +
                    "jednostkach na godzinę, zaokrąglona do 0,01 j./h. Suma dobowa to suma wszystkich 24 " +
                    "godzin. To zapis dla Ciebie i Twojego zespołu diabetologicznego — nie zmienia " +
                    "żadnych obliczeń bolusa." +
                    " Dotknij nagłówka, aby pokazać lub ukryć 24 godziny; suma dobowa jest zawsze widoczna. " +
                    "Dotknij godziny, aby zmienić jej wartość."
            TranslationKey.PumpBasalRateUnit -> "j./h"
            TranslationKey.ActionShowHours -> "Pokaż godziny"
            TranslationKey.ActionHideHours -> "Ukryj godziny"
            TranslationKey.PumpBasalDailyTotal -> $$"Suma dobowa: %1$s j."
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
            TranslationKey.AppUpdateViewOnGitHub -> "Zobacz kod źródłowy na GitHub"
            TranslationKey.AppUpdateObtainiumDescription ->
                "Aktualizacje są dostarczane przez Obtainium, które śledzi wydania tej aplikacji na GitHub " +
                    "i instaluje nowe wersje za Ciebie. Dodaj aplikację do Obtainium raz, a resztą zajmie się samo."
            TranslationKey.AppUpdateObtainiumAddButton -> "Dodaj do Obtainium"
            TranslationKey.AppUpdateObtainiumGetButton -> "Pobierz Obtainium"

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
            TranslationKey.AuditPumpBasalRateChanged ->
                $$"Dawka podstawowa pompy %1$s zmieniła się z %2$s na %3$s j./h."
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
