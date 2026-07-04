package sevynidd.diabetesapp.data.update

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

private const val POLL_INTERVAL_MILLIS = 300L

/** The APK download's current state, as observed from Android's [DownloadManager]. */
sealed interface ApkDownloadStatus {
    /** Still downloading; [bytesTotal] is `-1` if the server didn't report a content length. */
    data class InProgress(val bytesDownloaded: Long, val bytesTotal: Long) : ApkDownloadStatus

    /** Download finished; [fileUri] is an installable `content://` URI granted via FileProvider. */
    data class Succeeded(val fileUri: Uri) : ApkDownloadStatus

    /** The download was cancelled or failed. */
    data object Failed : ApkDownloadStatus
}

/** Downloads a release APK via [DownloadManager] and launches the system installer for it. */
class ApkInstaller(private val context: Context) {

    /** Enqueues [release]'s APK for download and returns the [DownloadManager] request id. */
    fun startDownload(release: GitHubRelease): Long {
        val destinationFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
            release.apkFileName
        )
        if (destinationFile.exists()) {
            destinationFile.delete()
        }

        val downloadManager = context.downloadManager()
        val request = DownloadManager.Request(release.apkDownloadUrl.toUri())
            .setTitle(release.apkFileName)
            .setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                release.apkFileName
            )
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        return downloadManager.enqueue(request)
    }

    /** Polls [DownloadManager] for [downloadId]'s progress until it succeeds or fails. */
    fun observeDownload(downloadId: Long): Flow<ApkDownloadStatus> = flow {
        val downloadManager = context.downloadManager()

        while (true) {
            val status = downloadManager
                .query(DownloadManager.Query().setFilterById(downloadId))
                .use { it.toDownloadStatus() }

            emit(status)
            if (status !is ApkDownloadStatus.InProgress) break
            delay(POLL_INTERVAL_MILLIS)
        }
    }

    private fun Cursor.toDownloadStatus(): ApkDownloadStatus {
        if (!moveToFirst()) return ApkDownloadStatus.Failed

        return when (getInt(getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))) {
            DownloadManager.STATUS_SUCCESSFUL -> {
                val localUri = getString(getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI)).toUri()
                ApkDownloadStatus.Succeeded(fileUri = localUri.toInstallableUri())
            }

            DownloadManager.STATUS_FAILED -> ApkDownloadStatus.Failed

            else -> ApkDownloadStatus.InProgress(
                bytesDownloaded = getLong(getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)),
                bytesTotal = getLong(getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
            )
        }
    }

    private fun Uri.toInstallableUri(): Uri {
        // DownloadManager's local URI is a file:// path; the package installer needs a
        // content:// URI from our FileProvider to be granted temporary read access to it.
        val file = File(requireNotNull(path))
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    /** Launches the system package installer for the downloaded APK at [apkUri]. */
    fun requestInstall(apkUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(apkUri, "application/vnd.android.package-archive")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
    }

    /** Whether the user has already granted permission to install APKs from this app. */
    fun canRequestPackageInstalls(): Boolean = context.packageManager.canRequestPackageInstalls()

    /** Opens system settings so the user can grant the "install unknown apps" permission. */
    fun requestInstallPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
            data = "package:${context.packageName}".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun Context.downloadManager(): DownloadManager {
        return getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
}
