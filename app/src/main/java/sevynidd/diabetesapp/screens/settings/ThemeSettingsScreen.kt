package sevynidd.diabetesapp.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
        ThemeModeCard(
            currentThemeMode = currentThemeMode,
            currentLanguage = currentLanguage,
            onThemeModeChange = onThemeModeChange
        )

        ContrastLevelCard(
            currentContrastLevel = currentContrastLevel,
            currentLanguage = currentLanguage,
            onContrastLevelChange = onContrastLevelChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeModeCard(
    currentThemeMode: ThemeMode,
    currentLanguage: AppLanguage,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = translate(TranslationKey.ThemeMode, currentLanguage),
                style = MaterialTheme.typography.titleMedium
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                ThemeMode.entries.forEachIndexed { index, mode ->
                    val shape = SegmentedButtonDefaults.itemShape(index, ThemeMode.entries.size)
                    SegmentedButton(
                        shape = shape,
                        selected = currentThemeMode == mode,
                        onClick = { onThemeModeChange(mode) },
                        label = { Text(themeModeLabel(mode, currentLanguage)) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContrastLevelCard(
    currentContrastLevel: ContrastLevel,
    currentLanguage: AppLanguage,
    onContrastLevelChange: (ContrastLevel) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = translate(TranslationKey.ContrastLevel, currentLanguage),
                style = MaterialTheme.typography.titleMedium
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                ContrastLevel.entries.forEachIndexed { index, level ->
                    val shape = SegmentedButtonDefaults.itemShape(index, ContrastLevel.entries.size)
                    SegmentedButton(
                        shape = shape,
                        selected = currentContrastLevel == level,
                        onClick = { onContrastLevelChange(level) },
                        label = { Text(contrastLevelLabel(level, currentLanguage)) }
                    )
                }
            }
        }
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

@Preview(showBackground = true)
@Composable
private fun ThemeSettingsScreenPreview() {
    ThemeSettingsScreen()
}
