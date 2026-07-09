# Bolus Manager

![Android](https://img.shields.io/badge/Android-31%2B-3DDC84?logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF?logo=kotlin&logoColor=white) ![Jetpack%20Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white) ![Material%203](https://img.shields.io/badge/Material%203-UI-6200EE) ![Room](https://img.shields.io/badge/Room-2.8.4-6D4C41) ![DataStore](https://img.shields.io/badge/DataStore-Preferences%201.2.1-1E88E5) ![AGP](https://img.shields.io/badge/AGP-9.2.1-34A853)

An Android app that helps you plan insulin bolus doses. It keeps track of your personal correction factors and time-of-day schedule, calculates normal and split boluses as you type, and lets you save frequently used carb amounts as quick shortcuts.

## Table of Contents

- [Disclaimer](#disclaimer)
- [Features](#features)
- [Calculation Logic](#calculation-logic)
- [Tech Stack](#tech-stack)
- [Requirements](#requirements)
- [Installation & Getting Started](#installation--getting-started)
- [Screenshot](#screenshot)
- [License](#license)

## Disclaimer

This is a hobby project and **not a certified medical device or medical
software**. It is not reviewed, validated, or approved by any regulatory or
medical authority. The calculations and data shown by this app must not be
used as the sole basis for insulin dosing or any other medical decision. Use
it entirely at your own risk; always verify results with your treatment plan
and consult your doctor or diabetes care team before acting on them.

## Features

### Factors & Schedule

- Keep a personal list of correction factors, each tied to a time of day, plus your basal rate
- A pie chart gives you an at-a-glance overview of your daily schedule
- The factor currently in effect is highlighted with a "Now" badge
- A **Period** toggle temporarily scales up all factors by a percentage you set

### Calculate

- **Normal** and **Split Bolus** modes, calculated automatically as you type
- Split bolus splits your carbs into an immediate and a delayed share, using the right factor for each
- An optional blood-sugar field adds a correction dose to the total automatically
- Uses your configured bread-unit value
- Save frequently used carb amounts as named, emoji-tagged **templates** for one-tap reuse

### Settings

- Light, dark, or system theme, with adjustable contrast
- Available in German, English, French, and Polish (or follow your system language)
- Tune the bread-unit value, Period surcharge, and blood-sugar correction thresholds to your needs, in either mg/dl or mmol/l
- Optional daily reminder notification for your basal rate
- Check for and install app updates directly from within the app
- Export your factor profile to a file and import it again later, e.g. to move to a new device

### UI & Navigation

- Adaptive navigation between Factors, Calculate, and Settings
- Clean Material 3 design that adapts to phone and tablet layouts

## Calculation Logic

### Normal Bolus

Your dose is calculated from the carbs you enter, the factor currently
active on your schedule, and your configured bread-unit value:

`Units = (Carbohydrates / BreadUnits) * Factor`

### Split Bolus

Split bolus divides your carbs into two shares: an **immediate share**,
calculated with the factor active right now, and a **delayed share**,
calculated with the factor that will be active once the delay has passed.
This lets the delayed share account for a different time window than the
immediate one.

### Period Surcharge

When the Period toggle is enabled, every factor used in a calculation is
scaled up by the percentage you've configured before the dose is worked out:

`EffectiveFactor = Factor * (1 + PeriodFactorPercent / 100)`

### Correction Dose

When you enter a blood-sugar value, extra correction insulin is added on
top of the carbohydrate-based dose: one unit for each configured step your
blood sugar is above the configured threshold, rounded to the nearest whole
unit. With the defaults (`160` mg/dl threshold, `30` mg/dl step), a blood
sugar of `170` mg/dl adds `0` units, `180` mg/dl adds `1` unit, and `220`
mg/dl adds `2` units.

## Tech Stack

- Kotlin `2.4.0`
- Android Gradle Plugin `9.2.1`
- Jetpack Compose BOM `2026.06.01`
- Material 3
- Material 3 Adaptive Navigation Suite
- Material Icons Extended
- Room `2.8.4`
- DataStore Preferences `1.2.1`
- Lifecycle Compose + ViewModel Compose
- KSP
- JUnit / AndroidX Test

## Requirements

- Android Studio (current stable version)
- JDK 11+
- Android SDK (`compileSdk 36`, `minSdk 31`, `targetSdk 36`)

## Installation & Getting Started

1. Clone the repository
2. Open the project in Android Studio
3. Run a Gradle sync
4. Launch the app on an emulator or device

Optional CLI build:

```powershell
Set-Location "<repo-path>"
.\gradlew.bat :app:compileDebugKotlin --console=plain
```

## Screenshot

<img src="docs/app.png" width="400" alt="App Screenshot">

## License

This project is licensed under the terms defined in `LICENSE`.
