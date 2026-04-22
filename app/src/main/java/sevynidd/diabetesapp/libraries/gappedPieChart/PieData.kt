package sevynidd.diabetesapp.libraries.gappedPieChart

import androidx.compose.ui.graphics.Color

data class PieData(
    val amount: Int,
    val color: Color,
    val title: String = "",
    val value: String = ""
)
