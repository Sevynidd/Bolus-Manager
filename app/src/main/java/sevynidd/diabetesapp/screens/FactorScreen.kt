package sevynidd.diabetesapp.screens

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.database.FactorsData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.localization.TranslationKey

@Composable
fun FactorScreen(
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    onFactorsChange: (FactorsData) -> Unit = {}
) {
    var morningFactor by rememberSaveable(factors.morningFactor) { mutableStateOf(factors.morningFactor) }
    var breakfastFactor by rememberSaveable(factors.breakfastFactor) { mutableStateOf(factors.breakfastFactor) }
    var lunchFactor by rememberSaveable(factors.lunchFactor) { mutableStateOf(factors.lunchFactor) }
    var afternoonFactor by rememberSaveable(factors.afternoonFactor) { mutableStateOf(factors.afternoonFactor) }
    var dinnerFactor by rememberSaveable(factors.dinnerFactor) { mutableStateOf(factors.dinnerFactor) }
    var lateFactor by rememberSaveable(factors.lateFactor) { mutableStateOf(factors.lateFactor) }
    var nightFactor by rememberSaveable(factors.nightFactor) { mutableStateOf(factors.nightFactor) }
    var basalRate by rememberSaveable(factors.basalRate) { mutableStateOf(factors.basalRate) }

    fun emitFactorsChanged() {
        onFactorsChange(
            FactorsData(
                morningFactor = morningFactor,
                breakfastFactor = breakfastFactor,
                lunchFactor = lunchFactor,
                afternoonFactor = afternoonFactor,
                dinnerFactor = dinnerFactor,
                lateFactor = lateFactor,
                nightFactor = nightFactor,
                basalRate = basalRate
            )
        )
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DoubleInputField(
            value = morningFactor,
            onValueChange = {
                morningFactor = it
                emitFactorsChanged()
            },
            description = "${
                translate(
                    TranslationKey.FactorMorning,
                    currentLanguage
                )
            } (05:00 - 09:29)",
            label = translate(TranslationKey.LabelFactor, currentLanguage),
            enabled = isEditMode
        )

        DoubleInputField(
            value = breakfastFactor,
            onValueChange = {
                breakfastFactor = it
                emitFactorsChanged()
            },
            description = "${
                translate(
                    TranslationKey.FactorBreakfast,
                    currentLanguage
                )
            } (09:30 - 11:59)",
            label = translate(TranslationKey.LabelFactor, currentLanguage),
            enabled = isEditMode
        )

        DoubleInputField(
            value = lunchFactor,
            onValueChange = {
                lunchFactor = it
                emitFactorsChanged()
            },
            description = "${
                translate(
                    TranslationKey.FactorLunch,
                    currentLanguage
                )
            } (12:00 - 14:29)",
            label = translate(TranslationKey.LabelFactor, currentLanguage),
            enabled = isEditMode
        )

        DoubleInputField(
            value = afternoonFactor,
            onValueChange = {
                afternoonFactor = it
                emitFactorsChanged()
            },
            description = "${
                translate(
                    TranslationKey.FactorAfternoon,
                    currentLanguage
                )
            } (14:30 - 17:29)",
            label = translate(TranslationKey.LabelFactor, currentLanguage),
            enabled = isEditMode
        )

        DoubleInputField(
            value = dinnerFactor,
            onValueChange = {
                dinnerFactor = it
                emitFactorsChanged()
            },
            description = "${
                translate(
                    TranslationKey.FactorDinner,
                    currentLanguage
                )
            } (17:30 - 19:59)",
            label = translate(TranslationKey.LabelFactor, currentLanguage),
            enabled = isEditMode
        )

        DoubleInputField(
            value = lateFactor,
            onValueChange = {
                lateFactor = it
                emitFactorsChanged()
            },
            description = "${
                translate(
                    TranslationKey.FactorLate,
                    currentLanguage
                )
            } (20:00 - 22:59)",
            label = translate(TranslationKey.LabelFactor, currentLanguage),
            enabled = isEditMode
        )

        DoubleInputField(
            value = nightFactor,
            onValueChange = {
                nightFactor = it
                emitFactorsChanged()
            },
            description = "${
                translate(
                    TranslationKey.FactorNight,
                    currentLanguage
                )
            } (23:00 - 04:59)",
            label = translate(TranslationKey.LabelFactor, currentLanguage),
            enabled = isEditMode
        )

        BasalRateInputField(
            value = basalRate,
            onValueChange = {
                basalRate = it
                emitFactorsChanged()
            },
            description = "${translate(TranslationKey.BasalRate, currentLanguage)} (19:00)",
            label = translate(TranslationKey.LabelBasalRate, currentLanguage),
            enabled = isEditMode
        )
    }
}

@Composable
private fun DoubleInputField(
    value: String,
    onValueChange: (String) -> Unit,
    description: String,
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var draftValue by rememberSaveable(value) { mutableStateOf(value) }
    var wasFocused by remember { mutableStateOf(false) }
    var wasEnabled by remember { mutableStateOf(enabled) }

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
                val sanitizedValue = newValue.replace('.', ',')

                // Allow free editing, normalize only when leaving the field.
                if (sanitizedValue.isEmpty() || sanitizedValue.matches(Regex("^\\d*,?\\d*$"))) {
                    draftValue = sanitizedValue
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (wasFocused && !focusState.isFocused) {
                        val normalized = normalizeQuarterStepValue(draftValue)
                        draftValue = normalized
                        onValueChange(normalized)
                    }
                    wasFocused = focusState.isFocused
                }
        )
    }

    LaunchedEffect(enabled) {
        if (wasEnabled && !enabled) {
            val normalized = normalizeQuarterStepValue(draftValue)
            draftValue = normalized
            onValueChange(normalized)
        }
        wasEnabled = enabled
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
    var draftValue by rememberSaveable(value) { mutableStateOf(value) }
    var wasFocused by remember { mutableStateOf(false) }
    var wasEnabled by remember { mutableStateOf(enabled) }

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
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (wasFocused && !focusState.isFocused) {
                        val normalized = normalizeBasalRateValue(draftValue)
                        draftValue = normalized
                        onValueChange(normalized)
                    }
                    wasFocused = focusState.isFocused
                }
        )
    }

    LaunchedEffect(enabled) {
        if (wasEnabled && !enabled) {
            val normalized = normalizeBasalRateValue(draftValue)
            draftValue = normalized
            onValueChange(normalized)
        }
        wasEnabled = enabled
    }
}

private fun normalizeQuarterStepValue(value: String): String {
    return value
        .replace(',', '.')
        .toDoubleOrNull()
        ?.let { raw ->
            val rounded = kotlin.math.ceil(raw / 0.25) * 0.25
            if (rounded % 1.0 == 0.0) {
                rounded.toInt().toString()
            } else {
                rounded
                    .toString()
                    .replace('.', ',')
                    .trimEnd('0')
                    .trimEnd(',')
            }
        }
        ?: ""
}

private fun normalizeBasalRateValue(value: String): String {
    return value.toIntOrNull()?.let { raw ->
        if (raw % 2 == 0) raw else raw + 1
    }?.toString() ?: ""
}

