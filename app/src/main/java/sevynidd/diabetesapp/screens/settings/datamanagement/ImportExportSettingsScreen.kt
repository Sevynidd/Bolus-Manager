package sevynidd.diabetesapp.screens.settings.datamanagement

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import sevynidd.diabetesapp.data.export.factorsExportFileName
import sevynidd.diabetesapp.data.export.parseFactorsExportJson
import sevynidd.diabetesapp.data.export.toExportJson
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.LocalDate

private const val EXPORT_MIME_TYPE = "application/json"

private enum class ImportExportOutcome { ExportSuccess, ExportFailure, ImportSuccess, ImportFailure }

/**
 * Lets the user save the current factor profile (correction factors, time windows, basal rate) to
 * a JSON file via the system file picker, or load one previously exported this way. Imported data
 * is handed to [onFactorsImported] exactly like a manual edit-and-save, so it goes through the same
 * persistence path as every other factor change.
 */
@Composable
fun ImportExportSettingsScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    onFactorsImported: (FactorsData) -> Unit = {}
) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val coroutineScope = rememberCoroutineScope()
    var outcome by remember { mutableStateOf<ImportExportOutcome?>(null) }

    val exportLauncher = rememberExportLauncher(context, coroutineScope, factors) { outcome = it }
    val importLauncher = rememberImportLauncher(context, coroutineScope, onFactorsImported) { outcome = it }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ImportExportCard(
            currentLanguage = currentLanguage,
            outcome = outcome,
            onExportClick = {
                outcome = null
                if (!isPreview) exportLauncher.launch(factorsExportFileName(LocalDate.now()))
            },
            onImportClick = {
                outcome = null
                if (!isPreview) importLauncher.launch(arrayOf(EXPORT_MIME_TYPE))
            }
        )
    }
}

@Composable
private fun rememberExportLauncher(
    context: Context,
    coroutineScope: CoroutineScope,
    factors: FactorsData,
    onOutcome: (ImportExportOutcome) -> Unit
): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(EXPORT_MIME_TYPE)) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        coroutineScope.launch {
            onOutcome(if (writeFactorsExport(context, uri, factors)) {
                ImportExportOutcome.ExportSuccess
            } else {
                ImportExportOutcome.ExportFailure
            })
        }
    }
}

@Composable
private fun rememberImportLauncher(
    context: Context,
    coroutineScope: CoroutineScope,
    onFactorsImported: (FactorsData) -> Unit,
    onOutcome: (ImportExportOutcome) -> Unit
): ActivityResultLauncher<Array<String>> {
    return rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        coroutineScope.launch {
            val imported = readFactorsImport(context, uri)
            if (imported != null) {
                onFactorsImported(imported)
                onOutcome(ImportExportOutcome.ImportSuccess)
            } else {
                onOutcome(ImportExportOutcome.ImportFailure)
            }
        }
    }
}

private fun writeFactorsExport(context: Context, uri: Uri, factors: FactorsData): Boolean {
    return runCatching {
        context.contentResolver.openOutputStream(uri)?.use { stream ->
            stream.write(factors.toExportJson().toByteArray(Charsets.UTF_8))
        } != null
    }.getOrDefault(false)
}

private fun readFactorsImport(context: Context, uri: Uri): FactorsData? {
    return runCatching {
        context.contentResolver.openInputStream(uri)
            ?.bufferedReader(Charsets.UTF_8)
            ?.use { it.readText() }
            ?.let(::parseFactorsExportJson)
    }.getOrNull()
}

@Composable
private fun ImportExportCard(
    currentLanguage: AppLanguage,
    outcome: ImportExportOutcome?,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = translate(TranslationKey.DataManagementDescription, currentLanguage),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onExportClick) {
                    Icon(imageVector = Icons.Filled.FileDownload, contentDescription = null)
                    Text(
                        text = translate(TranslationKey.ActionExport, currentLanguage),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                OutlinedButton(onClick = onImportClick) {
                    Icon(imageVector = Icons.Filled.FileUpload, contentDescription = null)
                    Text(
                        text = translate(TranslationKey.ActionImport, currentLanguage),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            outcome?.let {
                Text(
                    text = translate(it.toMessageKey(), currentLanguage),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (it.isFailure()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

private fun ImportExportOutcome.toMessageKey(): TranslationKey {
    return when (this) {
        ImportExportOutcome.ExportSuccess -> TranslationKey.ExportSuccessMessage
        ImportExportOutcome.ExportFailure -> TranslationKey.ExportErrorMessage
        ImportExportOutcome.ImportSuccess -> TranslationKey.ImportSuccessMessage
        ImportExportOutcome.ImportFailure -> TranslationKey.ImportErrorMessage
    }
}

private fun ImportExportOutcome.isFailure(): Boolean {
    return this == ImportExportOutcome.ExportFailure || this == ImportExportOutcome.ImportFailure
}

@Preview(showBackground = true)
@Composable
private fun ImportExportSettingsScreenPreview() {
    ImportExportSettingsScreen()
}
