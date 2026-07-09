package sevynidd.diabetesapp.screens.settings.notifications

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import sevynidd.diabetesapp.data.notifications.BasalReminderScheduler
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

/**
 * Toggle for the daily basal-rate reminder notification. Requests the `POST_NOTIFICATIONS`
 * runtime permission when turned on (API 33+), and offers a way to grant the exact-alarm
 * permission (required for the reminder to fire at the precise configured time) if it's missing,
 * re-checked whenever the screen resumes since that permission is granted via system Settings.
 */
@Composable
internal fun BasalReminderCard(
    currentLanguage: AppLanguage,
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    // Android Studio's @Preview renderer has no ActivityResultRegistryOwner, so the real
    // scheduler/permission launcher are skipped there to keep the preview from crashing.
    val isPreview = LocalInspectionMode.current
    val scheduler = remember(context, isPreview) { if (isPreview) null else BasalReminderScheduler(context) }
    val hasExactAlarmPermission = rememberExactAlarmPermission(scheduler)
    val notificationPermissionLauncher = rememberNotificationPermissionLauncher(isPreview)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = translate(TranslationKey.BasalReminderEnabled, currentLanguage),
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = isEnabled,
                    onCheckedChange = { checked ->
                        onEnabledChange(checked)
                        if (checked && !context.hasNotificationPermission()) {
                            notificationPermissionLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                )
            }

            if (isEnabled && !hasExactAlarmPermission && scheduler != null) {
                TextButton(onClick = scheduler::requestExactAlarmPermission) {
                    Text(translate(TranslationKey.BasalReminderExactAlarmHint, currentLanguage))
                }
            }
        }
    }
}

@Composable
private fun rememberExactAlarmPermission(scheduler: BasalReminderScheduler?): Boolean {
    val lifecycleOwner = LocalLifecycleOwner.current
    var resumeTick by remember { mutableIntStateOf(0) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                resumeTick++
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    return remember(resumeTick, scheduler) { scheduler?.canScheduleExactAlarms() ?: true }
}

@Composable
private fun rememberNotificationPermissionLauncher(isPreview: Boolean): ActivityResultLauncher<String>? {
    // The result is ignored: the switch already reflects `isEnabled` regardless of the grant
    // outcome, and BasalReminderNotifier silently skips posting if permission was denied.
    return if (isPreview) {
        null
    } else {
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
    }
}

private fun Context.hasNotificationPermission(): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true

    return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
        PackageManager.PERMISSION_GRANTED
}
