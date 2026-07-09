package sevynidd.diabetesapp.screens.settings.language

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

@Composable
fun LanguageSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    onLanguageChange: (AppLanguage) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Column {
                AppLanguage.entries.forEachIndexed { index, language ->
                    if (index != 0) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                    LanguageOptionRow(
                        selected = currentLanguage == language,
                        label = appLanguageLabel(language, currentLanguage),
                        onClick = { onLanguageChange(language) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageOptionRow(
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        headlineContent = {
            Text(text = label, style = MaterialTheme.typography.bodyLarge)
        },
        trailingContent = {
            RadioButton(selected = selected, onClick = onClick)
        }
    )
}

private fun appLanguageLabel(targetLanguage: AppLanguage, currentLanguage: AppLanguage): String {
    return when (targetLanguage) {
        AppLanguage.English -> translate(TranslationKey.LanguageEnglish, currentLanguage)
        AppLanguage.German -> translate(TranslationKey.LanguageGerman, currentLanguage)
        AppLanguage.French -> translate(TranslationKey.LanguageFrench, currentLanguage)
        AppLanguage.Polish -> translate(TranslationKey.LanguagePolish, currentLanguage)
        AppLanguage.System -> translate(TranslationKey.LanguageSystem, currentLanguage)
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageSettingsScreenPreview() {
    LanguageSettingsScreen()
}
