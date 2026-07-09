package sevynidd.diabetesapp.screens.settings.notifications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.localization.AppLanguage

/** Notification-related preferences: currently just the daily basal-rate reminder. */
@Composable
fun NotificationSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    isBasalReminderEnabled: Boolean = false,
    onBasalReminderEnabledChange: (Boolean) -> Unit = {}
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BasalReminderCard(
            currentLanguage = currentLanguage,
            isEnabled = isBasalReminderEnabled,
            onEnabledChange = onBasalReminderEnabledChange
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationSettingsScreenPreview() {
    NotificationSettingsScreen()
}
