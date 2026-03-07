package sevynidd.diabetesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.ui.theme.ContrastLevel
import sevynidd.diabetesapp.ui.theme.DiabetesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var themeMode by rememberSaveable { mutableStateOf(ThemeMode.System) }
            var contrastLevel by rememberSaveable { mutableStateOf(ContrastLevel.Normal) }

            val darkTheme = when (themeMode) {
                ThemeMode.System -> isSystemInDarkTheme()
                ThemeMode.Light -> false
                ThemeMode.Dark -> true
            }

            DiabetesAppTheme(
                darkTheme = darkTheme,
                dynamicColor = false, // Disable dynamic color to use contrast settings
                contrastLevel = contrastLevel
            ) {
                DiabetesAppApp(
                    themeMode = themeMode,
                    contrastLevel = contrastLevel,
                    onThemeModeChange = { themeMode = it },
                    onContrastLevelChange = { contrastLevel = it }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun DiabetesAppApp(
    themeMode: ThemeMode = ThemeMode.System,
    contrastLevel: ContrastLevel = ContrastLevel.Normal,
    onThemeModeChange: (ThemeMode) -> Unit = {},
    onContrastLevelChange: (ContrastLevel) -> Unit = {}
) {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.FACTORS) }
    var isEditMode by rememberSaveable { mutableStateOf(false) }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = it.label
                        )
                    },
                    label = { Text(it.label) },
                    selected = it == currentDestination,
                    onClick = {
                        currentDestination = it
                        isEditMode = false // Reset edit mode when changing screens
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(currentDestination.label) },
                    actions = {
                        // Show edit button only on Factors screen
                        if (currentDestination == AppDestinations.FACTORS) {
                            IconButton(onClick = { isEditMode = !isEditMode }) {
                                Icon(
                                    imageVector = if (isEditMode) Icons.Filled.Check else Icons.Filled.Edit,
                                    contentDescription = if (isEditMode) "Save" else "Edit"
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            val contentModifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)

            when (currentDestination) {
                AppDestinations.FACTORS -> FactorScreen(contentModifier, isEditMode)
                AppDestinations.CALCULATE -> CalculateScreen(contentModifier)
                AppDestinations.SETTINGS -> SettingsScreen(
                    modifier = contentModifier,
                    currentThemeMode = themeMode,
                    currentContrastLevel = contrastLevel,
                    onThemeModeChange = onThemeModeChange,
                    onContrastLevelChange = onContrastLevelChange
                )
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    FACTORS("Factors", Icons.Filled.Percent),
    CALCULATE("Calculate", Icons.Filled.Calculate),
    SETTINGS("Settings", Icons.Filled.Settings),
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiabetesAppTheme {
        FactorScreen()
    }
}