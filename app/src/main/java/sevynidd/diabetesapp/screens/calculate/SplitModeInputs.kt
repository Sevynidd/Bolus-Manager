package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.SplitBolusResult
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton

/** The user-editable fields of the split-bolus form, bundled to keep composable parameter lists short. */
internal data class SplitModeInputs(
    val carbohydrates: String,
    val onCarbohydratesChange: (String) -> Unit,
    val bloodSugar: String,
    val onBloodSugarChange: (String) -> Unit,
    val glucoseUnit: GlucoseUnit,
    val immediatePercent: String,
    val onImmediatePercentChange: (String) -> Unit,
    val restPercentValue: Int?,
    val durationMinutes: String,
    val onDurationMinutesChange: (String) -> Unit
)

@Composable
internal fun SplitInputsCard(
    inputs: SplitModeInputs,
    currentLanguage: AppLanguage,
    focusManager: FocusManager
) {
    val splitCarbohydratesRequester = remember { FocusRequester() }
    val splitBloodSugarRequester = remember { FocusRequester() }
    val splitImmediatePercentRequester = remember { FocusRequester() }
    val splitDurationRequester = remember { FocusRequester() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SplitCarbohydratesAndBloodSugarFields(
                inputs = inputs,
                currentLanguage = currentLanguage,
                focus = SplitCarbsAndBloodSugarFocus(
                    carbohydratesRequester = splitCarbohydratesRequester,
                    bloodSugarRequester = splitBloodSugarRequester,
                    onCarbohydratesNext = { splitBloodSugarRequester.requestFocus() },
                    onBloodSugarNext = { splitImmediatePercentRequester.requestFocus() }
                )
            )

            SplitPercentRow(
                inputs = inputs,
                currentLanguage = currentLanguage,
                immediatePercentRequester = splitImmediatePercentRequester,
                onImmediatePercentNext = { splitDurationRequester.requestFocus() }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                EditableNumberField(
                    value = inputs.durationMinutes,
                    onValueChange = inputs.onDurationMinutesChange,
                    label = translate(TranslationKey.BolusDurationMinutes, currentLanguage),
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) }),
                    sanitizeInput = { rawInput ->
                        val sanitized = rawInput.replace('.', ',')
                        if (sanitized.isEmpty() || sanitized.matches(DecimalInputRegex)) sanitized else null
                    },
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(splitDurationRequester)
                )
                HelpIconButton(
                    helpTextKey = TranslationKey.BolusDurationMinutesHelp,
                    currentLanguage = currentLanguage
                )
            }
        }
    }
}

/** Focus targets/callbacks for [SplitCarbohydratesAndBloodSugarFields], keeping its parameter list short. */
private data class SplitCarbsAndBloodSugarFocus(
    val carbohydratesRequester: FocusRequester,
    val bloodSugarRequester: FocusRequester,
    val onCarbohydratesNext: () -> Unit,
    val onBloodSugarNext: () -> Unit
)

@Composable
private fun SplitCarbohydratesAndBloodSugarFields(
    inputs: SplitModeInputs,
    currentLanguage: AppLanguage,
    focus: SplitCarbsAndBloodSugarFocus
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        EditableNumberField(
            value = inputs.carbohydrates,
            onValueChange = inputs.onCarbohydratesChange,
            label = translate(TranslationKey.Carbohydrates, currentLanguage),
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(onNext = { focus.onCarbohydratesNext() }),
            sanitizeInput = { rawInput ->
                val sanitized = rawInput.replace('.', ',')
                if (sanitized.isEmpty() || sanitized.matches(DecimalInputRegex)) sanitized else null
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focus.carbohydratesRequester)
        )
        HelpIconButton(helpTextKey = TranslationKey.CarbohydratesHelp, currentLanguage = currentLanguage)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        EditableNumberField(
            value = inputs.bloodSugar,
            onValueChange = inputs.onBloodSugarChange,
            label = splitBloodSugarLabel(currentLanguage, inputs.glucoseUnit),
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(onNext = { focus.onBloodSugarNext() }),
            sanitizeInput = { rawInput ->
                val sanitized = rawInput.replace('.', ',')
                if (sanitized.isEmpty() || sanitized.matches(DecimalInputRegex)) sanitized else null
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focus.bloodSugarRequester)
        )
        HelpIconButton(helpTextKey = TranslationKey.BloodSugarHelp, currentLanguage = currentLanguage)
    }
}

