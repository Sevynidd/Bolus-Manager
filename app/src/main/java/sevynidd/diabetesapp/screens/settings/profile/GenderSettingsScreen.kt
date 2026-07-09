package sevynidd.diabetesapp.screens.settings.profile

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
import sevynidd.diabetesapp.data.settings.profile.Gender
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

/** Lets the user pick their [Gender], which gates whether Period-related settings apply. */
@Composable
fun GenderSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    currentGender: Gender = Gender.PreferNotToSay,
    onGenderChange: (Gender) -> Unit = {}
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
                Gender.entries.forEachIndexed { index, gender ->
                    if (index != 0) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                    GenderOptionRow(
                        selected = currentGender == gender,
                        label = genderLabel(gender, currentLanguage),
                        onClick = { onGenderChange(gender) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GenderOptionRow(
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

private fun genderLabel(gender: Gender, currentLanguage: AppLanguage): String {
    return when (gender) {
        Gender.Male -> translate(TranslationKey.GenderMale, currentLanguage)
        Gender.Female -> translate(TranslationKey.GenderFemale, currentLanguage)
        Gender.PreferNotToSay -> translate(TranslationKey.GenderPreferNotToSay, currentLanguage)
    }
}

@Preview(showBackground = true)
@Composable
private fun GenderSettingsScreenPreview() {
    GenderSettingsScreen()
}
