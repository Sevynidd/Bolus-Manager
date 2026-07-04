# AGENTS.md

## Project

Bolus Manager — Android app (Kotlin, Jetpack Compose, Material 3, Room, DataStore)
for managing diabetes-relevant factors, time windows, and bolus calculations.

## Build & Test

- Build: `./gradlew assembleDebug`
- Unit tests: `./gradlew testDebugUnitTest`
- Instrumented tests: `./gradlew connectedDebugAndroidTest`
- Lint: `./gradlew lint`
Run unit tests after every change to `data/` or calculation logic before
considering a task done. Do not mark a task complete if `testDebugUnitTest` fails.

## Static Analysis (Detekt)

- Run locally: `./gradlew detekt`. Config lives at `config/detekt/detekt.yml`
  (builds upon Detekt's default rule set — only deviations are listed there,
  e.g. `FunctionNaming` ignores `@Composable` functions since PascalCase is
  the Compose convention, not a naming violation).
- The build **fails** on any new finding not already in
  `config/detekt/baseline.xml` (`maxIssues: 0`). The baseline grandfathers in
  findings that existed when Detekt was introduced — don't add new entries to
  it to silence a violation; fix the code instead. Paying down an existing
  baseline entry while touching that code is welcome but not required.
- Keep new/changed Kotlin under Detekt's default thresholds: short functions
  (`LongMethod`) with few parameters (`LongParameterList`), low cyclomatic
  complexity (`ComplexMethod`, `LargeClass`), named constants instead of
  magic numbers other than `0`/`1`/`-1` (`MagicNumber`), no wildcard imports,
  no catching/throwing generic `Exception`/`Throwable`.
- Detekt also runs in CI (`.github/workflows/detekt.yml`) on every push/PR to
  `main` and weekly, uploading results to the repo's Security → Code scanning
  tab. That CI run is a separate, non-blocking SARIF scan
  (`continue-on-error: true`) — the local `./gradlew detekt` run above is what
  actually gates the build.
- Run `./gradlew detekt` after changes to Kotlin source, same as unit tests;
  do not mark a task complete if it fails.

## Language

- All code, identifiers, comments, commit messages, and internal docs are
  written in English — regardless of which language the UI is localized into.
  Only user-facing strings in `localization/` may be non-English.
- UI strings go through the existing localization mechanism in
  `localization/` — no hardcoded German/English strings in Composables.

## Architecture

- Layers: `data` (Room entities/DAOs, DataStore, repositories) → domain/calculation
  logic (plain Kotlin, no Android imports) → `screens`/`ui` (Compose, stateless
  where possible) → `navigation`.
- State flows one direction: ViewModel/state holder → Composable. Composables must
  not read from Room/DataStore directly — always go through a repository or
  state holder.
- Bolus/factor calculation logic lives in plain Kotlin classes/functions with no
  `android.*` or Compose imports. This is non-negotiable: it's what makes the
  calculation testable without an emulator.
- Every top-level screen Composable (the ones wired into `navigation`/`MainWindow`)
  keeps a `@Preview` (or `@PreviewScreenSizes`) next to it, called with representative
  default parameter values, so the screen can always be inspected in Android Studio
  without running the app. Add the preview in the same commit that adds the screen;
  keep it working (update its args) whenever the screen's parameters change.

## Data / Persistence

- Room schema changes go through a proper migration, not ad-hoc column changes.
- Schema design must respect normal forms (up to 3NF unless there's a documented
  performance reason not to): no repeating groups, every non-key column depends
  on the whole key, no column depends on another non-key column. If a table
  stores the same fact in two places (e.g. a derived factor value duplicated
  across day-schedule rows), that's a modeling bug, not a shortcut.

## Testability

- Every function that contains a decision or a calculation (factor lookup,
  split-bolus math, time-window resolution) must be a pure function: same input
  → same output, no hidden state, no side effects — and must have a unit test.
- If a function needs `Context`, `Clock`, or the current time to be tested,
  inject it (pass `Clock`/`LocalTime` as a parameter) rather than calling
  `System.currentTimeMillis()`/`LocalTime.now()` inline.
- Favor small functions over large ones specifically because small functions are
  easy to unit test; if you can't describe a function's test cases in one
  sentence, split it.
- New calculation logic ships with tests covering: normal case, boundary of a
  time window, and an invalid/edge input (e.g. 0% immediate split, negative
  carbs).

## KISS / YAGNI / DRY

- Solve the task in front of you. Don't add configuration hooks, strategy
  patterns, or abstraction layers for a second use case that doesn't exist yet.
- Duplication across 2 call sites is fine; extract a shared function only once
  a third real use case appears (rule of three).
- Prefer Kotlin standard library and existing Compose/Room idioms over new
  abstractions or helper libraries.
- No speculative feature flags, no "just in case" nullable fields.

## Readability & Comments

- Names should make comments unnecessary: `calculateDelayedSplitUnits(...)` not
  `calc2(...)`.
- Comment only the non-obvious: *why* a formula uses a specific rounding rule,
  *why* a migration keeps an old column, a workaround for a Compose/Room quirk.
  Never comment *what* the code does if the code already says it.
- No commented-out code, no TODO without an owner or issue reference.
- Keep functions short enough to read without scrolling; if a Composable body
  exceeds ~40 lines, extract sub-composables.

## Documentation

- `README.md` must stay in sync with the code. Whenever a change adds,
  removes, or alters user-facing behavior (features, screens, settings,
  calculation rules, persisted data, tech-stack versions), update the
  matching README section (`Features`, `Datenhaltung & Persistenz`,
  `Berechnungslogik`, `Validierung & Eingabeverhalten`, `Tech-Stack`)
  in the same change. Do not mark a task complete if the README still
  describes the old behavior.
- README should be in English, matching the code and comments (see `Language`).
- Every public class, object, and function outside of `screens`/`ui` (i.e.
  `data`, domain/calculation logic, repositories, DAOs) gets a KDoc comment
  (`/** ... */`) stating what it represents or computes — not how, the code
  already shows how. Composables and other UI-layer functions are exempt
  unless they expose a non-obvious contract (e.g. a Composable that mutates
  shared state as a side effect).
- KDoc on a calculation function documents its formula/rule and any
  non-obvious rounding or clamping behavior (e.g. the `0,25`-step rounding
  and `100`% split-bolus cap from `Validierung & Eingabeverhalten`) so the
  contract is visible without cross-referencing the README.
- Don't restate the function signature in prose (no "returns a Boolean"); say
  what the value/state means.
- Keep KDoc in sync with the code it documents in the same commit — stale
  KDoc is worse than none.

## Definition of Done

- Compiles, unit tests pass, new/changed calculation logic has tests, no
  hardcoded strings, no dead/commented-out code left behind.
- `README.md` reflects the change, and touched public declarations in
  `data`/domain/calculation code have up-to-date KDoc (see `Documentation`).
- Every screen Composable still has a working `@Preview` (see `Architecture`).
