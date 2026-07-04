package sevynidd.diabetesapp.screens.settings

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import sevynidd.diabetesapp.data.update.GitHubRelease
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

private const val RELEASE_NOTES_MAX_LINES = 6

@Composable
fun UpdateSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    uiState: UpdateCheckUiState = UpdateCheckUiState(),
    canRequestPackageInstalls: Boolean = true,
    onCheckForUpdateRequested: () -> Unit = {},
    onDownloadAndInstallRequested: () -> Unit = {},
    onRequestInstallPermission: () -> Unit = {}
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SettingsInfoCard {
            Text(
                text = translate(TranslationKey.AppUpdateCurrentVersion, currentLanguage),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = uiState.currentVersion,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SettingsInfoCard {
            UpdateStatusContent(
                currentLanguage = currentLanguage,
                uiState = uiState,
                canRequestPackageInstalls = canRequestPackageInstalls,
                onCheckForUpdateRequested = onCheckForUpdateRequested,
                onDownloadAndInstallRequested = onDownloadAndInstallRequested,
                onRequestInstallPermission = onRequestInstallPermission
            )
        }

        GitHubRepoLink(currentLanguage = currentLanguage)
    }
}

@Composable
private fun GitHubRepoLink(currentLanguage: AppLanguage) {
    val context = LocalContext.current

    Text(
        text = translate(TranslationKey.AppUpdateViewOnGitHub, currentLanguage),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.primary,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable {
            context.startActivity(Intent(Intent.ACTION_VIEW, GITHUB_REPO_URL.toUri()))
        }
    )
}

@Composable
private fun SettingsInfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

@Composable
private fun UpdateStatusContent(
    currentLanguage: AppLanguage,
    uiState: UpdateCheckUiState,
    canRequestPackageInstalls: Boolean,
    onCheckForUpdateRequested: () -> Unit,
    onDownloadAndInstallRequested: () -> Unit,
    onRequestInstallPermission: () -> Unit
) {
    when (uiState.phase) {
        UpdateCheckPhase.Idle -> {
            Button(onClick = onCheckForUpdateRequested) {
                Text(translate(TranslationKey.AppUpdateCheckButton, currentLanguage))
            }
        }

        UpdateCheckPhase.Checking -> {
            CircularProgressIndicator()
            Text(
                text = translate(TranslationKey.AppUpdateChecking, currentLanguage),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        UpdateCheckPhase.UpToDate -> {
            Text(
                text = translate(TranslationKey.AppUpdateUpToDate, currentLanguage),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = onCheckForUpdateRequested) {
                Text(translate(TranslationKey.AppUpdateCheckButton, currentLanguage))
            }
        }

        UpdateCheckPhase.UpdateAvailable -> {
            UpdateAvailableContent(
                currentLanguage = currentLanguage,
                release = uiState.latestRelease,
                onDownloadAndInstallRequested = onDownloadAndInstallRequested
            )
        }

        UpdateCheckPhase.Downloading -> {
            val progress = uiState.downloadProgress
            if (progress != null) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Text(
                text = translate(TranslationKey.AppUpdateDownloading, currentLanguage),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        UpdateCheckPhase.ReadyToInstall -> {
            ReadyToInstallContent(
                currentLanguage = currentLanguage,
                canRequestPackageInstalls = canRequestPackageInstalls,
                onRequestInstallPermission = onRequestInstallPermission
            )
        }

        UpdateCheckPhase.Error -> {
            Text(
                text = translate(TranslationKey.AppUpdateError, currentLanguage),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = onCheckForUpdateRequested) {
                Text(translate(TranslationKey.AppUpdateRetryButton, currentLanguage))
            }
        }
    }
}

@Composable
private fun UpdateAvailableContent(
    currentLanguage: AppLanguage,
    release: GitHubRelease?,
    onDownloadAndInstallRequested: () -> Unit
) {
    Text(
        text = translate(TranslationKey.AppUpdateAvailable, currentLanguage),
        style = MaterialTheme.typography.titleSmall
    )

    if (release != null) {
        Text(
            text = release.tagName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

        if (release.releaseNotes.isNotBlank()) {
            Text(
                text = release.releaseNotes,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = RELEASE_NOTES_MAX_LINES
            )
        }
    }

    Button(onClick = onDownloadAndInstallRequested) {
        Text(translate(TranslationKey.AppUpdateDownloadButton, currentLanguage))
    }
}

@Composable
private fun ReadyToInstallContent(
    currentLanguage: AppLanguage,
    canRequestPackageInstalls: Boolean,
    onRequestInstallPermission: () -> Unit
) {
    if (canRequestPackageInstalls) {
        Text(
            text = translate(TranslationKey.AppUpdateReadyToInstall, currentLanguage),
            style = MaterialTheme.typography.bodyMedium
        )
    } else {
        Text(
            text = translate(TranslationKey.AppUpdatePermissionNeeded, currentLanguage),
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onRequestInstallPermission) {
            Text(translate(TranslationKey.AppUpdateOpenSettingsButton, currentLanguage))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UpdateSettingsScreenIdlePreview() {
    UpdateSettingsScreen(
        uiState = UpdateCheckUiState(currentVersion = "1.0")
    )
}

@Preview(showBackground = true)
@Composable
private fun UpdateSettingsScreenAvailablePreview() {
    UpdateSettingsScreen(
        uiState = UpdateCheckUiState(
            phase = UpdateCheckPhase.UpdateAvailable,
            currentVersion = "1.0",
            latestRelease = GitHubRelease(
                tagName = "v1.1.0",
                releaseNotes = "- Redesigned Templates screen\n- Faster factor calculations",
                apkDownloadUrl = "https://example.com/app.apk",
                apkFileName = "app.apk"
            )
        )
    )
}
