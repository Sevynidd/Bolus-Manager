package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Test

class BolusUnitsCalculatorTest {

    @Test
    fun `normal case computes units proportional to carbohydrates and factor`() {
        val units = calculateBolusUnits(carbohydrates = 60.0, breadUnits = 12.0, factor = 1.5)

        assertEquals(7.5, units, 0.0001)
    }

    @Test
    fun `boundary of zero carbohydrates yields zero units`() {
        val units = calculateBolusUnits(carbohydrates = 0.0, breadUnits = 12.0, factor = 1.5)

        assertEquals(0.0, units, 0.0001)
    }

    @Test
    fun `edge case of a zero factor yields zero units regardless of carbohydrates`() {
        val units = calculateBolusUnits(carbohydrates = 60.0, breadUnits = 12.0, factor = 0.0)

        assertEquals(0.0, units, 0.0001)
    }

    @Test
    fun `normal case splits carbohydrates and units between immediate and rest portions`() {
        val result = calculateSplitBolus(
            carbohydrates = 60.0,
            immediatePercent = 70,
            breadUnits = 12.0,
            immediateFactor = 1.0,
            restFactor = 1.2
        )

        assertEquals(42.0, result.immediateCarbohydrates, 0.0001)
        assertEquals(18.0, result.restCarbohydrates, 0.0001)
        assertEquals(3.5, result.immediateUnits, 0.0001)
        assertEquals(1.8, result.restUnits, 0.0001)
        assertEquals(5.3, result.totalUnits, 0.0001)
    }

    @Test
    fun `boundary of 0 percent immediate sends all carbohydrates to the rest portion`() {
        val result = calculateSplitBolus(
            carbohydrates = 60.0,
            immediatePercent = 0,
            breadUnits = 12.0,
            immediateFactor = 1.0,
            restFactor = 1.2
        )

        assertEquals(0.0, result.immediateCarbohydrates, 0.0001)
        assertEquals(0.0, result.immediateUnits, 0.0001)
        assertEquals(60.0, result.restCarbohydrates, 0.0001)
    }

    @Test
    fun `boundary of 100 percent immediate leaves nothing for the rest portion`() {
        val result = calculateSplitBolus(
            carbohydrates = 60.0,
            immediatePercent = 100,
            breadUnits = 12.0,
            immediateFactor = 1.0,
            restFactor = 1.2
        )

        assertEquals(0.0, result.restCarbohydrates, 0.0001)
        assertEquals(0.0, result.restUnits, 0.0001)
        assertEquals(result.immediateUnits, result.totalUnits, 0.0001)
    }
}