@Composable
private fun SplitPercentRow(
    inputs: SplitModeInputs,
    currentLanguage: AppLanguage,
    immediatePercentRequester: FocusRequester,
    onImmediatePercentNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EditableNumberField(
            value = inputs.immediatePercent,
            onValueChange = inputs.onImmediatePercentChange,
            label = translate(TranslationKey.BolusImmediatePercent, currentLanguage),
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
            keyboardActions = KeyboardActions(onNext = { onImmediatePercentNext() }),
            sanitizeInput = { rawInput -> sanitizePercentageInput(rawInput) },
            modifier = Modifier
                .weight(1f)
                .focusRequester(immediatePercentRequester)
        )

        OutlinedTextField(
            value = inputs.restPercentValue?.toString().orEmpty(),
            onValueChange = {},
            label = { Text(translate(TranslationKey.BolusExtendedPercent, currentLanguage)) },
            readOnly = true,
            singleLine = true,
            modifier = Modifier.weight(1f)
        )

        HelpIconButton(helpTextKey = TranslationKey.BolusImmediatePercentHelp, currentLanguage = currentLanguage)
    }
}

/** The values shown on [SplitResultsCard], bundled to keep the composable's parameter list short. */
internal data class SplitModeResults(
    val activeFactorText: String,
    val futureFactorText: String,
    val splitBolus: SplitBolusResult?,
    val totalUnitsText: String,
    val correctionUnitsText: String
)

@Composable
internal fun SplitResultsCard(
    results: SplitModeResults,
    currentLanguage: AppLanguage
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = translate(TranslationKey.Calculated, currentLanguage),
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultStat(
                    label = translate(TranslationKey.ActiveFactor, currentLanguage),
                    value = results.activeFactorText,
                    modifier = Modifier.weight(1f)
                )

                ResultStat(
                    label = translate(TranslationKey.FutureFactor, currentLanguage),
                    value = results.futureFactorText,
                    modifier = Modifier.weight(1f)
                )
            }

            ResultStat(
                label = translate(TranslationKey.CalculatedUnits, currentLanguage),
                value = results.totalUnitsText,
                modifier = Modifier.fillMaxWidth(),
                emphasize = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultStat(
                    label = translate(TranslationKey.BolusImmediateUnits, currentLanguage),
                    value = results.splitBolus?.immediateUnits.toUiDecimalOrEmpty(),
                    modifier = Modifier.weight(1f)
                )

                ResultStat(
                    label = translate(TranslationKey.BolusExtendedUnits, currentLanguage),
                    value = results.splitBolus?.restUnits.toUiDecimalOrEmpty(),
                    modifier = Modifier.weight(1f)
                )
            }

            ResultStat(
                label = translate(TranslationKey.CorrectionUnits, currentLanguage),
                value = results.correctionUnitsText
            )
        }
    }
}

private fun splitBloodSugarLabel(currentLanguage: AppLanguage, glucoseUnit: GlucoseUnit): String {
    val unitSuffix = when (glucoseUnit) {
        GlucoseUnit.MgDl -> "mg/dl"
        GlucoseUnit.MmolL -> "mmol/l"
    }
    return "${translate(TranslationKey.BloodSugar, currentLanguage)} ($unitSuffix)"
}

private fun sanitizePercentageInput(input: String): String? {
    if (input.isEmpty()) return ""
    if (!input.matches(PercentageInputRegex)) return null

    return input.toIntOrNull()?.coerceAtMost(100)?.toString() ?: input
}

private val PercentageInputRegex = Regex("^\\d{0,3}$")
