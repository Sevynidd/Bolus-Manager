package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PumpRatioConversionTest {

    @Test
    fun `normal case converts a factor to grams of carbs per unit`() {
        assertEquals(8.0, requireNotNull(gramsPerUnitFromFactor(factor = 1.5, breadUnits = 12.0)), DELTA)
    }

    @Test
    fun `normal case converts grams per unit back to a factor`() {
        assertEquals(1.2, requireNotNull(factorFromGramsPerUnit(gramsPerUnit = 10.0, breadUnits = 12.0)), DELTA)
    }

    @Test
    fun `zero or negative inputs have no pump ratio`() {
        assertNull(gramsPerUnitFromFactor(factor = 0.0, breadUnits = 12.0))
        assertNull(gramsPerUnitFromFactor(factor = 1.5, breadUnits = 0.0))
        assertNull(factorFromGramsPerUnit(gramsPerUnit = 0.0, breadUnits = 12.0))
        assertNull(factorFromGramsPerUnit(gramsPerUnit = -4.0, breadUnits = 12.0))
    }

    @Test
    fun `tenth gram rounding rounds a midpoint up`() {
        assertEquals(9.3, roundToTenthGram(9.25), DELTA)
        assertEquals(1.1, roundToTenthGram(1.05), DELTA)
        assertEquals(9.2, roundToTenthGram(9.2499), DELTA)
    }

    @Test
    fun `factor text is shown as a rounded pump ratio`() {
        assertEquals("8", factorTextToGramsPerUnitText("1,5", breadUnits = 12.0))
        assertEquals("9,6", factorTextToGramsPerUnitText("1,25", breadUnits = 12.0))
        assertEquals("9,2", factorTextToGramsPerUnitText("1,3", breadUnits = 12.0))
    }

    @Test
    fun `empty or zero factor text has no pump ratio text`() {
        assertEquals("", factorTextToGramsPerUnitText("", breadUnits = 12.0))
        assertEquals("", factorTextToGramsPerUnitText("0", breadUnits = 12.0))
        assertEquals("", factorTextToGramsPerUnitText("abc", breadUnits = 12.0))
    }

    @Test
    fun `pump ratio text is stored as an exact factor instead of the quarter grid`() {
        assertEquals("1,2", gramsPerUnitTextToFactorText("10", breadUnits = 12.0))
        assertEquals("1,33333", gramsPerUnitTextToFactorText("9", breadUnits = 12.0))
    }

    @Test
    fun `pump ratios round-trip through the stored factor text`() {
        val ratios = (MIN_RATIO_TENTHS..MAX_RATIO_TENTHS).map { it / TENTHS_PER_GRAM }
        listOf(10.0, 12.0).forEach { breadUnits ->
            ratios.forEach { ratio ->
                val ratioText = ratio.toString().replace('.', ',')
                val factorText = gramsPerUnitTextToFactorText(ratioText, breadUnits)
                val shownAgain = factorTextToGramsPerUnitText(factorText, breadUnits).replace(',', '.')
                assertEquals("ratio $ratio at $breadUnits g/BU", ratio, shownAgain.toDouble(), DELTA)
            }
        }
    }

    @Test
    fun `invalid pump ratio text yields an empty factor`() {
        assertEquals("", gramsPerUnitTextToFactorText("", breadUnits = 12.0))
        assertEquals("", gramsPerUnitTextToFactorText("0", breadUnits = 12.0))
    }

    @Test
    fun `typed pump ratio is normalized to a tenth of a gram`() {
        assertEquals("8,4", normalizeGramsPerUnitValue("8,35"))
        assertEquals("10", normalizeGramsPerUnitValue("10.04"))
    }

    @Test
    fun `typed pump ratio rounding to zero or not parsing is rejected`() {
        assertEquals("", normalizeGramsPerUnitValue("0,04"))
        assertEquals("", normalizeGramsPerUnitValue("abc"))
    }

    private companion object {
        const val DELTA = 1e-9
        const val TENTHS_PER_GRAM = 10.0
        const val MIN_RATIO_TENTHS = 10
        const val MAX_RATIO_TENTHS = 1500
    }
}
