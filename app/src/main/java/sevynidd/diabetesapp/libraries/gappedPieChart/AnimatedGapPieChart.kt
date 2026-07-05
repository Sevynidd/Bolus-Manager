package sevynidd.diabetesapp.libraries.gappedPieChart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.min

private val ArcStrokeWidth = 26.dp
private val RingOuterPadding = 4.dp
private const val GAP_DEGREES = 14f
private const val FULL_CIRCLE_DEGREES = 360f
private const val TOP_START_ANGLE_DEGREES = -90f
private const val ANIMATION_DURATION_MILLIS = 650
private const val ANIMATION_STAGGER_MILLIS = 70

private data class ArcData(
    val animation: Animatable<Float, AnimationVector1D>,
    val targetSweepAngle: Float,
    val startAngle: Float,
    val color: Color
)

/**
 * A minimal animated "gapped" donut ring: each [pieDataPoints] entry becomes a rounded arc
 * segment sized proportionally to its [PieData.amount], separated by small gaps, animating in
 * with a staggered sweep. Purely visual — size it via [modifier] and layer any center content or
 * legend around it from the caller, since this component draws only the ring itself.
 */
@Composable
fun AnimatedGapPieChart(
    modifier: Modifier = Modifier,
    pieDataPoints: List<PieData>
) {
    val numberOfGaps = pieDataPoints.size
    val remainingDegrees = FULL_CIRCLE_DEGREES - (GAP_DEGREES * numberOfGaps)
    val total = pieDataPoints.fold(0f) { acc, pieData -> acc + pieData.amount }.div(remainingDegrees)

    val arcs = remember(pieDataPoints) {
        var currentSum = 0f
        pieDataPoints.mapIndexed { index, pieData ->
            val startAngle = currentSum + (index * GAP_DEGREES)
            currentSum += pieData.amount / total
            ArcData(
                targetSweepAngle = pieData.amount / total,
                animation = Animatable(0f),
                startAngle = TOP_START_ANGLE_DEGREES + startAngle,
                color = pieData.color
            )
        }
    }

    LaunchedEffect(arcs) {
        arcs.mapIndexed { index, arcData ->
            launch {
                arcData.animation.animateTo(
                    targetValue = arcData.targetSweepAngle,
                    animationSpec = tween(
                        durationMillis = ANIMATION_DURATION_MILLIS,
                        easing = FastOutSlowInEasing,
                        delayMillis = index * ANIMATION_STAGGER_MILLIS
                    )
                )
            }
        }
    }

    Canvas(modifier = modifier) {
        val stroke = Stroke(width = ArcStrokeWidth.toPx(), cap = StrokeCap.Round)
        val canvasRadius = min(size.width, size.height) / 2f
        val arcOuterRadius = canvasRadius - (stroke.width / 2f) - RingOuterPadding.toPx()
        val arcDiameter = arcOuterRadius * 2f
        val arcTopLeft = Offset(center.x - arcOuterRadius, center.y - arcOuterRadius)

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
        }
    }
}
