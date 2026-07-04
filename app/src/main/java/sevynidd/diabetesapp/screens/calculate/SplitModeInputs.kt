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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.SplitBolusResult
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

/** The user-editable fields of the split-bolus form, bundled to keep composable parameter lists short. */
internal data class SplitModeInputs(
    val carbohydrates: String,
    val onCarbohydratesChange: (String) -> Unit,
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
            EditableNumberField(
                value = inputs.carbohydrates,
                onValueChange = inputs.onCarbohydratesChange,
                label = translate(TranslationKey.Carbohydrates, currentLanguage),
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(onNext = { splitImmediatePercentRequester.requestFocus() }),
                sanitizeInput = { rawInput ->
                    val sanitized = rawInput.replace('.', ',')
                    if (sanitized.isEmpty() || sanitized.matches(DecimalInputRegex)) sanitized else null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(splitCarbohydratesRequester)
            )

            SplitPercentRow(
                inputs = inputs,
                currentLanguage = currentLanguage,
                immediatePercentRequester = splitImmediatePercentRequester,
                onImmediatePercentNext = { splitDurationRequester.requestFocus() }
            )

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
                    .fillMaxWidth()
                    .focusRequester(splitDurationRequester)
            )
        }
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
        horizontalArrangement = Arrangement.spacedBy(12.dp)
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
    }
}

@Composable
internal fun SplitResultsCard(
    activeFactorText: String,
    futureFactorText: String,
    splitBolus: SplitBolusResult?,
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
                    value = activeFactorText,
                    modifier = Modifier.weight(1f)
                )

                ResultStat(
                    label = translate(TranslationKey.FutureFactor, currentLanguage),
                    value = futureFactorText,
                    modifier = Modifier.weight(1f)
                )
            }

            ResultStat(
                label = translate(TranslationKey.CalculatedUnits, currentLanguage),
                value = splitBolus?.totalUnits.toUiDecimalOrEmpty(),
                modifier = Modifier.fillMaxWidth(),
                emphasize = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResultStat(
                    label = translate(TranslationKey.BolusImmediateUnits, currentLanguage),
                    value = splitBolus?.immediateUnits.toUiDecimalOrEmpty(),
                    modifier = Modifier.weight(1f)
                )

                ResultStat(
                    label = translate(TranslationKey.BolusExtendedUnits, currentLanguage),
                    value = splitBolus?.restUnits.toUiDecimalOrEmpty(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun sanitizePercentageInput(input: String): String? {
    if (input.isEmpty()) return ""
    if (!input.matches(PercentageInputRegex)) return null

    return input.toIntOrNull()?.coerceAtMost(100)?.toString() ?: input
}

private val PercentageInputRegex = Regex("^\\d{0,3}$")
