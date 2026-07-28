package sevynidd.diabetesapp.screens.settings.statistics

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import sevynidd.diabetesapp.calculation.factorHistorySeries
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

/** The "Statistics" section: a value-history chart per factor and the basal rate, built from [auditLog]. */
@Composable
internal fun StatisticsCard(auditLog: List<FactorAuditLogEntry>, currentLanguage: AppLanguage) {
    val series = remember(auditLog) { factorHistorySeries(auditLog) }

    SettingsCard {
        SectionHeader(
            titleKey = TranslationKey.StatisticsSectionTitle,
            helpKey = TranslationKey.StatisticsSectionHelp,
            currentLanguage = currentLanguage
        )

        if (series.isEmpty()) {
            Text(
                text = translate(TranslationKey.StatisticsEmptyState, currentLanguage),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            series.forEach { oneSeries -> FactorTrendChart(series = oneSeries, currentLanguage = currentLanguage) }
        }
    }
}
