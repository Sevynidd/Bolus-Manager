package sevynidd.diabetesapp.screens.factors

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.formatUnits
import sevynidd.diabetesapp.calculation.normalizeHourlyBasalRateValue
import sevynidd.diabetesapp.calculation.pumpBasalDailyTotal
import sevynidd.diabetesapp.calculation.pumpBasalHourRangeLabel
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton

/**
 * The insulin pump's 24 hourly basal rates (U/h) with the daily total. The hour list starts
 * collapsed — the daily total stays visible — and expands from the header.
 */
@Composable
internal fun PumpBasalRatesCard(currentLanguage: AppLanguage, state: FactorScreenState, nowHour: Int) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PumpBasalRatesHeader(currentLanguage, isExpanded) {
                // Commit an in-progress edit via its focus-loss handler before collapsing removes the field.
                focusManager.clearFocus(force = true)
                isExpanded = !isExpanded
            }

            AnimatedVisibility(visible = isExpanded) {
                PumpBasalHourFields(currentLanguage, state, nowHour)
            }

            PumpBasalDailyTotalText(currentLanguage, state.factors.pumpBasalRates)
        }
    }
}

@Composable
private fun PumpBasalHourFields(currentLanguage: AppLanguage, state: FactorScreenState, nowHour: Int) {
    val unitLabel = translate(TranslationKey.PumpBasalRateUnit, currentLanguage)
    val activeNowLabel = translate(TranslationKey.ActiveNowBadge, currentLanguage)

    fun updateRate(hour: Int, value: String) {
        state.onFactorsChange { current ->
            current.copy(pumpBasalRates = current.pumpBasalRates.toMutableList().apply { this[hour] = value })
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        state.factors.pumpBasalRates.forEachIndexed { hour, rate ->
            DoubleInputField(
                value = rate,
                onValueChange = { updateRate(hour, it) },
                labels = FieldLabels(
                    label = unitLabel,
                    description = pumpBasalHourRangeLabel(hour),
                    activeNowLabel = activeNowLabel.takeIf { hour == nowHour }
                ),
                enabled = state.isEditMode,
                normalize = ::normalizeHourlyBasalRateValue
            )
        }
    }
}

@Composable
private fun PumpBasalRatesHeader(currentLanguage: AppLanguage, isExpanded: Boolean, onToggle: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
    ) {
        Text(
            text = translate(TranslationKey.PumpBasalRatesTitle, currentLanguage),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        HelpIconButton(helpTextKey = TranslationKey.PumpBasalRatesHelp, currentLanguage = currentLanguage)
        IconButton(onClick = onToggle) {
            Icon(
                imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = translate(
                    if (isExpanded) TranslationKey.ActionHideHours else TranslationKey.ActionShowHours,
                    currentLanguage
                )
            )
        }
    }
}

@Composable
private fun PumpBasalDailyTotalText(currentLanguage: AppLanguage, rates: List<String>) {
    val total = pumpBasalDailyTotal(rates)?.let(::formatUnits) ?: "-"
    Text(
        text = translate(TranslationKey.PumpBasalDailyTotal, currentLanguage).replace(TOTAL_PLACEHOLDER, total),
        style = MaterialTheme.typography.titleSmall
    )
}

private const val TOTAL_PLACEHOLDER = $$"%1$s"
