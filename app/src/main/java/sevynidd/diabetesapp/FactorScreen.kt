package sevynidd.diabetesapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
fun FactorScreen(modifier: Modifier = Modifier, isEditMode: Boolean = false) {
    var factor1 by rememberSaveable { mutableStateOf("") }
    var factor2 by rememberSaveable { mutableStateOf("") }
    var factor3 by rememberSaveable { mutableStateOf("") }
    var factor4 by rememberSaveable { mutableStateOf("") }
    var factor5 by rememberSaveable { mutableStateOf("") }
    var factor6 by rememberSaveable { mutableStateOf("") }
    var basalRate by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DoubleInputField(
            value = factor1,
            onValueChange = { factor1 = it },
            description = "Morning correction factor (05:00 - 09:00)",
            enabled = isEditMode
        )

        DoubleInputField(
            value = factor2,
            onValueChange = { factor2 = it },
            description = "Breakfast correction factor (09:00 - 12:00)",
            enabled = isEditMode
        )

        DoubleInputField(
            value = factor3,
            onValueChange = { factor3 = it },
            description = "Lunch correction factor (12:00 - 15:00)",
            enabled = isEditMode
        )

        DoubleInputField(
            value = factor4,
            onValueChange = { factor4 = it },
            description = "Afternoon correction factor (15:00 - 18:00)",
            enabled = isEditMode
        )

        DoubleInputField(
            value = factor5,
            onValueChange = { factor5 = it },
            description = "Dinner correction factor (18:00 - 21:00)",
            enabled = isEditMode
        )

        DoubleInputField(
            value = factor6,
            onValueChange = { factor6 = it },
            description = "Night correction factor (21:00 - 00:00)",
            enabled = isEditMode
        )

        BasalRateInputField(
            value = basalRate,
            onValueChange = { basalRate = it },
            description = "Basal rate (19:00)",
            enabled = isEditMode
        )
    }
}

@Composable
private fun DoubleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    description: String,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var draftValue by rememberSaveable(value) { mutableStateOf(value) }
    var wasFocused by remember { mutableStateOf(false) }

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
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    draftValue = newValue
                }
            },
            label = { Text("Factor") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (wasFocused && !focusState.isFocused) {
                        val normalized = draftValue.toDoubleOrNull()?.let { raw ->
                            val rounded = kotlin.math.ceil(raw / 0.25) * 0.25
                            if (rounded % 1.0 == 0.0) {
                                rounded.toInt().toString()
                            } else {
                                String.format(Locale.US, "%.2f", rounded).trimEnd('0').trimEnd('.')
                            }
                        } ?: ""
                        draftValue = normalized
                        onValueChange(normalized)
                    }
                    wasFocused = focusState.isFocused
                }
        )
    }
}

@Composable
private fun BasalRateInputField(
    value: String,
    onValueChange: (String) -> Unit,
    description: String,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var draftValue by rememberSaveable(value) { mutableStateOf(value) }
    var wasFocused by remember { mutableStateOf(false) }

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
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d+$"))) {
                    draftValue = newValue
                }
            },
            label = { Text("Basal Rate") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (wasFocused && !focusState.isFocused) {
                        val normalized = draftValue.toIntOrNull()?.let { raw ->
                            if (raw % 2 == 0) raw else raw + 1
                        }?.toString() ?: ""
                        draftValue = normalized
                        onValueChange(normalized)
                    }
                    wasFocused = focusState.isFocused
                }
        )
    }
}
