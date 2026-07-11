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
    ContrastLevel,
    Language,
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
    BasalRate,
    LabelFactor,
    FactorNameLabel,
    ActionSchedule,
    ActionAddFactor,
    FactorNameDuplicateError,
    ScheduleAutoOrderHint,

    // Bolus calculation
    BolusType,
    BolusNormal,
    BolusSplit,
    BolusUnits,
    BolusImmediatePercent,
    BolusExtendedPercent,
    BolusDurationMinutes,
    Carbohydrates,
    ActiveFactor,
    Calculated,
    CalculatedUnits,
    BolusImmediateUnits,
    BolusExtendedUnits,
    FutureFactor,
    PeriodLabel,
    PeriodFactorPercent,
    BreadUnits,
    FactorSettingsTitle,
    BloodSugar,
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
    TemplateEmojiOptional,
    TemplateEmoji,
    TemplateSortTitle,
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
    ImportErrorMessage
}

/** The localized text for [key] in [language]; [AppLanguage.System] resolves to the device locale. */
fun translate(key: TranslationKey, language: AppLanguage): String {
    val effectiveLanguage = resolveAppLanguage(language)
    return when (effectiveLanguage) {
        AppLanguage.English -> when (key) {
            // Appearance & theme
            TranslationKey.Appearance -> "Appearance"
            TranslationKey.ThemeMode -> "Theme mode"
            TranslationKey.ContrastLevel -> "Contrast level"
            TranslationKey.Language -> "Language"
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
            TranslationKey.BasalRate -> "Basal rate"
            TranslationKey.LabelFactor -> "Factor"
            TranslationKey.FactorNameLabel -> "Name"
            TranslationKey.ActionSchedule -> "Schedule"
            TranslationKey.ActionAddFactor -> "Add factor"
            TranslationKey.FactorNameDuplicateError -> "A factor with this name already exists"
            TranslationKey.ScheduleAutoOrderHint -> "Times are auto-corrected to keep the daily order."

            // Bolus calculation
            TranslationKey.BolusType -> "Bolus type"
            TranslationKey.BolusNormal -> "Normal"
            TranslationKey.BolusSplit -> "Split bolus"
            TranslationKey.BolusUnits -> "Bolus units"
            TranslationKey.BolusImmediatePercent -> "Immediate share (%)"
            TranslationKey.BolusExtendedPercent -> "Extended share (%)"
            TranslationKey.BolusDurationMinutes -> "Duration (minutes)"
            TranslationKey.Carbohydrates -> "Carbohydrates"
            TranslationKey.ActiveFactor -> "Active factor"
            TranslationKey.Calculated -> "Calculated"
            TranslationKey.CalculatedUnits -> "Calculated units"
            TranslationKey.BolusImmediateUnits -> "Immediate units"
            TranslationKey.BolusExtendedUnits -> "Extended units"
            TranslationKey.FutureFactor -> "Future factor"
            TranslationKey.PeriodLabel -> "Period?"
            TranslationKey.PeriodFactorPercent -> "Period increase (%)"
            TranslationKey.BreadUnits -> "Bread units"
            TranslationKey.FactorSettingsTitle -> "Factor settings"
            TranslationKey.BloodSugar -> "Blood sugar"
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
            TranslationKey.TemplateEmojiOptional -> "Emoji (optional)"
            TranslationKey.TemplateEmpty -> "No templates yet"
            TranslationKey.TemplateEmoji -> "Emoji"
            TranslationKey.TemplateSortTitle -> "Order by"
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
        }

        AppLanguage.German -> when (key) {
            // Appearance & theme
            TranslationKey.Appearance -> "Darstellung"
            TranslationKey.ThemeMode -> "Thema"
            TranslationKey.ContrastLevel -> "Kontrast"
            TranslationKey.Language -> "Sprache"
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
            TranslationKey.BasalRate -> "Basisrate"
            TranslationKey.LabelFactor -> "Faktor"
            TranslationKey.FactorNameLabel -> "Name"
            TranslationKey.ActionSchedule -> "Zeitplanung"
            TranslationKey.ActionAddFactor -> "Faktor hinzufügen"
            TranslationKey.FactorNameDuplicateError -> "Ein Faktor mit diesem Namen existiert bereits"
            TranslationKey.ScheduleAutoOrderHint -> "Zeiten werden automatisch angepasst, damit die Tagesreihenfolge erhalten bleibt."

            // Bolus calculation
            TranslationKey.BolusType -> "Bolus-Typ"
            TranslationKey.BolusNormal -> "Normal"
            TranslationKey.BolusSplit -> "Gesplitteter Bolus"
            TranslationKey.BolusUnits -> "Bolus-Einheiten"
            TranslationKey.BolusImmediatePercent -> "Sofortanteil (%)"
            TranslationKey.BolusExtendedPercent -> "Verzögerter Anteil (%)"
            TranslationKey.BolusDurationMinutes -> "Dauer (Minuten)"
            TranslationKey.Carbohydrates -> "Kohlenhydrate"
            TranslationKey.ActiveFactor -> "Aktiver Faktor"
            TranslationKey.Calculated -> "Berechnet"
            TranslationKey.CalculatedUnits -> "Berechnete Einheiten"
            TranslationKey.BolusImmediateUnits -> "Sofort-Einheiten"
            TranslationKey.BolusExtendedUnits -> "Verzögerte Einheiten"
            TranslationKey.FutureFactor -> "Zukünftiger Faktor"
            TranslationKey.PeriodLabel -> "Periode?"
            TranslationKey.PeriodFactorPercent -> "Periode-Erhöhung (%)"
            TranslationKey.BreadUnits -> "Broteinheiten"
            TranslationKey.FactorSettingsTitle -> "Faktor-Einstellungen"
            TranslationKey.BloodSugar -> "Blutzucker"
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
            TranslationKey.TemplateEmojiOptional -> "Emoji (optional)"
            TranslationKey.TemplateEmpty -> "Noch keine Vorlagen"
            TranslationKey.TemplateEmoji -> "Emoji"
            TranslationKey.TemplateSortTitle -> "Sortierung"
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
        }

        AppLanguage.French -> when (key) {
            // Appearance & theme
            TranslationKey.Appearance -> "Apparence"
            TranslationKey.ThemeMode -> "Mode Thème"
            TranslationKey.ContrastLevel -> "Niveau de contraste"
            TranslationKey.Language -> "Langue"
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
            TranslationKey.BasalRate -> "Débit de base"
            TranslationKey.LabelFactor -> "Facteur"
            TranslationKey.FactorNameLabel -> "Nom"
            TranslationKey.ActionSchedule -> "Calendrier"
            TranslationKey.ActionAddFactor -> "Ajouter un facteur"
            TranslationKey.FactorNameDuplicateError -> "Un facteur avec ce nom existe déjà"
            TranslationKey.ScheduleAutoOrderHint -> "Les heures sont corrigées automatiquement pour conserver l'ordre de la journée."

            // Bolus calculation
            TranslationKey.BolusType -> "Type de bolus"
            TranslationKey.BolusNormal -> "Normal"
            TranslationKey.BolusSplit -> "Bolus fractionné"
            TranslationKey.BolusUnits -> "Unités de bolus"
            TranslationKey.BolusImmediatePercent -> "Part immédiate (%)"
            TranslationKey.BolusExtendedPercent -> "Part prolongée (%)"
            TranslationKey.BolusDurationMinutes -> "Durée (minutes)"
            TranslationKey.Carbohydrates -> "Glucides"
            TranslationKey.ActiveFactor -> "Facteur actif"
            TranslationKey.Calculated -> "Calcule"
            TranslationKey.CalculatedUnits -> "Unités calculées"
            TranslationKey.BolusImmediateUnits -> "Unités immédiates"
            TranslationKey.BolusExtendedUnits -> "Unités prolongées"
            TranslationKey.FutureFactor -> "Facteur futur"
            TranslationKey.PeriodLabel -> "Période?"
            TranslationKey.PeriodFactorPercent -> "Augmentation Période (%)"
            TranslationKey.BreadUnits -> "Unités de pain"
            TranslationKey.FactorSettingsTitle -> "Paramètres des facteurs"
            TranslationKey.BloodSugar -> "Glycémie"
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
            TranslationKey.TemplateEmojiOptional -> "Emoji (optionnel)"
            TranslationKey.TemplateEmpty -> "Aucun modele"
            TranslationKey.TemplateEmoji -> "Emoji"
            TranslationKey.TemplateSortTitle -> "Trier par"
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
        }

        AppLanguage.Polish -> when (key) {
            // Appearance & theme
            TranslationKey.Appearance -> "Wygląd"
            TranslationKey.ThemeMode -> "Tryb motywu"
            TranslationKey.ContrastLevel -> "Poziom kontrastu"
            TranslationKey.Language -> "Język"
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
            TranslationKey.BasalRate -> "Wartość podstawowa"
            TranslationKey.LabelFactor -> "Czynnik"
            TranslationKey.FactorNameLabel -> "Nazwa"
            TranslationKey.ActionSchedule -> "Planuj"
            TranslationKey.ActionAddFactor -> "Dodaj czynnik"
            TranslationKey.FactorNameDuplicateError -> "Czynnik o tej nazwie już istnieje"
            TranslationKey.ScheduleAutoOrderHint -> "Godziny są automatycznie korygowane, aby zachować kolejność w ciągu dnia."

            // Bolus calculation
            TranslationKey.BolusType -> "Typ bolusa"
            TranslationKey.BolusNormal -> "Normalny"
            TranslationKey.BolusSplit -> "Bolus złożony"
            TranslationKey.BolusUnits -> "Jednostki bolusa"
            TranslationKey.BolusImmediatePercent -> "Część natychmiastowa (%)"
            TranslationKey.BolusExtendedPercent -> "Część przedłużona (%)"
            TranslationKey.BolusDurationMinutes -> "Czas trwania (minuty)"
            TranslationKey.Carbohydrates -> "Węglowodany"
            TranslationKey.ActiveFactor -> "Aktywny współczynnik"
            TranslationKey.Calculated -> "Obliczone"
            TranslationKey.CalculatedUnits -> "Obliczone jednostki"
            TranslationKey.BolusImmediateUnits -> "Jednostki natychmiastowe"
            TranslationKey.BolusExtendedUnits -> "Jednostki przedłużone"
            TranslationKey.FutureFactor -> "Przyszły współczynnik"
            TranslationKey.PeriodLabel -> "Okres?"
            TranslationKey.PeriodFactorPercent -> "Zwiększenie Okres (%)"
            TranslationKey.BreadUnits -> "Wymienniki chlebowe"
            TranslationKey.FactorSettingsTitle -> "Ustawienia współczynnika"
            TranslationKey.BloodSugar -> "Poziom cukru we krwi"
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
            TranslationKey.TemplateEmojiOptional -> "Emoji (opcjonalnie)"
            TranslationKey.TemplateEmpty -> "Brak szablonów"
            TranslationKey.TemplateEmoji -> "Emoji"
            TranslationKey.TemplateSortTitle -> "Sortowanie"
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
