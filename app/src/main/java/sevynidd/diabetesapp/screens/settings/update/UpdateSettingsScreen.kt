package sevynidd.diabetesapp.screens.settings.update

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

private const val GITHUB_REPO_URL = "https://github.com/Sevynidd/Bolus-Manager"
private const val OBTAINIUM_ADD_APP_URL = "obtainium://add/$GITHUB_REPO_URL"
private const val OBTAINIUM_REPO_URL = "https://github.com/ImranR98/Obtainium"

/**
 * Explains that app updates are delivered by Obtainium: offers a one-tap hand-off that adds this
 * app's GitHub releases to Obtainium, and a link to get Obtainium if it isn't installed yet.
 */
@Composable
fun UpdateSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    currentVersion: String = ""
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UpdateInfoRow(
            icon = Icons.Filled.Info,
            title = translate(TranslationKey.AppUpdateCurrentVersion, currentLanguage),
            supportingText = currentVersion
        )

        ObtainiumCard(
            currentLanguage = currentLanguage,
            onAddToObtainium = { context.openAddToObtainium() },
            onGetObtainium = { context.openUrl(OBTAINIUM_REPO_URL) }
        )

        UpdateInfoRow(
            icon = Icons.Filled.Code,
            title = translate(TranslationKey.AppUpdateViewOnGitHub, currentLanguage),
            trailingIcon = Icons.AutoMirrored.Filled.OpenInNew,
            onClick = { context.openUrl(GITHUB_REPO_URL) }
        )
    }
}

@Composable
private fun ObtainiumCard(
    currentLanguage: AppLanguage,
    onAddToObtainium: () -> Unit,
    onGetObtainium: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = translate(TranslationKey.AppUpdateObtainiumDescription, currentLanguage),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = onAddToObtainium) {
                Text(translate(TranslationKey.AppUpdateObtainiumAddButton, currentLanguage))
            }
            OutlinedButton(onClick = onGetObtainium) {
                Text(translate(TranslationKey.AppUpdateObtainiumGetButton, currentLanguage))
            }
        }
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

/** Hands this app's repository to Obtainium; without Obtainium installed, shows how to get it instead. */
private fun Context.openAddToObtainium() {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, OBTAINIUM_ADD_APP_URL.toUri()))
    } catch (ignored: ActivityNotFoundException) {
        openUrl(OBTAINIUM_REPO_URL)
    }
}

private fun Context.openUrl(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
}

@Preview(showBackground = true)
@Composable
private fun UpdateSettingsScreenPreview() {
    UpdateSettingsScreen(currentVersion = "1.83")
}
