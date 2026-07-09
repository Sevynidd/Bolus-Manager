package sevynidd.diabetesapp.data.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.data.AppSettingsStore
import sevynidd.diabetesapp.data.database.DiabetesDatabase
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

/** Action broadcast by [BasalReminderScheduler] when the daily basal-reminder alarm fires. */
const val ACTION_BASAL_REMINDER_FIRED = "sevynidd.diabetesapp.action.BASAL_REMINDER_FIRED"

/**
 * Handles the basal-reminder alarm firing and device boot: re-reads the persisted factor profile
 * (rather than trusting any data carried on the [Intent], since it may be stale after the app was
 * killed), posts the notification on an alarm fire, and re-arms tomorrow's alarm in both cases —
 * alarms don't survive a reboot, so [android.content.Intent.ACTION_BOOT_COMPLETED] needs the same
 * rescheduling as a normal fire, just without notifying. Any intent whose action isn't one of
 * these two expected values is rejected immediately, rather than trusting an arbitrary broadcast.
 */
class BasalReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_BASAL_REMINDER_FIRED && intent.action != Intent.ACTION_BOOT_COMPLETED) {
            return
        }

        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                handle(context, intent)
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun handle(context: Context, intent: Intent) {
        val dao = DiabetesDatabase.getInstance(context).factorProfileDao()
        val profile = dao.observeProfile().first()
        val basalTimeMinutes = profile?.basalTimeMinutes

        if (profile?.basalReminderEnabled != true || basalTimeMinutes == null) return

        if (intent.action == ACTION_BASAL_REMINDER_FIRED) {
            val language = AppSettingsStore(context).settingsFlow.first().language
            BasalReminderNotifier.ensureChannel(
                context,
                translate(TranslationKey.BasalReminderChannelName, language)
            )
            BasalReminderNotifier.show(
                context,
                title = translate(TranslationKey.BasalReminderNotificationTitle, language),
                body = translate(TranslationKey.BasalReminderNotificationBody, language)
            )
        }

        BasalReminderScheduler(context).scheduleDaily(basalTimeMinutes)
    }
}
