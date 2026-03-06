package sevynidd.diabetesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import sevynidd.diabetesapp.ui.theme.DiabetesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiabetesAppTheme {
                DiabetesAppApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun DiabetesAppApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.FACTORS) }

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
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val contentModifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)

            when (currentDestination) {
                AppDestinations.FACTORS -> FactorScreen(contentModifier)
                AppDestinations.FAVORITES -> FavoritesScreen(contentModifier)
                AppDestinations.SETTINGS -> SettingsScreen(contentModifier)
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    FACTORS("Factors", Icons.Filled.Calculate),
    FAVORITES("Favorites", Icons.Filled.Favorite),
    SETTINGS("Settings", Icons.Filled.Settings),
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiabetesAppTheme {
        FactorScreen()
    }
}