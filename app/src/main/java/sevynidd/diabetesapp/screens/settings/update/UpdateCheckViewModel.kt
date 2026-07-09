package sevynidd.diabetesapp.screens.settings.update

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.calculation.isNewerVersion
import sevynidd.diabetesapp.data.update.ApkDownloadStatus
import sevynidd.diabetesapp.data.update.ApkInstaller
import sevynidd.diabetesapp.data.update.GitHubRelease
import sevynidd.diabetesapp.data.update.GitHubReleaseRepository

private const val REPO_OWNER = "Sevynidd"
private const val REPO_NAME = "Bolus-Manager"

/** The project's GitHub repository, for linking out to it from the update screen. */
internal const val GITHUB_REPO_URL = "https://github.com/$REPO_OWNER/$REPO_NAME"

/** Which phase of the check/download/install flow [UpdateCheckUiState.phase] currently reflects. */
enum class UpdateCheckPhase {
    Idle,
    Checking,
    UpToDate,
    UpdateAvailable,
    Downloading,
    ReadyToInstall,
    Error
}

/** The in-app updater's current state: the installed version and where the check/download stands. */
data class UpdateCheckUiState(
    val phase: UpdateCheckPhase = UpdateCheckPhase.Idle,
    val currentVersion: String = "",
    val latestRelease: GitHubRelease? = null,
    val downloadProgress: Float? = null
)

/** Orchestrates checking GitHub for a newer release, downloading its APK, and installing it. */
class UpdateCheckViewModel(application: Application) : AndroidViewModel(application) {
    private val releaseRepository = GitHubReleaseRepository(owner = REPO_OWNER, repo = REPO_NAME)
    private val apkInstaller = ApkInstaller(application)

    var uiState by mutableStateOf(
        UpdateCheckUiState(currentVersion = application.currentAppVersionName())
    )
        private set

    /** Queries GitHub for the latest release and updates [uiState] with the result. */
    fun checkForUpdate() {
        uiState = uiState.copy(phase = UpdateCheckPhase.Checking)

        viewModelScope.launch {
            val release = releaseRepository.fetchLatestRelease()
            uiState = when {
                release == null -> uiState.copy(phase = UpdateCheckPhase.Error)
                isNewerVersion(uiState.currentVersion, release.tagName) ->
                    uiState.copy(phase = UpdateCheckPhase.UpdateAvailable, latestRelease = release)

                else -> uiState.copy(phase = UpdateCheckPhase.UpToDate)
            }
        }
    }

    /** Downloads the currently known [UpdateCheckUiState.latestRelease]'s APK and installs it. */
    fun downloadAndInstall() {
        val release = uiState.latestRelease ?: return
        uiState = uiState.copy(phase = UpdateCheckPhase.Downloading, downloadProgress = null)

        viewModelScope.launch {
            val downloadId = apkInstaller.startDownload(release)
            apkInstaller.observeDownload(downloadId).collect { status ->
                uiState = when (status) {
                    is ApkDownloadStatus.InProgress ->
                        uiState.copy(downloadProgress = status.progressFraction())

                    is ApkDownloadStatus.Succeeded -> {
                        apkInstaller.requestInstall(status.fileUri)
                        uiState.copy(phase = UpdateCheckPhase.ReadyToInstall)
                    }

                    ApkDownloadStatus.Failed -> uiState.copy(phase = UpdateCheckPhase.Error)
                }
            }
        }
    }

    /** Whether the user has already granted permission to install APKs from this app. */
    fun canRequestPackageInstalls(): Boolean = apkInstaller.canRequestPackageInstalls()

    /** Opens system settings so the user can grant the "install unknown apps" permission. */
    fun requestInstallPermission() = apkInstaller.requestInstallPermission()
}

private fun ApkDownloadStatus.InProgress.progressFraction(): Float? {
    return if (bytesTotal > 0) bytesDownloaded.toFloat() / bytesTotal else null
}

private fun Application.currentAppVersionName(): String {
    val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, 0)
    }
    return info.versionName.orEmpty()
}
