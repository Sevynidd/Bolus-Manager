package sevynidd.diabetesapp.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.settings.ThemeMode
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.ui.theme.ContrastLevel

@Composable
fun ThemeSettingsScreen(
    modifier: Modifier = Modifier,
    currentThemeMode: ThemeMode = ThemeMode.System,
    currentContrastLevel: ContrastLevel = ContrastLevel.Normal,
    currentLanguage: AppLanguage = AppLanguage.System,
    onThemeModeChange: (ThemeMode) -> Unit = {},
    onContrastLevelChange: (ContrastLevel) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = translate(TranslationKey.ThemeMode, currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ThemeMode.entries.forEachIndexed { index, mode ->
                    SettingsRadioOption(
                        selected = currentThemeMode == mode,
                        label = themeModeLabel(mode, currentLanguage),
                        onClick = { onThemeModeChange(mode) }
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = translate(TranslationKey.ContrastLevel, currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ContrastLevel.entries.forEachIndexed { index, level ->
                    SettingsRadioOption(
                        selected = currentContrastLevel == level,
                        label = contrastLevelLabel(level, currentLanguage),
                        onClick = { onContrastLevelChange(level) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsRadioOption(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

private fun themeModeLabel(mode: ThemeMode, language: AppLanguage): String {
    return when (mode) {
        ThemeMode.System -> translate(TranslationKey.ThemeSystem, language)
        ThemeMode.Light -> translate(TranslationKey.ThemeLight, language)
        ThemeMode.Dark -> translate(TranslationKey.ThemeDark, language)
    }
}

private fun contrastLevelLabel(level: ContrastLevel, language: AppLanguage): String {
    return when (level) {
        ContrastLevel.Normal -> translate(TranslationKey.ContrastNormal, language)
        ContrastLevel.MediumContrast -> translate(TranslationKey.ContrastMedium, language)
        ContrastLevel.HighContrast -> translate(TranslationKey.ContrastHigh, language)
    }
}

