package sevynidd.diabetesapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FactorScreen(modifier: Modifier = Modifier) {
    var factor1 by rememberSaveable { mutableStateOf("") }
    var factor2 by rememberSaveable { mutableStateOf("") }
    var factor3 by rememberSaveable { mutableStateOf("") }
    var factor4 by rememberSaveable { mutableStateOf("") }
    var factor5 by rememberSaveable { mutableStateOf("") }
    var factor6 by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Correction Factors",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        DoubleInputField(
            value = factor1,
            onValueChange = { factor1 = it },
            description = "Morning correction factor (00:00 - 06:00)"
        )

        DoubleInputField(
            value = factor2,
            onValueChange = { factor2 = it },
            description = "Breakfast correction factor (06:00 - 10:00)"
        )

        DoubleInputField(
            value = factor3,
            onValueChange = { factor3 = it },
            description = "Lunch correction factor (10:00 - 14:00)"
        )

        DoubleInputField(
            value = factor4,
            onValueChange = { factor4 = it },
            description = "Afternoon correction factor (14:00 - 18:00)"
        )

        DoubleInputField(
            value = factor5,
            onValueChange = { factor5 = it },
            description = "Dinner correction factor (18:00 - 22:00)"
        )

        DoubleInputField(
            value = factor6,
            onValueChange = { factor6 = it },
            description = "Night correction factor (22:00 - 00:00)"
        )
    }
}

@Composable
private fun DoubleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // Allow empty string, digits, and decimal point
                if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                    onValueChange(newValue)
                }
            },
            label = { Text("Factor") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = modifier.fillMaxWidth()
        )
    }


}

