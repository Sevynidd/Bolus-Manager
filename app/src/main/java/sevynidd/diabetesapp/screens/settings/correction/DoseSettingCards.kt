package sevynidd.diabetesapp.screens.settings.correction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.settings.correction.DOSE_STEP_OPTIONS_UNITS
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
import java.util.Locale

/** Picks the injection step the calculated dose is rounded to, from [DOSE_STEP_OPTIONS_UNITS]. */
@Composable
internal fun DoseStepCard(
    currentStepUnits: Double,
    currentLanguage: AppLanguage,
    onStepUnitsChange: (Double) -> Unit
) {
    val unitSymbol = translate(TranslationKey.DoseUnitSymbol, currentLanguage)

    DoseSettingCard {
        SettingCardHeader(
            title = "${translate(TranslationKey.DoseStep, currentLanguage)} ($unitSymbol)",
            helpTextKey = TranslationKey.DoseStepHelp,
            currentLanguage = currentLanguage
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DOSE_STEP_OPTIONS_UNITS.forEach { step ->
                FilterChip(
                    selected = step == currentStepUnits,
                    onClick = { onStepUnitsChange(step) },
                    label = { Text(step.toInputText()) }
                )
            }
        }
    }
}

/** A numeric setting measured in [unitSymbolKey]'s unit (units of insulin or grams), `0` or more. */
@Composable
internal fun UnitsValueCard(
    labels: CorrectionValueLabels,
    unitSymbolKey: TranslationKey,
    currentLanguage: AppLanguage,
    currentValue: Double,
    onValueChange: (Double) -> Unit
) {
    var draftValue by rememberSaveable(currentValue) { mutableStateOf(currentValue.toInputText()) }
    val label = "${translate(labels.titleKey, currentLanguage)} (${translate(unitSymbolKey, currentLanguage)})"

    DoseSettingCard {
        SettingCardHeader(title = label, helpTextKey = labels.helpTextKey, currentLanguage = currentLanguage)
        OutlinedTextField(
            value = draftValue,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(DecimalInputRegex)) {
                    draftValue = newValue
                    newValue.replace(',', '.').toDoubleOrNull()?.let(onValueChange)
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SettingCardHeader(title: String, helpTextKey: TranslationKey, currentLanguage: AppLanguage) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
        HelpIconButton(helpTextKey = helpTextKey, currentLanguage = currentLanguage)
    }
}

@Composable
private fun DoseSettingCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            content()
        }
    }
}

private fun Double.toInputText(): String =
    String.format(Locale.ROOT, "%.2f", this).replace('.', ',').trimEnd('0').trimEnd(',')
