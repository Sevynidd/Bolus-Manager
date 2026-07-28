package sevynidd.diabetesapp.screens.factors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData

data class FactorEditSessionUiState(
    val factors: FactorsData = FactorsData(),
    val isEditMode: Boolean = false,
    val pendingSave: Boolean = false
)

class FactorEditSessionViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    var uiState by mutableStateOf(savedStateHandle.restoreUiState())
        private set

    fun syncPersistedFactors(factors: FactorsData) {
        if (!uiState.isEditMode && !uiState.pendingSave && uiState.factors != factors) {
            updateState(uiState.copy(factors = factors))
        }
    }

    fun startEditing() {
        if (!uiState.isEditMode) {
            updateState(uiState.copy(isEditMode = true))
        }
    }

    fun leaveEditMode(shouldSave: Boolean) {
        if (uiState.isEditMode) {
            updateState(
                uiState.copy(
                    isEditMode = false,
                    pendingSave = uiState.pendingSave || shouldSave
                )
            )
        }
    }

    /**
     * Applies [transform] to the current draft rather than replacing it with a precomputed
     * [FactorsData]: several fields can normalize and commit in the same frame (e.g. every field
     * does so at once when edit mode ends), and each must see the others' just-applied edits
     * instead of overwriting them with a snapshot captured before those edits landed.
     */
    fun updateDraft(transform: (FactorsData) -> FactorsData) {
        val updatedFactors = transform(uiState.factors)
        if (uiState.factors != updatedFactors) {
            updateState(uiState.copy(factors = updatedFactors))
        }
    }

    fun updatePeriodEnabled(isEnabled: Boolean) {
        val updatedFactors = uiState.factors.copy(isPeriodEnabled = isEnabled)
        if (uiState.factors != updatedFactors) {
            updateState(
                uiState.copy(
                    factors = updatedFactors,
                    pendingSave = uiState.pendingSave || !uiState.isEditMode
                )
            )
        }
    }

    fun updateBasalReminderEnabled(isEnabled: Boolean) {
        val updatedFactors = uiState.factors.copy(basalReminderEnabled = isEnabled)
        if (uiState.factors != updatedFactors) {
            updateState(
                uiState.copy(
                    factors = updatedFactors,
                    pendingSave = uiState.pendingSave || !uiState.isEditMode
                )
            )
        }
    }

    fun consumePendingSave(): FactorsData? {
        if (uiState.isEditMode || !uiState.pendingSave) return null

        val factorsToSave = uiState.factors
        updateState(uiState.copy(pendingSave = false))
        return factorsToSave
    }

    private fun updateState(newState: FactorEditSessionUiState) {
        uiState = newState
        savedStateHandle.persistUiState(newState)
    }
}

private fun SavedStateHandle.restoreUiState(): FactorEditSessionUiState {
    val names = get<ArrayList<String>>(FACTOR_NAMES_KEY)
    val values = get<ArrayList<String>>(FACTOR_VALUES_KEY)
    val times = get<ArrayList<Int>>(FACTOR_TIMES_KEY)

    val factorSlots = if (names != null && values != null && times != null) {
        names.indices.map { index ->
            FactorSlot(
                name = names[index],
                factorValue = values.getOrElse(index) { "" },
                startTimeMinutes = times.getOrElse(index) { 0 }
            )
        }
    } else {
        FactorsData().factorSlots
    }

    return FactorEditSessionUiState(
        factors = FactorsData(
            isPeriodEnabled = get<Boolean>(IS_PERIOD_ENABLED_KEY) ?: false,
            factorSlots = factorSlots,
            basalRate = get<String>(BASAL_RATE_KEY).orEmpty(),
            basalTimeMinutes = get<Int>(BASAL_TIME_KEY) ?: (19 * 60),
            basalReminderEnabled = get<Boolean>(BASAL_REMINDER_ENABLED_KEY) ?: false
        ),
        isEditMode = get<Boolean>(IS_EDIT_MODE_KEY) ?: false,
        pendingSave = get<Boolean>(PENDING_SAVE_KEY) ?: false
    )
}

private fun SavedStateHandle.persistUiState(state: FactorEditSessionUiState) {
    set(IS_PERIOD_ENABLED_KEY, state.factors.isPeriodEnabled)
    set(FACTOR_NAMES_KEY, ArrayList(state.factors.factorSlots.map { it.name }))
    set(FACTOR_VALUES_KEY, ArrayList(state.factors.factorSlots.map { it.factorValue }))
    set(FACTOR_TIMES_KEY, ArrayList(state.factors.factorSlots.map { it.startTimeMinutes }))
    set(BASAL_RATE_KEY, state.factors.basalRate)
    set(BASAL_TIME_KEY, state.factors.basalTimeMinutes)
    set(BASAL_REMINDER_ENABLED_KEY, state.factors.basalReminderEnabled)
    set(IS_EDIT_MODE_KEY, state.isEditMode)
    set(PENDING_SAVE_KEY, state.pendingSave)
}

private const val IS_PERIOD_ENABLED_KEY = "factor_editor_is_period_enabled"
private const val FACTOR_NAMES_KEY = "factor_editor_names"
private const val FACTOR_VALUES_KEY = "factor_editor_values"
private const val FACTOR_TIMES_KEY = "factor_editor_times"
private const val BASAL_RATE_KEY = "factor_editor_basal_rate"
private const val BASAL_TIME_KEY = "factor_editor_basal_time"
private const val BASAL_REMINDER_ENABLED_KEY = "factor_editor_basal_reminder_enabled"
private const val IS_EDIT_MODE_KEY = "factor_editor_is_edit_mode"
private const val PENDING_SAVE_KEY = "factor_editor_pending_save"
