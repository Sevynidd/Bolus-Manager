# Bolus Manager

![Android](https://img.shields.io/badge/Android-31%2B-3DDC84?logo=android&logoColor=white) ![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF?logo=kotlin&logoColor=white) ![Jetpack%20Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white) ![Material%203](https://img.shields.io/badge/Material%203-UI-6200EE) ![Room](https://img.shields.io/badge/Room-2.8.4-6D4C41) ![DataStore](https://img.shields.io/badge/DataStore-Preferences%201.2.1-1E88E5) ![AGP](https://img.shields.io/badge/AGP-9.2.1-34A853)

Android app built with Jetpack Compose for managing diabetes-relevant factors, time windows, and bolus calculations. The app combines persistent factor and time profiles with a localized UI, theme/contrast settings, and a configurable bread-unit factor.

## Table of Contents

- [Disclaimer](#disclaimer)
- [Current Status](#current-status)
- [Features](#features)
- [Data Persistence](#data-persistence)
- [Calculation Logic](#calculation-logic)
- [Validation & Input Behavior](#validation--input-behavior)
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

## Current Status

The project is in active development, but already has a working workflow for:

- Factors and basal rate
- Time-of-day schedule with a pie chart
- Persistent storage via Room and DataStore
- Bolus calculation for **Normal** and **Split Bolus**
- Localized UI in multiple languages

## Features

### Factors & Schedule

- A variable-length list of user-defined, freely renamable time-of-day
  factors — add, rename, or delete factors directly on the Factors screen
  (at least one must always remain)
  - New factors are added via a dialog that asks for a name and a start time
    (pre-filled with the midpoint of the schedule's largest free gap)
  - New installs are seeded with 7 default factors (Morning, Breakfast,
    Lunch, Afternoon, Dinner, Late, Night) in the device's language, which
    can then be freely edited like any other factor
- Basal rate as a separate field
- Read-only by default, editable via the edit icon in the top app bar
- Dedicated schedule screen with editable times via Material `TimePicker`
- Pie chart with colored time segments (cycling through a fixed palette for
  any number of factors), titles, and times
- Schedule times are automatically kept in ascending order, with no two
  factors sharing the same minute, whether editing an existing time or
  adding a new factor
- A hint text on the schedule screen explains the automatic order correction
- Factor descriptions dynamically show their saved time ranges
- The factor whose time window is currently active is visually highlighted
  with a "Now" badge
- **Period** toggle: scales every active factor up by a configurable percentage
  (set in Settings) while enabled

### Calculate

- Tab selection between **Normal** and **Split Bolus**
- **Normal mode**:
  - Carbohydrate input
  - Automatic, real-time unit calculation
  - Displays the currently active factor, including its name
- **Split bolus mode**:
  - Carbohydrate input
  - Immediate-share input as a percentage
  - Automatic rest share (`100 - immediate share`)
  - Editable duration (default: `120` minutes)
  - Immediate share calculated with the currently active factor
  - Rest share calculated with the factor active at `now + duration`
  - Current and future factors displayed side by side
  - Immediate and delayed units displayed side by side
- Optional blood-sugar field, shown in both modes: when filled in, its
  correction units (see **Correction Dose** below) are automatically added
  into the calculated total; left blank, behavior is unchanged
- Configurable bread-unit value instead of a hardcoded divisor
- **Bolus templates**: save a name, optional emoji, and carbohydrate amount as a
  reusable shortcut
  - Reachable via the bookmark icon in the Calculate top app bar
  - Sortable by recently used or alphabetically
  - Names are unique regardless of casing/whitespace
  - Selecting a template prefills the carbohydrate field for the active tab and
    marks the template as recently used
  - Added and edited on a dedicated full-screen editor with a live emoji
    preview; emoji entry uses the device's own keyboard/emoji picker rather
    than a fixed, curated set of choices

### Settings

- Theme mode: `System`, `Light`, `Dark`
- Contrast level: `Normal`, `Medium`, `High`
- Language: `System`, `Deutsch`, `English`, `Français`, `Polski`
- **Factor settings** screen: **bread units** and the **Period** factor
  surcharge percentage, the two values that tune the bolus calculation itself
- **Correction** screen: the blood-sugar **correction threshold** and
  **correction step** (both configurable, defaulting to `160` mg/dl and
  `30` mg/dl), plus a toggle between **mg/dl** and **mmol/l** for entering
  and displaying blood-sugar values
- **Notifications** screen: optional daily **basal rate reminder** push
  notification, toggled off by default. Fires an exact alarm at the basal
  time configured on the Factors schedule screen, requesting the
  notification and exact-alarm permissions as needed, and reschedules
  itself for the next day (and after a device reboot)
- **App update** screen: checks the project's GitHub releases for a newer
  version, then downloads and installs the APK directly
  - Shows the currently installed version and, once checked, the latest
    release's tag and notes
  - Downloads the release's `.apk` asset via Android's `DownloadManager` with
    progress feedback, then hands it to the system package installer
  - Prompts the user to grant the "install unknown apps" permission if it
    hasn't been granted yet
  - Checks for an update automatically every time the app is opened; if one
    is available, posts a push notification whose action button reopens the
    app straight at this screen
- **Import & Export** screen: saves the current factor profile (correction
  factors, time windows, basal rate) to a JSON file via the system file
  picker, or loads one from a previously exported file, immediately applying
  it like a manual edit
- Animated navigation within settings

### UI & Navigation

- Adaptive navigation with `Factors`, `Calculate`, and `Settings`
- Material icons instead of drawable icons
- Material 3 UI with light/dark and contrast variants
- Multilingual text via a central translation function

## Data Persistence

### Room

Stored in `diabetes_app.db`, table `factor_profile`:

- Basal rate and its schedule time
- Whether the Period surcharge is enabled
- Whether the basal rate reminder notification is enabled

Table `factor_slot` — one row per user-defined factor, referenced nowhere
else, so it's simply replaced in full (delete-all, then insert) whenever the
factor list is saved:

- Name (free text, user-editable)
- Factor value
- Start time (minutes since midnight)

Table `bolus_template`:

- Name (plus a normalized, case/whitespace-insensitive copy for uniqueness)
- Optional emoji
- Carbohydrate amount
- Last-used timestamp, for "recently used" sorting

### DataStore Preferences

The following are persisted:

- Theme mode
- Contrast level
- Language
- Bread-unit value
- Period factor surcharge percentage
- Blood-sugar correction threshold and step (stored in mg/dl)
- Blood-glucose display unit (mg/dl or mmol/l)

### Edit Session / Save Behavior

Factor editing runs through `FactorEditSessionViewModel` + `SavedStateHandle`.

Auto-save is triggered on:

- Clicking Save (check icon)
- Leaving the `Factors` tab
- The app going to the background (`ON_STOP`)

Configuration changes (e.g. rotation) do not incorrectly trigger the background save (`isChangingConfigurations`).

### In-App Updates

The Settings → App update screen queries
`https://api.github.com/repos/Sevynidd/Bolus-Manager/releases/latest` and
compares its tag against the installed `versionName`. If the release
publishes a `.apk` asset and its version is newer, the user can download and
install it without leaving the app. This requires the `INTERNET` and
`REQUEST_INSTALL_PACKAGES` permissions and a `FileProvider` (declared in
`AndroidManifest.xml`) to hand the downloaded file to the system installer.

## Calculation Logic

### Normal Bolus

The calculated units are based on:

- Entered carbohydrates
- Currently active factor
- Configured bread-unit value

Formula:

`Units = (Carbohydrates / BreadUnits) * Factor`

### Split Bolus

The split bolus divides the carbohydrates into two shares:

- **Immediate share**, using the currently active factor
- **Delayed share**, using the factor active at `now + duration`

This lets the second share account for a different time window than the first.

### Period Surcharge

When the Period toggle is enabled, every resolved factor (immediate and, in
split-bolus mode, the delayed one) is scaled up by the configured percentage
before the unit calculation:

`EffectiveFactor = Factor * (1 + PeriodFactorPercent / 100)`

A negative configured percentage is treated as `0`.

### Correction Dose

When a blood-sugar value is entered, whole units of correction insulin are
added on top of the carbohydrate-based calculation: the number of configured
steps the blood sugar exceeds the configured threshold, rounded to the
nearest whole unit (never a fractional unit).

`CorrectionUnits = round(max(0, BloodSugar - Threshold) / Step)`

Threshold and step are always stored in mg/dl, regardless of which unit
(mg/dl or mmol/l) is currently selected for display/entry — the selected
unit only affects how the number is shown and typed, never the stored value
or the calculation itself. With the defaults (`160` mg/dl threshold, `30`
mg/dl step), a blood sugar of `160` or `170` mg/dl adds `0` units, `180` or
`190` mg/dl adds `1` unit, and `220` mg/dl adds `2` units.

## Validation & Input Behavior

- Decimal values are entered and displayed with a comma (e.g. `1,25`)
- Factor fields are rounded up to the nearest `0.25` step on blur/commit
- Basal rate is rounded up to the nearest even number on blur/commit
- Fields normalize their values when edit mode ends, so focused inputs are still saved correctly
- Schedule times are automatically corrected to keep a valid order
- The split-bolus immediate share is capped at `100`
- The rest share is calculated automatically and therefore can never exceed `100`
- A negative Period factor surcharge percentage is treated as `0`
- Bolus template names must be unique (case/whitespace-insensitive); saving a
  duplicate name is rejected with an inline error

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
