package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.database.BolusTemplateEntity
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
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
            if (templates.isNotEmpty()) {
                TemplateSortSelector(
                    sortOrder = sortOrder,
                    onSortOrderChange = { sortOrder = it },
                    currentLanguage = currentLanguage
                )
            }

            if (sortedTemplates.isEmpty()) {
                TemplateEmptyState(
                    currentLanguage = currentLanguage,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = FabClearanceHeight),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
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

        ExtendedFloatingActionButton(
            onClick = onAddTemplateRequested,
            icon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
            text = { Text(translate(TranslationKey.TemplateAdd, currentLanguage)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplateSortSelector(
    sortOrder: TemplateSortOrder,
    onSortOrderChange: (TemplateSortOrder) -> Unit,
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    val options = listOf(TemplateSortOrder.RecentlyUsed, TemplateSortOrder.Alphabetical)
    val groupLabel = translate(TranslationKey.TemplateSortTitle, currentLanguage)

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .weight(1f)
                .semantics { contentDescription = groupLabel }
        ) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    selected = option == sortOrder,
                    onClick = { onSortOrderChange(option) },
                    label = {
                        Text(
                            text = when (option) {
                                TemplateSortOrder.RecentlyUsed ->
                                    translate(TranslationKey.TemplateSortRecent, currentLanguage)
                                TemplateSortOrder.Alphabetical ->
                                    translate(TranslationKey.TemplateSortAlphabetical, currentLanguage)
                            }
                        )
                    }
                )
            }
        }
        HelpIconButton(helpTextKey = TranslationKey.TemplateSortHelp, currentLanguage = currentLanguage)
    }
}

@Composable
private fun TemplateEmptyState(
    currentLanguage: AppLanguage,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(EmptyStateBadgeSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(EmptyStateIconSize)
                )
            }
            Text(
                text = translate(TranslationKey.TemplateEmpty, currentLanguage),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
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
    ListItem(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onSelect),
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        leadingContent = { TemplateAvatar(emoji = template.emoji) },
        headlineContent = {
            Text(
                text = template.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = "${translate(TranslationKey.Carbohydrates, currentLanguage)}: " +
                    template.carbohydrates.toLocalizedInput(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            TemplateRowMenu(
                currentLanguage = currentLanguage,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
    )
}

@Composable
private fun TemplateRowMenu(
    currentLanguage: AppLanguage,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { isMenuExpanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = translate(TranslationKey.ActionMoreOptions, currentLanguage)
            )
        }

        DropdownMenu(
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(translate(TranslationKey.ActionEdit, currentLanguage)) },
                leadingIcon = { Icon(imageVector = Icons.Filled.Edit, contentDescription = null) },
                onClick = {
                    isMenuExpanded = false
                    onEdit()
                }
            )
            DropdownMenuItem(
                text = { Text(translate(TranslationKey.ActionDelete, currentLanguage)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                onClick = {
                    isMenuExpanded = false
                    onDelete()
                }
            )
        }
    }
}

@Composable
private fun TemplateAvatar(emoji: String?) {
    Box(
        modifier = Modifier
            .size(TemplateAvatarSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (emoji.isNullOrBlank()) {
            Icon(
                imageVector = Icons.Filled.RestaurantMenu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(TemplateAvatarIconSize)
            )
        } else {
            Text(
                text = emoji,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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

private val EmptyStateIconSize = 40.dp
private val EmptyStateBadgeSize = 88.dp
private val TemplateAvatarSize = 44.dp
private val TemplateAvatarIconSize = 20.dp
private val FabClearanceHeight = 96.dp
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

@Preview(showBackground = true)
@Composable
private fun TemplateManagerScreenEmptyPreview() {
    TemplateManagerScreen(
        currentLanguage = AppLanguage.System,
        templates = emptyList(),
        onTemplateSelected = {},
        onAddTemplateRequested = {},
        onEditTemplateRequested = {},
        onTemplateDeleteRequested = {}
    )
}
