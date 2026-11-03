package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.DoseBreakdown
import sevynidd.diabetesapp.calculation.mgDlToMmolL
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
import sevynidd.diabetesapp.ui.glucoseUnitLabel

/**
 * The pinned result of the current bolus mode: warning banners (low blood sugar, dose above the
 * maximum) followed by the dose to inject with its breakdown. [rawBloodSugar] is the text as the
 * user typed it, so the low-sugar banner can quote it back unchanged.
 */
@Composable
internal fun DoseResultArea(
    dose: DoseResult,
    rawBloodSugar: String,
    correctionSettings: CorrectionSettings,
    currentLanguage: AppLanguage
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (dose.isLowBloodSugar) {
            LowBloodSugarBanner(rawBloodSugar, correctionSettings, currentLanguage)
        }
        if (dose.isOverMaxDose) {
            WarningBanner(
                title = String.format(
                    translate(TranslationKey.MaxDoseExceeded, currentLanguage),
                    correctionSettings.maxDoseUnits.toUiDecimalOrEmpty(),
                    translate(TranslationKey.DoseUnitSymbol, currentLanguage)
                ),
                body = null
            )
        }
        DoseResultCard(dose, correctionSettings, currentLanguage)
    }
}

@Composable
private fun LowBloodSugarBanner(
    rawBloodSugar: String,
    correctionSettings: CorrectionSettings,
    currentLanguage: AppLanguage
) {
    val unit = glucoseUnitLabel(correctionSettings.glucoseUnit)
    val thresholdText = correctionSettings.lowThresholdMgDl.toDisplayGlucose(correctionSettings.glucoseUnit)
    val fastCarbs = correctionSettings.fastCarbsGrams.toUiDecimalOrEmpty() + " " +
        translate(TranslationKey.GramsUnit, currentLanguage)
    val explanation = String.format(
        translate(TranslationKey.LowBloodSugarBody, currentLanguage),
        "$rawBloodSugar $unit",
        "$thresholdText $unit"
    )
    val suggestion = translate(TranslationKey.FastCarbsSuggestion, currentLanguage) + ": " + fastCarbs

    WarningBanner(
        title = translate(TranslationKey.LowBloodSugarTitle, currentLanguage),
        body = explanation + "\n" + suggestion
    )
}

@Composable
private fun WarningBanner(title: String, body: String?) {
    Card(
        modifier = Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Assertive },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Filled.WarningAmber,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                body?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun DoseResultCard(dose: DoseResult, correctionSettings: CorrectionSettings, currentLanguage: AppLanguage) {
    val unitSymbol = translate(TranslationKey.DoseUnitSymbol, currentLanguage)
    val breakdown = dose.breakdown
    val totalText = breakdown?.let { it.roundedTotalUnits.toDoseText() + " " + unitSymbol } ?: "–"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 4.dp, bottom = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = translate(TranslationKey.DoseResultTitle, currentLanguage),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f)
                )
                HelpIconButton(helpTextKey = TranslationKey.DoseResultHelp, currentLanguage = currentLanguage)
            }
            Column(modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
                Text(
                    text = totalText,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                breakdown?.let { BreakdownLines(it, correctionSettings.doseStepUnits, unitSymbol, currentLanguage) }
            }
        }
    }
}

@Composable
private fun BreakdownLines(
    breakdown: DoseBreakdown,
    doseStepUnits: Double,
    unitSymbol: String,
    currentLanguage: AppLanguage
) {
    val color = MaterialTheme.colorScheme.onPrimaryContainer
    Text(
        text = String.format(
            translate(TranslationKey.DoseBreakdown, currentLanguage),
            breakdown.mealUnits.toDoseText(),
            breakdown.correctionUnits.toString(),
            breakdown.exactTotalUnits.toDoseText(),
            unitSymbol
        ),
        style = MaterialTheme.typography.bodyMedium,
        color = color
    )
    if (doseStepUnits > 0.0) {
        Text(
            text = String.format(
                translate(TranslationKey.DoseRoundedTo, currentLanguage),
                doseStepUnits.toUiDecimalOrEmpty(),
                unitSymbol
            ),
            style = MaterialTheme.typography.bodySmall,
            color = color
        )
    }
}

private fun Double.toDoseText(): String = toUiDecimalOrEmpty().ifBlank { "0" }

private fun Double.toDisplayGlucose(unit: GlucoseUnit): String =
    (if (unit == GlucoseUnit.MmolL) mgDlToMmolL(this) else this).toUiDecimalOrEmpty()

@Preview(showBackground = true)
@Composable
private fun DoseResultAreaPreview() {
    val settings = CorrectionSettings()
    DoseResultArea(
        dose = resolveDose(mealUnits = 1.2, rawBloodSugar = "62", correctionSettings = settings),
        rawBloodSugar = "62",
        correctionSettings = settings,
        currentLanguage = AppLanguage.System
    )
}
