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
    DestinationFactors,
    DestinationCalculate,
    DestinationSettings,
    ActionEdit,
    ActionSave,
    ActionCancel,
    ActionClose,
    ActionBack,
    ActionTemplates,
    ActionDelete,
    ActionMoreOptions,
    FactorMorning,
    FactorBreakfast,
    FactorLunch,
    FactorAfternoon,
    FactorDinner,
    FactorLate,
    FactorNight,
    BasalRate,
    LabelFactor,
    ActionSchedule,
    ScheduleAutoOrderHint,
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
    AppUpdateViewOnGitHub
}

/** The localized text for [key] in [language]; [AppLanguage.System] resolves to the device locale. */
fun translate(key: TranslationKey, language: AppLanguage): String {
    val effectiveLanguage = resolveAppLanguage(language)
    return when (effectiveLanguage) {
        AppLanguage.English -> when (key) {
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
            TranslationKey.DestinationFactors -> "Factors"
            TranslationKey.DestinationCalculate -> "Calculate"
            TranslationKey.DestinationSettings -> "Settings"
            TranslationKey.ActionEdit -> "Edit"
            TranslationKey.ActionSave -> "Save"
            TranslationKey.ActionCancel -> "Cancel"
            TranslationKey.ActionClose -> "Close"
            TranslationKey.ActionBack -> "Back"
            TranslationKey.ActionTemplates -> "Templates"
            TranslationKey.ActionDelete -> "Delete"
            TranslationKey.ActionMoreOptions -> "More options"
            TranslationKey.FactorMorning -> "Morning"
            TranslationKey.FactorBreakfast -> "Breakfast"
            TranslationKey.FactorLunch -> "Lunch"
            TranslationKey.FactorAfternoon -> "Afternoon"
            TranslationKey.FactorDinner -> "Dinner"
            TranslationKey.FactorLate -> "Late"
            TranslationKey.FactorNight -> "Night"
            TranslationKey.BasalRate -> "Basal rate"
            TranslationKey.LabelFactor -> "Factor"
            TranslationKey.ActionSchedule -> "Schedule"
            TranslationKey.ScheduleAutoOrderHint -> "Times are auto-corrected to keep the daily order."
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
        }

        AppLanguage.German -> when (key) {
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
            TranslationKey.DestinationFactors -> "Faktoren"
            TranslationKey.DestinationCalculate -> "Berechnen"
            TranslationKey.DestinationSettings -> "Einstellungen"
            TranslationKey.ActionEdit -> "Bearbeiten"
            TranslationKey.ActionSave -> "Speichern"
            TranslationKey.ActionCancel -> "Abbrechen"
            TranslationKey.ActionClose -> "Schließen"
            TranslationKey.ActionBack -> "Zurück"
            TranslationKey.ActionTemplates -> "Vorlagen"
            TranslationKey.ActionDelete -> "Löschen"
            TranslationKey.ActionMoreOptions -> "Weitere Optionen"
            TranslationKey.FactorMorning -> "Morgen"
            TranslationKey.FactorBreakfast -> "Frühstück"
            TranslationKey.FactorLunch -> "Mittagessen"
            TranslationKey.FactorAfternoon -> "Nachmittag"
            TranslationKey.FactorDinner -> "Abendessen"
            TranslationKey.FactorLate -> "Spätmahlzeit"
            TranslationKey.FactorNight -> "Nacht"
            TranslationKey.BasalRate -> "Basisrate"
            TranslationKey.LabelFactor -> "Faktor"
            TranslationKey.ActionSchedule -> "Zeitplanung"
            TranslationKey.ScheduleAutoOrderHint -> "Zeiten werden automatisch angepasst, damit die Tagesreihenfolge erhalten bleibt."
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
        }

        AppLanguage.French -> when (key) {
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
            TranslationKey.DestinationFactors -> "Facteurs"
            TranslationKey.DestinationCalculate -> "Calculer"
            TranslationKey.DestinationSettings -> "Paramètres"
            TranslationKey.ActionEdit -> "Modifier"
            TranslationKey.ActionSave -> "Enregistrer"
            TranslationKey.ActionCancel -> "Annuler"
            TranslationKey.ActionClose -> "Fermer"
            TranslationKey.ActionBack -> "Retour"
            TranslationKey.ActionTemplates -> "Modeles"
            TranslationKey.ActionDelete -> "Supprimer"
            TranslationKey.ActionMoreOptions -> "Plus d'options"
            TranslationKey.FactorMorning -> "Matin"
            TranslationKey.FactorBreakfast -> "Petit-déjeuner"
            TranslationKey.FactorLunch -> "Dejeuner"
            TranslationKey.FactorAfternoon -> "Après-midi"
            TranslationKey.FactorDinner -> "Diner"
            TranslationKey.FactorLate -> "Tard"
            TranslationKey.FactorNight -> "Nuit"
            TranslationKey.BasalRate -> "Débit de base"
            TranslationKey.LabelFactor -> "Facteur"
            TranslationKey.ActionSchedule -> "Calendrier"
            TranslationKey.ScheduleAutoOrderHint -> "Les heures sont corrigées automatiquement pour conserver l'ordre de la journée."
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
        }

        AppLanguage.Polish -> when (key) {
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
            TranslationKey.DestinationFactors -> "Czynniki"
            TranslationKey.DestinationCalculate -> "Oblicz"
            TranslationKey.DestinationSettings -> "Ustawienia"
            TranslationKey.ActionEdit -> "Edytuj"
            TranslationKey.ActionSave -> "Zapisz"
            TranslationKey.ActionCancel -> "Anuluj"
            TranslationKey.ActionClose -> "Zamknij"
            TranslationKey.ActionBack -> "Wstecz"
            TranslationKey.ActionTemplates -> "Szablony"
            TranslationKey.ActionDelete -> "Usuń"
            TranslationKey.ActionMoreOptions -> "Więcej opcji"
            TranslationKey.FactorMorning -> "Rano"
            TranslationKey.FactorBreakfast -> "Śniadanie"
            TranslationKey.FactorLunch -> "Obiad"
            TranslationKey.FactorAfternoon -> "Popołudnie"
            TranslationKey.FactorDinner -> "Kolacja"
            TranslationKey.FactorLate -> "Późno"
            TranslationKey.FactorNight -> "Noc"
            TranslationKey.BasalRate -> "Wartość podstawowa"
            TranslationKey.LabelFactor -> "Czynnik"
            TranslationKey.ActionSchedule -> "Planuj"
            TranslationKey.ScheduleAutoOrderHint -> "Godziny są automatycznie korygowane, aby zachować kolejność w ciągu dnia."
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
