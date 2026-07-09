package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.MINUTES_PER_DAY
import sevynidd.diabetesapp.calculation.activeFactorForTime
import sevynidd.diabetesapp.calculation.formatTimeOfDay
import sevynidd.diabetesapp.calculation.normalizeBasalRateValue
import sevynidd.diabetesapp.calculation.normalizeQuarterStepValue
import sevynidd.diabetesapp.calculation.suggestedNewSlotTimeMinutes
import sevynidd.diabetesapp.calculation.withUpdatedTime
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.profile.Gender
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.LocalTime
import java.util.Locale

private const val MIN_FACTOR_COUNT = 1

@Composable
fun FactorScreen(
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    gender: Gender = Gender.PreferNotToSay,
    onFactorsChange: (FactorsData) -> Unit = {},
    onPeriodEnabledChange: (Boolean) -> Unit = {},
    now: LocalTime = LocalTime.now()
) {
    var isPeriodEnabled by rememberSaveable(factors.isPeriodEnabled) { mutableStateOf(factors.isPeriodEnabled) }
    var basalRate by rememberSaveable(factors.basalRate) { mutableStateOf(factors.basalRate) }
    var isAddDialogVisible by rememberSaveable { mutableStateOf(false) }

    fun emitFactorsChanged(updatedSlots: List<FactorSlot> = factors.factorSlots) {
        onFactorsChange(
            factors.copy(
                isPeriodEnabled = isPeriodEnabled,
                factorSlots = updatedSlots,
                basalRate = basalRate
            )
        )
    }

    val nowMinutes = (now.hour * 60) + now.minute
    val activeIndex = activeFactorForTime(factors.factorSlots, nowMinutes).activeIndex
    val existingNames = factors.factorSlots.map { it.name }
    val duplicateNames = existingNames
        .map { it.trim().lowercase(Locale.ROOT) }
        .filter { it.isNotEmpty() }
        .groupingBy { it }
        .eachCount()
        .filterValues { it > 1 }
        .keys

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (gender == Gender.Female) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = translate(TranslationKey.PeriodLabel, currentLanguage),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Switch(
                    checked = isPeriodEnabled,
                    onCheckedChange = { checked ->
                        isPeriodEnabled = checked
                        onPeriodEnabledChange(checked)
                    }
                )
            }
        }

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
                val activeNowLabel = translate(TranslationKey.ActiveNowBadge, currentLanguage)
                factors.factorSlots.forEachIndexed { index, slot ->
                    val nextStart = factors.factorSlots[(index + 1) % factors.factorSlots.size].startTimeMinutes
                    val endMinutes = ((nextStart - 1) + MINUTES_PER_DAY) % MINUTES_PER_DAY
                    val range = "${formatTimeOfDay(slot.startTimeMinutes)} - ${formatTimeOfDay(endMinutes)}"
                    val isDuplicateName = slot.name.trim().lowercase(Locale.ROOT) in duplicateNames

                    FactorRow(
                        currentLanguage = currentLanguage,
                        activeNowLabel = activeNowLabel,
                        state = FactorRowState(
                            slot = slot,
                            timeRangeLabel = range,
                            enabled = isEditMode,
                            isActiveNow = index == activeIndex,
                            isDuplicateName = isDuplicateName,
                            canDelete = isEditMode && factors.factorSlots.size > MIN_FACTOR_COUNT
                        ),
                        callbacks = FactorRowCallbacks(
                            onNameChange = { newName ->
                                emitFactorsChanged(
                                    factors.factorSlots.toMutableList().apply {
                                        this[index] = this[index].copy(name = newName)
                                    }
                                )
                            },
                            onValueChange = { newValue ->
                                emitFactorsChanged(
                                    factors.factorSlots.toMutableList().apply {
                                        this[index] = this[index].copy(factorValue = newValue)
                                    }
                                )
                            },
                            onDeleteClick = {
                                emitFactorsChanged(
                                    factors.factorSlots.toMutableList().apply { removeAt(index) }
                                )
                            }
                        )
                    )
                }

                if (isEditMode) {
                    OutlinedButton(onClick = { isAddDialogVisible = true }, modifier = Modifier.fillMaxWidth()) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                        Text(
                            text = translate(TranslationKey.ActionAddFactor, currentLanguage),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
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
                    text = translate(TranslationKey.BasalRate, currentLanguage),
                    style = MaterialTheme.typography.titleSmall
                )

                BasalRateInputField(
                    value = basalRate,
                    onValueChange = {
                        basalRate = it
                        emitFactorsChanged()
                    },
                    description = "${
                        translate(
                            TranslationKey.BasalRate,
                            currentLanguage
                        )
                    } (${formatTimeOfDay(factors.basalTimeMinutes)})",
                    label = translate(TranslationKey.BasalRate, currentLanguage),
                    enabled = isEditMode
                )
            }
        }
    }

    if (isAddDialogVisible) {
        AddFactorDialog(
            currentLanguage = currentLanguage,
            existingNames = existingNames,
            initialTimeMinutes = factors.factorSlots.suggestedNewSlotTimeMinutes(),
            onDismissRequest = { isAddDialogVisible = false },
            onConfirm = { name, factorValue, timeMinutes ->
                val withNewSlot = factors.factorSlots +
                    FactorSlot(name = name, factorValue = factorValue, startTimeMinutes = timeMinutes)
                val reordered = withNewSlot.withUpdatedTime(withNewSlot.lastIndex, timeMinutes)
                emitFactorsChanged(reordered)
                isAddDialogVisible = false
            }
        )
    }
}

