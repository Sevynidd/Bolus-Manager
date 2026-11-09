package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InputPlausibilityTest {
    @Test
    fun `a value below the limit is plausible`() {
        assertFalse(exceedsPlausibleLimit(value = 180.0, limit = 600.0))
    }

    @Test
    fun `a value exactly at the limit is still plausible`() {
        assertFalse(exceedsPlausibleLimit(value = 600.0, limit = 600.0))
    }

    @Test
    fun `a value just above the limit is implausible`() {
        assertTrue(exceedsPlausibleLimit(value = 600.1, limit = 600.0))
    }

    @Test
    fun `a limit of zero switches the check off`() {
        assertFalse(exceedsPlausibleLimit(value = 5000.0, limit = 0.0))
    }

    @Test
    fun `a negative limit switches the check off`() {
        assertFalse(exceedsPlausibleLimit(value = 5000.0, limit = -1.0))
    }
}
