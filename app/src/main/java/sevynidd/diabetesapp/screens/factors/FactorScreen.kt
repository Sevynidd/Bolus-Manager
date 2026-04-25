package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.util.Locale
import kotlin.math.ceil

private data class FactorItem(
    val value: String,
    val onChange: (String) -> Unit,
    val description: String
)

@Composable
fun FactorScreen(
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    onFactorsChange: (FactorsData) -> Unit = {},
    onPeriodeEnabledChange: (Boolean) -> Unit = {}
) {
    var isPeriodeEnabled by rememberSaveable(factors.isPeriodeEnabled) { mutableStateOf(factors.isPeriodeEnabled) }
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
                isPeriodeEnabled = isPeriodeEnabled,
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

    val factorItems = listOf(
        FactorItem(
            morningFactor,
            { value: String ->
                morningFactor = value
                emitFactorsChanged()
            },
            "${translate(TranslationKey.FactorMorning, currentLanguage)} ($morningRange)"
        ),
        FactorItem(
            breakfastFactor,
            { value: String ->
                breakfastFactor = value
                emitFactorsChanged()
            },
            "${translate(TranslationKey.FactorBreakfast, currentLanguage)} ($breakfastRange)"
        ),
        FactorItem(
            lunchFactor,
            { value: String ->
                lunchFactor = value
                emitFactorsChanged()
            },
            "${translate(TranslationKey.FactorLunch, currentLanguage)} ($lunchRange)"
        ),
        FactorItem(
            afternoonFactor,
            { value: String ->
                afternoonFactor = value
                emitFactorsChanged()
            },
            "${translate(TranslationKey.FactorAfternoon, currentLanguage)} ($afternoonRange)"
        ),
        FactorItem(
            dinnerFactor,
            { value: String ->
                dinnerFactor = value
                emitFactorsChanged()
            },
            "${translate(TranslationKey.FactorDinner, currentLanguage)} ($dinnerRange)"
        ),
        FactorItem(
            lateFactor,
            { value: String ->
                lateFactor = value
                emitFactorsChanged()
            },
            "${translate(TranslationKey.FactorLate, currentLanguage)} ($lateRange)"
        ),
        FactorItem(
            nightFactor,
            { value: String ->
                nightFactor = value
                emitFactorsChanged()
            },
            "${translate(TranslationKey.FactorNight, currentLanguage)} ($nightRange)"
        )
    )

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = translate(TranslationKey.LabelFactor, currentLanguage),
                style = MaterialTheme.typography.titleMedium
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = translate(TranslationKey.PeriodeLabel, currentLanguage),
                    style = MaterialTheme.typography.labelLarge
                )
                Checkbox(
                    checked = isPeriodeEnabled,
                    onCheckedChange = { checked ->
                        isPeriodeEnabled = checked
                        onPeriodeEnabledChange(checked)
                    }
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                factorItems.forEach { item ->
                    DoubleInputField(
                        value = item.value,
                        onValueChange = item.onChange,
                        description = item.description,
                        label = translate(TranslationKey.LabelFactor, currentLanguage),
                        enabled = isEditMode
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = translate(TranslationKey.BasalRate, currentLanguage),
                    style = MaterialTheme.typography.titleSmall
                )

                BasalRateInputField(
                    value = basalRate,
                    onValueChange = {
                        basalRate = it
                        emitFactorsChanged()
                    },
                    description = "${
                        translate(
                            TranslationKey.BasalRate,
                            currentLanguage
                        )
                    } (${formatTimeOfDay(factors.basalTimeMinutes)})",
                    label = translate(TranslationKey.BasalRate, currentLanguage),
                    enabled = isEditMode
                )
            }
        }
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
private val DecimalInputRegex = Regex("^\\d*,?\\d*$")
private val IntegerInputRegex = Regex("^\\d+$")

@Composable
private fun NormalizeOnDisable(
    enabled: Boolean,
    focusManager: FocusManager,
    onDisabled: () -> Unit
) {
    var wasEnabled by remember { mutableStateOf(enabled) }

    LaunchedEffect(enabled) {
        if (wasEnabled && !enabled) {
            focusManager.clearFocus(force = true)
            onDisabled()
        }
        wasEnabled = enabled
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
    var draftValue by rememberSaveable(value, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    var wasFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun applyNormalization() {
        val normalized = normalizeQuarterStepValue(draftValue.text)
        draftValue = TextFieldValue(text = normalized, selection = TextRange(normalized.length))
        onValueChange(normalized)
    }

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
                val sanitizedValue = newValue.text.replace('.', ',')

                // Allow free editing, normalize only when leaving the field.
                if (sanitizedValue.isEmpty() || sanitizedValue.matches(DecimalInputRegex)) {
                    draftValue = newValue.copy(text = sanitizedValue)
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!wasFocused && focusState.isFocused) {
                        draftValue = draftValue.copy(selection = TextRange(draftValue.text.length))
                    }
                    if (enabled && wasFocused && !focusState.isFocused) {
                        applyNormalization()
                    }
                    wasFocused = focusState.isFocused
                }
        )
    }

    NormalizeOnDisable(enabled = enabled, focusManager = focusManager) {
        applyNormalization()
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
    var draftValue by rememberSaveable(value, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    var wasFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    fun applyNormalization() {
        val normalized = normalizeBasalRateValue(draftValue.text)
        draftValue = TextFieldValue(text = normalized, selection = TextRange(normalized.length))
        onValueChange(normalized)
    }

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
                if (newValue.text.isEmpty() || newValue.text.matches(IntegerInputRegex)) {
                    draftValue = newValue
                }
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus(force = true) }
            ),
            singleLine = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!wasFocused && focusState.isFocused) {
                        draftValue = draftValue.copy(selection = TextRange(draftValue.text.length))
                    }
                    if (enabled && wasFocused && !focusState.isFocused) {
                        applyNormalization()
                    }
                    wasFocused = focusState.isFocused
                }
        )
    }

    NormalizeOnDisable(enabled = enabled, focusManager = focusManager) {
        applyNormalization()
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

