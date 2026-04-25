package sevynidd.diabetesapp.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.util.Locale

@Composable
fun BreadUnitsSettingsScreen(
    modifier: Modifier = Modifier,
    currentBreadUnits: Double = 12.0,
    currentLanguage: AppLanguage = AppLanguage.System,
    onBreadUnitsChange: (Double) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var draftValue by rememberSaveable(currentBreadUnits) {
        mutableStateOf(currentBreadUnits.toBreadUnitsText())
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = translate(TranslationKey.BreadUnits, currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = draftValue,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(BreadUnitsInputRegex)) {
                            draftValue = newValue
                            newValue.replace(',', '.').toDoubleOrNull()?.takeIf { it > 0.0 }?.let(onBreadUnitsChange)
                        }
                    },
                    label = { Text(translate(TranslationKey.Carbohydrates, currentLanguage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun Double.toBreadUnitsText(): String {
    return String.format(Locale.ROOT, "%.2f", this)
        .replace('.', ',')
        .trimEnd('0')
        .trimEnd(',')
}

private val BreadUnitsInputRegex = Regex("^\\d*[.,]?\\d*$")

