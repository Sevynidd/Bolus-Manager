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
import sevynidd.diabetesapp.ui.theme.warningColors

/**
 * The pinned result of the current bolus mode: warning banners (low blood sugar, dose above the
 * maximum) followed by the dose to inject with its breakdown. [rawBloodSugar] is the text as the
 * user typed it, so the low-sugar banner can quote it back unchanged; a non-blank
 * [activeFactorText] is shown under the dose.
 */
@Composable
internal fun DoseResultArea(
    dose: DoseResult,
    rawBloodSugar: String,
    activeFactorText: String,
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
                body = null,
                isCaution = true
            )
        }
        DoseResultCard(dose, activeFactorText, correctionSettings, currentLanguage)
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
private fun WarningBanner(title: String, body: String?, isCaution: Boolean = false) {
    val caution = MaterialTheme.warningColors
    val containerColor = if (isCaution) caution.container else MaterialTheme.colorScheme.errorContainer
    val contentColor = if (isCaution) caution.onContainer else MaterialTheme.colorScheme.onErrorContainer

    Card(
        modifier = Modifier.fillMaxWidth().semantics { liveRegion = LiveRegionMode.Assertive },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Filled.WarningAmber,
                contentDescription = null,
                tint = contentColor
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = contentColor
                )
                body?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Composable
private fun DoseResultCard(
    dose: DoseResult,
    activeFactorText: String,
    correctionSettings: CorrectionSettings,
    currentLanguage: AppLanguage
) {
    val unitSymbol = translate(TranslationKey.DoseUnitSymbol, currentLanguage)
    val breakdown = dose.breakdown
    val totalText = breakdown?.let { it.roundedTotalUnits.toDoseText() + " " + unitSymbol } ?: "–"
    val isBlocked = dose.blocker != null
    val containerColor =
        if (isBlocked) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.primaryContainer
    val contentColor =
        if (isBlocked) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 4.dp, bottom = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = translate(TranslationKey.DoseResultTitle, currentLanguage),
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                    modifier = Modifier.weight(1f)
                )
                HelpIconButton(helpTextKey = TranslationKey.DoseResultHelp, currentLanguage = currentLanguage)
            }
            Column(modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
                Text(
                    text = totalText,
                    style = MaterialTheme.typography.displaySmall,
                    color = contentColor
                )
                dose.blocker?.let { blocker ->
                    Text(
                        text = blockerMessage(blocker, correctionSettings, currentLanguage),
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
                }
                breakdown?.let {
                    BreakdownLines(it, correctionSettings.doseStepUnits, unitSymbol, currentLanguage)
                }
                if (activeFactorText.isNotBlank()) {
                    Text(
                        text = translate(TranslationKey.ActiveFactor, currentLanguage) + ": " + activeFactorText,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor
                    )
                }
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

private fun blockerMessage(
    blocker: DoseBlocker,
    correctionSettings: CorrectionSettings,
    currentLanguage: AppLanguage
): String = when (blocker) {
    DoseBlocker.BloodSugarTooHigh -> String.format(
        translate(TranslationKey.DoseBlockedBloodSugar, currentLanguage),
        correctionSettings.maxBloodSugarMgDl.toDisplayGlucose(correctionSettings.glucoseUnit),
        glucoseUnitLabel(correctionSettings.glucoseUnit)
    )
    DoseBlocker.CarbohydratesTooHigh -> String.format(
        translate(TranslationKey.DoseBlockedCarbohydrates, currentLanguage),
        correctionSettings.maxCarbohydratesGrams.toUiDecimalOrEmpty(),
        translate(TranslationKey.GramsUnit, currentLanguage)
    )
    DoseBlocker.NoFactor -> translate(TranslationKey.DoseBlockedNoFactor, currentLanguage)
}

private fun Double.toDoseText(): String = toUiDecimalOrEmpty().ifBlank { "0" }

private fun Double.toDisplayGlucose(unit: GlucoseUnit): String =
    (if (unit == GlucoseUnit.MmolL) mgDlToMmolL(this) else this).toUiDecimalOrEmpty()
