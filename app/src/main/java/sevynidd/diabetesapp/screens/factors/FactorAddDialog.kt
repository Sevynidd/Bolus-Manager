package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.normalizeQuarterStepValue
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
import java.util.Locale

private const val MINUTES_PER_HOUR = 60

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddFactorDialog(
    currentLanguage: AppLanguage,
    existingNames: List<String>,
    initialTimeMinutes: Int,
    onDismissRequest: () -> Unit,
    onConfirm: (name: String, factorValue: String, timeMinutes: Int) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var factorValue by rememberSaveable { mutableStateOf("") }
    val pickerState = rememberTimePickerState(
        initialHour = (initialTimeMinutes / MINUTES_PER_HOUR) % HOURS_PER_DAY,
        initialMinute = initialTimeMinutes % MINUTES_PER_HOUR,
        is24Hour = true
    )

    val normalizedExisting = remember(existingNames) { existingNames.map { it.trim().lowercase(Locale.ROOT) } }
    val hasDuplicateName = name.trim().isNotEmpty() && normalizedExisting.contains(name.trim().lowercase(Locale.ROOT))
    val isValid = name.trim().isNotEmpty() && !hasDuplicateName

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(translate(TranslationKey.ActionAddFactor, currentLanguage)) },
        text = {
            AddFactorDialogFields(
                currentLanguage = currentLanguage,
                nameField = NameFieldState(
                    name = name,
                    onNameChange = { name = it },
                    hasDuplicateName = hasDuplicateName
                ),
                factorValue = factorValue,
                onFactorValueChange = { newValue ->
                    val sanitizedValue = newValue.replace('.', ',')
                    if (sanitizedValue.isEmpty() || sanitizedValue.matches(DecimalInputRegex)) {
                        factorValue = sanitizedValue
                    }
                },
                pickerState = pickerState
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val normalizedValue = normalizeQuarterStepValue(factorValue)
                    val timeMinutes = (pickerState.hour * MINUTES_PER_HOUR) + pickerState.minute
                    onConfirm(name.trim(), normalizedValue, timeMinutes)
                },
                enabled = isValid
            ) {
                Text(translate(TranslationKey.ActionAddFactor, currentLanguage))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(translate(TranslationKey.ActionCancel, currentLanguage))
            }
        }
    )
}

/** The name field's state for [AddFactorDialogFields], grouped to keep its parameter list short. */
private data class NameFieldState(
    val name: String,
    val onNameChange: (String) -> Unit,
    val hasDuplicateName: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddFactorDialogFields(
    currentLanguage: AppLanguage,
    nameField: NameFieldState,
    factorValue: String,
    onFactorValueChange: (String) -> Unit,
    pickerState: TimePickerState
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = nameField.name,
                onValueChange = nameField.onNameChange,
                label = { Text(translate(TranslationKey.FactorNameLabel, currentLanguage)) },
                singleLine = true,
                isError = nameField.hasDuplicateName,
                supportingText = {
                    if (nameField.hasDuplicateName) {
                        Text(translate(TranslationKey.FactorNameDuplicateError, currentLanguage))
                    }
                },
                modifier = Modifier.weight(1f)
            )
            HelpIconButton(helpTextKey = TranslationKey.FactorNameHelp, currentLanguage = currentLanguage)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = factorValue,
                onValueChange = onFactorValueChange,
                label = { Text(translate(TranslationKey.LabelFactor, currentLanguage)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            HelpIconButton(helpTextKey = TranslationKey.FactorValueHelp, currentLanguage = currentLanguage)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = translate(TranslationKey.FactorStartTime, currentLanguage),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )
            HelpIconButton(helpTextKey = TranslationKey.FactorStartTimeHelp, currentLanguage = currentLanguage)
        }
        TimePicker(state = pickerState)
    }
}

private const val HOURS_PER_DAY = 24
