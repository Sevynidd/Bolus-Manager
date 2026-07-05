package sevynidd.diabetesapp.screens.settings

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import sevynidd.diabetesapp.data.update.GitHubRelease
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import kotlin.math.roundToInt

private const val RELEASE_NOTES_MAX_LINES = 6
private const val PERCENT_MULTIPLIER = 100
private val CheckingIndicatorSize = 20.dp

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
    val context = LocalContext.current

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UpdateInfoRow(
            icon = Icons.Filled.Info,
            title = translate(TranslationKey.AppUpdateCurrentVersion, currentLanguage),
            supportingText = uiState.currentVersion
        )

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

        UpdateInfoRow(
            icon = Icons.Filled.Code,
            title = translate(TranslationKey.AppUpdateViewOnGitHub, currentLanguage),
            trailingIcon = Icons.AutoMirrored.Filled.OpenInNew,
            onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, GITHUB_REPO_URL.toUri())) }
        )
    }
}

@Composable
private fun UpdateInfoRow(
    icon: ImageVector,
    title: String,
    supportingText: String? = null,
    trailingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null
) {
    val rowModifier = Modifier
        .clip(MaterialTheme.shapes.large)
        .let { base -> if (onClick != null) base.clickable(onClick = onClick) else base }

    ListItem(
        modifier = rowModifier,
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        headlineContent = {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
        },
        supportingContent = supportingText?.let { text ->
            {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        trailingContent = trailingIcon?.let { trailing ->
            {
                Icon(
                    imageVector = trailing,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
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
private fun StatusRow(icon: ImageVector, text: String, tint: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint)
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(modifier = Modifier.size(CheckingIndicatorSize))
                Text(
                    text = translate(TranslationKey.AppUpdateChecking, currentLanguage),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        UpdateCheckPhase.UpToDate -> {
            StatusRow(
                icon = Icons.Filled.CheckCircle,
                text = translate(TranslationKey.AppUpdateUpToDate, currentLanguage),
                tint = MaterialTheme.colorScheme.primary
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
            DownloadingContent(currentLanguage = currentLanguage, progress = uiState.downloadProgress)
        }

        UpdateCheckPhase.ReadyToInstall -> {
            ReadyToInstallContent(
                currentLanguage = currentLanguage,
                canRequestPackageInstalls = canRequestPackageInstalls,
                onRequestInstallPermission = onRequestInstallPermission
            )
        }

        UpdateCheckPhase.Error -> {
            StatusRow(
                icon = Icons.Filled.ErrorOutline,
                text = translate(TranslationKey.AppUpdateError, currentLanguage),
                tint = MaterialTheme.colorScheme.error
            )
            Button(onClick = onCheckForUpdateRequested) {
                Text(translate(TranslationKey.AppUpdateRetryButton, currentLanguage))
            }
        }
    }
}

@Composable
private fun DownloadingContent(currentLanguage: AppLanguage, progress: Float?) {
    if (progress != null) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "${translate(TranslationKey.AppUpdateDownloading, currentLanguage)} " +
                "${(progress * PERCENT_MULTIPLIER).roundToInt()}%",
            style = MaterialTheme.typography.bodyMedium
        )
    } else {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        Text(
            text = translate(TranslationKey.AppUpdateDownloading, currentLanguage),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun UpdateAvailableContent(
    currentLanguage: AppLanguage,
    release: GitHubRelease?,
    onDownloadAndInstallRequested: () -> Unit
) {
    StatusRow(
        icon = Icons.Filled.CloudDownload,
        text = translate(TranslationKey.AppUpdateAvailable, currentLanguage),
        tint = MaterialTheme.colorScheme.primary
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
        StatusRow(
            icon = Icons.Filled.CheckCircle,
            text = translate(TranslationKey.AppUpdateReadyToInstall, currentLanguage),
            tint = MaterialTheme.colorScheme.primary
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
