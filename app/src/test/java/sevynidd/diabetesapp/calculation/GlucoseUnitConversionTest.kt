package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertEquals
import org.junit.Test

class GlucoseUnitConversionTest {

    @Test
    fun `normal case converts a known mmol per liter value to its mg per dl equivalent`() {
        val result = mmolLToMgDl(10.0)

        assertEquals(180.182, result, 0.0001)
    }

    @Test
    fun `normal case converts a known mg per dl value to its mmol per liter equivalent`() {
        val result = mgDlToMmolL(180.0)

        assertEquals(9.9899, result, 0.0001)
    }

    @Test
    fun `boundary of zero converts to zero in both directions`() {
        assertEquals(0.0, mmolLToMgDl(0.0), 0.0001)
        assertEquals(0.0, mgDlToMmolL(0.0), 0.0001)
    }

    @Test
    fun `edge case of a round trip conversion recovers the original value`() {
        val original = 154.0

        val roundTripped = mmolLToMgDl(mgDlToMmolL(original))

        assertEquals(original, roundTripped, 0.0001)
    }
}
