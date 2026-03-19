package sevynidd.diabetesapp.libraries.gappedPieChart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import sevynidd.diabetesapp.ui.theme.AppTypography
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

data class ArcData(
    val animation: Animatable<Float, AnimationVector1D>,
    val targetSweepAngle: Float,
    val startAngle: Float,
    val color: Color,
    val label: String
)

@Composable
fun AnimatedGapPieChart(
    modifier: Modifier = Modifier,
    pieDataPoints: List<PieData>
) {
    val gapDegrees = 16f
    val numberOfGaps = pieDataPoints.size
    val remainingDegrees = 360f - (gapDegrees * numberOfGaps)
    val localModifier = modifier.size(200.dp)
    val total = pieDataPoints.fold(0f) { acc, pieData -> acc + pieData.value }.div(remainingDegrees)
    var currentSum = 0f
    val textMeasurer = rememberTextMeasurer()
    val labelTextStyle = AppTypography.labelLarge.copy(color = MaterialTheme.colorScheme.onSurface)
    val arcs = pieDataPoints.mapIndexed { index, pieData ->
        val startAngle = currentSum + (index * gapDegrees)
        currentSum += pieData.value / total
        ArcData(
            targetSweepAngle = (pieData.value / total),
            animation = Animatable(0f),
            startAngle = -90 + startAngle,
            color = pieData.color,
            label = pieData.label
        )
    }

    LaunchedEffect(arcs) {
        arcs.mapIndexed { index, arcData ->
            launch {
                arcData.animation.animateTo(
                    targetValue = arcData.targetSweepAngle,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearEasing,
                        delayMillis = index * 300
                    )
                )
            }
        }
    }

    Canvas(
        modifier = localModifier.scale(1f)
    ) {
        val stroke = Stroke(width = 50f, cap = StrokeCap.Round)
        val canvasRadius = min(size.width, size.height) / 2f
        val arcOuterRadius = canvasRadius - 24.dp.toPx()
        val arcDiameter = arcOuterRadius * 2f
        val arcTopLeft = Offset(center.x - arcOuterRadius, center.y - arcOuterRadius)
        val labelRadius = arcOuterRadius + (stroke.width / 2f) + 16.dp.toPx()

        arcs.forEach { arcData ->
            drawArc(
                startAngle = arcData.startAngle,
                sweepAngle = arcData.animation.value,
                color = arcData.color,
                useCenter = false,
                topLeft = arcTopLeft,
                size = Size(arcDiameter, arcDiameter),
                style = stroke
            )

            val midAngleInDegrees = arcData.startAngle + (arcData.animation.value / 2f)
            val midAngleInRadians = Math.toRadians(midAngleInDegrees.toDouble())
            val labelPosition = Offset(
                x = center.x + (labelRadius * cos(midAngleInRadians).toFloat()),
                y = center.y + (labelRadius * sin(midAngleInRadians).toFloat())
            )

            val textLayout = textMeasurer.measure(
                text = arcData.label,
                style = labelTextStyle
            )
            val textTopLeft = Offset(
                x = labelPosition.x - (textLayout.size.width / 2f),
                y = labelPosition.y - (textLayout.size.height / 2f)
            )
            drawText(
                textLayoutResult = textLayout,
                topLeft = textTopLeft
            )
        }
    }
}