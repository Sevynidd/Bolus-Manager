package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.database.BolusTemplateEntity
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton

internal const val QUICK_TEMPLATE_COUNT = 5
internal const val CARBOHYDRATES_ADJUST_STEP_GRAMS = 10.0

/** The up to [limit] most recently used templates, ties broken alphabetically. */
internal fun quickTemplates(
    templates: List<BolusTemplateEntity>,
    limit: Int = QUICK_TEMPLATE_COUNT
): List<BolusTemplateEntity> = templates
    .sortedWith(
        compareByDescending<BolusTemplateEntity> { it.lastUsedAtEpochMillis }
            .thenBy(String.CASE_INSENSITIVE_ORDER) { it.name }
    )
    .take(limit)

/** The carbohydrate text [current] moved by [deltaGrams]; blank counts as 0 and the result is never negative. */
internal fun adjustCarbohydrates(current: String, deltaGrams: Double): String {
    val currentGrams = current.replace(',', '.').toDoubleOrNull() ?: 0.0
    return (currentGrams + deltaGrams).coerceAtLeast(0.0).toUiDecimalOrEmpty().ifBlank { "0" }
}

/** A chip label such as `🍝 Pasta 80`: the optional emoji, the name and the carb amount in grams. */
internal fun quickChipLabel(template: BolusTemplateEntity): String =
    listOfNotNull(
        template.emoji?.takeIf { it.isNotBlank() },
        template.name,
        template.carbohydrates.toUiDecimalOrEmpty()
    ).joinToString(" ")

/** What the quick-entry bar can do, bundled to keep its parameter list short. */
internal data class QuickEntryCallbacks(
    val onTemplateSelected: (BolusTemplateEntity) -> Unit = {},
    val onAdjustCarbohydrates: (deltaGrams: Double) -> Unit = {},
    val onAllTemplatesRequested: () -> Unit = {}
)

/**
 * A scrolling row of one-tap carb shortcuts: the most recent templates, a plus and a minus step
 * for the carbs typed so far, and a chip that opens the full template list.
 */
@Composable
internal fun QuickEntryBar(
    templates: List<BolusTemplateEntity>,
    callbacks: QuickEntryCallbacks,
    currentLanguage: AppLanguage
) {
    val gramsUnit = translate(TranslationKey.GramsUnit, currentLanguage)
    val stepText = CARBOHYDRATES_ADJUST_STEP_GRAMS.toUiDecimalOrEmpty()

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickTemplates(templates).forEach { template ->
                AssistChip(
                    onClick = { callbacks.onTemplateSelected(template) },
                    label = { Text(quickChipLabel(template)) }
                )
            }
            AssistChip(
                onClick = { callbacks.onAdjustCarbohydrates(CARBOHYDRATES_ADJUST_STEP_GRAMS) },
                label = { Text("+$stepText $gramsUnit") }
            )
            AssistChip(
                onClick = { callbacks.onAdjustCarbohydrates(-CARBOHYDRATES_ADJUST_STEP_GRAMS) },
                label = { Text("−$stepText $gramsUnit") }
            )
            AssistChip(
                onClick = callbacks.onAllTemplatesRequested,
                label = { Text(translate(TranslationKey.ActionTemplates, currentLanguage)) },
                leadingIcon = { Icon(imageVector = Icons.Filled.Bookmark, contentDescription = null) }
            )
        }
        HelpIconButton(helpTextKey = TranslationKey.QuickEntryHelp, currentLanguage = currentLanguage)
    }
}
