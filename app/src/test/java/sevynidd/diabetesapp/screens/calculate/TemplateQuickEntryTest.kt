package sevynidd.diabetesapp.screens.calculate

import org.junit.Assert.assertEquals
import org.junit.Test
import sevynidd.diabetesapp.data.database.BolusTemplateEntity

class TemplateQuickEntryTest {
    private fun template(name: String, lastUsed: Long = 0L, emoji: String? = null, carbs: Double = 30.0) =
        BolusTemplateEntity(
            name = name,
            nameNormalized = name.lowercase(),
            emoji = emoji,
            carbohydrates = carbs,
            lastUsedAtEpochMillis = lastUsed
        )

    @Test
    fun `quick templates are the most recently used first`() {
        val result = quickTemplates(listOf(template("Old", 1), template("New", 9), template("Mid", 5)))

        assertEquals(listOf("New", "Mid", "Old"), result.map { it.name })
    }

    @Test
    fun `quick templates are limited and ties are alphabetical`() {
        val templates = listOf(template("b"), template("A"), template("c"))

        assertEquals(listOf("A", "b"), quickTemplates(templates, limit = 2).map { it.name })
    }

    @Test
    fun `no templates give no quick templates`() {
        assertEquals(emptyList<BolusTemplateEntity>(), quickTemplates(emptyList()))
    }

    @Test
    fun `adding carbs to a blank field starts from zero`() {
        assertEquals("10", adjustCarbohydrates("", 10.0))
    }

    @Test
    fun `adding carbs keeps a decimal entry`() {
        assertEquals("55,5", adjustCarbohydrates("45,5", 10.0))
    }

    @Test
    fun `subtracting carbs never goes below zero`() {
        assertEquals("0", adjustCarbohydrates("4", -10.0))
        assertEquals("0", adjustCarbohydrates("", -10.0))
    }

    @Test
    fun `subtracting exactly the entered carbs gives zero`() {
        assertEquals("0", adjustCarbohydrates("10", -10.0))
    }

    @Test
    fun `chip label joins emoji name and carbs`() {
        assertEquals("🍝 Pasta 80", quickChipLabel(template("Pasta", emoji = "🍝", carbs = 80.0)))
    }

    @Test
    fun `chip label skips a missing or blank emoji`() {
        assertEquals("Bread 30", quickChipLabel(template("Bread")))
        assertEquals("Bread 30", quickChipLabel(template("Bread", emoji = " ")))
    }
}
