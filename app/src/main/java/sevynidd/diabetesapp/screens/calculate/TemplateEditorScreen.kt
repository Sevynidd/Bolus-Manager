package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.data.database.BolusTemplateEntity
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate
import sevynidd.diabetesapp.ui.HelpIconButton
import java.util.Locale

/**
 * Full-screen editor for creating or updating a [BolusTemplateEntity]. [template] being `null`
 * means "create a new template"; otherwise its fields seed the form and [onUpdateRequested] is
 * used instead of [onAddRequested] on save.
 */
@Composable
fun TemplateEditorScreen(
    modifier: Modifier = Modifier,
    currentLanguage: AppLanguage = AppLanguage.System,
    template: BolusTemplateEntity? = null,
    templates: List<BolusTemplateEntity> = emptyList(),
    onAddRequested: suspend (name: String, emoji: String?, carbohydrates: Double) -> Boolean = { _, _, _ -> false },
    onUpdateRequested: suspend (BolusTemplateEntity) -> Boolean = { false },
    onSaved: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    var name by rememberSaveable(template?.id) { mutableStateOf(template?.name.orEmpty()) }
    var emoji by rememberSaveable(template?.id) { mutableStateOf(template?.emoji.orEmpty()) }
    var carbohydrates by rememberSaveable(template?.id) {
        mutableStateOf(template?.carbohydrates?.toLocalizedTemplateInput().orEmpty())
    }

    val normalizedName = name.trim().lowercase()
    val hasDuplicateName = normalizedName.isNotBlank() && templates.any {
        it.id != template?.id && it.nameNormalized == normalizedName
    }
    val carbohydratesValue = carbohydrates.replace(',', '.').toDoubleOrNull()
    val isValid = name.trim().isNotEmpty() && carbohydratesValue != null && !hasDuplicateName

    fun save() {
        val parsedCarbohydrates = carbohydratesValue ?: return
        if (!isValid) return

        val trimmedEmoji = emoji.trim().takeUnless { it.isBlank() }
        coroutineScope.launch {
            val didSave = if (template != null) {
                onUpdateRequested(
                    template.copy(
                        name = name.trim(),
                        emoji = trimmedEmoji,
                        carbohydrates = parsedCarbohydrates
                    )
                )
            } else {
                onAddRequested(name.trim(), trimmedEmoji, parsedCarbohydrates)
            }
            if (didSave) onSaved()
        }
    }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EmojiPreview(emoji = emoji.ifBlank { null })

                Row(verticalAlignment = Alignment.CenterVertically) {
                    EmojiInputField(
                        value = emoji,
                        onValueChange = { emoji = it },
                        label = translate(TranslationKey.TemplateEmojiOptional, currentLanguage),
                        modifier = Modifier.weight(1f)
                    )
                    HelpIconButton(helpTextKey = TranslationKey.TemplateEmojiHelp, currentLanguage = currentLanguage)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(translate(TranslationKey.TemplateName, currentLanguage)) },
                        singleLine = true,
                        isError = hasDuplicateName,
                        supportingText = {
                            if (hasDuplicateName) {
                                Text(translate(TranslationKey.TemplateDuplicateNameError, currentLanguage))
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    HelpIconButton(helpTextKey = TranslationKey.TemplateNameHelp, currentLanguage = currentLanguage)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = carbohydrates,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.matches(CarbohydratesInputRegex)) {
                                carbohydrates = newValue
                            }
                        },
                        label = { Text(translate(TranslationKey.Carbohydrates, currentLanguage)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    HelpIconButton(helpTextKey = TranslationKey.CarbohydratesHelp, currentLanguage = currentLanguage)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text(translate(TranslationKey.ActionCancel, currentLanguage))
            }

            Button(
                onClick = { save() },
                enabled = isValid,
                modifier = Modifier.weight(1f)
            ) {
                Text(translate(TranslationKey.ActionSave, currentLanguage))
            }
        }
    }
}

@Composable
private fun EmojiPreview(emoji: String?) {
    Box(
        modifier = Modifier
            .size(EmojiPreviewSize)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        if (emoji != null) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.displayMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Filled.RestaurantMenu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * A short free-text field for entering a single emoji using the device's own keyboard/emoji
 * picker, rather than a fixed, curated set of choices.
 */
@Composable
private fun EmojiInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue -> onValueChange(newValue.take(EMOJI_MAX_LENGTH)) },
        label = { Text(label) },
        singleLine = true,
        textStyle = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Center),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = null)
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

private fun Double.toLocalizedTemplateInput(): String {
    return String.format(Locale.ROOT, "%.2f", this)
        .replace('.', ',')
        .trimEnd('0')
        .trimEnd(',')
}

private val CarbohydratesInputRegex = Regex("^\\d*[.,]?\\d*$")
private const val EMOJI_MAX_LENGTH = 8
private val EmojiPreviewSize = 88.dp
private const val PREVIEW_CARBOHYDRATES = 30.0

@Preview(showBackground = true)
@Composable
private fun TemplateEditorScreenAddPreview() {
    TemplateEditorScreen()
}

@Preview(showBackground = true)
@Composable
private fun TemplateEditorScreenEditPreview() {
    TemplateEditorScreen(
        template = BolusTemplateEntity(
            id = 1,
            name = "Bread roll",
            nameNormalized = "bread roll",
            emoji = "🍞",
            carbohydrates = PREVIEW_CARBOHYDRATES
        )
    )
}
