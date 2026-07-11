package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.data.settings.profile.Gender
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
import java.time.LocalTime
import java.util.Locale

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

/** The read-only inputs [CalculateScreen] needs, bundled to keep its parameter list short. */
data class CalculateScreenValues(
    val factors: FactorsData = FactorsData(),
    val breadUnits: Double = 12.0,
    val periodFactorPercent: Double = 0.0,
    val correctionSettings: CorrectionSettings = CorrectionSettings(),
    val gender: Gender = Gender.PreferNotToSay,
    val templatePrefillCarbohydrates: Double? = null,
    val templatePrefillToken: Int = 0,
    val selectedMode: BolusMode = BolusMode.Normal
)

/** Edit callbacks for [CalculateScreen], bundled to keep its parameter list short. */
data class CalculateScreenCallbacks(
    val onSelectedModeChange: (BolusMode) -> Unit = {},
    val onPeriodEnabledChange: (Boolean) -> Unit = {}
)

/** Everything [CalculateScreenBody] needs to render, bundled to keep its parameter list short. */
private data class CalculateScreenRenderState(
    val values: CalculateScreenValues,
    val callbacks: CalculateScreenCallbacks,
    val computed: CalculateScreenComputed,
    val normalInputs: NormalModeInputs,
    val splitInputs: SplitModeInputs
)

@Composable
fun CalculateScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    values: CalculateScreenValues = CalculateScreenValues(),
    callbacks: CalculateScreenCallbacks = CalculateScreenCallbacks(),
    now: LocalTime = LocalTime.now()
) {
    var carbohydrates by rememberSaveable { mutableStateOf("") }
    var bloodSugar by rememberSaveable { mutableStateOf("") }
    var splitCarbohydrates by rememberSaveable { mutableStateOf("") }
    var splitBloodSugar by rememberSaveable { mutableStateOf("") }
    var splitImmediatePercent by rememberSaveable { mutableStateOf("") }
    var splitDurationMinutes by rememberSaveable { mutableStateOf("120") }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(values.templatePrefillToken) {
        val value = values.templatePrefillCarbohydrates?.toUiDecimalOrEmpty().orEmpty()
        if (value.isBlank()) return@LaunchedEffect

        when (values.selectedMode) {
            BolusMode.Normal -> carbohydrates = value
            BolusMode.Split -> splitCarbohydrates = value
        }
    }

    val computed = computeCalculateScreenState(
        values = values,
        now = now,
        inputs = RawBolusInputs(
            carbohydrates = carbohydrates,
            bloodSugar = bloodSugar,
            splitCarbohydrates = splitCarbohydrates,
            splitBloodSugar = splitBloodSugar,
            splitImmediatePercent = splitImmediatePercent,
            splitDurationMinutes = splitDurationMinutes
        )
    )

    val renderState = CalculateScreenRenderState(
        values = values,
        callbacks = callbacks,
        computed = computed,
        normalInputs = NormalModeInputs(
            carbohydrates = carbohydrates,
            onCarbohydratesChange = { carbohydrates = it },
            bloodSugar = bloodSugar,
            onBloodSugarChange = { bloodSugar = it },
            glucoseUnit = values.correctionSettings.glucoseUnit
        ),
        splitInputs = SplitModeInputs(
            carbohydrates = splitCarbohydrates,
            onCarbohydratesChange = { splitCarbohydrates = it },
            bloodSugar = splitBloodSugar,
            onBloodSugarChange = { splitBloodSugar = it },
            glucoseUnit = values.correctionSettings.glucoseUnit,
            immediatePercent = splitImmediatePercent,
            onImmediatePercentChange = { splitImmediatePercent = it },
            restPercentValue = splitImmediatePercent.toIntOrNull()?.coerceIn(0, 100)?.let { 100 - it },
            durationMinutes = splitDurationMinutes,
            onDurationMinutesChange = { splitDurationMinutes = it }
        )
    )

    CalculateScreenBody(
        modifier = modifier,
        currentLanguage = currentLanguage,
        state = renderState,
        focusManager = focusManager
    )
}

