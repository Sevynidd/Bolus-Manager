package sevynidd.diabetesapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * The amber colors for cautions that are not errors, such as a dose above the user's maximum.
 * Material 3 has no role for them, and they stay amber whatever palette the theme uses.
 */
@Immutable
data class WarningColors(val container: Color, val onContainer: Color)

private const val AMBER_CONTAINER_LIGHT = 0xFFFFDEA6
private const val AMBER_ON_CONTAINER_LIGHT = 0xFF271900
private const val AMBER_CONTAINER_DARK = 0xFF5B4300
private const val AMBER_ON_CONTAINER_DARK = 0xFFFFDEA6

internal val warningColorsLight = WarningColors(
    container = Color(AMBER_CONTAINER_LIGHT),
    onContainer = Color(AMBER_ON_CONTAINER_LIGHT)
)

internal val warningColorsDark = WarningColors(
    container = Color(AMBER_CONTAINER_DARK),
    onContainer = Color(AMBER_ON_CONTAINER_DARK)
)

internal val LocalWarningColors = staticCompositionLocalOf { warningColorsLight }

/** The [WarningColors] of the current theme. */
val MaterialTheme.warningColors: WarningColors
    @Composable
    @ReadOnlyComposable
    get() = LocalWarningColors.current
