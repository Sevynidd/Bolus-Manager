package sevynidd.diabetesapp.screens.factors

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
import java.util.Locale
import kotlin.math.ceil

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
                basalRate = basalRate,
                morningTimeMinutes = factors.morningTimeMinutes,
                breakfastTimeMinutes = factors.breakfastTimeMinutes,
                lunchTimeMinutes = factors.lunchTimeMinutes,
                afternoonTimeMinutes = factors.afternoonTimeMinutes,
                dinnerTimeMinutes = factors.dinnerTimeMinutes,
                lateTimeMinutes = factors.lateTimeMinutes,
                nightTimeMinutes = factors.nightTimeMinutes,
                basalTimeMinutes = factors.basalTimeMinutes
            )
        )
    }

    val morningRange = buildTimeRange(factors.morningTimeMinutes, factors.breakfastTimeMinutes)
    val breakfastRange = buildTimeRange(factors.breakfastTimeMinutes, factors.lunchTimeMinutes)
    val lunchRange = buildTimeRange(factors.lunchTimeMinutes, factors.afternoonTimeMinutes)
    val afternoonRange = buildTimeRange(factors.afternoonTimeMinutes, factors.dinnerTimeMinutes)
    val dinnerRange = buildTimeRange(factors.dinnerTimeMinutes, factors.lateTimeMinutes)
    val lateRange = buildTimeRange(factors.lateTimeMinutes, factors.nightTimeMinutes)
    val nightRange = buildTimeRange(factors.nightTimeMinutes, factors.morningTimeMinutes)

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
            } ($morningRange)",
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
            } ($breakfastRange)",
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
            } ($lunchRange)",
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
            } ($afternoonRange)",
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
            } ($dinnerRange)",
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
            } ($lateRange)",
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
            } ($nightRange)",
            label = translate(TranslationKey.LabelFactor, currentLanguage),
            enabled = isEditMode
        )

        BasalRateInputField(
            value = basalRate,
            onValueChange = {
                basalRate = it
                emitFactorsChanged()
            },
            description = "${translate(TranslationKey.BasalRate, currentLanguage)} (${formatTimeOfDay(factors.basalTimeMinutes)})",
            label = translate(TranslationKey.BasalRate, currentLanguage),
            enabled = isEditMode
        )
    }
}

private fun buildTimeRange(startMinutes: Int, nextStartMinutes: Int): String {
    val endMinutes = ((nextStartMinutes - 1) + MINUTES_PER_DAY) % MINUTES_PER_DAY
    return "${formatTimeOfDay(startMinutes)} - ${formatTimeOfDay(endMinutes)}"
}

private fun formatTimeOfDay(totalMinutes: Int): String {
    val normalized = ((totalMinutes % MINUTES_PER_DAY) + MINUTES_PER_DAY) % MINUTES_PER_DAY
    val hours = normalized / 60
    val minutes = normalized % 60
    return String.format(Locale.ROOT, "%02d:%02d", hours, minutes)
}

private const val MINUTES_PER_DAY = 24 * 60

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
            val rounded = ceil(raw / 0.25) * 0.25
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