@Composable
private fun CalculateScreenBody(
    modifier: Modifier,
    currentLanguage: AppLanguage,
    state: CalculateScreenRenderState,
    focusManager: FocusManager
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = translate(TranslationKey.BolusType, currentLanguage),
            style = MaterialTheme.typography.titleLarge
        )

        BolusModeSelector(
            selectedMode = state.values.selectedMode,
            onSelectedModeChange = state.callbacks.onSelectedModeChange,
            currentLanguage = currentLanguage
        )

        if (state.values.gender == Gender.Female) {
            PeriodToggleRow(
                isPeriodEnabled = state.values.factors.isPeriodEnabled,
                onPeriodEnabledChange = state.callbacks.onPeriodEnabledChange,
                currentLanguage = currentLanguage
            )
        }

        when (state.values.selectedMode) {
            BolusMode.Normal -> NormalModeContent(
                inputs = state.normalInputs,
                result = NormalModeResult(
                    state.computed.activeFactorText,
                    state.computed.calculatedUnitsText,
                    state.computed.correctionUnitsText
                ),
                currentLanguage = currentLanguage,
                focusManager = focusManager
            )

            BolusMode.Split -> SplitModeSection(
                inputs = state.splitInputs,
                computed = state.computed,
                currentLanguage = currentLanguage,
                focusManager = focusManager
            )
        }
    }
}

@Composable
private fun SplitModeSection(
    inputs: SplitModeInputs,
    computed: CalculateScreenComputed,
    currentLanguage: AppLanguage,
    focusManager: FocusManager
) {
    SplitInputsCard(inputs = inputs, currentLanguage = currentLanguage, focusManager = focusManager)
    SplitResultsCard(
        results = SplitModeResults(
            activeFactorText = computed.activeFactorText,
            futureFactorText = computed.futureFactorText,
            splitBolus = computed.splitBolus,
            totalUnitsText = computed.splitUnits.totalUnitsText,
            correctionUnitsText = computed.splitUnits.correctionUnitsText
        ),
        currentLanguage = currentLanguage
    )
}

@Composable
private fun PeriodToggleRow(
    isPeriodEnabled: Boolean,
    onPeriodEnabledChange: (Boolean) -> Unit,
    currentLanguage: AppLanguage
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = translate(TranslationKey.PeriodLabel, currentLanguage),
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        Switch(checked = isPeriodEnabled, onCheckedChange = onPeriodEnabledChange)
        HelpIconButton(helpTextKey = TranslationKey.PeriodHelp, currentLanguage = currentLanguage)
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
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        SingleChoiceSegmentedButtonRow(modifier = Modifier.weight(1f)) {
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
        HelpIconButton(helpTextKey = TranslationKey.BolusTypeHelp, currentLanguage = currentLanguage)
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
            NormalModeInputFields(inputs = inputs, currentLanguage = currentLanguage, focusManager = focusManager)

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

@Composable
private fun NormalModeInputFields(
    inputs: NormalModeInputs,
    currentLanguage: AppLanguage,
    focusManager: FocusManager
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
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
            modifier = Modifier.weight(1f)
        )
        HelpIconButton(helpTextKey = TranslationKey.CarbohydratesHelp, currentLanguage = currentLanguage)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
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
            modifier = Modifier.weight(1f)
        )
        HelpIconButton(helpTextKey = TranslationKey.BloodSugarHelp, currentLanguage = currentLanguage)
    }
}

private fun bloodSugarLabel(currentLanguage: AppLanguage, glucoseUnit: GlucoseUnit): String {
    val unitSuffix = when (glucoseUnit) {
        GlucoseUnit.MgDl -> "mg/dl"
        GlucoseUnit.MmolL -> "mmol/l"
    }
    return "${translate(TranslationKey.BloodSugar, currentLanguage)} ($unitSuffix)"
}

internal fun Double?.toUiDecimalOrEmpty(): String {
    return this?.let { value ->
        String.format(Locale.ROOT, "%.2f", value)
            .replace('.', ',')
            .trimEnd('0')
            .trimEnd(',')
    }.orEmpty()
}

internal val DecimalInputRegex = Regex("^\\d*[.,]?\\d*$")

private const val PREVIEW_HOUR = 12
private const val PREVIEW_MINUTE = 30

@Preview(showBackground = true)
@Composable
private fun CalculateScreenPreview() {
    CalculateScreen(now = LocalTime.of(PREVIEW_HOUR, PREVIEW_MINUTE))
}
