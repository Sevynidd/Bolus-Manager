package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import kotlin.math.min

/** A labeled numeric text field whose displayed text is externally sanitized via [sanitizeInput]. */
@Composable
internal fun EditableNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    sanitizeInput: (String) -> String?,
    modifier: Modifier = Modifier
) {
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    var wasFocused by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        if (value != textFieldValue.text) {
            textFieldValue = TextFieldValue(text = value, selection = TextRange(value.length))
        }
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val sanitized = sanitizeInput(newValue.text) ?: return@OutlinedTextField
            val cursorPosition = min(newValue.selection.end, sanitized.length)
            textFieldValue = newValue.copy(
                text = sanitized,
                selection = TextRange(cursorPosition)
            )
            onValueChange(sanitized)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        modifier = modifier.onFocusChanged { focusState ->
            if (!wasFocused && focusState.isFocused) {
                textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
            }
            wasFocused = focusState.isFocused
        }
    )
}

/** A read-only [label]/[value] pair, shown in larger, colored type when [emphasize] is set. */
@Composable
internal fun ResultStat(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    emphasize: Boolean = false
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.ifBlank { "–" },
            style = if (emphasize) MaterialTheme.typography.displaySmall else MaterialTheme.typography.titleLarge,
            color = if (emphasize) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}
