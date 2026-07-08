package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import sevynidd.diabetesapp.calculation.ScheduleTimeSlot
import sevynidd.diabetesapp.calculation.activeFactorForTime
import sevynidd.diabetesapp.calculation.withUpdatedTime
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.libraries.gappedPieChart.AnimatedGapPieChart
import sevynidd.diabetesapp.libraries.gappedPieChart.PieData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.LocalTime
import java.util.Locale
import kotlin.math.max

private data class ScheduleFieldItem(
    val slot: ScheduleTimeSlot,
    val title: String,
    val timeMinutes: Int,
    val dotColor: Color?
)

/** Change callbacks for [ScheduleFactorScreen], grouped to keep the screen's own parameter list short. */
data class ScheduleFactorCallbacks(
    val onFactorsChange: (FactorsData) -> Unit = {},
    val onBasalReminderEnabledChange: (Boolean) -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleFactorScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    callbacks: ScheduleFactorCallbacks = ScheduleFactorCallbacks(),
    now: LocalTime = LocalTime.now()
) {
    var activePicker by rememberSaveable { mutableStateOf<ScheduleTimeSlot?>(null) }

    fun updateTime(slot: ScheduleTimeSlot, selectedMinutes: Int) {
        callbacks.onFactorsChange(factors.withUpdatedTime(slot, selectedMinutes))
    }

    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < MID_LUMINANCE
    val segmentColors = remember(isDarkTheme) { pieSegmentColors(isDarkTheme) }

    val pieDataPoints = remember(currentLanguage, segmentColors, factors) {
        buildPieDataPoints(factors, currentLanguage, segmentColors)
    }
    val scheduleFields = remember(currentLanguage, segmentColors, factors) {
        buildScheduleFields(factors, currentLanguage, segmentColors)
    }

    val nowMinutes = (now.hour * 60) + now.minute
    val activeFactorLabel = activeFactorForTime(factors, nowMinutes).factorLabel
    val activeChartIndex = remember(activeFactorLabel) { pieIndexForFactorLabel(activeFactorLabel) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScheduleChartCard(
            currentLanguage = currentLanguage,
            pieDataPoints = pieDataPoints,
            activeWindowLabel = translate(activeFactorLabel, currentLanguage),
            nowMinutes = nowMinutes,
            activeChartIndex = activeChartIndex
        )

        ScheduleTimesCard(
            scheduleFields = scheduleFields,
            onFieldClick = { activePicker = it }
        )

        BasalReminderCard(
            currentLanguage = currentLanguage,
            isEnabled = factors.basalReminderEnabled,
            onEnabledChange = callbacks.onBasalReminderEnabledChange
        )
    }

    activePicker?.let { slot ->
        ScheduleTimePickerDialog(
            slot = slot,
            currentLanguage = currentLanguage,
            initialMinutes = factors.minutesFor(slot),
            onDismissRequest = { activePicker = null },
            onConfirm = { selectedMinutes ->
                updateTime(slot, selectedMinutes)
                activePicker = null
            }
        )
    }
}

private fun FactorsData.minutesFor(slot: ScheduleTimeSlot): Int {
    return when (slot) {
        ScheduleTimeSlot.Morning -> morningTimeMinutes
        ScheduleTimeSlot.Breakfast -> breakfastTimeMinutes
        ScheduleTimeSlot.Lunch -> lunchTimeMinutes
        ScheduleTimeSlot.Afternoon -> afternoonTimeMinutes
        ScheduleTimeSlot.Dinner -> dinnerTimeMinutes
        ScheduleTimeSlot.Late -> lateTimeMinutes
        ScheduleTimeSlot.Night -> nightTimeMinutes
        ScheduleTimeSlot.Basal -> basalTimeMinutes
    }
}

