package sevynidd.diabetesapp.screens.factors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.libraries.gappedPieChart.AnimatedGapPieChart
import sevynidd.diabetesapp.libraries.gappedPieChart.PieData

@Composable
fun ScheduleFactorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            AnimatedGapPieChart(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(300.dp),
                pieDataPoints = listOf(
                    PieData(7, Color(0xFF9400D3), "1"),
                    PieData( 10, color = Color(0xFF42A1D5), "2"),
                    PieData( 12, color = Color(0xFF8D9311), "3"),
                    PieData( 18, color = Color(0xFFFF7F00), "4"),
                    PieData( 22, color = Color(0xFF47A4CF))
                )
            )
        }
    }
}