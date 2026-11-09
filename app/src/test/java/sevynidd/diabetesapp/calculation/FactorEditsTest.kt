package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sevynidd.diabetesapp.data.model.FactorSlot
import sevynidd.diabetesapp.data.model.FactorsData

class FactorEditsTest {
    private val slots = listOf(
        FactorSlot("Morning", "1,5", 300),
        FactorSlot("Lunch", "1", 720)
    )

    @Test
    fun `a new unique name is valid`() {
        assertTrue(isFactorNameValid("Dinner", listOf("Morning", "Lunch")))
    }

    @Test
    fun `a blank name is invalid`() {
        assertFalse(isFactorNameValid("   ", emptyList()))
    }

    @Test
    fun `a duplicate name is invalid regardless of case and spaces`() {
        assertFalse(isFactorNameValid(" lunch ", listOf("Morning", "Lunch")))
    }

    @Test
    fun `editing a slot changes name and value but keeps its time`() {
        val edited = slots.withSlotEdited(1, " Noon ", "1,25")

        assertEquals(FactorSlot("Noon", "1,25", 720), edited[1])
        assertEquals(slots[0], edited[0])
    }

    @Test
    fun `deleting a slot removes only that slot`() {
        assertEquals(listOf(slots[1]), slots.withoutSlot(0))
    }

    @Test
    fun `the last remaining slot cannot be deleted`() {
        val single = listOf(slots[0])

        assertEquals(single, single.withoutSlot(0))
    }

    @Test
    fun `deleting an index outside the list changes nothing`() {
        assertEquals(slots, slots.withoutSlot(5))
    }

    @Test
    fun `a rate is replaced only for its hour`() {
        assertEquals(listOf("", "0,8", ""), listOf("", "", "").withRateAt(1, "0,8"))
    }

    @Test
    fun `a rate for an hour outside the list changes nothing`() {
        assertEquals(listOf("1"), listOf("1").withRateAt(3, "2"))
    }

    @Test
    fun `a slot edit changes only the factor window`() {
        val factors = FactorsData(factorSlots = slots, basalRate = "20")

        val edited = factors.withEdit(FactorEditKind.Slot, 0, "Early", "2")

        assertEquals(FactorSlot("Early", "2", 300), edited.factorSlots[0])
        assertEquals("20", edited.basalRate)
    }

    @Test
    fun `a basal edit changes only the basal rate`() {
        val factors = FactorsData(factorSlots = slots, basalRate = "20")

        val edited = factors.withEdit(FactorEditKind.Basal, 0, "", "22")

        assertEquals("22", edited.basalRate)
        assertEquals(slots, edited.factorSlots)
    }

    @Test
    fun `a pump hour edit changes only that hour`() {
        val edited = FactorsData().withEdit(FactorEditKind.PumpHour, 3, "", "0,8")

        assertEquals("0,8", edited.pumpBasalRates[3])
        assertEquals("", edited.pumpBasalRates[2])
    }
}
