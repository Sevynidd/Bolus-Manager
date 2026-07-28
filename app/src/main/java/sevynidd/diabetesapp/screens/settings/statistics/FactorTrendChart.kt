package sevynidd.diabetesapp.screens.settings.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.FactorHistoryPoint
import sevynidd.diabetesapp.calculation.FactorHistorySeries
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val ChartHeight = 120.dp
private const val CHART_STROKE_WIDTH = 4f
private const val CHART_POINT_RADIUS = 5f
private const val MIN_POINTS_FOR_A_LINE = 2

private val ChartDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")

/**
 * Renders one factor's (or, when [FactorHistorySeries.factorName] is `null`, the basal rate's)
 * value history as a simple line chart, or a "not enough data" hint if it has fewer than
 * [MIN_POINTS_FOR_A_LINE] points.
 */
@Composable
fun FactorTrendChart(
    series: FactorHistorySeries,
    currentLanguage: AppLanguage = AppLanguage.System,
    modifier: Modifier = Modifier
) {
    val title = series.factorName ?: translate(TranslationKey.BasalRate, currentLanguage)
    val points = series.points

    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = title, style = MaterialTheme.typography.titleSmall)

        if (points.size < MIN_POINTS_FOR_A_LINE) {
            Text(
                text = translate(TranslationKey.StatisticsSeriesNotEnoughData, currentLanguage),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            TrendLine(points = points, modifier = Modifier.padding(top = 8.dp))

            val firstDate = points.first().timestampMillis.toDateLabel()
            val lastDate = points.last().timestampMillis.toDateLabel()
            Text(
                text = "$firstDate – $lastDate",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TrendLine(points: List<FactorHistoryPoint>, modifier: Modifier = Modifier) {
    val minValue = points.minOf { it.value }
    val maxValue = points.maxOf { it.value }
    val valueRange = (maxValue - minValue).takeIf { it > 0.0 } ?: 1.0
    val lineColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(ChartHeight)
    ) {
        val stepX = size.width / (points.size - 1)
        val offsets = points.mapIndexed { index, point ->
            val normalizedY = ((point.value - minValue) / valueRange).toFloat()
            Offset(x = index * stepX, y = size.height - normalizedY * size.height)
        }

        for (index in 0 until offsets.size - 1) {
            drawLine(
                color = lineColor,
                start = offsets[index],
                end = offsets[index + 1],
                strokeWidth = CHART_STROKE_WIDTH,
                cap = StrokeCap.Round
            )
        }
        offsets.forEach { offset -> drawCircle(color = lineColor, radius = CHART_POINT_RADIUS, center = offset) }
    }
}

private fun Long.toDateLabel(): String {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).format(ChartDateFormatter)
}

@Preview(showBackground = true)
@Composable
private fun FactorTrendChartPreview() {
    FactorTrendChart(
        series = FactorHistorySeries(
            factorName = "Morning",
            points = listOf(
                FactorHistoryPoint(timestampMillis = 0L, value = 1.2),
                FactorHistoryPoint(timestampMillis = 86_400_000L, value = 1.5),
                FactorHistoryPoint(timestampMillis = 172_800_000L, value = 1.4)
            )
        )
    )
}
