package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.database.BolusTemplateEntity
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.util.Locale

enum class TemplateSortOrder {
    Alphabetical,
    RecentlyUsed
}

@Composable
fun TemplateManagerScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage,
    templates: List<BolusTemplateEntity>,
    onTemplateSelected: (template: BolusTemplateEntity) -> Unit,
    onAddTemplateRequested: () -> Unit,
    onEditTemplateRequested: (BolusTemplateEntity) -> Unit,
    onTemplateDeleteRequested: (BolusTemplateEntity) -> Unit
) {
    var sortOrder by remember { mutableStateOf(TemplateSortOrder.RecentlyUsed) }
    var templateBeingDeleted by remember { mutableStateOf<BolusTemplateEntity?>(null) }

    val sortedTemplates = remember(templates, sortOrder) {
        when (sortOrder) {
            TemplateSortOrder.Alphabetical -> templates.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name })
            TemplateSortOrder.RecentlyUsed -> templates.sortedWith(
                compareByDescending<BolusTemplateEntity> { it.lastUsedAtEpochMillis }
                    .thenBy(String.CASE_INSENSITIVE_ORDER) { it.name }
            )
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = translate(TranslationKey.TemplatesTitle, currentLanguage),
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(onClick = {
                    sortOrder = when (sortOrder) {
                        TemplateSortOrder.RecentlyUsed -> TemplateSortOrder.Alphabetical
                        TemplateSortOrder.Alphabetical -> TemplateSortOrder.RecentlyUsed
                    }
                }) {
                    Icon(
                        imageVector = when (sortOrder) {
                            TemplateSortOrder.RecentlyUsed -> Icons.Filled.AccessTime
                            TemplateSortOrder.Alphabetical -> Icons.Filled.SortByAlpha
                        },
                        contentDescription = translate(
                            TranslationKey.TemplateSortTitle,
                            currentLanguage
                        )
                    )
                }
            }

            if (sortedTemplates.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                    ) {
                        Text(
                            text = translate(TranslationKey.TemplateEmpty, currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sortedTemplates, key = { it.id }) { template ->
                        TemplateListRow(
                            template = template,
                            currentLanguage = currentLanguage,
                            onSelect = { onTemplateSelected(template) },
                            onEdit = { onEditTemplateRequested(template) },
                            onDelete = { templateBeingDeleted = template }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddTemplateRequested,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = translate(TranslationKey.TemplateAdd, currentLanguage)
            )
        }
    }

    val deletedTemplate = templateBeingDeleted
    if (deletedTemplate != null) {
        AlertDialog(
            onDismissRequest = { templateBeingDeleted = null },
            title = { Text(text = translate(TranslationKey.TemplateDelete, currentLanguage)) },
            text = { Text(text = deletedTemplate.displayLabel()) },
            confirmButton = {
                TextButton(
                    onClick = {
                        onTemplateDeleteRequested(deletedTemplate)
                        templateBeingDeleted = null
                    }
                ) {
                    Text(text = translate(TranslationKey.ActionDelete, currentLanguage))
                }
            },
            dismissButton = {
                TextButton(onClick = { templateBeingDeleted = null }) {
                    Text(text = translate(TranslationKey.ActionCancel, currentLanguage))
                }
            }
        )
    }
}

@Composable
private fun TemplateListRow(
    template: BolusTemplateEntity,
    currentLanguage: AppLanguage,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
            .padding(vertical = 2.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = template.displayLabel(),
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "${
                        translate(
                            TranslationKey.Carbohydrates,
                            currentLanguage
                        )
                    }: ${template.carbohydrates.toLocalizedInput()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = translate(TranslationKey.ActionEdit, currentLanguage)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = translate(TranslationKey.ActionDelete, currentLanguage)
                )
            }
        }
    }
}

private fun BolusTemplateEntity.displayLabel(): String {
    return if (emoji.isNullOrBlank()) name else "$emoji $name"
}

private fun Double.toLocalizedInput(): String {
    return String.format(Locale.ROOT, "%.2f", this)
        .replace('.', ',')
        .trimEnd('0')
        .trimEnd(',')
}

private const val PREVIEW_BREAD_ROLL_CARBOHYDRATES = 30.0
private const val PREVIEW_PIZZA_SLICE_CARBOHYDRATES = 45.0

@Preview(showBackground = true)
@Composable
private fun TemplateManagerScreenPreview() {
    TemplateManagerScreen(
        currentLanguage = AppLanguage.System,
        templates = listOf(
            BolusTemplateEntity(
                id = 1,
                name = "Bread roll",
                nameNormalized = "bread roll",
                emoji = "🍞",
                carbohydrates = PREVIEW_BREAD_ROLL_CARBOHYDRATES
            ),
            BolusTemplateEntity(
                id = 2,
                name = "Pizza slice",
                nameNormalized = "pizza slice",
                emoji = "🍕",
                carbohydrates = PREVIEW_PIZZA_SLICE_CARBOHYDRATES
            )
        ),
        onTemplateSelected = {},
        onAddTemplateRequested = {},
        onEditTemplateRequested = {},
        onTemplateDeleteRequested = {}
    )
}
