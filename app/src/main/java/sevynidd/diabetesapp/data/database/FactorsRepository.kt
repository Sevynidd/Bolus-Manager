package sevynidd.diabetesapp.data.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import java.util.Locale

private const val DEFAULT_MORNING_MINUTES = 5 * 60
private const val DEFAULT_BREAKFAST_MINUTES = 9 * 60
private const val DEFAULT_LUNCH_MINUTES = 12 * 60
private const val DEFAULT_AFTERNOON_MINUTES = 14 * 60
private const val DEFAULT_DINNER_MINUTES = 17 * 60
private const val DEFAULT_LATE_MINUTES = 20 * 60
private const val DEFAULT_NIGHT_MINUTES = 23 * 60
private const val DEFAULT_BASAL_TIME_MINUTES = 19 * 60

private val DEFAULT_TIMES_MINUTES = listOf(
    DEFAULT_MORNING_MINUTES,
    DEFAULT_BREAKFAST_MINUTES,
    DEFAULT_LUNCH_MINUTES,
    DEFAULT_AFTERNOON_MINUTES,
    DEFAULT_DINNER_MINUTES,
    DEFAULT_LATE_MINUTES,
    DEFAULT_NIGHT_MINUTES
)

/** Bridges the persisted [FactorProfileEntity]/[FactorSlotEntity] rows and the UI-friendly [FactorsData] model. */
class FactorsRepository(
    private val dao: FactorProfileDao,
    private val slotDao: FactorSlotDao
) {
    /** The current factor profile as a [FactorsData], falling back to defaults if unset. */
    val factorsFlow: Flow<FactorsData> = combine(dao.observeProfile(), slotDao.observeAll()) { entity, slots ->
        FactorsData(
            isPeriodEnabled = entity?.isPeriodEnabled ?: false,
            factorSlots = slots.map { it.toFactorSlot() },
            basalRate = entity?.basalRate?.toString().orEmpty(),
            basalTimeMinutes = entity?.basalTimeMinutes ?: DEFAULT_BASAL_TIME_MINUTES,
            basalReminderEnabled = entity?.basalReminderEnabled ?: false
        )
    }

    /** Persists [data] as the (single) factor profile, replacing the entire factor slot list. */
    suspend fun saveFactors(data: FactorsData) {
        dao.upsertProfile(data.toEntity())
        slotDao.replaceAll(data.factorSlots.map { it.toEntity() })
    }

    /**
     * Seeds [defaultNames] (paired positionally with today's original default time-of-day
     * boundaries) as the initial factor slots, but only if none exist yet — i.e. only on a
     * genuinely fresh install. Never overwrites factors the user has since added, removed, or
     * renamed. Callers should pass already-localized names (e.g. via `translate()`), since this
     * repository has no access to the current app language.
     */
    suspend fun seedDefaultFactorsIfEmpty(defaultNames: List<String>) {
        if (slotDao.count() > 0) return

        val seeded = defaultNames.mapIndexed { index, name ->
            FactorSlotEntity(
                name = name,
                factorValue = null,
                startTimeMinutes = DEFAULT_TIMES_MINUTES.getOrElse(index) { 0 }
            )
        }
        slotDao.insertAll(seeded)
    }
}

private fun FactorSlotEntity.toFactorSlot(): FactorSlot {
    return FactorSlot(
        name = name,
        factorValue = factorValue.toUiString(),
        startTimeMinutes = startTimeMinutes
    )
}

private fun FactorSlot.toEntity(): FactorSlotEntity {
    return FactorSlotEntity(
        name = name,
        factorValue = factorValue.toDbDoubleOrNull(),
        startTimeMinutes = startTimeMinutes
    )
}

private fun FactorsData.toEntity(): FactorProfileEntity {
    return FactorProfileEntity(
        id = FactorProfileEntity.SINGLE_PROFILE_ID,
        isPeriodEnabled = isPeriodEnabled,
        basalRate = basalRate.toIntOrNull(),
        basalTimeMinutes = basalTimeMinutes,
        basalReminderEnabled = basalReminderEnabled
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
