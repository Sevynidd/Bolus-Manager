package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.data.database.BolusTemplateEntity
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import java.util.Locale

enum class TemplateSortOrder {
    Alphabetical,
    RecentlyUsed
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TemplateManagerScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage,
    templates: List<BolusTemplateEntity>,
    applyToBothModes: Boolean,
    onApplyToBothModesChange: (Boolean) -> Unit,
    onTemplateSelected: (BolusTemplateEntity, Boolean) -> Unit,
    onTemplateAddRequested: suspend (name: String, emoji: String?, carbohydrates: Double) -> Boolean,
    onTemplateUpdateRequested: suspend (BolusTemplateEntity) -> Boolean,
    onTemplateDeleteRequested: (BolusTemplateEntity) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var sortOrder by remember { mutableStateOf(TemplateSortOrder.RecentlyUsed) }
    var templateBeingEdited by remember { mutableStateOf<BolusTemplateEntity?>(null) }
    var templateBeingDeleted by remember { mutableStateOf<BolusTemplateEntity?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }

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
            Text(
                text = translate(TranslationKey.TemplatesTitle, currentLanguage),
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = translate(TranslationKey.TemplateSortTitle, currentLanguage),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = sortOrder == TemplateSortOrder.RecentlyUsed,
                            onClick = { sortOrder = TemplateSortOrder.RecentlyUsed },
                            label = { Text(translate(TranslationKey.TemplateSortRecent, currentLanguage)) }
                        )

                        FilterChip(
                            selected = sortOrder == TemplateSortOrder.Alphabetical,
                            onClick = { sortOrder = TemplateSortOrder.Alphabetical },
                            label = { Text(translate(TranslationKey.TemplateSortAlphabetical, currentLanguage)) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilterChip(
                            selected = applyToBothModes,
                            onClick = { onApplyToBothModesChange(!applyToBothModes) },
                            label = {
                                Text(
                                    translate(
                                        TranslationKey.TemplateApplyToBothModes,
                                        currentLanguage
                                    )
                                )
                            }
                        )
                    }
                }
            }

            if (sortedTemplates.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
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
                            onSelect = { onTemplateSelected(template, applyToBothModes) },
                            onEdit = { templateBeingEdited = template },
                            onDelete = { templateBeingDeleted = template }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showCreateDialog = true },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = translate(TranslationKey.TemplateAdd, currentLanguage)
            )
        }
    }

    if (showCreateDialog) {
        TemplateEditorDialog(
            currentLanguage = currentLanguage,
            titleKey = TranslationKey.TemplateAdd,
            initialName = "",
            initialEmoji = null,
            initialCarbohydrates = "",
            templates = templates,
            onDismissRequest = { showCreateDialog = false },
            onConfirm = { name, emoji, carbohydrates ->
                coroutineScope.launch {
                    val didSave = onTemplateAddRequested(name, emoji, carbohydrates)
                    if (didSave) {
                        showCreateDialog = false
                    }
                }
            }
        )
    }

    val editedTemplate = templateBeingEdited
    if (editedTemplate != null) {
        TemplateEditorDialog(
            currentLanguage = currentLanguage,
            titleKey = TranslationKey.TemplateEdit,
            initialName = editedTemplate.name,
            initialEmoji = editedTemplate.emoji,
            initialCarbohydrates = editedTemplate.carbohydrates.toLocalizedInput(),
            templates = templates,
            editingId = editedTemplate.id,
            onDismissRequest = { templateBeingEdited = null },
            onConfirm = { name, emoji, carbohydrates ->
                coroutineScope.launch {
                    val didSave = onTemplateUpdateRequested(
                        editedTemplate.copy(
                            name = name,
                            emoji = emoji,
                            carbohydrates = carbohydrates
                        )
                    )
                    if (didSave) {
                        templateBeingEdited = null
                    }
                }
            }
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
        shape = RoundedCornerShape(12.dp),
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

@Composable
private fun TemplateEditorDialog(
    currentLanguage: AppLanguage,
    titleKey: TranslationKey,
    initialName: String,
    initialEmoji: String?,
    initialCarbohydrates: String,
    templates: List<BolusTemplateEntity>,
    editingId: Long? = null,
    onDismissRequest: () -> Unit,
    onConfirm: (name: String, emoji: String?, carbohydrates: Double) -> Unit
) {
    var name by remember(initialName) { mutableStateOf(initialName) }
    var selectedEmoji by remember(initialEmoji) { mutableStateOf(initialEmoji) }
    var carbohydrates by remember(initialCarbohydrates) { mutableStateOf(initialCarbohydrates) }

    val normalizedName = name.trim().lowercase()
    val hasDuplicateName = normalizedName.isNotBlank() && templates.any {
        it.id != editingId && it.nameNormalized == normalizedName
    }
    val carbohydratesValue = carbohydrates.replace(',', '.').toDoubleOrNull()
    val isValid = name.trim().isNotEmpty() && carbohydratesValue != null && !hasDuplicateName

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = translate(titleKey, currentLanguage)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(
                            text = translate(
                                TranslationKey.TemplateName,
                                currentLanguage
                            )
                        )
                    },
                    singleLine = true,
                    isError = hasDuplicateName,
                    supportingText = {
                        if (hasDuplicateName) {
                            Text(
                                translate(
                                    TranslationKey.TemplateDuplicateNameError,
                                    currentLanguage
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = translate(TranslationKey.TemplateEmoji, currentLanguage),
                    style = MaterialTheme.typography.labelMedium
                )

                EmojiPickerRow(
                    selectedEmoji = selectedEmoji,
                    onEmojiSelected = { selectedEmoji = it }
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                OutlinedTextField(
                    value = carbohydrates,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*[.,]?\\d*$"))) {
                            carbohydrates = newValue
                        }
                    },
                    label = {
                        Text(
                            text = translate(
                                TranslationKey.Carbohydrates,
                                currentLanguage
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    carbohydratesValue?.let { parsedCarbohydrates ->
                        if (isValid) {
                            onConfirm(name.trim(), selectedEmoji, parsedCarbohydrates)
                        }
                    }
                },
                enabled = isValid
            ) {
                Text(text = translate(TranslationKey.ActionSave, currentLanguage))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = translate(TranslationKey.ActionCancel, currentLanguage))
            }
        }
    )
}

@Composable
private fun EmojiPickerRow(
    selectedEmoji: String?,
    onEmojiSelected: (String?) -> Unit
) {
    val options = listOf("🍎", "🍌", "🍞", "🍝", "🍚", "🍕", "🍫", "🥛")

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedEmoji == null,
            onClick = { onEmojiSelected(null) },
            label = { Text("-") }
        )

        options.forEach { emoji ->
            FilterChip(
                selected = selectedEmoji == emoji,
                onClick = { onEmojiSelected(emoji) },
                label = { Text(emoji) }
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




