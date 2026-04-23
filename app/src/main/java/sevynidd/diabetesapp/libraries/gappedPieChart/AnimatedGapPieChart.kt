package sevynidd.diabetesapp.libraries.gappedPieChart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
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
import kotlinx.coroutines.launch
import sevynidd.diabetesapp.ui.theme.AppTypography
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private val LabelArcPadding = 32.dp
private val ChartTopPadding = 16.dp

data class ArcData(
    val animation: Animatable<Float, AnimationVector1D>,
    val targetSweepAngle: Float,
    val startAngle: Float,
    val color: Color,
    val title: String,
    val value: String
)

@Composable
fun AnimatedGapPieChart(
    modifier: Modifier = Modifier,
    pieDataPoints: List<PieData>
) {
    val gapDegrees = 16f
    val numberOfGaps = pieDataPoints.size
    val remainingDegrees = 360f - (gapDegrees * numberOfGaps)
    val containerWidth = LocalWindowInfo.current.containerSize.width
    val screenWidth = with(LocalDensity.current) { containerWidth.toDp() }
    val localModifier = modifier
        .padding(top = ChartTopPadding)
        .size(screenWidth)
    val total = pieDataPoints.fold(0f) { acc, pieData -> acc + pieData.amount }.div(remainingDegrees)
    var currentSum = 0f
    val textMeasurer = rememberTextMeasurer()
    val titleTextStyle = AppTypography.labelLarge.copy(color = MaterialTheme.colorScheme.onSurface)
    val valueTextStyle = AppTypography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurface)
    val valueLineTopPadding = 2.dp
    val arcs = pieDataPoints.mapIndexed { index, pieData ->
        val startAngle = currentSum + (index * gapDegrees)
        currentSum += pieData.amount / total
        ArcData(
            targetSweepAngle = (pieData.amount / total),
            animation = Animatable(0f),
            startAngle = -90 + startAngle,
            color = pieData.color,
            title = pieData.title,
            value = pieData.value
        )
    }

    LaunchedEffect(arcs) {
        arcs.mapIndexed { index, arcData ->
            launch {
                arcData.animation.animateTo(
                    targetValue = arcData.targetSweepAngle,
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing,
                        delayMillis = index * 200
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

        // Pre-measure every label so we can reserve space for the widest one.
        val labelLayouts = arcs.map { arcData ->
            textMeasurer.measure(text = arcData.title, style = titleTextStyle) to
                    textMeasurer.measure(text = arcData.value, style = valueTextStyle)
        }
        val maxLabelHalfWidth = labelLayouts.maxOfOrNull { (title, value) ->
            maxOf(title.size.width, value.size.width)
        }?.div(2f) ?: 0f

        // Shrink the arc so that labels (centred at labelRadius) never exceed canvasRadius.
        val arcOuterRadius = canvasRadius - (stroke.width / 2f) - LabelArcPadding.toPx() - maxLabelHalfWidth
        val arcDiameter = arcOuterRadius * 2f
        val arcTopLeft = Offset(center.x - arcOuterRadius, center.y - arcOuterRadius)
        val labelRadius = arcOuterRadius + (stroke.width / 2f) + LabelArcPadding.toPx()

        arcs.forEachIndexed { index, arcData ->
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

            val (titleLayout, valueLayout) = labelLayouts[index]

            val lineGapPx = valueLineTopPadding.toPx()
            val totalLabelHeight = titleLayout.size.height + lineGapPx + valueLayout.size.height
            val topY = labelPosition.y - (totalLabelHeight / 2f)

            val titleTopLeft = Offset(
                x = labelPosition.x - (titleLayout.size.width / 2f),
                y = topY
            )
            val valueTopLeft = Offset(
                x = labelPosition.x - (valueLayout.size.width / 2f),
                y = topY + titleLayout.size.height + lineGapPx
            )

            drawText(
                textLayoutResult = titleLayout,
                topLeft = titleTopLeft
            )
            drawText(
                textLayoutResult = valueLayout,
                topLeft = valueTopLeft
            )
        }
    }
}