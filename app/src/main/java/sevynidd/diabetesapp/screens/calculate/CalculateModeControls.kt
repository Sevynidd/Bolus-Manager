package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton

@Composable
internal fun PeriodToggleRow(
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
internal fun BolusModeSelector(
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

