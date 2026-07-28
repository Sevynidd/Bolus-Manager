package sevynidd.diabetesapp.data.export

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorAuditLogEntry
import sevynidd.diabetesapp.localization.AppLanguage
import java.time.ZoneOffset

class AuditLogExportTest {

    @Test
    fun `normal case exports header plus one row per entry`() {
        val entries = listOf(
            FactorAuditLogEntry(
                timestampMillis = 0L,
                changeType = "FACTOR_ADDED",
                factorName = "Morning",
                oldValue = null,
                newValue = 1.2,
                oldTimeMinutes = null,
                newTimeMinutes = 300
            )
        )

        val csv = entries.toAuditLogCsv(AppLanguage.English, ZoneOffset.UTC)
        val lines = csv.lines()

        assertEquals(2, lines.size)
        assertEquals("Date;Description", lines[0])
        assertTrue(lines[1].contains("Morning"))
    }

    @Test
    fun `boundary of a description containing the delimiter is quoted`() {
        val entries = listOf(
            FactorAuditLogEntry(
                timestampMillis = 0L,
                changeType = "FACTOR_ADDED",
                factorName = "A;B",
                oldValue = null,
                newValue = 1.0,
                oldTimeMinutes = null,
                newTimeMinutes = 0
            )
        )

        val csv = entries.toAuditLogCsv(AppLanguage.English, ZoneOffset.UTC)

        assertTrue(csv.lines()[1].contains("\"") )
    }

    @Test
    fun `invalid case of an empty log exports only the header`() {
        val csv = emptyList<FactorAuditLogEntry>().toAuditLogCsv(AppLanguage.English)

        assertEquals(listOf("Date;Description"), csv.lines())
    }
}
