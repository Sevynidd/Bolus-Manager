package sevynidd.diabetesapp.data.export

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData
import sevynidd.diabetesapp.data.settings.correction.CorrectionSettings
import sevynidd.diabetesapp.data.settings.correction.GlucoseUnit
import sevynidd.diabetesapp.data.settings.profile.Gender
import java.time.LocalDate

class FactorsExportTest {

    @Test
    fun `round trip preserves a fully populated profile with several factor slots`() {
        val original = FactorsExportBundle(
            factors = FactorsData(
                isPeriodEnabled = true,
                factorSlots = listOf(
                    FactorSlot(name = "Morning", factorValue = "2,5", startTimeMinutes = 6 * 60),
                    FactorSlot(name = "Breakfast", factorValue = "1,75", startTimeMinutes = 8 * 60),
                    FactorSlot(name = "Lunch", factorValue = "1", startTimeMinutes = 12 * 60 + 30),
                    FactorSlot(name = "Afternoon", factorValue = "1,25", startTimeMinutes = 15 * 60),
                    FactorSlot(name = "Dinner", factorValue = "2", startTimeMinutes = 18 * 60),
                    FactorSlot(name = "Late", factorValue = "2,25", startTimeMinutes = 21 * 60),
                    FactorSlot(name = "Night", factorValue = "3", startTimeMinutes = 23 * 60 + 30)
                ),
                basalRate = "12",
                basalTimeMinutes = 20 * 60,
                basalReminderEnabled = true
            ),
            breadUnits = 10.0,
            periodFactorPercent = 15.0,
            correctionSettings = CorrectionSettings(
                thresholdMgDl = 150.0,
                stepMgDl = 25.0,
                glucoseUnit = GlucoseUnit.MmolL
            ),
            gender = Gender.Female
        )

        val roundTripped = parseFactorsExportJson(original.toExportJson())

        assertEquals(original, roundTripped)
    }

    @Test
    fun `round trip preserves a single factor slot`() {
        val original = FactorsExportBundle(
            factors = FactorsData(
                factorSlots = listOf(FactorSlot(name = "All day", factorValue = "1,5", startTimeMinutes = 0))
            ),
            breadUnits = 12.0,
            periodFactorPercent = 0.0,
            correctionSettings = CorrectionSettings(),
            gender = Gender.PreferNotToSay
        )

        val roundTripped = parseFactorsExportJson(original.toExportJson())

        assertEquals(original, roundTripped)
    }

    @Test
    fun `round trip preserves an empty default profile`() {
        val original = FactorsExportBundle(
            factors = FactorsData(),
            breadUnits = 12.0,
            periodFactorPercent = 0.0,
            correctionSettings = CorrectionSettings(),
            gender = Gender.PreferNotToSay
        )

        val roundTripped = parseFactorsExportJson(original.toExportJson())

        assertEquals(original, roundTripped)
    }

    @Test
    fun `round trip preserves a factor name containing quotes, backslashes, brackets, and commas`() {
        val trickyName = """Weird "name", with [brackets] \ backslash"""
        val original = FactorsExportBundle(
            factors = FactorsData(
                factorSlots = listOf(FactorSlot(name = trickyName, factorValue = "1", startTimeMinutes = 0))
            ),
            breadUnits = 12.0,
            periodFactorPercent = 0.0,
            correctionSettings = CorrectionSettings(),
            gender = Gender.PreferNotToSay
        )

        val roundTripped = parseFactorsExportJson(original.toExportJson())

        assertEquals(original, roundTripped)
    }

    @Test
    fun `parse rejects malformed json`() {
        assertNull(parseFactorsExportJson("not json at all"))
    }

    @Test
    fun `parse rejects a v1 export from before dynamic factors`() {
        val v1Json = """
            {
              "schemaVersion": 1,
              "isPeriodEnabled": false,
              "basalReminderEnabled": false,
              "morningFactor": "1",
              "breakfastFactor": "2",
              "lunchFactor": "3",
              "afternoonFactor": "4",
              "dinnerFactor": "5",
              "lateFactor": "6",
              "nightFactor": "0,5",
              "basalRate": "10",
              "morningTimeMinutes": 300,
              "breakfastTimeMinutes": 540,
              "lunchTimeMinutes": 720,
              "afternoonTimeMinutes": 840,
              "dinnerTimeMinutes": 1020,
              "lateTimeMinutes": 1200,
              "nightTimeMinutes": 1380,
              "basalTimeMinutes": 1140
            }
        """.trimIndent()

        assertNull(parseFactorsExportJson(v1Json))
    }

    @Test
    fun `parse rejects an unsupported schema version`() {
        val defaultBundle = FactorsExportBundle(
            factors = FactorsData(),
            breadUnits = 12.0,
            periodFactorPercent = 0.0,
            correctionSettings = CorrectionSettings(),
            gender = Gender.PreferNotToSay
        )
        val futureVersionJson = defaultBundle.toExportJson().replace(
            "\"schemaVersion\": 3",
            "\"schemaVersion\": 99"
        )

        assertNull(parseFactorsExportJson(futureVersionJson))
    }

    @Test
    fun `parse rejects json missing required fields`() {
        assertNull(parseFactorsExportJson("{\n  \"schemaVersion\": 3\n}"))
    }

    @Test
    fun `parse rejects json with no factors array at all`() {
        assertNull(parseFactorsExportJson("{\n  \"schemaVersion\": 3,\n  \"isPeriodEnabled\": false\n}"))
    }

    @Test
    fun `export file name embeds the given date`() {
        val fileName = factorsExportFileName(LocalDate.of(2026, 7, 9))

        assertEquals("bolus-manager-factors-2026-07-09.json", fileName)
    }

    @Test
    fun `export file name zero-pads single-digit month and day`() {
        val fileName = factorsExportFileName(LocalDate.of(2026, 1, 5))

        assertEquals("bolus-manager-factors-2026-01-05.json", fileName)
    }
}
