package sevynidd.diabetesapp.screens.settings.statistics

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import sevynidd.diabetesapp.data.export.buildFactorsPdfReportContent
import sevynidd.diabetesapp.data.export.factorsPdfReportFileName
import sevynidd.diabetesapp.data.export.toAuditLogCsv
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.LocalDate

private const val CSV_EXPORT_MIME_TYPE = "text/csv"
private const val PDF_EXPORT_MIME_TYPE = "application/pdf"

private enum class LogExportOutcome { Success, Failure }

/**
 * Shows how factors and the basal rate developed over time (Statistics) and a chronological log
 * of every add/edit/delete (Documentation), both derived from [auditLog], and lets the user export
 * the full log as CSV, or [factors] and the log together as a printable PDF report, to share with
 * their endocrinologist.
 */
@Composable
fun StatisticsSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    auditLog: List<FactorAuditLogEntry> = emptyList(),
    factors: FactorsData = FactorsData()
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val coroutineScope = rememberCoroutineScope()
    var csvExportOutcome by remember { mutableStateOf<LogExportOutcome?>(null) }
    var pdfExportOutcome by remember { mutableStateOf<LogExportOutcome?>(null) }
    val csvExportLauncher = rememberCsvExportLauncher(context, coroutineScope, auditLog, currentLanguage) {
        csvExportOutcome = it
    }
    val pdfExportRequest = PdfExportRequest(factors, auditLog, currentLanguage)
    val pdfExportLauncher = rememberPdfExportLauncher(context, coroutineScope, pdfExportRequest) {
        pdfExportOutcome = it
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
            csvOutcome = csvExportOutcome,
            pdfOutcome = pdfExportOutcome,
            onExportCsvClick = {
                csvExportOutcome = null
                if (!isPreview) csvExportLauncher.launch(auditLogExportFileName(LocalDate.now()))
            },
            onExportPdfClick = {
                pdfExportOutcome = null
                if (!isPreview) pdfExportLauncher.launch(factorsPdfReportFileName(LocalDate.now()))
            }
        )

        StatisticsCard(auditLog = auditLog, currentLanguage = currentLanguage)

        DocumentationCard(auditLog = auditLog, currentLanguage = currentLanguage)
    }
}

@Composable
private fun rememberCsvExportLauncher(
    context: Context,
    coroutineScope: CoroutineScope,
    auditLog: List<FactorAuditLogEntry>,
    currentLanguage: AppLanguage,
    onOutcome: (LogExportOutcome) -> Unit
): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(CSV_EXPORT_MIME_TYPE)) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val success = writeAuditLogExport(context, uri, auditLog, currentLanguage)
            onOutcome(if (success) LogExportOutcome.Success else LogExportOutcome.Failure)
        }
    }
}

/** The inputs [rememberPdfExportLauncher] needs, bundled to keep its parameter count reasonable. */
private data class PdfExportRequest(
    val factors: FactorsData,
    val auditLog: List<FactorAuditLogEntry>,
    val language: AppLanguage
)

@Composable
private fun rememberPdfExportLauncher(
    context: Context,
    coroutineScope: CoroutineScope,
    request: PdfExportRequest,
    onOutcome: (LogExportOutcome) -> Unit
): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(PDF_EXPORT_MIME_TYPE)) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val success = writeFactorsPdfExport(context, uri, request)
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

private fun writeFactorsPdfExport(context: Context, uri: Uri, request: PdfExportRequest): Boolean {
    return runCatching {
        val content = buildFactorsPdfReportContent(request.factors, request.auditLog, request.language)
        context.contentResolver.openOutputStream(uri)?.use { stream ->
            renderFactorsPdfReport(content, stream)
        } == true
    }.getOrDefault(false)
}

@Composable
private fun ExportCard(
    currentLanguage: AppLanguage,
    csvOutcome: LogExportOutcome?,
    pdfOutcome: LogExportOutcome?,
    onExportCsvClick: () -> Unit,
    onExportPdfClick: () -> Unit
) {
    SettingsCard {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onExportCsvClick) {
                Icon(imageVector = Icons.Filled.FileDownload, contentDescription = null)
                Text(
                    text = translate(TranslationKey.ActionExportCsv, currentLanguage),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            OutlinedButton(onClick = onExportPdfClick) {
                Icon(imageVector = Icons.Filled.PictureAsPdf, contentDescription = null)
                Text(
                    text = translate(TranslationKey.ActionExportPdf, currentLanguage),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        csvOutcome?.let {
            Text(
                text = translate(it.toMessageKey(isPdf = false), currentLanguage),
                style = MaterialTheme.typography.bodyMedium,
                color = it.toMessageColor()
            )
        }

        pdfOutcome?.let {
            Text(
                text = translate(it.toMessageKey(isPdf = true), currentLanguage),
                style = MaterialTheme.typography.bodyMedium,
                color = it.toMessageColor()
            )
        }
    }
}

@Composable
private fun LogExportOutcome.toMessageColor() =
    if (this == LogExportOutcome.Failure) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

private fun LogExportOutcome.toMessageKey(isPdf: Boolean): TranslationKey {
    return when (this) {
        LogExportOutcome.Success ->
            if (isPdf) TranslationKey.PdfExportSuccessMessage else TranslationKey.LogExportSuccessMessage
        LogExportOutcome.Failure ->
            if (isPdf) TranslationKey.PdfExportErrorMessage else TranslationKey.LogExportErrorMessage
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
