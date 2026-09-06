package sevynidd.diabetesapp.data.export

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.localization.AppLanguage
import java.time.LocalDateTime
import java.time.ZoneOffset

class FactorsPdfReportTest {

    private val fixedGeneratedAt: LocalDateTime = LocalDateTime.of(2026, 1, 1, 8, 0)

    @Test
    fun `normal case builds one row per factor with its time window and the basal rate summary`() {
        val factors = FactorsData(
            factorSlots = listOf(
                FactorSlot(name = "Morning", factorValue = "1,2", startTimeMinutes = 5 * 60),
                FactorSlot(name = "Evening", factorValue = "1,0", startTimeMinutes = 18 * 60)
            ),
            basalRate = "12,0",
            basalTimeMinutes = 19 * 60
        )

        val content = buildFactorsPdfReportContent(
            factors = factors,
            auditLog = emptyList(),
            language = AppLanguage.English,
            zone = ZoneOffset.UTC,
            generatedAt = fixedGeneratedAt
        )

        assertEquals(2, content.factorRows.size)
        assertEquals(FactorReportRow("Morning", "1,2", "05:00 - 17:59"), content.factorRows[0])
        assertEquals(FactorReportRow("Evening", "1,0", "18:00 - 04:59"), content.factorRows[1])
        assertEquals("12,0 units/day at 19:00", content.basalRateSummary)
        assertTrue(content.generatedAtLabel.contains("2026-01-01 08:00"))
    }

    @Test
    fun `boundary of a single factor slot wraps its time window to itself`() {
        val factors = FactorsData(factorSlots = listOf(FactorSlot(name = "AllDay", startTimeMinutes = 0)))

        val content = buildFactorsPdfReportContent(
            factors = factors,
            auditLog = emptyList(),
            language = AppLanguage.English,
            zone = ZoneOffset.UTC,
            generatedAt = fixedGeneratedAt
        )

        assertEquals("00:00 - 23:59", content.factorRows.single().timeRange)
    }

    @Test
    fun `invalid case of no factors and no log entries produces empty sections without crashing`() {
        val content = buildFactorsPdfReportContent(
            factors = FactorsData(factorSlots = emptyList()),
            auditLog = emptyList(),
            language = AppLanguage.English,
            zone = ZoneOffset.UTC,
            generatedAt = fixedGeneratedAt
        )

        assertTrue(content.factorRows.isEmpty())
        assertTrue(content.logEntries.isEmpty())
    }

    @Test
    fun `log entries are sorted chronologically regardless of input order`() {
        val later = FactorAuditLogEntry(
            timestampMillis = 2_000L,
            changeType = "FACTOR_ADDED",
            factorName = "Later",
            oldValue = null,
            newValue = 1.0,
            oldTimeMinutes = null,
            newTimeMinutes = 0
        )
        val earlier = later.copy(timestampMillis = 1_000L, factorName = "Earlier")

        val content = buildFactorsPdfReportContent(
            factors = FactorsData(factorSlots = emptyList()),
            auditLog = listOf(later, earlier),
            language = AppLanguage.English,
            zone = ZoneOffset.UTC,
            generatedAt = fixedGeneratedAt
        )

        assertEquals(2, content.logEntries.size)
        assertTrue(content.logEntries[0].description.contains("Earlier"))
        assertTrue(content.logEntries[1].description.contains("Later"))
    }
}
