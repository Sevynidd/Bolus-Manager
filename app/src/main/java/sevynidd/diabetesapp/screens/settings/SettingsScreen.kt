package sevynidd.diabetesapp.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.localization.TranslationKey
import java.util.Locale

private val FooterIconSize = 18.dp
private val FooterIconTextGap = 8.dp
private val ChevronIconSize = 16.dp

/** Navigation callbacks for [SettingsScreen], grouped to keep the screen's own parameter list short. */
data class SettingsNavigationCallbacks(
    val onNavigateToTheme: () -> Unit = {},
    val onNavigateToLanguage: () -> Unit = {},
    val onNavigateToBreadUnits: () -> Unit = {},
    val onNavigateToNotifications: () -> Unit = {},
    val onNavigateToUpdates: () -> Unit = {}
)

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    currentPeriodFactorPercent: Double = 0.0,
    onPeriodFactorPercentChange: (Double) -> Unit = {},
    navigation: SettingsNavigationCallbacks = SettingsNavigationCallbacks()
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsGroupCard {
                SettingsNavigationItem(
                    icon = Icons.Filled.Palette,
                    title = translate(TranslationKey.Appearance, currentLanguage),
                    onClick = navigation.onNavigateToTheme
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                SettingsNavigationItem(
                    icon = Icons.Filled.Language,
                    title = translate(TranslationKey.Language, currentLanguage),
                    onClick = navigation.onNavigateToLanguage
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                SettingsNavigationItem(
                    icon = Icons.Filled.Grain,
                    title = translate(TranslationKey.BreadUnits, currentLanguage),
                    onClick = navigation.onNavigateToBreadUnits
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                SettingsNavigationItem(
                    icon = Icons.Filled.Notifications,
                    title = translate(TranslationKey.NotificationSettingsTitle, currentLanguage),
                    onClick = navigation.onNavigateToNotifications
                )
            }

            PeriodFactorCard(
                currentLanguage = currentLanguage,
                currentPeriodFactorPercent = currentPeriodFactorPercent,
                onPeriodFactorPercentChange = onPeriodFactorPercentChange
            )
        }

        AppUpdateFooterLink(
            currentLanguage = currentLanguage,
            onClick = navigation.onNavigateToUpdates
        )
    }
}

@Composable
private fun SettingsGroupCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(content = content)
    }
}

@Composable
private fun SettingsNavigationItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        headlineContent = {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(ChevronIconSize)
            )
        }
    )
}

@Composable
private fun PeriodFactorCard(
    currentLanguage: AppLanguage,
    currentPeriodFactorPercent: Double,
    onPeriodFactorPercentChange: (Double) -> Unit
) {
    var draftPeriodPercent by rememberSaveable(currentPeriodFactorPercent) {
        mutableStateOf(currentPeriodFactorPercent.toLocalizedInput())
    }

    SettingsGroupCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    imageVector = Icons.Filled.Percent,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = translate(TranslationKey.PeriodFactorPercent, currentLanguage),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            OutlinedTextField(
                value = draftPeriodPercent,
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.matches(PercentageInputRegex)) {
                        draftPeriodPercent = newValue
                        newValue.replace(',', '.')
                            .toDoubleOrNull()
                            ?.takeIf { it >= 0.0 }
                            ?.let(onPeriodFactorPercentChange)
                    }
                },
                label = { Text(translate(TranslationKey.PeriodFactorPercent, currentLanguage)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * A low-emphasis footer link pinned below the scrollable settings list, deliberately styled
 * unlike [SettingsNavigationItem] (no card background, smaller centered text) so it doesn't read
 * as just another settings entry.
 */
@Composable
private fun AppUpdateFooterLink(
    currentLanguage: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Update,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(FooterIconSize)
            )
            Spacer(modifier = Modifier.width(FooterIconTextGap))
            Text(
                text = translate(TranslationKey.AppUpdateTitle, currentLanguage),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen()
}
