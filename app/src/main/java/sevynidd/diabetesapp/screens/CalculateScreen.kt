package sevynidd.diabetesapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.database.FactorsData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.LocalTime
import java.util.Locale

enum class BolusMode {
    Normal,
    Split
}

@Composable
fun CalculateScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData()
) {
    var selectedMode by rememberSaveable { mutableStateOf(BolusMode.Normal) }
    var carbohydrates by rememberSaveable { mutableStateOf("") }
    var splitImmediatePercent by rememberSaveable { mutableStateOf("") }
    var splitExtendedPercent by rememberSaveable { mutableStateOf("") }
    var splitDurationMinutes by rememberSaveable { mutableStateOf("") }

    val now = LocalTime.now()
    val nowMinutes = (now.hour * 60) + now.minute
    val activeFactor = activeFactorForTime(factors, nowMinutes)
    val carbohydratesValue = carbohydrates.replace(',', '.').toDoubleOrNull()
    val calculatedUnits = if (carbohydratesValue != null && activeFactor != null) {
        carbohydratesValue * activeFactor
    } else {
        null
    }
    val calculatedUnitsText = calculatedUnits?.let { units ->
        String.format(Locale.ROOT, "%.2f", units).replace('.', ',').trimEnd('0').trimEnd(',')
    }.orEmpty()
    val activeFactorText = activeFactor?.let { factor ->
        String.format(Locale.ROOT, "%.2f", factor).replace('.', ',').trimEnd('0').trimEnd(',')
    }.orEmpty()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = translate(TranslationKey.BolusType, currentLanguage),
            style = MaterialTheme.typography.titleMedium
        )

        Column(modifier = Modifier.selectableGroup()) {
            BolusModeItem(
                text = translate(TranslationKey.BolusNormal, currentLanguage),
                selected = selectedMode == BolusMode.Normal,
                onClick = { selectedMode = BolusMode.Normal }
            )
            BolusModeItem(
                text = translate(TranslationKey.BolusSplit, currentLanguage),
                selected = selectedMode == BolusMode.Split,
                onClick = { selectedMode = BolusMode.Split }
            )
        }

        when (selectedMode) {
            BolusMode.Normal -> {
                OutlinedTextField(
                    value = carbohydrates,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*[.,]?\\d*$"))) {
                            carbohydrates = newValue
                        }
                    },
                    label = { Text(translate(TranslationKey.Carbohydrates, currentLanguage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = activeFactorText,
                    onValueChange = {},
                    label = { Text(translate(TranslationKey.ActiveFactor, currentLanguage)) },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = calculatedUnitsText,
                    onValueChange = {},
                    label = { Text(translate(TranslationKey.CalculatedUnits, currentLanguage)) },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            BolusMode.Split -> {
                OutlinedTextField(
                    value = splitImmediatePercent,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d{0,3}$"))) {
                            splitImmediatePercent = newValue
                        }
                    },
                    label = { Text(translate(TranslationKey.BolusImmediatePercent, currentLanguage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = splitExtendedPercent,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d{0,3}$"))) {
                            splitExtendedPercent = newValue
                        }
                    },
                    label = { Text(translate(TranslationKey.BolusExtendedPercent, currentLanguage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = splitDurationMinutes,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d{0,4}$"))) {
                            splitDurationMinutes = newValue
                        }
                    },
                    label = { Text(translate(TranslationKey.BolusDurationMinutes, currentLanguage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun activeFactorForTime(factors: FactorsData, nowMinutes: Int): Double? {
    val morning = factors.morningTimeMinutes
    val breakfast = factors.breakfastTimeMinutes
    val lunch = factors.lunchTimeMinutes
    val afternoon = factors.afternoonTimeMinutes
    val dinner = factors.dinnerTimeMinutes
    val late = factors.lateTimeMinutes
    val night = factors.nightTimeMinutes

    val factorText = when {
        nowMinutes !in morning..<night -> factors.nightFactor
        nowMinutes < breakfast -> factors.morningFactor
        nowMinutes < lunch -> factors.breakfastFactor
        nowMinutes < afternoon -> factors.lunchFactor
        nowMinutes < dinner -> factors.afternoonFactor
        nowMinutes < late -> factors.dinnerFactor
        else -> factors.lateFactor
    }

    return factorText.replace(',', '.').toDoubleOrNull()
}

@Composable
private fun BolusModeItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
