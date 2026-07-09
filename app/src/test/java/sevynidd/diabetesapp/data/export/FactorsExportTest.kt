package sevynidd.diabetesapp.data.export

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorsData

class FactorsExportTest {

    @Test
    fun `round trip preserves a fully populated profile`() {
        val original = FactorsData(
            isPeriodEnabled = true,
            morningFactor = "2,5",
            breakfastFactor = "1,75",
            lunchFactor = "1",
            afternoonFactor = "1,25",
            dinnerFactor = "2",
            lateFactor = "2,25",
            nightFactor = "3",
            basalRate = "12",
            morningTimeMinutes = 6 * 60,
            breakfastTimeMinutes = 8 * 60,
            lunchTimeMinutes = 12 * 60 + 30,
            afternoonTimeMinutes = 15 * 60,
            dinnerTimeMinutes = 18 * 60,
            lateTimeMinutes = 21 * 60,
            nightTimeMinutes = 23 * 60 + 30,
            basalTimeMinutes = 20 * 60,
            basalReminderEnabled = true
        )

        val roundTripped = parseFactorsExportJson(original.toExportJson())

        assertEquals(original, roundTripped)
    }

    @Test
    fun `round trip preserves an empty default profile`() {
        val original = FactorsData()

        val roundTripped = parseFactorsExportJson(original.toExportJson())

        assertEquals(original, roundTripped)
    }

    @Test
    fun `parse rejects malformed json`() {
        assertNull(parseFactorsExportJson("not json at all"))
    }

    @Test
    fun `parse rejects an unsupported schema version`() {
        val futureVersionJson = FactorsData().toExportJson().replace(
            "\"schemaVersion\": 1",
            "\"schemaVersion\": 99"
        )

        assertNull(parseFactorsExportJson(futureVersionJson))
    }

    @Test
    fun `parse rejects json missing required fields`() {
        assertNull(parseFactorsExportJson("{\n  \"schemaVersion\": 1\n}"))
    }
}
