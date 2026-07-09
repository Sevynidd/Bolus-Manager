package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.ActiveFactorInfo
import sevynidd.diabetesapp.calculation.MINUTES_PER_DAY
import sevynidd.diabetesapp.calculation.activeFactorForTime
import sevynidd.diabetesapp.calculation.applyPeriodMultiplier
import sevynidd.diabetesapp.calculation.calculateBolusUnits
import sevynidd.diabetesapp.calculation.calculateSplitBolus
import sevynidd.diabetesapp.calculation.SplitBolusResult
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.CorrectionSettings
import sevynidd.diabetesapp.data.settings.Gender
import sevynidd.diabetesapp.data.settings.GlucoseUnit
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.LocalTime
import java.util.Locale
import kotlin.math.roundToInt

enum class BolusMode {
    Normal,
    Split
}

/** The active/calculated values shown for the normal-bolus tab, bundled to keep parameter lists short. */
private data class NormalModeResult(
    val activeFactorText: String,
    val calculatedUnitsText: String,
    val correctionUnitsText: String
)

/** The user-editable fields of the normal-bolus form, bundled to keep composable parameter lists short. */
private data class NormalModeInputs(
    val carbohydrates: String,
    val onCarbohydratesChange: (String) -> Unit,
    val bloodSugar: String,
    val onBloodSugarChange: (String) -> Unit,
    val glucoseUnit: GlucoseUnit
)

