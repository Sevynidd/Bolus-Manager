package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import sevynidd.diabetesapp.calculation.withUpdatedTime
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.libraries.gappedPieChart.AnimatedGapPieChart
import sevynidd.diabetesapp.libraries.gappedPieChart.PieData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.util.Locale
import kotlin.math.max

private data class ScheduleFieldItem(
    val slot: ScheduleTimeSlot,
    val title: String,
    val timeMinutes: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleFactorScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    onFactorsChange: (FactorsData) -> Unit = {}
) {
    var activePicker by rememberSaveable { mutableStateOf<ScheduleTimeSlot?>(null) }

    val morningTimeMinutes = factors.morningTimeMinutes
    val breakfastTimeMinutes = factors.breakfastTimeMinutes
    val lunchTimeMinutes = factors.lunchTimeMinutes
    val afternoonTimeMinutes = factors.afternoonTimeMinutes
    val dinnerTimeMinutes = factors.dinnerTimeMinutes
    val lateTimeMinutes = factors.lateTimeMinutes
    val nightTimeMinutes = factors.nightTimeMinutes
    val basalTimeMinutes = factors.basalTimeMinutes

    fun updateTime(slot: ScheduleTimeSlot, selectedMinutes: Int) {
        onFactorsChange(factors.withUpdatedTime(slot, selectedMinutes))
    }

    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < MID_LUMINANCE
    val segmentColors = remember(isDarkTheme) { pieSegmentColors(isDarkTheme) }

    val pieDataPoints = remember(
        currentLanguage,
        segmentColors,
        morningTimeMinutes,
        breakfastTimeMinutes,
        lunchTimeMinutes,
        afternoonTimeMinutes,
        dinnerTimeMinutes,
        lateTimeMinutes,
        nightTimeMinutes
    ) {
        listOf(
            PieData(
                max(1, morningTimeMinutes),
                color = segmentColors[0],
                title = translate(TranslationKey.FactorMorning, currentLanguage),
                value = formatTimeLabel(morningTimeMinutes)
            ),
            PieData(
                max(1, breakfastTimeMinutes),
                color = segmentColors[1],
                title = translate(TranslationKey.FactorBreakfast, currentLanguage),
                value = formatTimeLabel(breakfastTimeMinutes)
            ),
            PieData(
                max(1, lunchTimeMinutes),
                color = segmentColors[2],
                title = translate(TranslationKey.FactorLunch, currentLanguage),
                value = formatTimeLabel(lunchTimeMinutes)
            ),
            PieData(
                max(1, afternoonTimeMinutes),
                color = segmentColors[3],
                title = translate(TranslationKey.FactorAfternoon, currentLanguage),
                value = formatTimeLabel(afternoonTimeMinutes)
            ),
            PieData(
                max(1, dinnerTimeMinutes),
                color = segmentColors[4],
                title = translate(TranslationKey.FactorDinner, currentLanguage),
                value = formatTimeLabel(dinnerTimeMinutes)
            ),
            PieData(
                max(1, lateTimeMinutes),
                color = segmentColors[5],
                title = translate(TranslationKey.FactorLate, currentLanguage),
                value = formatTimeLabel(lateTimeMinutes)
            ),
            PieData(
                max(1, nightTimeMinutes),
                color = segmentColors[6],
                title = translate(TranslationKey.FactorNight, currentLanguage),
                value = formatTimeLabel(nightTimeMinutes)
            )
        )
    }

    val scheduleFields = listOf(
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Morning,
            title = translate(TranslationKey.FactorMorning, currentLanguage),
            timeMinutes = morningTimeMinutes
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Breakfast,
            title = translate(TranslationKey.FactorBreakfast, currentLanguage),
            timeMinutes = breakfastTimeMinutes
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Lunch,
            title = translate(TranslationKey.FactorLunch, currentLanguage),
            timeMinutes = lunchTimeMinutes
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Afternoon,
            title = translate(TranslationKey.FactorAfternoon, currentLanguage),
            timeMinutes = afternoonTimeMinutes
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Dinner,
            title = translate(TranslationKey.FactorDinner, currentLanguage),
            timeMinutes = dinnerTimeMinutes
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Late,
            title = translate(TranslationKey.FactorLate, currentLanguage),
            timeMinutes = lateTimeMinutes
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Night,
            title = translate(TranslationKey.FactorNight, currentLanguage),
            timeMinutes = nightTimeMinutes
        ),
        ScheduleFieldItem(
            slot = ScheduleTimeSlot.Basal,
            title = translate(TranslationKey.BasalRate, currentLanguage),
            timeMinutes = basalTimeMinutes
        )
    )

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = translate(TranslationKey.ActionSchedule, currentLanguage),
            style = MaterialTheme.typography.titleLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                AnimatedGapPieChart(
                    modifier = Modifier.align(Alignment.Center),
                    pieDataPoints = pieDataPoints
                )
            }

            Text(
                text = translate(TranslationKey.ScheduleAutoOrderHint, currentLanguage),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }

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
                        onClick = { activePicker = item.slot }
                    )
                }
            }
        }
    }

    activePicker?.let { slot ->
        val initialMinutes = when (slot) {
            ScheduleTimeSlot.Morning -> morningTimeMinutes
            ScheduleTimeSlot.Breakfast -> breakfastTimeMinutes
            ScheduleTimeSlot.Lunch -> lunchTimeMinutes
            ScheduleTimeSlot.Afternoon -> afternoonTimeMinutes
            ScheduleTimeSlot.Dinner -> dinnerTimeMinutes
            ScheduleTimeSlot.Late -> lateTimeMinutes
            ScheduleTimeSlot.Night -> nightTimeMinutes
            ScheduleTimeSlot.Basal -> basalTimeMinutes
        }

        val pickerState = rememberTimePickerState(
            initialHour = (initialMinutes / 60) % 24,
            initialMinute = initialMinutes % 60,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { activePicker = null },
            title = {
                Text(
                    text = slotTitle(slot, currentLanguage)
                )
            },
            text = { TimePicker(state = pickerState) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMinutes = (pickerState.hour * 60) + pickerState.minute
                        updateTime(slot, selectedMinutes)
                        activePicker = null
                    }
                ) {
                    Text(stringResource(android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { activePicker = null }) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun TimePickerField(
    description: String,
    timeLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
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
 * The 7 pie-chart segment colors for morning/breakfast/lunch/afternoon/dinner/late/night, in
 * that order. Kept as a dedicated categorical palette rather than the theme's primary/secondary/
 * tertiary roles: this app's generated Material scheme is a monochromatic rust/brown palette, so
 * routing the chart through those roles would make all 7 slices look nearly identical. [isDarkTheme]
 * selects a lighter, less saturated variant so segments stay legible against a dark surface.
 */
private fun pieSegmentColors(isDarkTheme: Boolean): List<Color> {
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

private fun formatTimeLabel(totalMinutes: Int): String {
    val hour = (totalMinutes / 60) % 24
    val minute = totalMinutes % 60
    return String.format(Locale.ROOT, "%02d:%02d", hour, minute)
}

@Preview(showBackground = true)
@Composable
private fun ScheduleFactorScreenPreview() {
    ScheduleFactorScreen()
}

private const val MID_LUMINANCE = 0.5f