private fun buildPieDataPoints(
    factors: FactorsData,
    currentLanguage: AppLanguage,
    segmentColors: List<Color>
): List<PieData> {
    return listOf(
        PieData(
            max(1, factors.morningTimeMinutes),
            color = segmentColors[0],
            title = translate(TranslationKey.FactorMorning, currentLanguage),
            value = formatTimeLabel(factors.morningTimeMinutes)
        ),
        PieData(
            max(1, factors.breakfastTimeMinutes),
            color = segmentColors[1],
            title = translate(TranslationKey.FactorBreakfast, currentLanguage),
            value = formatTimeLabel(factors.breakfastTimeMinutes)
        ),
        PieData(
            max(1, factors.lunchTimeMinutes),
            color = segmentColors[2],
            title = translate(TranslationKey.FactorLunch, currentLanguage),
            value = formatTimeLabel(factors.lunchTimeMinutes)
        ),
        PieData(
            max(1, factors.afternoonTimeMinutes),
            color = segmentColors[3],
            title = translate(TranslationKey.FactorAfternoon, currentLanguage),
            value = formatTimeLabel(factors.afternoonTimeMinutes)
        ),
        PieData(
            max(1, factors.dinnerTimeMinutes),
            color = segmentColors[4],
            title = translate(TranslationKey.FactorDinner, currentLanguage),
            value = formatTimeLabel(factors.dinnerTimeMinutes)
        ),
        PieData(
            max(1, factors.lateTimeMinutes),
            color = segmentColors[5],
            title = translate(TranslationKey.FactorLate, currentLanguage),
            value = formatTimeLabel(factors.lateTimeMinutes)
        ),
        PieData(
            max(1, factors.nightTimeMinutes),
            color = segmentColors[6],
            title = translate(TranslationKey.FactorNight, currentLanguage),
            value = formatTimeLabel(factors.nightTimeMinutes)
        )
    )
}

private fun buildScheduleFields(
    factors: FactorsData,
    currentLanguage: AppLanguage,
    segmentColors: List<Color>
): List<ScheduleFieldItem> {
    return listOf(
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Morning,
            title = translate(TranslationKey.FactorMorning, currentLanguage),
            timeMinutes = factors.morningTimeMinutes,
            dotColor = segmentColors[0]
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Breakfast,
            title = translate(TranslationKey.FactorBreakfast, currentLanguage),
            timeMinutes = factors.breakfastTimeMinutes,
            dotColor = segmentColors[1]
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Lunch,
            title = translate(TranslationKey.FactorLunch, currentLanguage),
            timeMinutes = factors.lunchTimeMinutes,
            dotColor = segmentColors[2]
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Afternoon,
            title = translate(TranslationKey.FactorAfternoon, currentLanguage),
            timeMinutes = factors.afternoonTimeMinutes,
            dotColor = segmentColors[3]
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Dinner,
            title = translate(TranslationKey.FactorDinner, currentLanguage),
            timeMinutes = factors.dinnerTimeMinutes,
            dotColor = segmentColors[4]
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Late,
            title = translate(TranslationKey.FactorLate, currentLanguage),
            timeMinutes = factors.lateTimeMinutes,
            dotColor = segmentColors[5]
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Night,
            title = translate(TranslationKey.FactorNight, currentLanguage),
            timeMinutes = factors.nightTimeMinutes,
            dotColor = segmentColors[6]
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Basal,
            title = translate(TranslationKey.BasalRate, currentLanguage),
            timeMinutes = factors.basalTimeMinutes,
            dotColor = null
        )
    )
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
                    currentTimeLabel = formatTimeLabel(nowMinutes)
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
    scheduleFields: List<ScheduleFieldItem>,
    onFieldClick: (ScheduleTimeSlot) -> Unit
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
            scheduleFields.forEach { item ->
                TimePickerField(
                    description = item.title,
                    timeLabel = formatTimeLabel(item.timeMinutes),
                    dotColor = item.dotColor,
                    onClick = { onFieldClick(item.slot) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleTimePickerDialog(
    slot: ScheduleTimeSlot,
    currentLanguage: AppLanguage,
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
        title = { Text(text = slotTitle(slot, currentLanguage)) },
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

private fun slotTitle(slot: ScheduleTimeSlot, currentLanguage: AppLanguage): String {
    return when (slot) {
        ScheduleTimeSlot.Morning -> translate(TranslationKey.FactorMorning, currentLanguage)
        ScheduleTimeSlot.Breakfast -> translate(TranslationKey.FactorBreakfast, currentLanguage)
        ScheduleTimeSlot.Lunch -> translate(TranslationKey.FactorLunch, currentLanguage)
        ScheduleTimeSlot.Afternoon -> translate(TranslationKey.FactorAfternoon, currentLanguage)
        ScheduleTimeSlot.Dinner -> translate(TranslationKey.FactorDinner, currentLanguage)
        ScheduleTimeSlot.Late -> translate(TranslationKey.FactorLate, currentLanguage)
        ScheduleTimeSlot.Night -> translate(TranslationKey.FactorNight, currentLanguage)
        ScheduleTimeSlot.Basal -> translate(TranslationKey.BasalRate, currentLanguage)
    }
}

private fun formatTimeLabel(totalMinutes: Int): String {
    val hour = (totalMinutes / 60) % 24
    val minute = totalMinutes % 60
    return String.format(Locale.ROOT, "%02d:%02d", hour, minute)
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
