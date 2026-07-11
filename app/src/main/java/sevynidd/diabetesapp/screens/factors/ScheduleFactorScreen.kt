package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.calculation.MINUTES_PER_DAY
import sevynidd.diabetesapp.calculation.activeFactorForTime
import sevynidd.diabetesapp.calculation.formatTimeOfDay
import sevynidd.diabetesapp.calculation.withUpdatedTime
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.libraries.gappedPieChart.AnimatedGapPieChart
import sevynidd.diabetesapp.libraries.gappedPieChart.PieData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
import java.time.LocalTime
import kotlin.math.max

/** Which time field a schedule edit applies to: one of the dynamic factor slots, or the basal time. */
private sealed interface ScheduleEditTarget {
    data class Factor(val index: Int) : ScheduleEditTarget
    data object Basal : ScheduleEditTarget
}

private data class ScheduleFieldItem(
    val target: ScheduleEditTarget,
    val title: String,
    val timeMinutes: Int,
    val dotColor: Color?
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleFactorScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    onFactorsChange: (FactorsData) -> Unit = {},
    now: LocalTime = LocalTime.now()
) {
    var activePicker by rememberSaveable { mutableStateOf<ScheduleEditTarget?>(null) }

    fun updateTime(target: ScheduleEditTarget, selectedMinutes: Int) {
        onFactorsChange(
            when (target) {
                is ScheduleEditTarget.Factor -> factors.copy(
                    factorSlots = factors.factorSlots.withUpdatedTime(target.index, selectedMinutes)
                )

                ScheduleEditTarget.Basal -> factors.copy(
                    basalTimeMinutes = selectedMinutes.coerceIn(0, MINUTES_PER_DAY - 1)
                )
            }
        )
    }

    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < MID_LUMINANCE
    val segmentColors = remember(isDarkTheme) { pieSegmentColors(isDarkTheme) }

    val pieDataPoints = remember(segmentColors, factors.factorSlots) {
        buildPieDataPoints(factors.factorSlots, segmentColors)
    }
    val scheduleFields = remember(currentLanguage, segmentColors, factors) {
        buildScheduleFields(factors, currentLanguage, segmentColors)
    }

    val nowMinutes = (now.hour * 60) + now.minute
    val activeFactorInfo = activeFactorForTime(factors.factorSlots, nowMinutes)

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScheduleChartCard(
            currentLanguage = currentLanguage,
            pieDataPoints = pieDataPoints,
            activeWindowLabel = activeFactorInfo.factorName,
            nowMinutes = nowMinutes,
            activeChartIndex = activeFactorInfo.activeIndex.takeIf { it >= 0 }
        )

        ScheduleTimesCard(
            currentLanguage = currentLanguage,
            scheduleFields = scheduleFields,
            onFieldClick = { activePicker = it }
        )
    }

    activePicker?.let { target ->
        ScheduleTimePickerDialog(
            title = targetTitle(target, factors, currentLanguage),
            initialMinutes = target.minutesFor(factors),
            onDismissRequest = { activePicker = null },
            onConfirm = { selectedMinutes ->
                updateTime(target, selectedMinutes)
                activePicker = null
            }
        )
    }
}

private fun ScheduleEditTarget.minutesFor(factors: FactorsData): Int {
    return when (this) {
        is ScheduleEditTarget.Factor -> factors.factorSlots[index].startTimeMinutes
        ScheduleEditTarget.Basal -> factors.basalTimeMinutes
    }
}

private fun targetTitle(target: ScheduleEditTarget, factors: FactorsData, currentLanguage: AppLanguage): String {
    return when (target) {
        is ScheduleEditTarget.Factor -> factors.factorSlots[target.index].name
        ScheduleEditTarget.Basal -> translate(TranslationKey.BasalRate, currentLanguage)
    }
}

private fun buildPieDataPoints(factorSlots: List<FactorSlot>, segmentColors: List<Color>): List<PieData> {
    return factorSlots.mapIndexed { index, slot ->
        val nextStart = factorSlots[(index + 1) % factorSlots.size].startTimeMinutes
        val duration = if (nextStart <= slot.startTimeMinutes) {
            (MINUTES_PER_DAY - slot.startTimeMinutes) + nextStart
        } else {
            nextStart - slot.startTimeMinutes
        }

        PieData(
            amount = max(1, duration),
            color = segmentColors[index % segmentColors.size],
            title = slot.name,
            value = formatTimeOfDay(slot.startTimeMinutes)
        )
    }
}

private fun buildScheduleFields(
    factors: FactorsData,
    currentLanguage: AppLanguage,
    segmentColors: List<Color>
): List<ScheduleFieldItem> {
    val factorFields = factors.factorSlots.mapIndexed { index, slot ->
        ScheduleFieldItem(
            target = ScheduleEditTarget.Factor(index),
            title = slot.name,
            timeMinutes = slot.startTimeMinutes,
            dotColor = segmentColors[index % segmentColors.size]
        )
    }
    val basalField = ScheduleFieldItem(
        target = ScheduleEditTarget.Basal,
        title = translate(TranslationKey.BasalRate, currentLanguage),
        timeMinutes = factors.basalTimeMinutes,
        dotColor = null
    )
    return factorFields + basalField
}

@Composable
private fun ScheduleChartCard(
    currentLanguage: AppLanguage,
    pieDataPoints: List<PieData>,
    activeWindowLabel: String,
    nowMinutes: Int,
    activeChartIndex: Int?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                AnimatedGapPieChart(
                    modifier = Modifier.size(ChartSize),
                    pieDataPoints = pieDataPoints,
                    activeIndex = activeChartIndex
                )
                ScheduleChartCenterLabel(
                    nowLabel = translate(TranslationKey.ActiveNowBadge, currentLanguage),
                    activeWindowLabel = activeWindowLabel,
                    currentTimeLabel = formatTimeOfDay(nowMinutes)
                )
            }

            ScheduleLegend(pieDataPoints = pieDataPoints)

            Text(
                text = translate(TranslationKey.ScheduleAutoOrderHint, currentLanguage),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ScheduleTimesCard(
    currentLanguage: AppLanguage,
    scheduleFields: List<ScheduleFieldItem>,
    onFieldClick: (ScheduleEditTarget) -> Unit
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = translate(TranslationKey.FactorStartTime, currentLanguage),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )
                HelpIconButton(helpTextKey = TranslationKey.FactorStartTimeHelp, currentLanguage = currentLanguage)
            }
            scheduleFields.forEach { item ->
                TimePickerField(
                    description = item.title,
                    timeLabel = formatTimeOfDay(item.timeMinutes),
                    dotColor = item.dotColor,
                    onClick = { onFieldClick(item.target) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleTimePickerDialog(
    title: String,
    initialMinutes: Int,
    onDismissRequest: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val pickerState = rememberTimePickerState(
        initialHour = (initialMinutes / 60) % 24,
        initialMinute = initialMinutes % 60,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = title) },
        text = { TimePicker(state = pickerState) },
        confirmButton = {
            TextButton(
                onClick = { onConfirm((pickerState.hour * 60) + pickerState.minute) }
            ) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(android.R.string.cancel))
            }
        }
    )
}

private val ChartSize = 220.dp
private const val MID_LUMINANCE = 0.5f
private const val PREVIEW_HOUR = 21
private const val PREVIEW_MINUTE = 15

@Preview(showBackground = true)
@Composable
private fun ScheduleFactorScreenPreview() {
    ScheduleFactorScreen(now = LocalTime.of(PREVIEW_HOUR, PREVIEW_MINUTE))
}
