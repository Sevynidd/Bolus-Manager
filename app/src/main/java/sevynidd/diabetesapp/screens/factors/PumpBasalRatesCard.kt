package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.formatUnits
import sevynidd.diabetesapp.calculation.normalizeHourlyBasalRateValue
import sevynidd.diabetesapp.calculation.pumpBasalDailyTotal
import sevynidd.diabetesapp.calculation.pumpBasalHourRangeLabel
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton

/** The insulin pump's 24 hourly basal rates (U/h), with the current hour highlighted and the daily total. */
@Composable
internal fun PumpBasalRatesCard(currentLanguage: AppLanguage, state: FactorScreenState, nowHour: Int) {
    val rates = state.factors.pumpBasalRates
    val unitLabel = translate(TranslationKey.PumpBasalRateUnit, currentLanguage)
    val activeNowLabel = translate(TranslationKey.ActiveNowBadge, currentLanguage)

    fun updateRate(hour: Int, value: String) {
        state.onFactorsChange { current ->
            current.copy(pumpBasalRates = current.pumpBasalRates.toMutableList().apply { this[hour] = value })
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            PumpBasalRatesHeader(currentLanguage)

            rates.forEachIndexed { hour, rate ->
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

            PumpBasalDailyTotalText(currentLanguage, rates)
        }
    }
}

@Composable
private fun PumpBasalRatesHeader(currentLanguage: AppLanguage) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = translate(TranslationKey.PumpBasalRatesTitle, currentLanguage),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        HelpIconButton(helpTextKey = TranslationKey.PumpBasalRatesHelp, currentLanguage = currentLanguage)
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
