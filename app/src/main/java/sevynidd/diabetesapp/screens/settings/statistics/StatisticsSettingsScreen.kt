package sevynidd.diabetesapp.screens.settings.statistics

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.data.export.auditLogExportFileName
import sevynidd.diabetesapp.data.export.toAuditLogCsv
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.LocalDate

private const val EXPORT_MIME_TYPE = "text/csv"

private enum class LogExportOutcome { Success, Failure }

/**
 * Shows how factors and the basal rate developed over time (Statistics) and a chronological log
 * of every add/edit/delete (Documentation), both derived from [auditLog], and lets the user export
 * the full log as CSV to share with their endocrinologist.
 */
@Composable
fun StatisticsSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    auditLog: List<FactorAuditLogEntry> = emptyList()
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val coroutineScope = rememberCoroutineScope()
    var exportOutcome by remember { mutableStateOf<LogExportOutcome?>(null) }
    val exportLauncher = rememberLogExportLauncher(context, coroutineScope, auditLog, currentLanguage) {
        exportOutcome = it
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionHeader(
            titleKey = TranslationKey.StatisticsSettingsTitle,
            helpKey = TranslationKey.StatisticsSettingsHelp,
            currentLanguage = currentLanguage
        )

        ExportCard(
            currentLanguage = currentLanguage,
            outcome = exportOutcome,
            onExportClick = {
                exportOutcome = null
                if (!isPreview) exportLauncher.launch(auditLogExportFileName(LocalDate.now()))
            }
        )

        StatisticsCard(auditLog = auditLog, currentLanguage = currentLanguage)

        DocumentationCard(auditLog = auditLog, currentLanguage = currentLanguage)
    }
}

@Composable
private fun rememberLogExportLauncher(
    context: Context,
    coroutineScope: CoroutineScope,
    auditLog: List<FactorAuditLogEntry>,
    currentLanguage: AppLanguage,
    onOutcome: (LogExportOutcome) -> Unit
): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(EXPORT_MIME_TYPE)) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val success = writeAuditLogExport(context, uri, auditLog, currentLanguage)
            onOutcome(if (success) LogExportOutcome.Success else LogExportOutcome.Failure)
        }
    }
}

private fun writeAuditLogExport(
    context: Context,
    uri: Uri,
    auditLog: List<FactorAuditLogEntry>,
    currentLanguage: AppLanguage
): Boolean {
    return runCatching {
        context.contentResolver.openOutputStream(uri)?.use { stream ->
            stream.write(auditLog.toAuditLogCsv(currentLanguage).toByteArray(Charsets.UTF_8))
        } != null
    }.getOrDefault(false)
}

@Composable
private fun ExportCard(
    currentLanguage: AppLanguage,
    outcome: LogExportOutcome?,
    onExportClick: () -> Unit
) {
    SettingsCard {
        Button(onClick = onExportClick) {
            Icon(imageVector = Icons.Filled.FileDownload, contentDescription = null)
            Text(
                text = translate(TranslationKey.ActionExport, currentLanguage),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        outcome?.let {
            Text(
                text = translate(it.toMessageKey(), currentLanguage),
                style = MaterialTheme.typography.bodyMedium,
                color = if (it == LogExportOutcome.Failure) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}

private fun LogExportOutcome.toMessageKey(): TranslationKey {
    return when (this) {
        LogExportOutcome.Success -> TranslationKey.LogExportSuccessMessage
        LogExportOutcome.Failure -> TranslationKey.LogExportErrorMessage
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsSettingsScreenPreview() {
    StatisticsSettingsScreen(
        auditLog = listOf(
            FactorAuditLogEntry(
                timestampMillis = 0L,
                changeType = "FACTOR_ADDED",
                factorName = "Morning",
                oldValue = null,
                newValue = 1.2,
                oldTimeMinutes = null,
                newTimeMinutes = 300
            ),
            FactorAuditLogEntry(
                timestampMillis = 86_400_000L,
                changeType = "FACTOR_VALUE_CHANGED",
                factorName = "Morning",
                oldValue = 1.2,
                newValue = 1.5,
                oldTimeMinutes = null,
                newTimeMinutes = null
            )
        )
    )
}
