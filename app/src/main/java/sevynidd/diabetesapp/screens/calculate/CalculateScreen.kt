package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import java.time.LocalTime
import java.util.Locale
import kotlin.math.min
import kotlin.math.roundToInt

enum class BolusMode {
    Normal,
    Split
}

@Composable
fun CalculateScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    breadUnits: Double = 12.0,
    templatePrefillCarbohydrates: Double? = null,
    templatePrefillToken: Int = 0,
    selectedMode: BolusMode = BolusMode.Normal,
    onSelectedModeChange: (BolusMode) -> Unit = {}
) {
    var carbohydrates by rememberSaveable { mutableStateOf("") }
    var splitCarbohydrates by rememberSaveable { mutableStateOf("") }
    var splitImmediatePercent by rememberSaveable { mutableStateOf("") }
    var splitDurationMinutes by rememberSaveable { mutableStateOf("120") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(templatePrefillToken) {
        val value = templatePrefillCarbohydrates?.toUiDecimalOrEmpty().orEmpty()
        if (value.isBlank()) return@LaunchedEffect

        when (selectedMode) {
            BolusMode.Normal -> carbohydrates = value
            BolusMode.Split -> splitCarbohydrates = value
        }
    }

    val now = LocalTime.now()
    val nowMinutes = (now.hour * 60) + now.minute
    val activeFactorInfo = activeFactorForTime(factors, nowMinutes)
    val activeFactor = activeFactorInfo.factor
    val activeFactorText = activeFactorInfo.toDisplayText(currentLanguage)
    val effectiveBreadUnits = breadUnits.takeIf { it > 0.0 } ?: 12.0

    val carbohydratesValue = carbohydrates.replace(',', '.').toDoubleOrNull()
    val calculatedUnits = if (carbohydratesValue != null && activeFactor != null) {
        (carbohydratesValue / effectiveBreadUnits) * activeFactor
    } else {
        null
    }
    val calculatedUnitsText = calculatedUnits.toUiDecimalOrEmpty()

    val splitCarbohydratesValue = splitCarbohydrates.replace(',', '.').toDoubleOrNull()
    val splitImmediatePercentValue = splitImmediatePercent.toIntOrNull()?.coerceIn(0, 100)
    val splitRestPercentValue = splitImmediatePercentValue?.let { 100 - it }
    val splitDurationValue = splitDurationMinutes.replace(',', '.').toDoubleOrNull()
    val splitDurationOffsetMinutes = splitDurationValue?.roundToInt()?.coerceAtLeast(0) ?: 120
    val futureFactorTimeMinutes = (nowMinutes + splitDurationOffsetMinutes) % MINUTES_PER_DAY
    val futureFactorInfo = activeFactorForTime(factors, futureFactorTimeMinutes)
    val futureFactor = futureFactorInfo.factor
    val futureFactorText = futureFactorInfo.toDisplayText(currentLanguage)

    val splitImmediateCarbohydrates = if (splitCarbohydratesValue != null && splitImmediatePercentValue != null) {
        splitCarbohydratesValue * splitImmediatePercentValue / 100.0
    } else {
        null
    }
    val splitRestCarbohydrates = if (splitCarbohydratesValue != null && splitRestPercentValue != null) {
        splitCarbohydratesValue * splitRestPercentValue / 100.0
    } else {
        null
    }
    val splitImmediateUnits = if (splitImmediateCarbohydrates != null && activeFactor != null) {
        (splitImmediateCarbohydrates / effectiveBreadUnits) * activeFactor
    } else {
        null
    }
    val splitRestUnits = if (splitRestCarbohydrates != null && futureFactor != null) {
        (splitRestCarbohydrates / effectiveBreadUnits) * futureFactor
    } else {
        null
    }
    val splitTotalUnits = if (splitImmediateUnits != null && splitRestUnits != null) {
        splitImmediateUnits + splitRestUnits
    } else {
        null
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = translate(TranslationKey.BolusType, currentLanguage),
            style = MaterialTheme.typography.titleMedium
        )

        val bolusTabs = listOf(BolusMode.Normal, BolusMode.Split)
        PrimaryTabRow(
            selectedTabIndex = bolusTabs.indexOf(selectedMode),
            modifier = Modifier.fillMaxWidth()
        ) {
            bolusTabs.forEach { tabMode ->
                Tab(
                    selected = tabMode == selectedMode,
                    onClick = { onSelectedModeChange(tabMode) },
                    text = {
                        Text(
                            text = when (tabMode) {
                                BolusMode.Normal -> translate(TranslationKey.BolusNormal, currentLanguage)
                                BolusMode.Split -> translate(TranslationKey.BolusSplit, currentLanguage)
                            }
                        )
                    }
                )
            }
        }

        when (selectedMode) {
            BolusMode.Normal -> {
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
                        EditableNumberField(
                            value = carbohydrates,
                            onValueChange = { carbohydrates = it },
                            label = translate(TranslationKey.Carbohydrates, currentLanguage),
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done,
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) }),
                            sanitizeInput = { rawInput ->
                                val sanitized = rawInput.replace('.', ',')
                                if (sanitized.isEmpty() || sanitized.matches(DecimalInputRegex)) sanitized else null
                            },
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
                }
            }

            BolusMode.Split -> {
                val splitCarbohydratesRequester = remember { FocusRequester() }
                val splitImmediatePercentRequester = remember { FocusRequester() }
                val splitDurationRequester = remember { FocusRequester() }

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
                        EditableNumberField(
                            value = splitCarbohydrates,
                            onValueChange = { splitCarbohydrates = it },
                            label = translate(TranslationKey.Carbohydrates, currentLanguage),
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next,
                            keyboardActions = KeyboardActions(onNext = { splitImmediatePercentRequester.requestFocus() }),
                            sanitizeInput = { rawInput ->
                                val sanitized = rawInput.replace('.', ',')
                                if (sanitized.isEmpty() || sanitized.matches(DecimalInputRegex)) sanitized else null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(splitCarbohydratesRequester)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            EditableNumberField(
                                value = splitImmediatePercent,
                                onValueChange = { splitImmediatePercent = it },
                                label = translate(TranslationKey.BolusImmediatePercent, currentLanguage),
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next,
                                keyboardActions = KeyboardActions(onNext = { splitDurationRequester.requestFocus() }),
                                sanitizeInput = { rawInput -> sanitizePercentageInput(rawInput) },
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(splitImmediatePercentRequester)
                            )

                            OutlinedTextField(
                                value = splitRestPercentValue?.toString().orEmpty(),
                                onValueChange = {},
                                label = { Text(translate(TranslationKey.BolusExtendedPercent, currentLanguage)) },
                                readOnly = true,
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        EditableNumberField(
                            value = splitDurationMinutes,
                            onValueChange = { splitDurationMinutes = it },
                            label = translate(TranslationKey.BolusDurationMinutes, currentLanguage),
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done,
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) }),
                            sanitizeInput = { rawInput ->
                                val sanitized = rawInput.replace('.', ',')
                                if (sanitized.isEmpty() || sanitized.matches(DecimalInputRegex)) sanitized else null
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(splitDurationRequester)
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
                        Text(
                            text = translate(TranslationKey.Calculated, currentLanguage),
                            style = MaterialTheme.typography.titleSmall
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = activeFactorText,
                                onValueChange = {},
                                label = { Text(translate(TranslationKey.ActiveFactor, currentLanguage)) },
                                readOnly = true,
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = futureFactorText,
                                onValueChange = {},
                                label = { Text(translate(TranslationKey.FutureFactor, currentLanguage)) },
                                readOnly = true,
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = splitTotalUnits.toUiDecimalOrEmpty(),
                            onValueChange = {},
                            label = { Text(translate(TranslationKey.CalculatedUnits, currentLanguage)) },
                            readOnly = true,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = splitImmediateUnits.toUiDecimalOrEmpty(),
                                onValueChange = {},
                                label = { Text(translate(TranslationKey.BolusImmediateUnits, currentLanguage)) },
                                readOnly = true,
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )

                            OutlinedTextField(
                                value = splitRestUnits.toUiDecimalOrEmpty(),
                                onValueChange = {},
                                label = { Text(translate(TranslationKey.BolusExtendedUnits, currentLanguage)) },
                                readOnly = true,
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditableNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    sanitizeInput: (String) -> String?,
    modifier: Modifier = Modifier
) {
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
    }
    var wasFocused by remember { mutableStateOf(false) }

    LaunchedEffect(value) {
        if (value != textFieldValue.text) {
            textFieldValue = TextFieldValue(text = value, selection = TextRange(value.length))
        }
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newValue ->
            val sanitized = sanitizeInput(newValue.text) ?: return@OutlinedTextField
            val cursorPosition = min(newValue.selection.end, sanitized.length)
            textFieldValue = newValue.copy(
                text = sanitized,
                selection = TextRange(cursorPosition)
            )
            onValueChange(sanitized)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        modifier = modifier.onFocusChanged { focusState ->
            if (!wasFocused && focusState.isFocused) {
                textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
            }
            wasFocused = focusState.isFocused
        }
    )
}

private data class ActiveFactorInfo(
    val factor: Double?,
    val durationMinutes: Int,
    val factorLabel: TranslationKey
)

private fun activeFactorForTime(factors: FactorsData, nowMinutes: Int): ActiveFactorInfo {
    val morning = factors.morningTimeMinutes
    val breakfast = factors.breakfastTimeMinutes
    val lunch = factors.lunchTimeMinutes
    val afternoon = factors.afternoonTimeMinutes
    val dinner = factors.dinnerTimeMinutes
    val late = factors.lateTimeMinutes
    val night = factors.nightTimeMinutes

    val (factorText, duration, factorLabel) = when {
        nowMinutes !in morning..<night -> Triple(
            factors.nightFactor,
            (MINUTES_PER_DAY - night) + morning,
            TranslationKey.FactorNight
        )

        nowMinutes < breakfast -> Triple(
            factors.morningFactor,
            breakfast - morning,
            TranslationKey.FactorMorning
        )

        nowMinutes < lunch -> Triple(
            factors.breakfastFactor,
            lunch - breakfast,
            TranslationKey.FactorBreakfast
        )

        nowMinutes < afternoon -> Triple(
            factors.lunchFactor,
            afternoon - lunch,
            TranslationKey.FactorLunch
        )

        nowMinutes < dinner -> Triple(
            factors.afternoonFactor,
            dinner - afternoon,
            TranslationKey.FactorAfternoon
        )

        nowMinutes < late -> Triple(
            factors.dinnerFactor,
            late - dinner,
            TranslationKey.FactorDinner
        )

        else -> Triple(
            factors.lateFactor,
            night - late,
            TranslationKey.FactorLate
        )
    }

    return ActiveFactorInfo(
        factor = factorText.replace(',', '.').toDoubleOrNull(),
        durationMinutes = duration.coerceAtLeast(1),
        factorLabel = factorLabel
    )
}

private fun ActiveFactorInfo.toDisplayText(language: AppLanguage): String {
    val factorName = translate(factorLabel, language)
    val factorValue = factor.toUiDecimalOrEmpty()
    return if (factorValue.isBlank()) factorName else "$factorName · $factorValue"
}

private fun Double?.toUiDecimalOrEmpty(): String {
    return this?.let { value ->
        String.format(Locale.ROOT, "%.2f", value)
            .replace('.', ',')
            .trimEnd('0')
            .trimEnd(',')
    }.orEmpty()
}

private fun sanitizePercentageInput(input: String): String? {
    if (input.isEmpty()) return ""
    if (!input.matches(PercentageInputRegex)) return null

    return input.toIntOrNull()?.coerceAtMost(100)?.toString() ?: input
}

private const val MINUTES_PER_DAY = 24 * 60
private val DecimalInputRegex = Regex("^\\d*[.,]?\\d*$")
private val PercentageInputRegex = Regex("^\\d{0,3}$")

