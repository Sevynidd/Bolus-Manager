package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData

class FactorAuditDiffTest {

    private val baseFactors = FactorsData(
        factorSlots = listOf(
            FactorSlot(name = "Morning", factorValue = "1,2", startTimeMinutes = 300),
            FactorSlot(name = "Lunch", factorValue = "1,5", startTimeMinutes = 720)
        ),
        basalRate = "18",
        basalTimeMinutes = 1140
    )

    @Test
    fun `normal case adding a new slot produces a single FACTOR_ADDED event`() {
        val dinner = FactorSlot(name = "Dinner", factorValue = "2", startTimeMinutes = 1020)
        val updated = baseFactors.copy(factorSlots = baseFactors.factorSlots + dinner)

        val events = diffFactorChanges(baseFactors, updated)

        assertEquals(1, events.size)
        val event = events.single()
        assertEquals(FactorChangeType.FACTOR_ADDED, event.changeType)
        assertEquals("Dinner", event.factorName)
        assertEquals(2.0, event.newValue)
        assertEquals(1020, event.newTimeMinutes)
    }

    @Test
    fun `normal case editing a slot's value produces a single FACTOR_VALUE_CHANGED event`() {
        val updated = baseFactors.copy(
            factorSlots = baseFactors.factorSlots.map {
                if (it.name == "Morning") it.copy(factorValue = "1,8") else it
            }
        )

        val events = diffFactorChanges(baseFactors, updated)

        assertEquals(1, events.size)
        val event = events.single()
        assertEquals(FactorChangeType.FACTOR_VALUE_CHANGED, event.changeType)
        assertEquals("Morning", event.factorName)
        assertEquals(1.2, event.oldValue)
        assertEquals(1.8, event.newValue)
    }

    @Test
    fun `normal case deleting a slot produces a single FACTOR_DELETED event`() {
        val updated = baseFactors.copy(factorSlots = baseFactors.factorSlots.filterNot { it.name == "Lunch" })

        val events = diffFactorChanges(baseFactors, updated)

        assertEquals(1, events.size)
        val event = events.single()
        assertEquals(FactorChangeType.FACTOR_DELETED, event.changeType)
        assertEquals("Lunch", event.factorName)
        assertEquals(1.5, event.oldValue)
        assertEquals(720, event.oldTimeMinutes)
    }

    @Test
    fun `boundary of editing only the start time does not also report a value change`() {
        val updated = baseFactors.copy(
            factorSlots = baseFactors.factorSlots.map {
                if (it.name == "Lunch") it.copy(startTimeMinutes = 780) else it
            }
        )

        val events = diffFactorChanges(baseFactors, updated)

        assertEquals(1, events.size)
        val event = events.single()
        assertEquals(FactorChangeType.FACTOR_TIME_CHANGED, event.changeType)
        assertEquals(720, event.oldTimeMinutes)
        assertEquals(780, event.newTimeMinutes)
    }

    @Test
    fun `boundary of editing both value and time on the same slot reports two events`() {
        val updated = baseFactors.copy(
            factorSlots = baseFactors.factorSlots.map {
                if (it.name == "Lunch") it.copy(factorValue = "1,7", startTimeMinutes = 780) else it
            }
        )

        val events = diffFactorChanges(baseFactors, updated)

        assertEquals(2, events.size)
        assertTrue(events.any { it.changeType == FactorChangeType.FACTOR_VALUE_CHANGED })
        assertTrue(events.any { it.changeType == FactorChangeType.FACTOR_TIME_CHANGED })
    }

    @Test
    fun `invalid case of no changes at all produces no events`() {
        val events = diffFactorChanges(baseFactors, baseFactors.copy())

        assertTrue(events.isEmpty())
    }

    @Test
    fun `basal rate going from unset to set produces a BASAL_RATE_CHANGED event with a null old value`() {
        val previous = baseFactors.copy(basalRate = "")
        val updated = baseFactors.copy(basalRate = "20")

        val events = diffFactorChanges(previous, updated)

        assertEquals(1, events.size)
        val event = events.single()
        assertEquals(FactorChangeType.BASAL_RATE_CHANGED, event.changeType)
        assertEquals(null, event.oldValue)
        assertEquals(20.0, event.newValue)
    }

    @Test
    fun `basal time change produces a BASAL_TIME_CHANGED event`() {
        val updated = baseFactors.copy(basalTimeMinutes = 1200)

        val events = diffFactorChanges(baseFactors, updated)

        assertEquals(1, events.size)
        val event = events.single()
        assertEquals(FactorChangeType.BASAL_TIME_CHANGED, event.changeType)
        assertEquals(1140, event.oldTimeMinutes)
        assertEquals(1200, event.newTimeMinutes)
    }
}
