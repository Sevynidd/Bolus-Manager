package sevynidd.diabetesapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import sevynidd.diabetesapp.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Inter"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("Manrope"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()

// Material 3's stock sizes read as too large across this app's compact, field-dense screens, so
// every role is scaled down by the same factor to keep the type hierarchy's proportions intact.
private const val APP_TEXT_SCALE = 0.9f

private fun TextStyle.scaledDown(factor: Float = APP_TEXT_SCALE): TextStyle {
    return copy(fontSize = fontSize * factor, lineHeight = lineHeight * factor)
}

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily).scaledDown(),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily).scaledDown(),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily).scaledDown(),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily).scaledDown(),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily).scaledDown(),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily).scaledDown(),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily).scaledDown(),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily).scaledDown(),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily).scaledDown(),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily).scaledDown(),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily).scaledDown(),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily).scaledDown(),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily).scaledDown(),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily).scaledDown(),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily).scaledDown(),
)