@Composable
fun CalculateScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    breadUnits: Double = 12.0,
    periodFactorPercent: Double = 0.0,
    correctionSettings: CorrectionSettings = CorrectionSettings(),
    gender: Gender = Gender.PreferNotToSay,
    templatePrefillCarbohydrates: Double? = null,
    templatePrefillToken: Int = 0,
    selectedMode: BolusMode = BolusMode.Normal,
    onSelectedModeChange: (BolusMode) -> Unit = {},
    now: LocalTime = LocalTime.now()
) {
    var carbohydrates by rememberSaveable { mutableStateOf("") }
    var bloodSugar by rememberSaveable { mutableStateOf("") }
    var splitCarbohydrates by rememberSaveable { mutableStateOf("") }
    var splitBloodSugar by rememberSaveable { mutableStateOf("") }
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

    val isPeriodApplicable = factors.isPeriodEnabled && gender == Gender.Female
    val nowMinutes = (now.hour * 60) + now.minute
    val activeFactorInfo = activeFactorForTime(factors.factorSlots, nowMinutes)
    val activeFactor = applyPeriodMultiplier(activeFactorInfo.factor, isPeriodApplicable, periodFactorPercent)
    val activeFactorText = activeFactorInfo.toDisplayText(activeFactor)
    val effectiveBreadUnits = breadUnits.takeIf { it > 0.0 } ?: 12.0

    val carbohydratesValue = carbohydrates.replace(',', '.').toDoubleOrNull()
    val normalUnits = resolveNormalUnits(
        carbohydratesValue = carbohydratesValue,
        activeFactor = activeFactor,
        effectiveBreadUnits = effectiveBreadUnits,
        bloodSugar = bloodSugar,
        correctionSettings = correctionSettings
    )
    val calculatedUnitsText = normalUnits.calculatedUnitsText
    val correctionUnitsText = normalUnits.correctionUnitsText

    val splitCarbohydratesValue = splitCarbohydrates.replace(',', '.').toDoubleOrNull()
    val splitImmediatePercentValue = splitImmediatePercent.toIntOrNull()?.coerceIn(0, 100)
    val splitRestPercentValue = splitImmediatePercentValue?.let { 100 - it }
    val splitDurationValue = splitDurationMinutes.replace(',', '.').toDoubleOrNull()
    val splitDurationOffsetMinutes = splitDurationValue?.roundToInt()?.coerceAtLeast(0) ?: 120
    val futureFactorTimeMinutes = (nowMinutes + splitDurationOffsetMinutes) % MINUTES_PER_DAY
    val futureFactorInfo = activeFactorForTime(factors.factorSlots, futureFactorTimeMinutes)
    val futureFactor = applyPeriodMultiplier(futureFactorInfo.factor, isPeriodApplicable, periodFactorPercent)
    val futureFactorText = futureFactorInfo.toDisplayText(futureFactor)

    val splitBolus = splitBolusOrNull(
        carbohydrates = splitCarbohydratesValue,
        immediatePercent = splitImmediatePercentValue,
        breadUnits = effectiveBreadUnits,
        immediateFactor = activeFactor,
        restFactor = futureFactor
    )

    val splitUnits = resolveSplitUnits(
        splitBolus = splitBolus,
        splitBloodSugar = splitBloodSugar,
        correctionSettings = correctionSettings
    )

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = translate(TranslationKey.BolusType, currentLanguage),
            style = MaterialTheme.typography.titleLarge
        )

        BolusModeSelector(
            selectedMode = selectedMode,
            onSelectedModeChange = onSelectedModeChange,
            currentLanguage = currentLanguage
        )

        when (selectedMode) {
            BolusMode.Normal -> NormalModeContent(
                inputs = NormalModeInputs(
                    carbohydrates = carbohydrates,
                    onCarbohydratesChange = { carbohydrates = it },
                    bloodSugar = bloodSugar,
                    onBloodSugarChange = { bloodSugar = it },
                    glucoseUnit = correctionSettings.glucoseUnit
                ),
                result = NormalModeResult(activeFactorText, calculatedUnitsText, correctionUnitsText),
                currentLanguage = currentLanguage,
                focusManager = focusManager
            )

            BolusMode.Split -> {
                val inputs = SplitModeInputs(
                    carbohydrates = splitCarbohydrates,
                    onCarbohydratesChange = { splitCarbohydrates = it },
                    bloodSugar = splitBloodSugar,
                    onBloodSugarChange = { splitBloodSugar = it },
                    glucoseUnit = correctionSettings.glucoseUnit,
                    immediatePercent = splitImmediatePercent,
                    onImmediatePercentChange = { splitImmediatePercent = it },
                    restPercentValue = splitRestPercentValue,
                    durationMinutes = splitDurationMinutes,
                    onDurationMinutesChange = { splitDurationMinutes = it }
                )

                SplitInputsCard(inputs = inputs, currentLanguage = currentLanguage, focusManager = focusManager)
                SplitResultsCard(
                    results = SplitModeResults(
                        activeFactorText = activeFactorText,
                        futureFactorText = futureFactorText,
                        splitBolus = splitBolus,
                        totalUnitsText = splitUnits.totalUnitsText,
                        correctionUnitsText = splitUnits.correctionUnitsText
                    ),
                    currentLanguage = currentLanguage
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BolusModeSelector(
    selectedMode: BolusMode,
    onSelectedModeChange: (BolusMode) -> Unit,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val bolusModes = listOf(BolusMode.Normal, BolusMode.Split)
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        bolusModes.forEachIndexed { index, mode ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = bolusModes.size),
                selected = mode == selectedMode,
                onClick = { onSelectedModeChange(mode) },
                label = {
                    Text(
                        text = when (mode) {
                            BolusMode.Normal -> translate(TranslationKey.BolusNormal, currentLanguage)
                            BolusMode.Split -> translate(TranslationKey.BolusSplit, currentLanguage)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun NormalModeContent(
    inputs: NormalModeInputs,
    result: NormalModeResult,
    currentLanguage: AppLanguage,
    focusManager: FocusManager
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EditableNumberField(
                value = inputs.carbohydrates,
                onValueChange = inputs.onCarbohydratesChange,
                label = translate(TranslationKey.Carbohydrates, currentLanguage),
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                sanitizeInput = { rawInput ->
                    val sanitized = rawInput.replace('.', ',')
                    if (sanitized.isEmpty() || sanitized.matches(DecimalInputRegex)) sanitized else null
                },
                modifier = Modifier.fillMaxWidth()
            )

            EditableNumberField(
                value = inputs.bloodSugar,
                onValueChange = inputs.onBloodSugarChange,
                label = bloodSugarLabel(currentLanguage, inputs.glucoseUnit),
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) }),
                sanitizeInput = { rawInput ->
                    val sanitized = rawInput.replace('.', ',')
                    if (sanitized.isEmpty() || sanitized.matches(DecimalInputRegex)) sanitized else null
                },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            ResultStat(
                label = translate(TranslationKey.ActiveFactor, currentLanguage),
                value = result.activeFactorText
            )

            ResultStat(
                label = translate(TranslationKey.CorrectionUnits, currentLanguage),
                value = result.correctionUnitsText
            )

            ResultStat(
                label = translate(TranslationKey.CalculatedUnits, currentLanguage),
                value = result.calculatedUnitsText,
                modifier = Modifier.fillMaxWidth(),
                emphasize = true
            )
        }
    }
}

private fun bloodSugarLabel(currentLanguage: AppLanguage, glucoseUnit: GlucoseUnit): String {
    val unitSuffix = when (glucoseUnit) {
        GlucoseUnit.MgDl -> "mg/dl"
        GlucoseUnit.MmolL -> "mmol/l"
    }
    return "${translate(TranslationKey.BloodSugar, currentLanguage)} ($unitSuffix)"
}

private fun ActiveFactorInfo.toDisplayText(factorValue: Double? = factor): String {
    val valueText = factorValue.toUiDecimalOrEmpty()
    return if (valueText.isBlank()) factorName else "$factorName · $valueText"
}

internal fun Double?.toUiDecimalOrEmpty(): String {
    return this?.let { value ->
        String.format(Locale.ROOT, "%.2f", value)
            .replace('.', ',')
            .trimEnd('0')
            .trimEnd(',')
    }.orEmpty()
}

private fun splitBolusOrNull(
    carbohydrates: Double?,
    immediatePercent: Int?,
    breadUnits: Double,
    immediateFactor: Double?,
    restFactor: Double?
): SplitBolusResult? {
    val carbsAndPercent = carbohydrates?.let { carbs -> immediatePercent?.let { percent -> carbs to percent } }
    val factors = immediateFactor?.let { immediate -> restFactor?.let { rest -> immediate to rest } }
    return carbsAndPercent?.let { (carbs, percent) ->
        factors?.let { (immediate, rest) ->
            calculateSplitBolus(carbs, percent, breadUnits, immediate, rest)
        }
    }
}

internal val DecimalInputRegex = Regex("^\\d*[.,]?\\d*$")

private const val PREVIEW_HOUR = 12
private const val PREVIEW_MINUTE = 30

@Preview(showBackground = true)
@Composable
private fun CalculateScreenPreview() {
    CalculateScreen(now = LocalTime.of(PREVIEW_HOUR, PREVIEW_MINUTE))
}
