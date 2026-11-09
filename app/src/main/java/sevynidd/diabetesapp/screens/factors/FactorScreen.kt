package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.FactorEditKind
import sevynidd.diabetesapp.calculation.MINUTES_PER_DAY
import sevynidd.diabetesapp.calculation.activeFactorForTime
import sevynidd.diabetesapp.calculation.formatTimeOfDay
import sevynidd.diabetesapp.calculation.suggestedNewSlotTimeMinutes
import sevynidd.diabetesapp.calculation.withUpdatedTime
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.therapy.TherapyMode
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
import java.time.LocalTime

private const val MINUTES_PER_HOUR = 60

/** Shown in place of a value that has not been set yet. */
internal const val NOT_SET = "–"

/**
 * The factor state [FactorScreen] displays and its change callback, bundled to keep its
 * parameter list short. [onFactorsChange] saves the transformed factors right away.
 */
data class FactorScreenState(
    val factors: FactorsData = FactorsData(),
    val displayFormat: FactorDisplayFormat = FactorDisplayFormat(),
    val onFactorsChange: ((FactorsData) -> FactorsData) -> Unit = {}
)

@Composable
fun FactorScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    state: FactorScreenState = FactorScreenState(),
    now: LocalTime = LocalTime.now()
) {
    val factors = state.factors
    var editKind by rememberSaveable { mutableStateOf<FactorEditKind?>(null) }
    var editIndex by rememberSaveable { mutableIntStateOf(0) }
    var isAddDialogVisible by rememberSaveable { mutableStateOf(false) }

    fun openEditor(kind: FactorEditKind, index: Int = 0) {
        editKind = kind
        editIndex = index
    }

    val nowMinutes = (now.hour * MINUTES_PER_HOUR) + now.minute
    val activeIndex = activeFactorForTime(factors.factorSlots, nowMinutes).activeIndex

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FactorsListCard(
            currentLanguage = currentLanguage,
            state = FactorsListState(factors, activeIndex, state.displayFormat),
            onEditClick = { openEditor(FactorEditKind.Slot, it) },
            onAddClick = { isAddDialogVisible = true }
        )

        if (state.displayFormat.mode == TherapyMode.Pump) {
            PumpBasalRatesCard(
                currentLanguage = currentLanguage,
                rates = factors.pumpBasalRates,
                nowHour = now.hour,
                onEditHour = { openEditor(FactorEditKind.PumpHour, it) }
            )
        } else {
            BasalRateCard(currentLanguage, factors) { openEditor(FactorEditKind.Basal) }
        }
    }

    editKind?.let { kind ->
        FactorEditSheetHost(currentLanguage, state, kind, editIndex) { editKind = null }
    }

    if (isAddDialogVisible) {
        AddFactorDialog(
            currentLanguage = currentLanguage,
            newFactor = NewFactorContext(
                existingNames = factors.factorSlots.map { it.name },
                initialTimeMinutes = factors.factorSlots.suggestedNewSlotTimeMinutes(),
                displayFormat = state.displayFormat
            ),
            onDismissRequest = { isAddDialogVisible = false },
            onConfirm = { name, factorValue, timeMinutes ->
                state.onFactorsChange { current ->
                    val withNewSlot = current.factorSlots +
                        FactorSlot(name = name, factorValue = factorValue, startTimeMinutes = timeMinutes)
                    current.copy(factorSlots = withNewSlot.withUpdatedTime(withNewSlot.lastIndex, timeMinutes))
                }
                isAddDialogVisible = false
            }
        )
    }
}

/** The read-only state [FactorsListCard] renders, bundled to keep its parameter list short. */
private data class FactorsListState(
    val factors: FactorsData,
    val activeIndex: Int,
    val displayFormat: FactorDisplayFormat
)

@Composable
private fun FactorsListCard(
    currentLanguage: AppLanguage,
    state: FactorsListState,
    onEditClick: (index: Int) -> Unit,
    onAddClick: () -> Unit
) {
    FactorCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = translate(TranslationKey.FactorsListTitle, currentLanguage),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            HelpIconButton(helpTextKey = state.displayFormat.listHelpKey, currentLanguage = currentLanguage)
        }

        val activeNowLabel = translate(TranslationKey.ActiveNowBadge, currentLanguage)
        state.factors.factorSlots.forEachIndexed { index, slot ->
            TappableValueRow(
                content = factorRowContent(state, index, slot, activeNowLabel),
                onClick = { onEditClick(index) }
            )
        }

        OutlinedButton(onClick = onAddClick, modifier = Modifier.fillMaxWidth()) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            Text(
                text = translate(TranslationKey.ActionAddFactor, currentLanguage),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

private fun factorRowContent(
    state: FactorsListState,
    index: Int,
    slot: FactorSlot,
    activeNowLabel: String
): ValueRowContent {
    val factorSlots = state.factors.factorSlots
    val nextStart = factorSlots[(index + 1) % factorSlots.size].startTimeMinutes
    val endMinutes = ((nextStart - 1) + MINUTES_PER_DAY) % MINUTES_PER_DAY
    return ValueRowContent(
        title = slot.name,
        description = "${formatTimeOfDay(slot.startTimeMinutes)} - ${formatTimeOfDay(endMinutes)}",
        valueText = state.displayFormat.toDisplayText(slot.factorValue).ifBlank { NOT_SET },
        activeNowLabel = activeNowLabel.takeIf { index == state.activeIndex }
    )
}

@Composable
private fun BasalRateCard(currentLanguage: AppLanguage, factors: FactorsData, onClick: () -> Unit) {
    FactorCard(modifier = Modifier.padding(bottom = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = translate(TranslationKey.BasalRate, currentLanguage),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            HelpIconButton(helpTextKey = TranslationKey.BasalRateHelp, currentLanguage = currentLanguage)
        }

        TappableValueRow(
            content = ValueRowContent(
                title = null,
                description = formatTimeOfDay(factors.basalTimeMinutes),
                valueText = factors.basalRate.ifBlank { NOT_SET }
            ),
            onClick = onClick
        )
    }
}

/** The card chrome shared by the factor screen's sections. */
@Composable
internal fun FactorCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            content()
        }
    }
}

private const val PREVIEW_HOUR = 10
private const val PREVIEW_MINUTE = 0

@Preview(showBackground = true)
@Composable
private fun FactorScreenPreview() {
    FactorScreen(now = LocalTime.of(PREVIEW_HOUR, PREVIEW_MINUTE))
}

@Preview(showBackground = true)
@Composable
private fun FactorScreenPumpRatioPreview() {
    FactorScreen(
        state = FactorScreenState(displayFormat = FactorDisplayFormat(mode = TherapyMode.Pump)),
        now = LocalTime.of(PREVIEW_HOUR, PREVIEW_MINUTE)
    )
}
