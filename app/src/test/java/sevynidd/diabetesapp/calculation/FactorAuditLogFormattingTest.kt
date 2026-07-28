package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.localization.AppLanguage

private fun entry(
    changeType: FactorChangeType,
    timestampMillis: Long = 0L,
    factorName: String? = "Morning",
    values: Pair<Double?, Double?> = null to null,
    times: Pair<Int?, Int?> = null to null
) = FactorAuditLogEntry(
    timestampMillis = timestampMillis,
    changeType = changeType.name,
    factorName = factorName,
    oldValue = values.first,
    newValue = values.second,
    oldTimeMinutes = times.first,
    newTimeMinutes = times.second
)

class FactorAuditLogFormattingTest {

    @Test
    fun `normal case describes a factor value change with both old and new value`() {
        val description = entry(
            changeType = FactorChangeType.FACTOR_VALUE_CHANGED,
            values = 1.2 to 1.5
        ).describe(AppLanguage.English)

        assertTrue(description.contains("Morning"))
        assertTrue(description.contains("1,2"))
        assertTrue(description.contains("1,5"))
    }

    @Test
    fun `normal case describes a factor addition with the new value`() {
        val entryToDescribe = entry(changeType = FactorChangeType.FACTOR_ADDED, values = null to 2.0)

        val description = entryToDescribe.describe(AppLanguage.English)

        assertTrue(description.contains("Morning"))
        assertTrue(description.contains("2"))
    }

    @Test
    fun `boundary of a null old value is described as not set rather than crashing`() {
        val description = entry(
            changeType = FactorChangeType.BASAL_RATE_CHANGED,
            factorName = null,
            values = null to 18.0
        ).describe(AppLanguage.English)

        assertTrue(description.contains("18"))
    }

    @Test
    fun `invalid case of an unrecognized change type falls back to a generic description`() {
        val base = entry(changeType = FactorChangeType.FACTOR_ADDED, values = null to 1.0)
        val corrupted = base.copy(changeType = "NOT_A_REAL_TYPE")

        val description = corrupted.describe(AppLanguage.English)

        assertEquals(true, description.isNotBlank())
    }

    @Test
    fun `normal case builds a chronological history series per factor from value changes`() {
        val entries = listOf(
            entry(timestampMillis = 200L, changeType = FactorChangeType.FACTOR_VALUE_CHANGED, values = null to 1.5),
            entry(timestampMillis = 100L, changeType = FactorChangeType.FACTOR_ADDED, values = null to 1.2)
        )

        val series = factorHistorySeries(entries)

        assertEquals(1, series.size)
        val morning = series.single { it.factorName == "Morning" }
        assertEquals(listOf(1.2, 1.5), morning.points.map { it.value })
        assertEquals(listOf(100L, 200L), morning.points.map { it.timestampMillis })
    }

    @Test
    fun `boundary of a deleted factor still keeps its earlier value history`() {
        val entries = listOf(
            entry(timestampMillis = 100L, changeType = FactorChangeType.FACTOR_ADDED, values = null to 1.2),
            entry(timestampMillis = 200L, changeType = FactorChangeType.FACTOR_DELETED, values = 1.2 to null)
        )

        val series = factorHistorySeries(entries)

        assertEquals(1, series.size)
        assertEquals(listOf(1.2), series.single().points.map { it.value })
    }

    @Test
    fun `invalid case of only time changes produces no history series`() {
        val entries = listOf(
            entry(changeType = FactorChangeType.FACTOR_TIME_CHANGED, times = 300 to 360)
        )

        val series = factorHistorySeries(entries)

        assertTrue(series.isEmpty())
    }

    @Test
    fun `normal case separates the basal rate series from named factor series`() {
        val entries = listOf(
            entry(timestampMillis = 100L, changeType = FactorChangeType.FACTOR_ADDED, values = null to 1.2),
            entry(
                timestampMillis = 150L,
                changeType = FactorChangeType.BASAL_RATE_CHANGED,
                factorName = null,
                values = null to 18.0
            )
        )

        val series = factorHistorySeries(entries)

        assertEquals(2, series.size)
        assertTrue(series.any { it.factorName == null })
        assertTrue(series.any { it.factorName == "Morning" })
    }
}
