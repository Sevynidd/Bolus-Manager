package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.FactorEditKind
import sevynidd.diabetesapp.calculation.MIN_FACTOR_COUNT
import sevynidd.diabetesapp.calculation.isFactorNameValid
import sevynidd.diabetesapp.calculation.normalizeBasalRateValue
import sevynidd.diabetesapp.calculation.normalizeHourlyBasalRateValue
import sevynidd.diabetesapp.calculation.pumpBasalHourRangeLabel
import sevynidd.diabetesapp.calculation.withEdit
import sevynidd.diabetesapp.calculation.withoutSlot
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton

private val IntegerInputRegex = Regex("^\\d*$")

/**
 * What [FactorEditSheet] edits: a [title], an optional window [name] (null when the value has none),
 * the current display [value], and how typed text becomes the stored value ([toStoredValue]).
 */
internal data class FactorSheetSpec(
    val title: String,
    val name: String?,
    val otherNames: List<String>,
    val value: String,
    val valueLabel: String,
    val helpKey: TranslationKey,
    val isInteger: Boolean,
    val toStoredValue: (String) -> String
)

/** What the editor does with the user's input: save it, or delete the window (when deleting is allowed). */
internal data class FactorSheetActions(
    val onSave: (name: String, storedValue: String) -> Unit,
    val onDelete: (() -> Unit)?,
    val onDismiss: () -> Unit
)

/**
 * A bottom sheet for changing one factor, the basal rate, or one pump hour. Nothing is applied until
 * the user taps Save, so a half-typed value never reaches the schedule.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FactorEditSheet(
    currentLanguage: AppLanguage,
    spec: FactorSheetSpec,
    actions: FactorSheetActions
) {
    var name by rememberSaveable { mutableStateOf(spec.name.orEmpty()) }
    var value by rememberSaveable { mutableStateOf(spec.value) }
    val isNameValid = spec.name == null || isFactorNameValid(name, spec.otherNames)

    ModalBottomSheet(
        onDismissRequest = actions.onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = spec.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                HelpIconButton(helpTextKey = spec.helpKey, currentLanguage = currentLanguage)
            }

            if (spec.name != null) {
                FactorNameField(currentLanguage, name, isNameValid) { name = it }
            }
            FactorValueField(spec, value) { value = it }

            SheetButtons(currentLanguage, actions, isSaveEnabled = isNameValid) {
                actions.onSave(name, spec.toStoredValue(value))
            }
        }
    }
}

@Composable
private fun FactorNameField(
    currentLanguage: AppLanguage,
    name: String,
    isNameValid: Boolean,
    onNameChange: (String) -> Unit
) {
    val showDuplicateError = name.isNotBlank() && !isNameValid
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text(translate(TranslationKey.FactorNameLabel, currentLanguage)) },
        singleLine = true,
        isError = showDuplicateError,
        supportingText = {
            if (showDuplicateError) {
                Text(translate(TranslationKey.FactorNameDuplicateError, currentLanguage))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FactorValueField(spec: FactorSheetSpec, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { typed ->
            val sanitized = if (spec.isInteger) typed else typed.replace('.', ',')
            val allowed = if (spec.isInteger) IntegerInputRegex else DecimalInputRegex
            val matches = allowed.matches(sanitized)
            if (matches) onValueChange(sanitized)
        },
        label = { Text(spec.valueLabel) },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (spec.isInteger) KeyboardType.Number else KeyboardType.Decimal
        ),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SheetButtons(
    currentLanguage: AppLanguage,
    actions: FactorSheetActions,
    isSaveEnabled: Boolean,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        actions.onDelete?.let { onDelete ->
            TextButton(onClick = onDelete) {
                Text(
                    text = translate(TranslationKey.ActionDelete, currentLanguage),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = actions.onDismiss) {
            Text(translate(TranslationKey.ActionCancel, currentLanguage))
        }
        Button(onClick = onSaveClick, enabled = isSaveEnabled) {
            Text(translate(TranslationKey.ActionSave, currentLanguage))
        }
    }
}

/**
 * Opens [FactorEditSheet] for the [kind] value at [index] of [state]'s factors, saving through
 * [FactorScreenState.onFactorsChange]. Closes straight away if that value no longer exists.
 */
@Composable
internal fun FactorEditSheetHost(
    currentLanguage: AppLanguage,
    state: FactorScreenState,
    kind: FactorEditKind,
    index: Int,
    onClose: () -> Unit
) {
    val spec = buildFactorSheetSpec(kind, index, state, currentLanguage)
    if (spec == null) {
        LaunchedEffect(Unit) { onClose() }
        return
    }

    val canDelete = kind == FactorEditKind.Slot && state.factors.factorSlots.size > MIN_FACTOR_COUNT
    FactorEditSheet(
        currentLanguage = currentLanguage,
        spec = spec,
        actions = FactorSheetActions(
            onSave = { name, storedValue ->
                state.onFactorsChange { it.withEdit(kind, index, name, storedValue) }
                onClose()
            },
            onDelete = {
                state.onFactorsChange { it.copy(factorSlots = it.factorSlots.withoutSlot(index)) }
                onClose()
            }.takeIf { canDelete },
            onDismiss = onClose
        )
    )
}

private fun buildFactorSheetSpec(
    kind: FactorEditKind,
    index: Int,
    state: FactorScreenState,
    language: AppLanguage
): FactorSheetSpec? = when (kind) {
    FactorEditKind.Slot -> slotSheetSpec(index, state, language)
    FactorEditKind.Basal -> FactorSheetSpec(
        title = translate(TranslationKey.BasalRate, language),
        name = null,
        otherNames = emptyList(),
        value = state.factors.basalRate,
        valueLabel = translate(TranslationKey.BasalRate, language),
        helpKey = TranslationKey.BasalRateHelp,
        isInteger = true,
        toStoredValue = ::normalizeBasalRateValue
    )
    FactorEditKind.PumpHour -> state.factors.pumpBasalRates.getOrNull(index)?.let { rate ->
        FactorSheetSpec(
            title = pumpBasalHourRangeLabel(index),
            name = null,
            otherNames = emptyList(),
            value = rate,
            valueLabel = translate(TranslationKey.PumpBasalRateUnit, language),
            helpKey = TranslationKey.PumpBasalRatesHelp,
            isInteger = false,
            toStoredValue = ::normalizeHourlyBasalRateValue
        )
    }
}

private fun slotSheetSpec(index: Int, state: FactorScreenState, language: AppLanguage): FactorSheetSpec? {
    val slots = state.factors.factorSlots
    val slot = slots.getOrNull(index) ?: return null
    val format = state.displayFormat
    val shownValue = format.toDisplayText(slot.factorValue)
    return FactorSheetSpec(
        title = slot.name,
        name = slot.name,
        otherNames = slots.filterIndexed { slotIndex, _ -> slotIndex != index }.map { it.name },
        value = shownValue,
        valueLabel = translate(format.labelKey, language),
        helpKey = format.valueHelpKey,
        isInteger = false,
        // An untouched value stays as stored: re-rounding would e.g. snap a pump ratio onto the 0.25 grid.
        toStoredValue = { typed ->
            if (typed == shownValue) slot.factorValue else format.toFactorText(format.normalizeInput(typed))
        }
    )
}
