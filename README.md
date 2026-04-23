# Diabetes App

![Android](https://img.shields.io/badge/Android-31%2B-3DDC84?logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/Kotlin-2.3.20-7F52FF?logo=kotlin&logoColor=white) ![Jetpack%20Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white) ![Material%203](https://img.shields.io/badge/Material%203-UI-6200EE) ![Room](https://img.shields.io/badge/Room-2.8.4-6D4C41) ![DataStore](https://img.shields.io/badge/DataStore-Preferences%201.2.1-1E88E5) ![AGP](https://img.shields.io/badge/AGP-9.2.0-34A853)

Android-App auf Basis von Jetpack Compose zur Verwaltung diabetesrelevanter Faktoren, Zeitfenster und Bolus-Berechnungen. Die App kombiniert persistente Faktoren- und Zeitprofile mit lokalisierter UI, Theme-/Kontrast-Einstellungen sowie einem konfigurierbaren Broteinheiten-Faktor.

## Inhaltsverzeichnis

- [Aktueller Stand](#aktueller-stand)
- [Features](#features)
- [Datenhaltung & Persistenz](#datenhaltung--persistenz)
- [Berechnungslogik](#berechnungslogik)
- [Validierung & Eingabeverhalten](#validierung--eingabeverhalten)
- [Tech-Stack](#tech-stack)
- [Projektstruktur (Auszug)](#projektstruktur-auszug)
- [Voraussetzungen](#voraussetzungen)
- [Installation und Start](#installation-und-start)
- [Screenshot](#screenshot)
- [Roadmap](#roadmap)
- [Lizenz](#lizenz)

## Aktueller Stand

Das Projekt befindet sich in aktiver Entwicklung, enthält aber bereits einen funktionsfähigen Workflow für:

- Faktoren und Basalrate
- Tageszeit-Schedule mit Pie-Chart
- persistente Speicherung über Room und DataStore
- Bolus-Berechnung für **Normal** und **Split Bolus**
- lokalisierte Oberfläche in mehreren Sprachen

## Features

### Faktoren & Schedule

- 7 Tages-Faktoren: Morning, Breakfast, Lunch, Afternoon, Dinner, Late, Night
- Basalrate als separates Feld
- Standardmäßig Read-only, Bearbeitung über Edit-Icon in der TopAppBar
- Eigener Schedule-Screen mit editierbaren Zeiten per Material `TimePicker`
- Pie-Chart mit farbigen Zeitsegmenten, Titeln und Uhrzeiten
- Schedule-Zeiten werden automatisch in aufsteigender Reihenfolge gehalten
- Hinweistext im Schedule-Screen erklärt die automatische Korrektur der Reihenfolge
- Faktor-Beschreibungen zeigen dynamisch die gespeicherten Zeitbereiche an

### Berechnen

- Tab-Auswahl zwischen **Normal** und **Split Bolus**
- **Normal-Modus**:
  - Eingabe von Kohlenhydraten
  - automatische Berechnung der Einheiten in Echtzeit
  - Anzeige des aktuell gültigen Faktors inklusive Faktorname
- **Split-Bolus-Modus**:
  - Eingabe von Kohlenhydraten
  - Eingabe des Sofort-Anteils in Prozent
  - automatischer Rest-Anteil (`100 - Sofort-Anteil`)
  - editierbare Dauer (Default: `120` Minuten)
  - Berechnung mit aktuellem Faktor für den Sofort-Anteil
  - Berechnung mit zukünftigem Faktor für den verzögerten Anteil (`jetzt + Dauer`)
  - Anzeige von aktuellem Faktor und zukünftigem Faktor nebeneinander
  - Anzeige von Sofort- und verzögerten Einheiten nebeneinander
- Konfigurierbarer Broteinheiten-Wert statt fest kodiertem Divisor

### Einstellungen

- Theme-Modus: `System`, `Light`, `Dark`
- Kontraststufe: `Normal`, `Medium`, `High`
- Sprache: `System`, `Deutsch`, `English`, `Français`, `Polski`
- Eigene Einstellung für **Broteinheiten**
- Animierte Navigation innerhalb der Einstellungen

### UI & Navigation

- Adaptive Navigation mit `Factors`, `Calculate` und `Settings`
- Material Icons statt Drawable-Icons
- Material 3 UI mit Light/Dark und Kontrastvarianten
- Mehrsprachige Texte über zentrale Übersetzungsfunktion

## Datenhaltung & Persistenz

### Room

Gespeichert in `diabetes_app.db`, Tabelle `factor_profile`:

- alle 7 Faktoren
- Basalrate
- alle Schedule-Zeiten:
  - Morning
  - Breakfast
  - Lunch
  - Afternoon
  - Dinner
  - Late
  - Night
  - Basal-Zeit

### DataStore Preferences

Persistiert werden:

- Theme-Modus
- Kontraststufe
- Sprache
- Broteinheiten-Wert

### Edit-Session / Save-Verhalten

Die Faktorbearbeitung läuft über `FactorEditSessionViewModel` + `SavedStateHandle`.

Auto-Save wird ausgelöst bei:

- Klick auf Save (Check-Icon)
- Verlassen des `Factors`-Tabs
- App-Wechsel in den Hintergrund (`ON_STOP`)

Bei Konfigurationsänderungen (z. B. Rotation) wird der Hintergrund-Save nicht fälschlich ausgelöst (`isChangingConfigurations`).

## Berechnungslogik

### Normal-Bolus

Die berechneten Einheiten basieren auf:

- eingegebenen Kohlenhydraten
- aktuell gültigem Faktor
- konfiguriertem Broteinheiten-Wert

Formel:

`Einheiten = (Kohlenhydrate / Broteinheiten) * Faktor`

### Split-Bolus

Der Split-Bolus teilt die Kohlenhydrate in zwei Anteile:

- **Sofort-Anteil** über den aktuell gültigen Faktor
- **Verzögerter Anteil** über den Faktor, der nach `jetzt + Dauer` gilt

Damit kann sich der zweite Anteil an einem anderen Zeitfenster orientieren als der erste.

## Validierung & Eingabeverhalten

- Dezimalwerte werden mit Komma erfasst und angezeigt (z. B. `1,25`)
- Faktorfelder werden beim Verlassen/Commit auf den nächsten `0,25`-Schritt aufgerundet
- Basalrate wird beim Verlassen/Commit auf die nächste gerade Zahl aufgerundet
- Felder normalisieren ihre Werte beim Ende des Edit-Modus, damit auch fokussierte Eingaben korrekt gespeichert werden
- Schedule-Zeiten werden automatisch korrigiert, damit die Reihenfolge gültig bleibt
- Split-Bolus-Sofortanteil wird auf maximal `100` begrenzt
- Der Rest-Anteil wird automatisch berechnet und kann daher nie über `100` hinausgehen

## Tech-Stack

- Kotlin `2.3.20`
- Android Gradle Plugin `9.2.0`
- Jetpack Compose BOM `2026.03.01`
- Material 3
- Material 3 Adaptive Navigation Suite
- Material Icons Extended
- Room `2.8.4`
- DataStore Preferences `1.2.1`
- Lifecycle Compose + ViewModel Compose
- KSP
- JUnit / AndroidX Test

## Projektstruktur (Auszug)

- `app/src/main/java/sevynidd/diabetesapp/MainActivity.kt` - App-Start, Theme-Anbindung, Flows für Settings/Faktoren
- `app/src/main/java/sevynidd/diabetesapp/screens/MainWindow.kt` - Scaffold, Navigation, Save-Trigger und Settings-Unterseiten
- `app/src/main/java/sevynidd/diabetesapp/screens/factors/FactorScreen.kt` - Faktoren-UI mit dynamischen Zeitbereichen
- `app/src/main/java/sevynidd/diabetesapp/screens/factors/ScheduleFactorScreen.kt` - Schedule-Editor mit Pie-Chart und TimePicker
- `app/src/main/java/sevynidd/diabetesapp/screens/calculate/CalculateScreen.kt` - Normal-/Split-Bolus-Berechnung
- `app/src/main/java/sevynidd/diabetesapp/screens/calculate/TemplateManagerScreen.kt` - Template-Auswahl und -Verwaltung für die Bolus-Berechnung
- `app/src/main/java/sevynidd/diabetesapp/screens/settings/` - Theme-, Language- und Broteinheiten-Einstellungen
- `app/src/main/java/sevynidd/diabetesapp/data/database/` - Room (`DiabetesDatabase`, Entities, DAOs, Repositories)
- `app/src/main/java/sevynidd/diabetesapp/data/model/FactorsData.kt` - UI-freundliches Faktorenmodell
- `app/src/main/java/sevynidd/diabetesapp/data/settings/ThemeMode.kt` - Persistierter Theme-Modus
- `app/src/main/java/sevynidd/diabetesapp/data/AppSettingsStore.kt` - Persistenz von Theme, Kontrast, Sprache und Broteinheiten
- `app/src/main/java/sevynidd/diabetesapp/navigation/Navigation.kt` - Destinationen und Transitionen
- `app/src/main/java/sevynidd/diabetesapp/localization/Localization.kt` - Übersetzungslogik für EN/DE/FR/PL/System
- `app/src/main/java/sevynidd/diabetesapp/libraries/gappedPieChart/` - Pie-Chart-Komponenten

## Voraussetzungen

- Android Studio (aktuelle stabile Version)
- JDK 11+
- Android SDK (`compileSdk 36`, `minSdk 31`, `targetSdk 36`)

## Installation und Start

1. Repository klonen
2. Projekt in Android Studio öffnen
3. Gradle-Sync ausführen
4. App auf Emulator oder Gerät starten

Optionaler CLI-Build:

```powershell
Set-Location "C:\Users\sevyn\Documents\GitHub\DiabetesApp"
.\gradlew.bat :app:compileDebugKotlin --console=plain
```

## Screenshot

<img src="docs/app.png" width="400" alt="App Screenshot">

## Roadmap

- Weitere medizinische Regeln und Plausibilitätsprüfungen für Berechnungen
- Optional: Export/Import von Profilen
- Optional: Cloud-Sync / Backup
- UI-Feinschliff für Calculate- und Settings-Screens

## Lizenz

Dieses Projekt steht unter der in `LICENSE` definierten Lizenz.
