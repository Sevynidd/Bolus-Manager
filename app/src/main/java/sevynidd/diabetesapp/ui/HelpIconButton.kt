package sevynidd.diabetesapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.tooling.preview.Preview
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

/**
 * A `?` icon that opens a dialog with [helpTextKey]'s localized copy, so an editable field or
 * function can expose in-app help without leaving the screen.
 */
@Composable
fun HelpIconButton(helpTextKey: TranslationKey, currentLanguage: AppLanguage = AppLanguage.System) {
    var isHelpVisible by remember { mutableStateOf(false) }

    IconButton(onClick = { isHelpVisible = true }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.HelpOutline,
            contentDescription = translate(TranslationKey.HelpIconContentDescription, currentLanguage),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    if (isHelpVisible) {
        AlertDialog(
            onDismissRequest = { isHelpVisible = false },
            confirmButton = {
                TextButton(onClick = { isHelpVisible = false }) {
                    Text(translate(TranslationKey.ActionClose, currentLanguage))
                }
            },
            text = { Text(translate(helpTextKey, currentLanguage)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HelpIconButtonPreview() {
    HelpIconButton(helpTextKey = TranslationKey.CorrectionThresholdHelp)
}
