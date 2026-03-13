package sevynidd.diabetesapp.data.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Locale

class FactorsRepository(
    private val dao: FactorProfileDao
) {
    val factorsFlow: Flow<FactorsData> = dao.observeProfile().map { entity ->
        entity?.toFactorsData() ?: FactorsData()
    }

    suspend fun saveFactors(data: FactorsData) {
        dao.upsertProfile(data.toEntity())
    }
}

private fun FactorProfileEntity.toFactorsData(): FactorsData {
    return FactorsData(
        morningFactor = morningFactor.toUiString(),
        breakfastFactor = breakfastFactor.toUiString(),
        lunchFactor = lunchFactor.toUiString(),
        afternoonFactor = afternoonFactor.toUiString(),
        dinnerFactor = dinnerFactor.toUiString(),
        lateFactor = lateFactor.toUiString(),
        nightFactor = nightFactor.toUiString(),
        basalRate = basalRate?.toString().orEmpty()
    )
}

private fun FactorsData.toEntity(): FactorProfileEntity {
    return FactorProfileEntity(
        id = FactorProfileEntity.SINGLE_PROFILE_ID,
        morningFactor = morningFactor.toDbDoubleOrNull(),
        breakfastFactor = breakfastFactor.toDbDoubleOrNull(),
        lunchFactor = lunchFactor.toDbDoubleOrNull(),
        afternoonFactor = afternoonFactor.toDbDoubleOrNull(),
        dinnerFactor = dinnerFactor.toDbDoubleOrNull(),
        lateFactor = lateFactor.toDbDoubleOrNull(),
        nightFactor = nightFactor.toDbDoubleOrNull(),
        basalRate = basalRate.toIntOrNull()
    )
}

private fun String.toDbDoubleOrNull(): Double? {
    return replace(',', '.').toDoubleOrNull()
}

private fun Double?.toUiString(): String {
    if (this == null) return ""
    if (this % 1.0 == 0.0) return toInt().toString()

    return String.format(Locale.US, "%.2f", this)
        .replace('.', ',')
        .trimEnd('0')
        .trimEnd(',')
}

