package sevynidd.diabetesapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.navigation.AppDestinations
import sevynidd.diabetesapp.navigation.destinationLabel

private val BarHeight = 72.dp
private val BarHorizontalMargin = 24.dp
private val BarBottomMargin = 16.dp
private val BarElevation = 6.dp
private val ItemIndicatorCornerRadius = 20.dp
private val ItemIndicatorPaddingHorizontal = 20.dp
private val ItemIndicatorPaddingVertical = 4.dp

/**
 * The system-bar/cutout insets [FloatingNavigationBar] keeps itself clear of: the navigation bar
 * (bottom in portrait, a side in landscape on some devices) unioned with the display cutout
 * (typically a top notch in portrait, but can land on a side edge in landscape), restricted to
 * the bottom and horizontal sides since this is a bottom-anchored bar that never needs top
 * clearance.
 */
@Composable
private fun floatingNavigationBarInsets(): WindowInsets {
    return WindowInsets.navigationBars
        .union(WindowInsets.displayCutout)
        .only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal)
}

/**
 * The bar's total vertical footprint — its own height and margin, plus however much the system
 * reserves below it right now (see [floatingNavigationBarInsets]) — that callers should apply as
 * bottom content padding. [FloatingNavigationBar] is rendered outside of `Scaffold` (in a plain
 * `Box`), so callers must also set `Scaffold`'s own `contentWindowInsets` to zero: otherwise both
 * this and `Scaffold`'s `innerPadding` would reserve the system inset, doubling the gap above
 * the bar.
 */
@Composable
fun floatingNavigationBarReservedHeight(): Dp {
    val bottomInset = floatingNavigationBarInsets().asPaddingValues().calculateBottomPadding()
    return BarHeight + BarBottomMargin + bottomInset
}

/**
 * The app's main tab switcher, styled as a floating rounded pill inset from the screen edges
 * (rather than a bar flush with the bottom edge) with a pill-shaped highlight behind the selected
 * icon. Used for compact (phone) window widths; wider windows fall back to Material3's standard
 * navigation rail/drawer instead, see [sevynidd.diabetesapp.screens.BolusManagerMainWindow].
 */
@Composable
fun FloatingNavigationBar(
    currentDestination: AppDestinations,
    currentLanguage: AppLanguage,
    onDestinationSelected: (AppDestinations) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .windowInsetsPadding(floatingNavigationBarInsets())
            .padding(start = BarHorizontalMargin, end = BarHorizontalMargin, bottom = BarBottomMargin)
            .fillMaxWidth()
            .height(BarHeight),
        shape = RoundedCornerShape(percent = 50),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = BarElevation,
        shadowElevation = BarElevation
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppDestinations.entries.forEach { destination ->
                FloatingNavigationBarItem(
                    destination = destination,
                    selected = destination == currentDestination,
                    label = destinationLabel(destination, currentLanguage),
                    onClick = { onDestinationSelected(destination) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FloatingNavigationBarItem(
    destination: AppDestinations,
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.onSecondaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val indicatorColor = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        Color.Transparent
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(ItemIndicatorCornerRadius))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(ItemIndicatorCornerRadius))
                .background(indicatorColor)
                .padding(horizontal = ItemIndicatorPaddingHorizontal, vertical = ItemIndicatorPaddingVertical)
        ) {
            Icon(imageVector = destination.icon, contentDescription = null, tint = contentColor)
        }
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = contentColor)
    }
}

@Preview(showBackground = true)
@Composable
private fun FloatingNavigationBarPreview() {
    FloatingNavigationBar(
        currentDestination = AppDestinations.CALCULATE,
        currentLanguage = AppLanguage.System,
        onDestinationSelected = {}
    )
}
