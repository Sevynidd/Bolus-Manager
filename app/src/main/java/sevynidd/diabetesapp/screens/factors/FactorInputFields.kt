package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.normalizeBasalRateValue
import sevynidd.diabetesapp.calculation.normalizeQuarterStepValue

internal val DecimalInputRegex = Regex("^\\d*,?\\d*$")
private val IntegerInputRegex = Regex("^\\d+$")

@Composable
internal fun Modifier.activeFieldHighlight(isActive: Boolean): Modifier {
    if (!isActive) return this
    return clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(8.dp)
}

@Composable
internal fun FieldDescriptionRow(description: String, activeNowLabel: String?) {
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

/** The label text of an editable field, bundled to keep composable parameter lists short. */
internal data class FieldLabels(
    val label: String,
    val description: String,
    val activeNowLabel: String? = null
)

@Composable
internal fun DoubleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    labels: FieldLabels,
    enabled: Boolean,
    modifier: Modifier = Modifier
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

    Column(modifier = modifier.activeFieldHighlight(labels.activeNowLabel != null)) {
        if (labels.description.isNotEmpty() || labels.activeNowLabel != null) {
            FieldDescriptionRow(description = labels.description, activeNowLabel = labels.activeNowLabel)
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
            label = { Text(labels.label) },
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
internal fun BasalRateInputField(
    value: String,
    onValueChange: (String) -> Unit,
    labels: FieldLabels,
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
            text = labels.description,
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
            label = { Text(labels.label) },
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
