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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.MINUTES_PER_DAY
import sevynidd.diabetesapp.calculation.activeFactorForTime
import sevynidd.diabetesapp.calculation.formatTimeOfDay
import sevynidd.diabetesapp.calculation.suggestedNewSlotTimeMinutes
import sevynidd.diabetesapp.calculation.withUpdatedTime
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
import java.time.LocalTime
import java.util.Locale

private const val MIN_FACTOR_COUNT = 1
private const val MINUTES_PER_HOUR = 60

/**
 * The editable factor state [FactorScreen] displays and its change callback, bundled to keep its
 * parameter list short.
 */
data class FactorScreenState(
    val factors: FactorsData = FactorsData(),
    val isEditMode: Boolean = false,
    val onFactorsChange: ((FactorsData) -> FactorsData) -> Unit = {}
)

/**
 * Values purely derived from [FactorScreenState.factors] and the current time, computed once per
 * recomposition.
 */
private data class FactorScreenDerived(
    val activeIndex: Int,
    val existingNames: List<String>,
    val duplicateNames: Set<String>
)

private fun deriveFactorScreenState(factors: FactorsData, now: LocalTime): FactorScreenDerived {
    val nowMinutes = (now.hour * MINUTES_PER_HOUR) + now.minute
    val activeIndex = activeFactorForTime(factors.factorSlots, nowMinutes).activeIndex
    val existingNames = factors.factorSlots.map { it.name }
    val duplicateNames = existingNames
        .map { it.trim().lowercase(Locale.ROOT) }
        .filter { it.isNotEmpty() }
        .groupingBy { it }
        .eachCount()
        .filterValues { it > 1 }
        .keys
    return FactorScreenDerived(activeIndex, existingNames, duplicateNames)
}

@Composable
fun FactorScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    state: FactorScreenState = FactorScreenState(),
    now: LocalTime = LocalTime.now()
) {
    val factors = state.factors
    val isEditMode = state.isEditMode
    var basalRate by rememberSaveable(factors.basalRate) { mutableStateOf(factors.basalRate) }
    var isAddDialogVisible by rememberSaveable { mutableStateOf(false) }

    fun emitFactorsChanged(updateSlots: (List<FactorSlot>) -> List<FactorSlot> = { it }) {
        state.onFactorsChange { current ->
            current.copy(
                factorSlots = updateSlots(current.factorSlots),
                basalRate = basalRate
            )
        }
    }

    val derived = deriveFactorScreenState(factors, now)

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FactorsListCard(
            currentLanguage = currentLanguage,
            state = FactorsListState(factors, isEditMode, derived),
            callbacks = FactorsListCallbacks(
                onSlotsChanged = ::emitFactorsChanged,
                onAddClick = { isAddDialogVisible = true }
            )
        )

        BasalRateCard(
            currentLanguage = currentLanguage,
            factors = factors,
            basalRate = basalRate,
            isEditMode = isEditMode,
            onBasalRateChange = {
                basalRate = it
                emitFactorsChanged()
            }
        )
    }

    if (isAddDialogVisible) {
        AddFactorDialog(
            currentLanguage = currentLanguage,
            existingNames = derived.existingNames,
            initialTimeMinutes = factors.factorSlots.suggestedNewSlotTimeMinutes(),
            onDismissRequest = { isAddDialogVisible = false },
            onConfirm = { name, factorValue, timeMinutes ->
                emitFactorsChanged { currentSlots ->
                    val withNewSlot = currentSlots +
                        FactorSlot(name = name, factorValue = factorValue, startTimeMinutes = timeMinutes)
                    withNewSlot.withUpdatedTime(withNewSlot.lastIndex, timeMinutes)
                }
                isAddDialogVisible = false
            }
        )
    }
}

/** The read-only state [FactorsListCard] renders, bundled to keep its parameter list short. */
private data class FactorsListState(
    val factors: FactorsData,
    val isEditMode: Boolean,
    val derived: FactorScreenDerived
)

/** The edit actions for [FactorsListCard], bundled to keep its parameter list short. */
private data class FactorsListCallbacks(
    val onSlotsChanged: ((List<FactorSlot>) -> List<FactorSlot>) -> Unit,
    val onAddClick: () -> Unit
)

