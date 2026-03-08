package sevynidd.diabetesapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.ui.theme.ContrastLevel


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    currentThemeMode: ThemeMode = ThemeMode.System,
    currentContrastLevel: ContrastLevel = ContrastLevel.Normal,
    currentLanguage: AppLanguage = AppLanguage.System,
    onThemeModeChange: (ThemeMode) -> Unit = {},
    onContrastLevelChange: (ContrastLevel) -> Unit = {},
    onLanguageChange: (AppLanguage) -> Unit = {}
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = translate(TranslationKey.Appearance, currentLanguage),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Theme Mode Section
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = translate(TranslationKey.ThemeMode, currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ThemeMode.entries.forEach { mode ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onThemeModeChange(mode) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentThemeMode == mode,
                            onClick = { onThemeModeChange(mode) }
                        )
                        Text(
                            text = themeModeLabel(mode, currentLanguage),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        // Contrast Level Section
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = translate(TranslationKey.ContrastLevel, currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ContrastLevel.entries.forEach { level ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onContrastLevelChange(level) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentContrastLevel == level,
                            onClick = { onContrastLevelChange(level) }
                        )
                        Text(
                            text = contrastLevelLabel(level, currentLanguage),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        // Language Section
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = translate(TranslationKey.Language, currentLanguage),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                AppLanguage.entries.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageChange(language) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentLanguage == language,
                            onClick = { onLanguageChange(language) }
                        )
                        Text(
                            text = appLanguageLabel(language, currentLanguage),
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
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

private fun appLanguageLabel(targetLanguage: AppLanguage, currentLanguage: AppLanguage): String {
    return when (targetLanguage) {
        AppLanguage.English -> translate(TranslationKey.LanguageEnglish, currentLanguage)
        AppLanguage.German -> translate(TranslationKey.LanguageGerman, currentLanguage)
        AppLanguage.System -> translate(
            TranslationKey.LanguageSystem,
            currentLanguage
        )
    }
}
