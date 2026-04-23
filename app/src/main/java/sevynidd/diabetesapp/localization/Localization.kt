package sevynidd.diabetesapp.localization

import java.util.Locale

enum class AppLanguage {
    System,
    English,
    German,
    French,
    Polish
}

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
    ActionTemplates,
    ActionDelete,
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
    TemplateApplyToBothModes,
    TemplateDuplicateNameError
}

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
            TranslationKey.ActionTemplates -> "Templates"
            TranslationKey.ActionDelete -> "Delete"
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
            TranslationKey.TemplateApplyToBothModes -> "Apply to both modes"
            TranslationKey.TemplateDuplicateNameError -> "A template with this name already exists"
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
            TranslationKey.ActionTemplates -> "Vorlagen"
            TranslationKey.ActionDelete -> "Löschen"
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
            TranslationKey.TemplateApplyToBothModes -> "Auf beide Modi anwenden"
            TranslationKey.TemplateDuplicateNameError -> "Eine Vorlage mit diesem Namen existiert bereits"
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
            TranslationKey.ActionTemplates -> "Modeles"
            TranslationKey.ActionDelete -> "Supprimer"
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
            TranslationKey.TemplateApplyToBothModes -> "Appliquer aux deux modes"
            TranslationKey.TemplateDuplicateNameError -> "Un modele avec ce nom existe deja"
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
            TranslationKey.ActionTemplates -> "Szablony"
            TranslationKey.ActionDelete -> "Usuń"
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
            TranslationKey.TemplateApplyToBothModes -> "Zastosuj do obu trybów"
            TranslationKey.TemplateDuplicateNameError -> "Szablon o tej nazwie już istnieje"
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
