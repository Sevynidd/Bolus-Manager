package sevynidd.diabetesapp.screens.factors

import org.junit.Assert.assertEquals
import org.junit.Test
import sevynidd.diabetesapp.data.settings.factor.FactorDisplayMode
import sevynidd.diabetesapp.localization.TranslationKey

class FactorDisplayFormatTest {

    private val perBreadUnit = FactorDisplayFormat(FactorDisplayMode.UnitsPerBreadUnit, breadUnits = 12.0)
    private val pump = FactorDisplayFormat(FactorDisplayMode.GramsPerUnit, breadUnits = 12.0)

    @Test
    fun `per bread unit mode passes factor text through unchanged`() {
        assertEquals("1,5", perBreadUnit.toDisplayText("1,5"))
        assertEquals("1,5", perBreadUnit.toFactorText("1,5"))
        assertEquals("1,25", perBreadUnit.normalizeInput("1,1"))
        assertEquals(TranslationKey.LabelFactor, perBreadUnit.labelKey)
    }

    @Test
    fun `pump mode shows and stores factors as grams per unit`() {
        assertEquals("8", pump.toDisplayText("1,5"))
        assertEquals("1,2", pump.toFactorText("10"))
        assertEquals("8,4", pump.normalizeInput("8,35"))
        assertEquals(TranslationKey.LabelPumpRatio, pump.labelKey)
    }

    @Test
    fun `pump mode follows the configured bread unit value`() {
        val tenGramBreadUnits = pump.copy(breadUnits = 10.0)
        assertEquals("6,7", tenGramBreadUnits.toDisplayText("1,5"))
    }

    @Test
    fun `pump mode keeps an empty factor empty`() {
        assertEquals("", pump.toDisplayText(""))
        assertEquals("", pump.toFactorText(""))
    }
}
