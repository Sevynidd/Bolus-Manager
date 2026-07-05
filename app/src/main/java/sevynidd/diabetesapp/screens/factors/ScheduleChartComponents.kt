package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.ScheduleTimeSlot
import sevynidd.diabetesapp.libraries.gappedPieChart.PieData
import sevynidd.diabetesapp.localization.TranslationKey

internal val LegendDotSize = 10.dp
private val LegendItemMinWidth = 130.dp

@Composable
internal fun ScheduleChartCenterLabel(
    nowLabel: String,
    activeWindowLabel: String,
    currentTimeLabel: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = nowLabel,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = activeWindowLabel,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = currentTimeLabel,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ScheduleLegend(pieDataPoints: List<PieData>, modifier: Modifier = Modifier) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        pieDataPoints.forEach { data ->
            ScheduleLegendItem(
                color = data.color,
                label = data.title,
                timeLabel = data.value,
                modifier = Modifier.widthIn(min = LegendItemMinWidth)
            )
        }
    }
}

@Composable
private fun ScheduleLegendItem(
    color: Color,
    label: String,
    timeLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(LegendDotSize)
                .clip(CircleShape)
                .background(color)
        )
        Column {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
            Text(
                text = timeLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
internal fun TimePickerField(
    description: String,
    timeLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    dotColor: Color? = null
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (dotColor != null) {
                Box(
                    modifier = Modifier
                        .size(LegendDotSize)
                        .clip(CircleShape)
                        .background(dotColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = timeLabel,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// Named hex color constants — detekt's MagicNumber check still flags a subset of these hex
// literals depending on declaration order, even though each one is already a well-named constant.
@Suppress("MagicNumber")
private object PieSegmentColors {
    val morningDark = Color(0xFFCF94E8)
    val breakfastDark = Color(0xFF8AC6EA)
    val lunchDark = Color(0xFFCBCE6E)
    val afternoonDark = Color(0xFF80CBC4)
    val dinnerDark = Color(0xFFFFB877)
    val lateDark = Color(0xFFF48FB1)
    val nightDark = Color(0xFF9FA8DA)

    val morningLight = Color(0xFF9400D3)
    val breakfastLight = Color(0xFF42A1D5)
    val lunchLight = Color(0xFF8D9311)
    val afternoonLight = Color(0xFF009688)
    val dinnerLight = Color(0xFFFF7F00)
    val lateLight = Color(0xFFE91E63)
    val nightLight = Color(0xFF3949AB)
}

/**
 * Maps the label returned by `activeFactorForTime` to its position in the pie chart's data
 * points, which are built in morning/breakfast/lunch/afternoon/dinner/late/night order — the
 * same order as [ScheduleTimeSlot]'s waking-hour entries. Returns `null` for labels outside that
 * set (there is no `Basal` slice), so the chart can highlight the arc matching the current time.
 */
internal fun pieIndexForFactorLabel(factorLabel: TranslationKey): Int? {
    val slot = when (factorLabel) {
        TranslationKey.FactorMorning -> ScheduleTimeSlot.Morning
        TranslationKey.FactorBreakfast -> ScheduleTimeSlot.Breakfast
        TranslationKey.FactorLunch -> ScheduleTimeSlot.Lunch
        TranslationKey.FactorAfternoon -> ScheduleTimeSlot.Afternoon
        TranslationKey.FactorDinner -> ScheduleTimeSlot.Dinner
        TranslationKey.FactorLate -> ScheduleTimeSlot.Late
        TranslationKey.FactorNight -> ScheduleTimeSlot.Night
        else -> return null
    }
    return slot.ordinal
}

/**
 * The 7 pie-chart segment colors for morning/breakfast/lunch/afternoon/dinner/late/night, in
 * that order. Kept as a dedicated categorical palette rather than the theme's primary/secondary/
 * tertiary roles: this app's generated Material scheme is a monochromatic rust/brown palette, so
 * routing the chart through those roles would make all 7 slices look nearly identical. [isDarkTheme]
 * selects a lighter, less saturated variant so segments stay legible against a dark surface.
 */
internal fun pieSegmentColors(isDarkTheme: Boolean): List<Color> {
    return if (isDarkTheme) {
        listOf(
            PieSegmentColors.morningDark,
            PieSegmentColors.breakfastDark,
            PieSegmentColors.lunchDark,
            PieSegmentColors.afternoonDark,
            PieSegmentColors.dinnerDark,
            PieSegmentColors.lateDark,
            PieSegmentColors.nightDark
        )
    } else {
        listOf(
            PieSegmentColors.morningLight,
            PieSegmentColors.breakfastLight,
            PieSegmentColors.lunchLight,
            PieSegmentColors.afternoonLight,
            PieSegmentColors.dinnerLight,
            PieSegmentColors.lateLight,
            PieSegmentColors.nightLight
        )
    }
}
