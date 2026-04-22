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
            basalRate = get<String>(BASAL_RATE_KEY).orEmpty(),
            morningTimeMinutes = get<Int>(MORNING_TIME_KEY) ?: (5 * 60),
            breakfastTimeMinutes = get<Int>(BREAKFAST_TIME_KEY) ?: (9 * 60),
            lunchTimeMinutes = get<Int>(LUNCH_TIME_KEY) ?: (12 * 60),
            afternoonTimeMinutes = get<Int>(AFTERNOON_TIME_KEY) ?: (14 * 60),
            dinnerTimeMinutes = get<Int>(DINNER_TIME_KEY) ?: (17 * 60),
            lateTimeMinutes = get<Int>(LATE_TIME_KEY) ?: (20 * 60),
            nightTimeMinutes = get<Int>(NIGHT_TIME_KEY) ?: (23 * 60),
            basalTimeMinutes = get<Int>(BASAL_TIME_KEY) ?: (19 * 60)
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
    set(MORNING_TIME_KEY, state.factors.morningTimeMinutes)
    set(BREAKFAST_TIME_KEY, state.factors.breakfastTimeMinutes)
    set(LUNCH_TIME_KEY, state.factors.lunchTimeMinutes)
    set(AFTERNOON_TIME_KEY, state.factors.afternoonTimeMinutes)
    set(DINNER_TIME_KEY, state.factors.dinnerTimeMinutes)
    set(LATE_TIME_KEY, state.factors.lateTimeMinutes)
    set(NIGHT_TIME_KEY, state.factors.nightTimeMinutes)
    set(BASAL_TIME_KEY, state.factors.basalTimeMinutes)
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
private const val MORNING_TIME_KEY = "factor_editor_morning_time"
private const val BREAKFAST_TIME_KEY = "factor_editor_breakfast_time"
private const val LUNCH_TIME_KEY = "factor_editor_lunch_time"
private const val AFTERNOON_TIME_KEY = "factor_editor_afternoon_time"
private const val DINNER_TIME_KEY = "factor_editor_dinner_time"
private const val LATE_TIME_KEY = "factor_editor_late_time"
private const val NIGHT_TIME_KEY = "factor_editor_night_time"
private const val BASAL_TIME_KEY = "factor_editor_basal_time"
private const val IS_EDIT_MODE_KEY = "factor_editor_is_edit_mode"
private const val PENDING_SAVE_KEY = "factor_editor_pending_save"

