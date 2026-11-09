package sevynidd.diabetesapp.screens.calculate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.data.database.BolusTemplateEntity
import sevynidd.diabetesapp.localization.AppLanguage
import sevynidd.diabetesapp.localization.TranslationKey
import sevynidd.diabetesapp.localization.translate

/** The template list as a bottom sheet over the calculator, so picking a template never leaves the screen. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TemplateSheet(
    currentLanguage: AppLanguage,
    templates: List<BolusTemplateEntity>,
    callbacks: TemplateSheetCallbacks,
    onDismiss: () -> Unit
) {
    val listHeight = (LocalConfiguration.current.screenHeightDp * SHEET_LIST_HEIGHT_FRACTION).dp

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(modifier = Modifier.navigationBarsPadding().padding(horizontal = 16.dp)) {
            Text(
                text = translate(TranslationKey.TemplatesTitle, currentLanguage),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            TemplateManagerScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(listHeight),
                currentLanguage = currentLanguage,
                templates = templates,
                onTemplateSelected = callbacks.onSelected,
                onAddTemplateRequested = callbacks.onAddRequested,
                onEditTemplateRequested = callbacks.onEditRequested,
                onTemplateDeleteRequested = callbacks.onDeleteRequested
            )
        }
    }
}

private const val SHEET_LIST_HEIGHT_FRACTION = 0.5f
