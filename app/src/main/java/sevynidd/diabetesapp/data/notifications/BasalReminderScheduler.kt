package sevynidd.diabetesapp.data.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri
import sevynidd.diabetesapp.calculation.nextDailyTriggerEpochMillis
import java.time.ZonedDateTime

/** Schedules and cancels the daily exact alarm that triggers the basal-rate reminder. */
class BasalReminderScheduler(private val context: Context) {

    /**
     * Arms an alarm for the next occurrence of [basalTimeMinutes] (minutes since midnight).
     * Uses an exact alarm when [canScheduleExactAlarms] allows it; otherwise falls back to an
     * inexact alarm rather than calling `setExactAndAllowWhileIdle`, which throws a
     * `SecurityException` on API 31+ without that permission (it isn't auto-granted on API 33+).
     */
    fun scheduleDaily(basalTimeMinutes: Int) {
        val triggerAtMillis = nextDailyTriggerEpochMillis(basalTimeMinutes, ZonedDateTime.now())
        val pendingIntent = requireNotNull(alarmPendingIntent(PendingIntent.FLAG_UPDATE_CURRENT)) {
            "PendingIntent.getBroadcast only returns null with FLAG_NO_CREATE"
        }
        val alarmManager = context.alarmManager()
        if (canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    /** Cancels any currently pending basal-reminder alarm. */
    fun cancel() {
        val pendingIntent = alarmPendingIntent(PendingIntent.FLAG_NO_CREATE) ?: return
        context.alarmManager().cancel(pendingIntent)
        pendingIntent.cancel()
    }

    /** Whether the app currently holds permission to schedule exact alarms. */
    fun canScheduleExactAlarms(): Boolean = context.alarmManager().canScheduleExactAlarms()

    /** Opens system settings so the user can grant the exact-alarm permission. */
    fun requestExactAlarmPermission() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = "package:${context.packageName}".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun alarmPendingIntent(flags: Int): PendingIntent? {
        val intent = Intent(context, BasalReminderReceiver::class.java).apply {
            action = ACTION_BASAL_REMINDER_FIRED
            setPackage(context.packageName)
        }
        return PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            flags or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun Context.alarmManager(): AlarmManager {
        return getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
}

private const val ALARM_REQUEST_CODE = 2001
