package sevynidd.diabetesapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.database.FactorsData
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.time.LocalTime
import java.util.Locale
import kotlin.math.roundToInt

enum class BolusMode {
    Normal,
    Split
}

@Composable
fun CalculateScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    factors: FactorsData = FactorsData(),
    breadUnits: Double = 12.0
) {
    var selectedMode by rememberSaveable { mutableStateOf(BolusMode.Normal) }
    var carbohydrates by rememberSaveable { mutableStateOf("") }
    var splitCarbohydrates by rememberSaveable { mutableStateOf("") }
    var splitImmediatePercent by rememberSaveable { mutableStateOf("") }
    var splitDurationMinutes by rememberSaveable { mutableStateOf("120") }

    val now = LocalTime.now()
    val nowMinutes = (now.hour * 60) + now.minute
    val activeFactorInfo = activeFactorForTime(factors, nowMinutes)
    val activeFactor = activeFactorInfo.factor
    val activeFactorText = activeFactorInfo.toDisplayText(currentLanguage)
    val effectiveBreadUnits = breadUnits.takeIf { it > 0.0 } ?: 12.0

    val carbohydratesValue = carbohydrates.replace(',', '.').toDoubleOrNull()
    val calculatedUnits = if (carbohydratesValue != null && activeFactor != null) {
        (carbohydratesValue / effectiveBreadUnits) * activeFactor
    } else {
        null
    }
    val calculatedUnitsText = calculatedUnits.toUiDecimalOrEmpty()

    val splitCarbohydratesValue = splitCarbohydrates.replace(',', '.').toDoubleOrNull()
    val splitImmediatePercentValue = splitImmediatePercent.toIntOrNull()?.coerceIn(0, 100)
    val splitRestPercentValue = splitImmediatePercentValue?.let { 100 - it }
    val splitDurationValue = splitDurationMinutes.replace(',', '.').toDoubleOrNull()
    val splitDurationOffsetMinutes = splitDurationValue?.roundToInt()?.coerceAtLeast(0) ?: 120
    val futureFactorTimeMinutes = (nowMinutes + splitDurationOffsetMinutes) % MINUTES_PER_DAY
    val futureFactorInfo = activeFactorForTime(factors, futureFactorTimeMinutes)
    val futureFactor = futureFactorInfo.factor
    val futureFactorText = futureFactorInfo.toDisplayText(currentLanguage)

    val splitImmediateCarbohydrates = if (splitCarbohydratesValue != null && splitImmediatePercentValue != null) {
        splitCarbohydratesValue * splitImmediatePercentValue / 100.0
    } else {
        null
    }
    val splitRestCarbohydrates = if (splitCarbohydratesValue != null && splitRestPercentValue != null) {
        splitCarbohydratesValue * splitRestPercentValue / 100.0
    } else {
        null
    }
    val splitImmediateUnits = if (splitImmediateCarbohydrates != null && activeFactor != null) {
        (splitImmediateCarbohydrates / effectiveBreadUnits) * activeFactor
    } else {
        null
    }
    val splitRestUnits = if (splitRestCarbohydrates != null && futureFactor != null) {
        (splitRestCarbohydrates / effectiveBreadUnits) * futureFactor
    } else {
        null
    }
    val splitTotalUnits = if (splitImmediateUnits != null && splitRestUnits != null) {
        splitImmediateUnits + splitRestUnits
    } else {
        null
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = translate(TranslationKey.BolusType, currentLanguage),
            style = MaterialTheme.typography.titleMedium
        )

        val bolusTabs = listOf(BolusMode.Normal, BolusMode.Split)
        PrimaryTabRow(
            selectedTabIndex = bolusTabs.indexOf(selectedMode),
            modifier = Modifier.fillMaxWidth()
        ) {
            bolusTabs.forEach { tabMode ->
                Tab(
                    selected = tabMode == selectedMode,
                    onClick = { selectedMode = tabMode },
                    text = {
                        Text(
                            text = when (tabMode) {
                                BolusMode.Normal -> translate(TranslationKey.BolusNormal, currentLanguage)
                                BolusMode.Split -> translate(TranslationKey.BolusSplit, currentLanguage)
                            }
                        )
                    }
                )
            }
        }

        when (selectedMode) {
            BolusMode.Normal -> {
                OutlinedTextField(
                    value = carbohydrates,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*[.,]?\\d*$"))) {
                            carbohydrates = newValue
                        }
                    },
                    label = { Text(translate(TranslationKey.Carbohydrates, currentLanguage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = activeFactorText,
                    onValueChange = {},
                    label = { Text(translate(TranslationKey.ActiveFactor, currentLanguage)) },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = calculatedUnitsText,
                    onValueChange = {},
                    label = { Text(translate(TranslationKey.CalculatedUnits, currentLanguage)) },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            BolusMode.Split -> {
                OutlinedTextField(
                    value = splitCarbohydrates,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*[.,]?\\d*$"))) {
                            splitCarbohydrates = newValue
                        }
                    },
                    label = { Text(translate(TranslationKey.Carbohydrates, currentLanguage)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = splitImmediatePercent,
                        onValueChange = { newValue ->
                            sanitizePercentageInput(newValue)?.let { sanitizedValue ->
                                splitImmediatePercent = sanitizedValue
                            }
                        },
                        label = { Text(translate(TranslationKey.BolusImmediatePercent, currentLanguage)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = splitRestPercentValue?.toString().orEmpty(),
                        onValueChange = {},
                        label = { Text(translate(TranslationKey.BolusExtendedPercent, currentLanguage)) },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = splitDurationMinutes,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*[.,]?\\d*$"))) {
                            splitDurationMinutes = newValue
                        }
                    },
                    label = { Text(translate(TranslationKey.BolusDurationMinutes, currentLanguage)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Calculated",
                    style = MaterialTheme.typography.titleSmall
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = activeFactorText,
                        onValueChange = {},
                        label = { Text(translate(TranslationKey.ActiveFactor, currentLanguage)) },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = futureFactorText,
                        onValueChange = {},
                        label = { Text(translate(TranslationKey.FutureFactor, currentLanguage)) },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = splitTotalUnits.toUiDecimalOrEmpty(),
                    onValueChange = {},
                    label = { Text(translate(TranslationKey.CalculatedUnits, currentLanguage)) },
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = splitImmediateUnits.toUiDecimalOrEmpty(),
                        onValueChange = {},
                        label = { Text(translate(TranslationKey.BolusImmediateUnits, currentLanguage)) },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = splitRestUnits.toUiDecimalOrEmpty(),
                        onValueChange = {},
                        label = { Text(translate(TranslationKey.BolusExtendedUnits, currentLanguage)) },
                        readOnly = true,
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

private data class ActiveFactorInfo(
    val factor: Double?,
    val durationMinutes: Int,
    val factorLabel: TranslationKey
)

private fun activeFactorForTime(factors: FactorsData, nowMinutes: Int): ActiveFactorInfo {
    val morning = factors.morningTimeMinutes
    val breakfast = factors.breakfastTimeMinutes
    val lunch = factors.lunchTimeMinutes
    val afternoon = factors.afternoonTimeMinutes
    val dinner = factors.dinnerTimeMinutes
    val late = factors.lateTimeMinutes
    val night = factors.nightTimeMinutes

    val (factorText, duration, factorLabel) = when {
        nowMinutes !in morning..<night -> Triple(
            factors.nightFactor,
            (MINUTES_PER_DAY - night) + morning,
            TranslationKey.FactorNight
        )

        nowMinutes < breakfast -> Triple(
            factors.morningFactor,
            breakfast - morning,
            TranslationKey.FactorMorning
        )

        nowMinutes < lunch -> Triple(
            factors.breakfastFactor,
            lunch - breakfast,
            TranslationKey.FactorBreakfast
        )

        nowMinutes < afternoon -> Triple(
            factors.lunchFactor,
            afternoon - lunch,
            TranslationKey.FactorLunch
        )

        nowMinutes < dinner -> Triple(
            factors.afternoonFactor,
            dinner - afternoon,
            TranslationKey.FactorAfternoon
        )

        nowMinutes < late -> Triple(
            factors.dinnerFactor,
            late - dinner,
            TranslationKey.FactorDinner
        )

        else -> Triple(
            factors.lateFactor,
            night - late,
            TranslationKey.FactorLate
        )
    }

    return ActiveFactorInfo(
        factor = factorText.replace(',', '.').toDoubleOrNull(),
        durationMinutes = duration.coerceAtLeast(1),
        factorLabel = factorLabel
    )
}

private fun ActiveFactorInfo.toDisplayText(language: AppLanguage): String {
    val factorName = translate(factorLabel, language)
    val factorValue = factor.toUiDecimalOrEmpty()
    return if (factorValue.isBlank()) factorName else "$factorName · $factorValue"
}

private fun Double?.toUiDecimalOrEmpty(): String {
    return this?.let { value ->
        String.format(Locale.ROOT, "%.2f", value)
            .replace('.', ',')
            .trimEnd('0')
            .trimEnd(',')
    }.orEmpty()
}

private fun sanitizePercentageInput(input: String): String? {
    if (input.isEmpty()) return ""
    if (!input.matches(Regex("^\\d{0,3}$"))) return null

    return input.toIntOrNull()?.coerceAtMost(100)?.toString() ?: input
}

private const val MINUTES_PER_DAY = 24 * 60
