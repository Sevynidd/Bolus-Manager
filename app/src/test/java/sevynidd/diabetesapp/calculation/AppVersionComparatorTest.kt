package sevynidd.diabetesapp.calculation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppVersionComparatorTest {

    @Test
    fun `normal case recognizes a higher minor version as newer`() {
        assertTrue(isNewerVersion(currentVersion = "1.2.0", latestVersion = "1.3.0"))
    }

    @Test
    fun `normal case recognizes a lower minor version as not newer`() {
        assertFalse(isNewerVersion(currentVersion = "1.3.0", latestVersion = "1.2.9"))
    }

    @Test
    fun `normal case ignores a leading v prefix and a pre-release suffix on the tag`() {
        assertTrue(isNewerVersion(currentVersion = "1.0.0", latestVersion = "v1.1.0-beta.1"))
    }

    @Test
    fun `boundary of identical versions is not newer`() {
        assertFalse(isNewerVersion(currentVersion = "1.0", latestVersion = "1.0"))
    }

    @Test
    fun `boundary of a missing trailing zero segment is not newer`() {
        assertFalse(isNewerVersion(currentVersion = "1.0", latestVersion = "1.0.0"))
    }

    @Test
    fun `invalid non-numeric tag is treated as not newer instead of throwing`() {
        assertFalse(isNewerVersion(currentVersion = "1.0", latestVersion = "not-a-version"))
    }
}
