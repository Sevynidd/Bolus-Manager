package sevynidd.diabetesapp.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.util.Locale

/** Settings that tune the bolus/factor calculation itself: bread units and the period surcharge. */
@Composable
fun FactorSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    values: FactorSettingsValues = FactorSettingsValues(),
    onBreadUnitsChange: (Double) -> Unit = {},
    onPeriodFactorPercentChange: (Double) -> Unit = {}
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BreadUnitsCard(
            currentLanguage = currentLanguage,
            currentBreadUnits = values.breadUnits,
            onBreadUnitsChange = onBreadUnitsChange
        )

        PeriodFactorCard(
            currentLanguage = currentLanguage,
            currentPeriodFactorPercent = values.periodFactorPercent,
            onPeriodFactorPercentChange = onPeriodFactorPercentChange
        )
    }
}

@Composable
private fun FactorSettingCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content
        )
    }
}

@Composable
private fun BreadUnitsCard(
    currentLanguage: AppLanguage,
    currentBreadUnits: Double,
    onBreadUnitsChange: (Double) -> Unit
) {
    var draftValue by rememberSaveable(currentBreadUnits) {
        mutableStateOf(currentBreadUnits.toDecimalInput())
    }

    FactorSettingCard {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(
                imageVector = Icons.Filled.Grain,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = translate(TranslationKey.BreadUnits, currentLanguage),
                style = MaterialTheme.typography.titleSmall
            )
        }

        OutlinedTextField(
            value = draftValue,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(DecimalInputRegex)) {
                    draftValue = newValue
                    newValue.replace(',', '.').toDoubleOrNull()?.takeIf { it > 0.0 }?.let(onBreadUnitsChange)
                }
            },
            label = { Text(translate(TranslationKey.BreadUnits, currentLanguage)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PeriodFactorCard(
    currentLanguage: AppLanguage,
    currentPeriodFactorPercent: Double,
    onPeriodFactorPercentChange: (Double) -> Unit
) {
    var draftPeriodPercent by rememberSaveable(currentPeriodFactorPercent) {
        mutableStateOf(currentPeriodFactorPercent.toDecimalInput())
    }

    FactorSettingCard {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(
                imageVector = Icons.Filled.Percent,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = translate(TranslationKey.PeriodFactorPercent, currentLanguage),
                style = MaterialTheme.typography.titleSmall
            )
        }

        OutlinedTextField(
            value = draftPeriodPercent,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(DecimalInputRegex)) {
                    draftPeriodPercent = newValue
                    newValue.replace(',', '.')
                        .toDoubleOrNull()
                        ?.takeIf { it >= 0.0 }
                        ?.let(onPeriodFactorPercentChange)
                }
            },
            label = { Text(translate(TranslationKey.PeriodFactorPercent, currentLanguage)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun Double.toDecimalInput(): String {
    return String.format(Locale.ROOT, "%.2f", this)
        .replace('.', ',')
        .trimEnd('0')
        .trimEnd(',')
}

private val DecimalInputRegex = Regex("^\\d*[.,]?\\d*$")

@Preview(showBackground = true)
@Composable
private fun FactorSettingsScreenPreview() {
    FactorSettingsScreen()
}
