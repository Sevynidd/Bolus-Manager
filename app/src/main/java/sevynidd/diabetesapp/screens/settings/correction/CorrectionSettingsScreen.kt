package sevynidd.diabetesapp.screens.settings.correction

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
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.mgDlToMmolL
import sevynidd.diabetesapp.calculation.mmolLToMgDl
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
import java.util.Locale

/** Settings that tune the blood-sugar correction dose: high/low thresholds, step, and display unit. */
@Composable
fun CorrectionSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    values: CorrectionSettingsValues = CorrectionSettingsValues(),
    callbacks: CorrectionSettingsCallbacks = CorrectionSettingsCallbacks()
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GlucoseUnitCard(
            currentLanguage = currentLanguage,
            currentGlucoseUnit = values.glucoseUnit,
            onGlucoseUnitChange = callbacks.onGlucoseUnitChange
        )

        CorrectionValueCard(
            labels = CorrectionValueLabels(TranslationKey.CorrectionThreshold, TranslationKey.CorrectionThresholdHelp),
            currentLanguage = currentLanguage,
            currentValueMgDl = values.correctionThresholdMgDl,
            glucoseUnit = values.glucoseUnit,
            onValueMgDlChange = callbacks.onCorrectionThresholdMgDlChange
        )

        CorrectionValueCard(
            labels = CorrectionValueLabels(
                TranslationKey.CorrectionLowThreshold,
                TranslationKey.CorrectionLowThresholdHelp
            ),
            currentLanguage = currentLanguage,
            currentValueMgDl = values.correctionLowThresholdMgDl,
            glucoseUnit = values.glucoseUnit,
            onValueMgDlChange = callbacks.onCorrectionLowThresholdMgDlChange
        )

        CorrectionValueCard(
            labels = CorrectionValueLabels(TranslationKey.CorrectionStep, TranslationKey.CorrectionStepHelp),
            currentLanguage = currentLanguage,
            currentValueMgDl = values.correctionStepMgDl,
            glucoseUnit = values.glucoseUnit,
            onValueMgDlChange = callbacks.onCorrectionStepMgDlChange
        )
    }
}

@Composable
private fun CorrectionSettingCard(content: @Composable ColumnScope.() -> Unit) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GlucoseUnitCard(
    currentLanguage: AppLanguage,
    currentGlucoseUnit: GlucoseUnit,
    onGlucoseUnitChange: (GlucoseUnit) -> Unit
) {
    CorrectionSettingCard {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Bloodtype,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = translate(TranslationKey.GlucoseUnitLabel, currentLanguage),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            HelpIconButton(helpTextKey = TranslationKey.GlucoseUnitHelp, currentLanguage = currentLanguage)
        }

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            GlucoseUnit.entries.forEachIndexed { index, unit ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index, GlucoseUnit.entries.size),
                    selected = currentGlucoseUnit == unit,
                    onClick = { onGlucoseUnitChange(unit) },
                    label = { Text(glucoseUnitLabel(unit)) }
                )
            }
        }
    }
}

/** The translated title and in-app help text a [CorrectionValueCard] displays. */
private data class CorrectionValueLabels(val titleKey: TranslationKey, val helpTextKey: TranslationKey)

@Composable
private fun CorrectionValueCard(
    labels: CorrectionValueLabels,
    currentLanguage: AppLanguage,
    currentValueMgDl: Double,
    glucoseUnit: GlucoseUnit,
    onValueMgDlChange: (Double) -> Unit
) {
    val currentDisplayValue = currentValueMgDl.toDisplayValue(glucoseUnit)
    var draftValue by rememberSaveable(currentDisplayValue) {
        mutableStateOf(currentDisplayValue.toDecimalInput())
    }
    val label = "${translate(labels.titleKey, currentLanguage)} (${glucoseUnitLabel(glucoseUnit)})"

    CorrectionSettingCard {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Percent,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(text = label, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            HelpIconButton(helpTextKey = labels.helpTextKey, currentLanguage = currentLanguage)
        }

        OutlinedTextField(
            value = draftValue,
            onValueChange = { newValue ->
                if (newValue.isEmpty() || newValue.matches(DecimalInputRegex)) {
                    draftValue = newValue
                    newValue.replace(',', '.').toDoubleOrNull()?.takeIf { it >= 0.0 }?.let { displayValue ->
                        onValueMgDlChange(displayValue.toStorageValueMgDl(glucoseUnit))
                    }
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun glucoseUnitLabel(unit: GlucoseUnit): String = when (unit) {
    GlucoseUnit.MgDl -> "mg/dl"
    GlucoseUnit.MmolL -> "mmol/l"
}

private fun Double.toDisplayValue(unit: GlucoseUnit): Double =
    if (unit == GlucoseUnit.MmolL) mgDlToMmolL(this) else this

private fun Double.toStorageValueMgDl(unit: GlucoseUnit): Double =
    if (unit == GlucoseUnit.MmolL) mmolLToMgDl(this) else this

private fun Double.toDecimalInput(): String {
    return String.format(Locale.ROOT, "%.2f", this)
        .replace('.', ',')
        .trimEnd('0')
        .trimEnd(',')
}

private val DecimalInputRegex = Regex("^\\d*[.,]?\\d*$")

@Preview(showBackground = true)
@Composable
private fun CorrectionSettingsScreenPreview() {
    CorrectionSettingsScreen()
}
