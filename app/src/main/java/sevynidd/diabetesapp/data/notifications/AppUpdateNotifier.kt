package sevynidd.diabetesapp.data.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/** Notification channel id for the "app update available" notification. */
const val APP_UPDATE_CHANNEL_ID = "app_update"

/** Intent extra on the notification's tap target, telling `MainActivity` to open the update screen. */
const val EXTRA_OPEN_APP_UPDATE = "sevynidd.diabetesapp.extra.OPEN_APP_UPDATE"

private const val APP_UPDATE_NOTIFICATION_ID = 1002
private const val APP_UPDATE_REQUEST_CODE = 2002

/** Creates the app-update notification channel and posts the "update available" notification. */
object AppUpdateNotifier {

    /** Creates the app-update [NotificationChannel] with [channelName], if not already present. */
    fun ensureChannel(context: Context, channelName: String) {
        val channel = NotificationChannel(
            APP_UPDATE_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    /**
     * Posts (or refreshes) the "update available" notification for [versionTag]. Both the
     * notification body and its [actionLabel] action button reopen the app straight at the update
     * screen. Does nothing if notification permission hasn't been granted.
     */
    fun show(context: Context, title: String, body: String, versionTag: String, actionLabel: String) {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) return

        val contentIntent = openAppUpdatePendingIntent(context)
        val notification = NotificationCompat.Builder(context, APP_UPDATE_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download_done)
            .setContentTitle(title)
            .setContentText("$body $versionTag")
            .setContentIntent(contentIntent)
            .addAction(0, actionLabel, contentIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(APP_UPDATE_NOTIFICATION_ID, notification)
    }

    private fun openAppUpdatePendingIntent(context: Context): PendingIntent {
        val launchIntent = requireNotNull(context.packageManager.getLaunchIntentForPackage(context.packageName)) {
            "The app's own launcher intent must exist while the app itself is running"
        }
        launchIntent.putExtra(EXTRA_OPEN_APP_UPDATE, true)
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        return PendingIntent.getActivity(
            context,
            APP_UPDATE_REQUEST_CODE,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
