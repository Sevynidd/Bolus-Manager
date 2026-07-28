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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.localization.TranslationKey

private val FooterIconSize = 18.dp
private val FooterIconTextGap = 8.dp
private val ChevronIconSize = 16.dp

/** Navigation callbacks for [SettingsScreen], grouped to keep the screen's own parameter list short. */
data class SettingsNavigationCallbacks(
    val onNavigateToTheme: () -> Unit = {},
    val onNavigateToLanguage: () -> Unit = {},
    val onNavigateToGender: () -> Unit = {},
    val onNavigateToFactorSettings: () -> Unit = {},
    val onNavigateToCorrection: () -> Unit = {},
    val onNavigateToNotifications: () -> Unit = {},
    val onNavigateToDataManagement: () -> Unit = {},
    val onNavigateToStatistics: () -> Unit = {},
    val onNavigateToUpdates: () -> Unit = {},
    val onReplayTutorial: () -> Unit = {}
)

/** One row's icon, title, and click target, used to build the settings list data-driven. */
private data class SettingsEntry(
    val icon: ImageVector,
    val titleKey: TranslationKey,
    val onClick: () -> Unit
)

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    navigation: SettingsNavigationCallbacks = SettingsNavigationCallbacks()
) {
    val entries = listOf(
        SettingsEntry(Icons.Filled.Palette, TranslationKey.Appearance, navigation.onNavigateToTheme),
        SettingsEntry(Icons.Filled.Language, TranslationKey.Language, navigation.onNavigateToLanguage),
        SettingsEntry(Icons.Filled.Wc, TranslationKey.GenderSettingsTitle, navigation.onNavigateToGender),
        SettingsEntry(
            Icons.Filled.ImportExport,
            TranslationKey.DataManagementTitle,
            navigation.onNavigateToDataManagement
        ),
        SettingsEntry(
            Icons.Filled.Notifications,
            TranslationKey.NotificationSettingsTitle,
            navigation.onNavigateToNotifications
        ),
        SettingsEntry(Icons.Filled.Tune, TranslationKey.FactorSettingsTitle, navigation.onNavigateToFactorSettings),
        SettingsEntry(
            Icons.Filled.Bloodtype,
            TranslationKey.CorrectionSettingsTitle,
            navigation.onNavigateToCorrection
        ),
        SettingsEntry(
            Icons.Filled.Assessment,
            TranslationKey.StatisticsSettingsTitle,
            navigation.onNavigateToStatistics
        ),
        SettingsEntry(Icons.Filled.History, TranslationKey.ReplayTutorial, navigation.onReplayTutorial)
    )

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsGroupCard {
                entries.forEachIndexed { index, entry ->
                    if (index != 0) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    }
                    SettingsNavigationItem(
                        icon = entry.icon,
                        title = translate(entry.titleKey, currentLanguage),
                        onClick = entry.onClick
                    )
                }
            }
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

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen()
}
