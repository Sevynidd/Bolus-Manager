package sevynidd.diabetesapp.data.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/** Notification channel id and id for the daily basal-rate reminder notification. */
const val BASAL_REMINDER_CHANNEL_ID = "basal_reminder"
private const val BASAL_REMINDER_NOTIFICATION_ID = 1001

/** Creates the basal-reminder notification channel and posts the reminder itself. */
object BasalReminderNotifier {

    /** Creates the basal-reminder [NotificationChannel] with [channelName], if not already present. */
    fun ensureChannel(context: Context, channelName: String) {
        val channel = NotificationChannel(
            BASAL_REMINDER_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    /** Posts the basal-reminder notification with [title]/[body], if permission was granted. */
    fun show(context: Context, title: String, body: String) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return

        val notification = NotificationCompat.Builder(context, BASAL_REMINDER_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(BASAL_REMINDER_NOTIFICATION_ID, notification)
    }
}
