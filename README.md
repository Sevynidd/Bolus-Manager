# Diabetes App

![Android](https://img.shields.io/badge/Android-31%2B-3DDC84?logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-7F52FF?logo=kotlin&logoColor=white) ![Jetpack%20Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white) ![Material%203](https://img.shields.io/badge/Material%203-UI-6200EE) ![Room](https://img.shields.io/badge/Room-2.8.4-6D4C41) ![DataStore](https://img.shields.io/badge/DataStore-Preferences-1E88E5) ![AGP](https://img.shields.io/badge/AGP-9.1.0-34A853)

Android-App auf Basis von Jetpack Compose zur Verwaltung diabetesrelevanter Faktoren, mit persistenter Speicherung, lokalisierter UI (DE/EN/System) und Theme-/Kontrast-Einstellungen.

## Inhaltsverzeichnis

- [Aktueller Stand](#aktueller-stand)
- [Features](#features)
- [Datenhaltung & Persistenz](#datenhaltung--persistenz)
- [Validierung & Eingabeverhalten](#validierung--eingabeverhalten)
- [Tech-Stack](#tech-stack)
- [Projektstruktur (Auszug)](#projektstruktur-auszug)
- [Voraussetzungen](#voraussetzungen)
- [Installation und Start](#installation-und-start)
- [Screenshot](#screenshot)
- [Roadmap](#roadmap)
- [Lizenz](#lizenz)

## Aktueller Stand

Das Projekt befindet sich in aktiver Entwicklung. Der Faktoren-Workflow inklusive Bearbeitungsmodus, Persistenz und Rotation-/Hintergrund-Verhalten ist umgesetzt.

## Features

- 7 Tages-Faktoren: Morning, Breakfast, Lunch, Afternoon, Dinner, Late, Night
- Basalrate als separates Feld
- Read-only Standardmodus, Bearbeitung per Edit-Icon in der TopAppBar
- Adaptive Navigation mit `Factors`, `Calculate` und `Settings`
- Animierte Navigation innerhalb der Einstellungen (`Theme` / `Language`)
- Theme-Modus: `System`, `Light`, `Dark`
- Kontraststufe: `Normal`, `Medium`, `High`
- Sprache: `System`, `Deutsch`, `English`
- Material Icons (ohne Drawable-Icons)

## Datenhaltung & Persistenz

- Faktoren + Basalrate werden in Room gespeichert (`diabetes_app.db`, Tabelle `factor_profile`)
- App-Einstellungen (Theme/Kontrast/Sprache) werden über DataStore Preferences gespeichert
- Faktor-Bearbeitung läuft über `FactorEditSessionViewModel` + `SavedStateHandle`
- Auto-Save der Faktoren wird ausgelöst bei:
  - Klick auf Save (Check-Icon)
  - Verlassen des `Factors`-Tabs
  - App-Wechsel in den Hintergrund (`ON_STOP`)
- Bei Konfigurationsänderungen (z. B. Rotation) wird der Hintergrund-Save nicht fälschlich ausgelöst (`isChangingConfigurations`)

## Validierung & Eingabeverhalten

- Dezimalwerte werden mit Komma erfasst und angezeigt (z. B. `1,25`)
- Faktorfelder werden beim Verlassen/Commit auf den nächsten `0,25`-Schritt aufgerundet
- Basalrate wird beim Verlassen/Commit auf die nächste gerade Zahl aufgerundet
- Felder normalisieren ihre Werte beim Ende des Edit-Modus, damit auch fokussierte Eingaben korrekt gespeichert werden

## Tech-Stack

- Kotlin
- Android Gradle Plugin `9.1.0`
- Jetpack Compose + Material 3
- Material Icons Extended
- Room (`runtime`, `ktx`, `ksp` compiler)
- DataStore Preferences
- Lifecycle Compose + ViewModel Compose
- JUnit / AndroidX Test

## Projektstruktur (Auszug)

- `app/src/main/java/sevynidd/diabetesapp/MainActivity.kt` - App-Start, Theme-Anbindung, Flows für Settings/Faktoren
- `app/src/main/java/sevynidd/diabetesapp/screens/MainWindow.kt` - Scaffold, Navigation, Save-Trigger
- `app/src/main/java/sevynidd/diabetesapp/screens/FactorScreen.kt` - Faktor-UI + Eingabekomponenten
- `app/src/main/java/sevynidd/diabetesapp/screens/FactorEditSessionViewModel.kt` - Edit-Session-Status via `SavedStateHandle`
- `app/src/main/java/sevynidd/diabetesapp/data/database/` - Room (`DiabetesDatabase`, `FactorProfileEntity`, `FactorProfileDao`, `FactorsRepository`)
- `app/src/main/java/sevynidd/diabetesapp/data/AppSettingsStore.kt` - Persistenz von Theme/Kontrast/Sprache
- `app/src/main/java/sevynidd/diabetesapp/settings/` - Settings-Screens
- `app/src/main/java/sevynidd/diabetesapp/navigation/Navigation.kt` - Destinationen und Transitionen
- `app/src/main/java/sevynidd/diabetesapp/localization/Localization.kt` - Übersetzungslogik (DE/EN/System)

## Voraussetzungen

- Android Studio (aktuelle stabile Version)
- JDK 11+
- Android SDK (`compileSdk 36`, `minSdk 31`)

## Installation und Start

1. Repository klonen
2. Projekt in Android Studio öffnen
3. Gradle-Sync ausführen
4. App auf Emulator oder Gerät starten

## Screenshot

<img src="docs/app.png" width="400" alt="App Screenshot">

## Roadmap

- Implementierung der eigentlichen Berechnungslogik im `CalculateScreen`
- Erweiterung um zusätzliche medizinische Regeln/Checks
- Optional: Import/Export oder Cloud-Sync

## Lizenz

Dieses Projekt steht unter der in `LICENSE` definierten Lizenz.