@Composable
private fun FactorsListCard(
    currentLanguage: AppLanguage,
    state: FactorsListState,
    callbacks: FactorsListCallbacks
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = translate(TranslationKey.FactorsListTitle, currentLanguage),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                HelpIconButton(helpTextKey = TranslationKey.FactorsListHelp, currentLanguage = currentLanguage)
            }

            val activeNowLabel = translate(TranslationKey.ActiveNowBadge, currentLanguage)
            state.factors.factorSlots.withIndex().forEach { item ->
                FactorListItem(
                    item = item,
                    currentLanguage = currentLanguage,
                    activeNowLabel = activeNowLabel,
                    state = state,
                    onSlotsChanged = callbacks.onSlotsChanged
                )
            }

            if (state.isEditMode) {
                OutlinedButton(onClick = callbacks.onAddClick, modifier = Modifier.fillMaxWidth()) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                    Text(
                        text = translate(TranslationKey.ActionAddFactor, currentLanguage),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun FactorListItem(
    item: IndexedValue<FactorSlot>,
    currentLanguage: AppLanguage,
    activeNowLabel: String,
    state: FactorsListState,
    onSlotsChanged: ((List<FactorSlot>) -> List<FactorSlot>) -> Unit
) {
    val (index, slot) = item
    val factorSlots = state.factors.factorSlots
    val nextStart = factorSlots[(index + 1) % factorSlots.size].startTimeMinutes
    val endMinutes = ((nextStart - 1) + MINUTES_PER_DAY) % MINUTES_PER_DAY
    val range = "${formatTimeOfDay(slot.startTimeMinutes)} - ${formatTimeOfDay(endMinutes)}"
    val isDuplicateName = slot.name.trim().lowercase(Locale.ROOT) in state.derived.duplicateNames

    FactorRow(
        currentLanguage = currentLanguage,
        activeNowLabel = activeNowLabel,
        state = FactorRowState(
            slot = slot,
            timeRangeLabel = range,
            enabled = state.isEditMode,
            isActiveNow = index == state.derived.activeIndex,
            isDuplicateName = isDuplicateName,
            canDelete = state.isEditMode && factorSlots.size > MIN_FACTOR_COUNT
        ),
        callbacks = FactorRowCallbacks(
            onNameChange = { newName ->
                onSlotsChanged { currentSlots ->
                    currentSlots.toMutableList().apply { this[index] = this[index].copy(name = newName) }
                }
            },
            onValueChange = { newValue ->
                onSlotsChanged { currentSlots ->
                    currentSlots.toMutableList().apply { this[index] = this[index].copy(factorValue = newValue) }
                }
            },
            onDeleteClick = {
                onSlotsChanged { currentSlots -> currentSlots.toMutableList().apply { removeAt(index) } }
            }
        )
    )
}

@Composable
private fun BasalRateCard(
    currentLanguage: AppLanguage,
    factors: FactorsData,
    basalRate: String,
    isEditMode: Boolean,
    onBasalRateChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = translate(TranslationKey.BasalRate, currentLanguage),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                HelpIconButton(helpTextKey = TranslationKey.BasalRateHelp, currentLanguage = currentLanguage)
            }

            BasalRateInputField(
                value = basalRate,
                onValueChange = onBasalRateChange,
                labels = FieldLabels(
                    label = translate(TranslationKey.BasalRate, currentLanguage),
                    description = "${translate(TranslationKey.BasalRate, currentLanguage)} " +
                        "(${formatTimeOfDay(factors.basalTimeMinutes)})"
                ),
                enabled = isEditMode
            )
        }
    }
}

/** The display state for one [FactorRow] — everything about the slot except its callbacks. */
private data class FactorRowState(
    val slot: FactorSlot,
    val timeRangeLabel: String,
    val enabled: Boolean,
    val isActiveNow: Boolean,
    val isDuplicateName: Boolean,
    val canDelete: Boolean
)

/** The edit actions for one [FactorRow], grouped to keep the composable's own parameter list short. */
private data class FactorRowCallbacks(
    val onNameChange: (String) -> Unit,
    val onValueChange: (String) -> Unit,
    val onDeleteClick: () -> Unit
)

@Composable
private fun FactorRow(
    currentLanguage: AppLanguage,
    activeNowLabel: String,
    state: FactorRowState,
    callbacks: FactorRowCallbacks
) {
    Column(modifier = Modifier.activeFieldHighlight(state.isActiveNow)) {
        if (state.enabled) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = state.slot.name,
                    onValueChange = callbacks.onNameChange,
                    label = { Text(translate(TranslationKey.FactorNameLabel, currentLanguage)) },
                    singleLine = true,
                    isError = state.isDuplicateName,
                    supportingText = {
                        if (state.isDuplicateName) {
                            Text(translate(TranslationKey.FactorNameDuplicateError, currentLanguage))
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
                if (state.canDelete) {
                    IconButton(onClick = callbacks.onDeleteClick) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = translate(TranslationKey.ActionDelete, currentLanguage)
                        )
                    }
                }
            }
            FieldDescriptionRow(
                description = state.timeRangeLabel,
                activeNowLabel = activeNowLabel.takeIf { state.isActiveNow }
            )
        } else {
            FieldDescriptionRow(
                description = "${state.slot.name} (${state.timeRangeLabel})",
                activeNowLabel = activeNowLabel.takeIf { state.isActiveNow }
            )
        }

        DoubleInputField(
            value = state.slot.factorValue,
            onValueChange = callbacks.onValueChange,
            labels = FieldLabels(label = translate(TranslationKey.LabelFactor, currentLanguage), description = ""),
            enabled = state.enabled
        )
    }
}

private const val PREVIEW_HOUR = 10
private const val PREVIEW_MINUTE = 0

@Preview(showBackground = true)
@Composable
private fun FactorScreenPreview() {
    FactorScreen(now = LocalTime.of(PREVIEW_HOUR, PREVIEW_MINUTE))
}
