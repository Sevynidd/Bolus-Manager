package sevynidd.diabetesapp.screens.settings.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.describe
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val LogTimestampFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

/** The "Documentation" section: a chronological, human-readable record of every entry in [auditLog]. */
@Composable
internal fun DocumentationCard(auditLog: List<FactorAuditLogEntry>, currentLanguage: AppLanguage) {
    SettingsCard {
        SectionHeader(
            titleKey = TranslationKey.DocumentationSectionTitle,
            helpKey = TranslationKey.DocumentationSectionHelp,
            currentLanguage = currentLanguage
        )

        if (auditLog.isEmpty()) {
            Text(
                text = translate(TranslationKey.DocumentationEmptyState, currentLanguage),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            auditLog.forEachIndexed { index, entry ->
                if (index != 0) HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                AuditLogRow(entry = entry, currentLanguage = currentLanguage)
            }
        }
    }
}

@Composable
private fun AuditLogRow(entry: FactorAuditLogEntry, currentLanguage: AppLanguage) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = entry.timestampMillis.toLogTimestampLabel(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = entry.describe(currentLanguage), style = MaterialTheme.typography.bodyMedium)
    }
}

private fun Long.toLogTimestampLabel(): String {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).format(LogTimestampFormatter)
}
