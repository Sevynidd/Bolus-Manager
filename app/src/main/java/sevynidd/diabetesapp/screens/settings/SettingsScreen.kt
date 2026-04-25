package sevynidd.diabetesapp.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.localization.TranslationKey
import java.util.Locale

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    currentPeriodeFactorPercent: Double = 0.0,
    onPeriodeFactorPercentChange: (Double) -> Unit = {},
    onNavigateToTheme: () -> Unit = {},
    onNavigateToLanguage: () -> Unit = {},
    onNavigateToBreadUnits: () -> Unit = {}
) {
    var draftPeriodePercent by rememberSaveable(currentPeriodeFactorPercent) {
        mutableStateOf(currentPeriodeFactorPercent.toLocalizedInput())
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = translate(TranslationKey.DestinationSettings, currentLanguage),
            style = MaterialTheme.typography.titleMedium
        )

        SettingsCardItem(
            title = translate(TranslationKey.Appearance, currentLanguage),
            onClick = onNavigateToTheme
        )

        SettingsCardItem(
            title = translate(TranslationKey.Language, currentLanguage),
            onClick = onNavigateToLanguage
        )

        SettingsCardItem(
            title = translate(TranslationKey.BreadUnits, currentLanguage),
            onClick = onNavigateToBreadUnits
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = translate(TranslationKey.PeriodeFactorPercent, currentLanguage),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = draftPeriodePercent,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(PercentageInputRegex)) {
                            draftPeriodePercent = newValue
                            newValue.replace(',', '.')
                                .toDoubleOrNull()
                                ?.takeIf { it >= 0.0 }
                                ?.let(onPeriodeFactorPercentChange)
                        }
                    },
                    label = { Text(translate(TranslationKey.PeriodeFactorPercent, currentLanguage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SettingsCardItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall
            )
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null
            )
        }
    }
}

private fun Double.toLocalizedInput(): String {
    return String.format(Locale.ROOT, "%.2f", this)
        .replace('.', ',')
        .trimEnd('0')
        .trimEnd(',')
}

private val PercentageInputRegex = Regex("^\\d*[.,]?\\d*$")

