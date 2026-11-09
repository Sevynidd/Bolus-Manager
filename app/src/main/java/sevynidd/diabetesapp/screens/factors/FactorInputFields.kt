package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

internal val DecimalInputRegex = Regex("^\\d*,?\\d*$")

@Composable
internal fun Modifier.activeFieldHighlight(isActive: Boolean): Modifier {
    if (!isActive) return this
    return clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(8.dp)
}

@Composable
internal fun FieldDescriptionRow(description: String, activeNowLabel: String?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = if (activeNowLabel != null) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.padding(bottom = 4.dp)
        )

        if (activeNowLabel != null) {
            Text(
                text = activeNowLabel,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(start = 8.dp, bottom = 4.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

/** The text of one [TappableValueRow]: an optional [title], a [description], and the current value. */
internal data class ValueRowContent(
    val title: String?,
    val description: String,
    val valueText: String,
    val activeNowLabel: String? = null
)

/** A read-only row showing one value that opens its editor when tapped. */
@Composable
internal fun TappableValueRow(content: ValueRowContent, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onClick)
            .activeFieldHighlight(content.activeNowLabel != null)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            content.title?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
            FieldDescriptionRow(description = content.description, activeNowLabel = content.activeNowLabel)
        }
        Text(text = content.valueText, style = MaterialTheme.typography.titleMedium)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