/** The display state for one [FactorRow] — everything about the slot except its callbacks. */
private data class FactorRowState(
    val slot: FactorSlot,
    val timeRangeLabel: String,
    val enabled: Boolean,
    val isActiveNow: Boolean,
    val isDuplicateName: Boolean,
    val canDelete: Boolean
)

/** The edit actions for one [FactorRow], grouped to keep the composable's own parameter list short. */
private data class FactorRowCallbacks(
    val onNameChange: (String) -> Unit,
    val onValueChange: (String) -> Unit,
    val onDeleteClick: () -> Unit
)

@Composable
private fun FactorRow(
    currentLanguage: AppLanguage,
    activeNowLabel: String,
    state: FactorRowState,
    callbacks: FactorRowCallbacks
) {
    Column(modifier = Modifier.activeFieldHighlight(state.isActiveNow)) {
        if (state.enabled) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = state.slot.name,
                    onValueChange = callbacks.onNameChange,
                    label = { Text(translate(TranslationKey.FactorNameLabel, currentLanguage)) },
                    singleLine = true,
                    isError = state.isDuplicateName,
                    supportingText = {
                        if (state.isDuplicateName) {
                            Text(translate(TranslationKey.FactorNameDuplicateError, currentLanguage))
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                if (state.canDelete) {
                    IconButton(onClick = callbacks.onDeleteClick) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = translate(TranslationKey.ActionDelete, currentLanguage)
                        )
                    }
                }
            }
            FieldDescriptionRow(
                description = state.timeRangeLabel,
                activeNowLabel = activeNowLabel.takeIf { state.isActiveNow }
            )
        } else {
            FieldDescriptionRow(
                description = "${state.slot.name} (${state.timeRangeLabel})",
                activeNowLabel = activeNowLabel.takeIf { state.isActiveNow }
            )
        }

        DoubleInputField(
            value = state.slot.factorValue,
            onValueChange = callbacks.onValueChange,
            description = "",
            label = translate(TranslationKey.LabelFactor, currentLanguage),
            enabled = state.enabled
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddFactorDialog(
    currentLanguage: AppLanguage,
    existingNames: List<String>,
    initialTimeMinutes: Int,
    onDismissRequest: () -> Unit,
    onConfirm: (name: String, factorValue: String, timeMinutes: Int) -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var factorValue by rememberSaveable { mutableStateOf("") }
    val pickerState = rememberTimePickerState(
        initialHour = (initialTimeMinutes / 60) % 24,
        initialMinute = initialTimeMinutes % 60,
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
                    onConfirm(name.trim(), normalizedValue, (pickerState.hour * 60) + pickerState.minute)
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
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = factorValue,
            onValueChange = onFactorValueChange,
            label = { Text(translate(TranslationKey.LabelFactor, currentLanguage)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        TimePicker(state = pickerState)
    }
}

private val DecimalInputRegex = Regex("^\\d*,?\\d*$")
private val IntegerInputRegex = Regex("^\\d+$")

@Composable
private fun NormalizeOnDisable(
    enabled: Boolean,
    focusManager: FocusManager,
    onDisabled: () -> Unit
) {
    var wasEnabled by remember { mutableStateOf(enabled) }

    LaunchedEffect(enabled) {
        if (wasEnabled && !enabled) {
            focusManager.clearFocus(force = true)
            onDisabled()
        }
        wasEnabled = enabled
    }
}

@Composable
private fun DoubleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    description: String,
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    activeNowLabel: String? = null
) {
    var draftValue by rememberSaveable(value, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    var wasFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun applyNormalization() {
        val normalized = normalizeQuarterStepValue(draftValue.text)
        draftValue = TextFieldValue(text = normalized, selection = TextRange(normalized.length))
        onValueChange(normalized)
    }

    Column(modifier = modifier.activeFieldHighlight(activeNowLabel != null)) {
        if (description.isNotEmpty() || activeNowLabel != null) {
            FieldDescriptionRow(description = description, activeNowLabel = activeNowLabel)
        }
        OutlinedTextField(
            value = draftValue,
            onValueChange = { newValue ->
                val sanitizedValue = newValue.text.replace('.', ',')

                // Allow free editing, normalize only when leaving the field.
                if (sanitizedValue.isEmpty() || sanitizedValue.matches(DecimalInputRegex)) {
                    draftValue = newValue.copy(text = sanitizedValue)
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!wasFocused && focusState.isFocused) {
                        draftValue = draftValue.copy(selection = TextRange(draftValue.text.length))
                    }
                    if (enabled && wasFocused && !focusState.isFocused) {
                        applyNormalization()
                    }
                    wasFocused = focusState.isFocused
                }
        )
    }

    NormalizeOnDisable(enabled = enabled, focusManager = focusManager) {
        applyNormalization()
    }
}

@Composable
private fun Modifier.activeFieldHighlight(isActive: Boolean): Modifier {
    if (!isActive) return this
    return clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(8.dp)
}

@Composable
private fun FieldDescriptionRow(description: String, activeNowLabel: String?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = if (activeNowLabel != null) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (activeNowLabel != null) {
            Text(
                text = activeNowLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 4.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun BasalRateInputField(
    value: String,
    onValueChange: (String) -> Unit,
    description: String,
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var draftValue by rememberSaveable(value, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    var wasFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun applyNormalization() {
        val normalized = normalizeBasalRateValue(draftValue.text)
        draftValue = TextFieldValue(text = normalized, selection = TextRange(normalized.length))
        onValueChange(normalized)
    }

    Column(modifier = modifier) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = draftValue,
            onValueChange = { newValue ->
                // Allow free editing, normalize only when leaving the field.
                if (newValue.text.isEmpty() || newValue.text.matches(IntegerInputRegex)) {
                    draftValue = newValue
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus(force = true) }
            ),
            singleLine = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!wasFocused && focusState.isFocused) {
                        draftValue = draftValue.copy(selection = TextRange(draftValue.text.length))
                    }
                    if (enabled && wasFocused && !focusState.isFocused) {
                        applyNormalization()
                    }
                    wasFocused = focusState.isFocused
                }
        )
    }

    NormalizeOnDisable(enabled = enabled, focusManager = focusManager) {
        applyNormalization()
    }
}

private const val PREVIEW_HOUR = 10
private const val PREVIEW_MINUTE = 0

@Preview(showBackground = true)
@Composable
private fun FactorScreenPreview() {
    FactorScreen(now = LocalTime.of(PREVIEW_HOUR, PREVIEW_MINUTE))
}
