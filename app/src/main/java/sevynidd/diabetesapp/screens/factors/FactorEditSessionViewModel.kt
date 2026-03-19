package sevynidd.diabetesapp.screens.factors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import sevynidd.diabetesapp.data.database.FactorsData

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

    fun updateDraft(factors: FactorsData) {
        if (uiState.factors != factors) {
            updateState(uiState.copy(factors = factors))
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
    return FactorEditSessionUiState(
        factors = FactorsData(
            morningFactor = get<String>(MORNING_FACTOR_KEY).orEmpty(),
            breakfastFactor = get<String>(BREAKFAST_FACTOR_KEY).orEmpty(),
            lunchFactor = get<String>(LUNCH_FACTOR_KEY).orEmpty(),
            afternoonFactor = get<String>(AFTERNOON_FACTOR_KEY).orEmpty(),
            dinnerFactor = get<String>(DINNER_FACTOR_KEY).orEmpty(),
            lateFactor = get<String>(LATE_FACTOR_KEY).orEmpty(),
            nightFactor = get<String>(NIGHT_FACTOR_KEY).orEmpty(),
            basalRate = get<String>(BASAL_RATE_KEY).orEmpty()
        ),
        isEditMode = get<Boolean>(IS_EDIT_MODE_KEY) ?: false,
        pendingSave = get<Boolean>(PENDING_SAVE_KEY) ?: false
    )
}

private fun SavedStateHandle.persistUiState(state: FactorEditSessionUiState) {
    set(MORNING_FACTOR_KEY, state.factors.morningFactor)
    set(BREAKFAST_FACTOR_KEY, state.factors.breakfastFactor)
    set(LUNCH_FACTOR_KEY, state.factors.lunchFactor)
    set(AFTERNOON_FACTOR_KEY, state.factors.afternoonFactor)
    set(DINNER_FACTOR_KEY, state.factors.dinnerFactor)
    set(LATE_FACTOR_KEY, state.factors.lateFactor)
    set(NIGHT_FACTOR_KEY, state.factors.nightFactor)
    set(BASAL_RATE_KEY, state.factors.basalRate)
    set(IS_EDIT_MODE_KEY, state.isEditMode)
    set(PENDING_SAVE_KEY, state.pendingSave)
}

private const val MORNING_FACTOR_KEY = "factor_editor_morning"
private const val BREAKFAST_FACTOR_KEY = "factor_editor_breakfast"
private const val LUNCH_FACTOR_KEY = "factor_editor_lunch"
private const val AFTERNOON_FACTOR_KEY = "factor_editor_afternoon"
private const val DINNER_FACTOR_KEY = "factor_editor_dinner"
private const val LATE_FACTOR_KEY = "factor_editor_late"
private const val NIGHT_FACTOR_KEY = "factor_editor_night"
private const val BASAL_RATE_KEY = "factor_editor_basal_rate"
private const val IS_EDIT_MODE_KEY = "factor_editor_is_edit_mode"
private const val PENDING_SAVE_KEY = "factor_editor_pending_save"

