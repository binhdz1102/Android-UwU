package com.binhdz.androiduwu

import android.graphics.Typeface
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.roundToInt

@Composable
fun HLimitedSlider(
    modifier: Modifier = Modifier,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    limitStart: Float,
    limitEnd: Float? = null,
    enabled: Boolean = true,
    step: Int = 0,
    colors: LimitedSliderColors = LimitedSliderDefaults.colors(),
    thumbSize: DpSize = DpSize(width = 32.dp, height = 56.dp),
    trackHeight: Dp = 16.dp,
    changedTrackHeight: Dp = 28.dp,
    limitationMarkSize: DpSize = DpSize(width = 4.dp, height = 40.dp),
    limitedSliderTooltipPosition: LimitedSliderTooltipPosition = LimitedSliderTooltipPosition.LEFT,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val minValue = valueRange.start
    val maxValue = valueRange.endInclusive

    // Input validation
    require(minValue < limitStart) { "minValue ($minValue) must be < limitStart ($limitStart)." }
    if (limitEnd != null) {
        require(limitStart < limitEnd) { "limitStart ($limitStart) must be < limitEnd ($limitEnd)." }
        require(limitEnd < maxValue) { "limitEnd ($limitEnd) must be < maxValue ($maxValue)." }
        require(value in limitStart..limitEnd) { "Value ($value) must be within limitStart ($limitStart) and limitEnd ($limitEnd)." }
    } else {
        require(limitStart < maxValue) { "limitStart ($limitStart) must be < maxValue ($maxValue)." }
        require(value in limitStart..maxValue) { "Value ($value) must be within limitStart ($limitStart) and maxValue ($maxValue)." }
    }
    require(value in valueRange) { "Value ($value) must be within valueRange ($valueRange)." }
    require(step >= 0) { "Step ($step) must be >= 0." }

    val effectiveLimit2 = limitEnd ?: maxValue

    // Clamp the value to be within the limits
    val clampedValue = value.coerceIn(limitStart, effectiveLimit2)

    // Convert dimensions from Dp to Px
    val density = LocalDensity.current
    val thumbSizePx = with(density) { Size(thumbSize.width.toPx(), thumbSize.height.toPx()) }
    val trackHeightPx = with(density) { trackHeight.toPx() }
    val changedTrackHeightPx = with(density) { changedTrackHeight.toPx() }
    var strokeWidth by remember { mutableFloatStateOf(trackHeightPx) }
    val animatedStrokeWidth by animateFloatAsState(
        targetValue = strokeWidth,
        animationSpec = tween(durationMillis = 200),
        label = "track animation",
    )
    val limitationMarkSizePx =
        with(density) { Size(limitationMarkSize.width.toPx(), limitationMarkSize.height.toPx()) }

    // Slider's information
    var sliderWidth by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var isTapping by remember { mutableStateOf(false) }
    var valuePositionPx by remember { mutableFloatStateOf(0f) }

    val inactiveTrackColor: Color = colors.inactiveTrackColor(enabled)
    val activeTrackColor: Color = colors.activeTrackColor(enabled)
    val thumbColor: Color = colors.thumbColor(enabled)
    val limitationMarkColor: Color = colors.limitationMarkColor(enabled)

    val tooltipTextColor = Color.Black
    val tooltipTextSize = 28.sp
    val tooltipBackGroundColor = Color(0xFFC8C8C8)

    fun calculateSnappedValue(position: Float): Float {
        val startPosition = thumbSizePx.width
        val endPosition = sliderWidth - thumbSizePx.width
        val trackLength = endPosition - startPosition

        val limit1Fraction = (limitStart - minValue) / (maxValue - minValue)
        val limit1Position = startPosition + limit1Fraction * trackLength

        val limit2Fraction = (effectiveLimit2 - minValue) / (maxValue - minValue)
        val limit2Position = startPosition + limit2Fraction * trackLength

        val newValue = positionToValue(
            position = position,
            limitStart = limitStart,
            limitEnd = effectiveLimit2,
            limit1Position = limit1Position,
            limit2Position = limit2Position,
        )
        return snapValue(newValue, limitStart, effectiveLimit2, step)
    }

    Box(
        modifier = modifier
            .pointerInput(enabled) {
                if (enabled) {
                    detectTapGestures(
                        onTap = { offset ->
                            val snappedValue = calculateSnappedValue(offset.x)
                            onValueChange(snappedValue)
                            onValueChangeFinished?.invoke()
                        },
                        onPress = {
                            strokeWidth = changedTrackHeightPx
                            isTapping = true
                            try {
                                awaitRelease()
                                isTapping = false
                                strokeWidth = trackHeightPx
                            } catch (e: CancellationException) {
                                isTapping = false
                            }
                        },
                    )
                }
            }
            .pointerInput(enabled) {
                if (enabled) {
                    detectDragGestures(
                        onDragStart = {
                            strokeWidth = changedTrackHeightPx
                            isDragging = true
                        },
                        onDragEnd = {
                            isDragging = false
                            onValueChangeFinished?.invoke()
                            strokeWidth = trackHeightPx
                        },
                        onDrag = { change, _ ->
                            val snappedValue = calculateSnappedValue(change.position.x)
                            onValueChange(snappedValue)
                        },
                    )
                }
            },
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            sliderWidth = size.width
            val startPosition = thumbSizePx.width
            val endPosition = size.width - thumbSizePx.width
            val trackLength = endPosition - startPosition

            val limit1Fraction = (limitStart - minValue) / (maxValue - minValue)
            val limit1Position = startPosition + limit1Fraction * trackLength

            val limit2Fraction = (effectiveLimit2 - minValue) / (maxValue - minValue)
            val limit2Position = startPosition + limit2Fraction * trackLength

            val valueFraction = (clampedValue - minValue) / (maxValue - minValue)
            val valuePosition = startPosition + valueFraction * trackLength
            valuePositionPx = valuePosition

            // Draw inactive track
            drawLine(
                color = inactiveTrackColor,
                start = Offset(startPosition, center.y),
                end = Offset(endPosition, center.y),
                strokeWidth = animatedStrokeWidth,
                cap = StrokeCap.Round,
            )

            // Draw active track
            drawLine(
                color = activeTrackColor,
                start = Offset(limit1Position, center.y),
                end = Offset(valuePosition, center.y),
                strokeWidth = animatedStrokeWidth,
            )

            // Draw limitation marks
            drawLimitationMark(
                limitationMarkColor = limitationMarkColor,
                limitPosition = limit1Position,
                markWidth = limitationMarkSizePx.width,
                markHeight = limitationMarkSizePx.height,
            )
            if (limitEnd != null) {
                drawLimitationMark(
                    limitationMarkColor = limitationMarkColor,
                    limitPosition = limit2Position,
                    markWidth = limitationMarkSizePx.width,
                    markHeight = limitationMarkSizePx.height,
                )
            }

            // Draw thumb
            drawThumbWithShadow(
                thumbColor = thumbColor,
                valuePosition = valuePosition,
                centerY = center.y,
                thumbWidth = thumbSizePx.width,
                thumbHeight = thumbSizePx.height,
            )

            // Draw tooltip when dragging or tapping
            drawTooltip(
                limitedSliderTooltipPosition = limitedSliderTooltipPosition, // Pass tooltip position
                isDragging = isDragging,
                isTapping = isTapping,
                clampedValue = clampedValue,
                thumbWidthPx = thumbSizePx.width,
                thumbHeightPx = thumbSizePx.height,
                valuePosition = valuePosition,
                center = center,
                tooltipTextSize = tooltipTextSize,
                tooltipTextColor = tooltipTextColor,
                tooltipBackGroundColor = tooltipBackGroundColor,
            )
        }
    }
}

private fun DrawScope.drawLimitationMark(
    limitationMarkColor: Color,
    limitPosition: Float,
    markWidth: Float = 4.dp.toPx(),
    markHeight: Float = 40.dp.toPx(),
) {
    drawRect(
        color = limitationMarkColor,
        topLeft = Offset(
            x = limitPosition - markWidth / 2,
            y = (size.height - markHeight) / 2,
        ),
        size = Size(markWidth, markHeight),
    )
}

private fun DrawScope.drawThumbWithShadow(
    thumbColor: Color,
    shadowColor: Color = Color.Gray,
    shadowRadius: Float = 8.dp.toPx(),
    valuePosition: Float,
    centerY: Float,
    thumbWidth: Float = 32.dp.toPx(),
    thumbHeight: Float = 56.dp.toPx(),
) {
    // Draw shadow for thumb
    drawIntoCanvas { canvas ->
        val paint = Paint().apply {
            color = shadowColor
            asFrameworkPaint().apply {
                setShadowLayer(shadowRadius, 0f, 4.dp.toPx(), shadowColor.toArgb())
            }
        }

        canvas.drawRoundRect(
            left = valuePosition - thumbWidth / 2,
            top = centerY - thumbHeight / 2,
            right = valuePosition + thumbWidth / 2,
            bottom = centerY + thumbHeight / 2,
            radiusX = thumbWidth / 2,
            radiusY = thumbWidth / 2,
            paint = paint,
        )
    }

    // Draw thumb
    drawRoundRect(
        color = thumbColor,
        size = Size(thumbWidth, thumbHeight),
        topLeft = Offset(
            x = valuePosition - thumbWidth / 2,
            y = centerY - thumbHeight / 2,
        ),
        cornerRadius = CornerRadius(x = thumbWidth / 2, y = thumbWidth / 2),
    )
}

private fun DrawScope.drawTooltip(
    limitedSliderTooltipPosition: LimitedSliderTooltipPosition,
    isDragging: Boolean,
    isTapping: Boolean,
    clampedValue: Float,
    thumbWidthPx: Float,
    thumbHeightPx: Float,
    valuePosition: Float,
    center: Offset,
    tooltipTextSize: androidx.compose.ui.unit.TextUnit,
    tooltipTextColor: Color,
    tooltipBackGroundColor: Color,
) {
    if (isDragging || isTapping) {
        drawIntoCanvas { canvas ->
            // Define content tooltip
            val tooltipText = clampedValue.toInt().toString()
            val textPaint = android.graphics.Paint().apply {
                isAntiAlias = true
                textSize = tooltipTextSize.toPx()
                color = tooltipTextColor.toArgb()
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                textAlign = android.graphics.Paint.Align.LEFT
            }
            val textWidth = textPaint.measureText(tooltipText)
            val textHeight = textPaint.descent() - textPaint.ascent()
            // Define shape tooltip
            val paddingHorizontal = 8.dp.toPx()
            val paddingVertical = 13.dp.toPx()
            val tooltipWidth = textWidth + paddingHorizontal * 2
            val tooltipHeight = textHeight + paddingVertical * 2

            // Calculate tooltip position based on LimitedSliderTooltipPosition
            val (tooltipX, tooltipY) = when (limitedSliderTooltipPosition) {
                LimitedSliderTooltipPosition.LEFT -> {
                    val x =
                        valuePosition - thumbWidthPx - tooltipWidth - 8.dp.toPx() // Distance between tooltip and thumb
                    val y = center.y - tooltipHeight / 2
                    Pair(x, y)
                }

                LimitedSliderTooltipPosition.ABOVE -> {
                    val x = valuePosition - tooltipWidth / 2
                    val y =
                        center.y - thumbHeightPx / 2 - tooltipHeight - 16.dp.toPx() // Distance above the thumb
                    Pair(x, y)
                }

                LimitedSliderTooltipPosition.NONE -> {
                    // Do not display tooltip
                    Pair(Float.NaN, Float.NaN)
                }
            }

            if (limitedSliderTooltipPosition != LimitedSliderTooltipPosition.NONE) {
                val rect = android.graphics.RectF(
                    tooltipX,
                    tooltipY,
                    tooltipX + tooltipWidth,
                    tooltipY + tooltipHeight,
                )
                val backgroundPaint = android.graphics.Paint().apply {
                    color = tooltipBackGroundColor.toArgb()
                    isAntiAlias = true
                    setShadowLayer(4f, 0f, 1f, android.graphics.Color.DKGRAY)
                }

                // Define arrow based on LimitedSliderTooltipPosition
                val arrowWidth = 9.dp.toPx()
                val arrowSize = 14.dp.toPx()
                val arrowPath = android.graphics.Path().apply {
                    when (limitedSliderTooltipPosition) {
                        LimitedSliderTooltipPosition.LEFT -> {
                            // Arrow on the right when tooltip is on the left
                            moveTo(
                                tooltipX + tooltipWidth - 0.4f,
                                tooltipY + tooltipHeight / 2 - arrowSize / 2
                            )
                            lineTo(
                                tooltipX + tooltipWidth + arrowWidth,
                                tooltipY + tooltipHeight / 2
                            )
                            lineTo(
                                tooltipX + tooltipWidth - 0.4f,
                                tooltipY + tooltipHeight / 2 + arrowSize / 2
                            )
                        }

                        LimitedSliderTooltipPosition.ABOVE -> {
                            // Arrow at the bottom when tooltip is above the thumb
                            moveTo(
                                tooltipX + tooltipWidth / 2 - arrowSize / 2,
                                tooltipY + tooltipHeight
                            )
                            lineTo(
                                tooltipX + tooltipWidth / 2,
                                tooltipY + tooltipHeight + arrowWidth
                            )
                            lineTo(
                                tooltipX + tooltipWidth / 2 + arrowSize / 2,
                                tooltipY + tooltipHeight
                            )
                        }

                        LimitedSliderTooltipPosition.NONE -> {
                            // Do not draw arrow
                        }
                    }
                    close()
                }
                val arrowPaint = android.graphics.Paint().apply {
                    color = tooltipBackGroundColor.toArgb()
                    isAntiAlias = true
                    setShadowLayer(1f, 1f, 0.5f, android.graphics.Color.DKGRAY)
                }
                // Draw tooltip background
                canvas.nativeCanvas.drawRoundRect(
                    rect,
                    4.dp.toPx(),
                    4.dp.toPx(),
                    backgroundPaint,
                )
                // Draw arrow
                canvas.nativeCanvas.drawPath(arrowPath, arrowPaint)
                // Draw text
                canvas.nativeCanvas.drawText(
                    tooltipText,
                    tooltipX + paddingHorizontal,
                    tooltipY + paddingVertical - textPaint.ascent(),
                    textPaint,
                )
            }
        }
    }
}

// Helper function to map position to value
private fun positionToValue(
    position: Float,
    limitStart: Float,
    limitEnd: Float,
    limit1Position: Float,
    limit2Position: Float,
): Float {
    val fraction =
        ((position - limit1Position) / (limit2Position - limit1Position)).coerceIn(0f, 1f)
    return limitStart + fraction * (limitEnd - limitStart)
}

// Helper function to snap value to steps
private fun snapValue(
    value: Float,
    limitStart: Float,
    limitEnd: Float,
    step: Int,
): Float {
    return if (step > 0) {
        val stepSize = (limitEnd - limitStart) / step
        val stepIndex = ((value - limitStart) / stepSize).roundToInt().coerceIn(0, step)
        (limitStart + stepIndex * stepSize).coerceIn(limitStart, limitEnd)
    } else {
        value.coerceIn(limitStart, limitEnd)
    }
}

enum class LimitedSliderTooltipPosition {
    ABOVE,
    LEFT,
    NONE,
}

// Colors class for the LimitedSlider
@Immutable
class LimitedSliderColors internal constructor(
    private val thumbColor: Color,
    private val activeTrackColor: Color,
    private val inactiveTrackColor: Color,
    private val limitationMarkColor: Color,
    private val disabledThumbColor: Color,
    private val disabledActiveTrackColor: Color,
    private val disabledInactiveTrackColor: Color,
    private val disabledLimitationMarkColor: Color,
) {
    @Composable
    internal fun thumbColor(enabled: Boolean): Color {
        return if (enabled) thumbColor else disabledThumbColor
    }

    @Composable
    internal fun activeTrackColor(enabled: Boolean): Color {
        return if (enabled) activeTrackColor else disabledActiveTrackColor
    }

    @Composable
    internal fun inactiveTrackColor(enabled: Boolean): Color {
        return if (enabled) inactiveTrackColor else disabledInactiveTrackColor
    }

    @Composable
    internal fun limitationMarkColor(enabled: Boolean): Color {
        return if (enabled) limitationMarkColor else disabledLimitationMarkColor
    }
}

// Defaults for the LimitedSlider
private object LimitedSliderDefaults {
    @Composable
    fun colors(
        thumbColor: Color = Color(0xFFFFFFFF),
        activeTrackColor: Color = Color(0xFF0037FF),
        inactiveTrackColor: Color = Color(0xFFA09C9C),
        limitationMarkColor: Color = Color(0xFF0037FF),
        disabledThumbColor: Color =  Color(0xFFFFFFFF),
        disabledActiveTrackColor: Color = Color(0xFF0037FF).copy(alpha = 0.5f),
        disabledInactiveTrackColor: Color = Color(0xFFA09C9C).copy(alpha = 0.5f),
        disabledLimitationMarkColor: Color = Color(0xFF0037FF),
    ): LimitedSliderColors = LimitedSliderColors(
        thumbColor = thumbColor,
        activeTrackColor = activeTrackColor,
        inactiveTrackColor = inactiveTrackColor,
        limitationMarkColor = limitationMarkColor,
        disabledThumbColor = disabledThumbColor,
        disabledActiveTrackColor = disabledActiveTrackColor,
        disabledInactiveTrackColor = disabledInactiveTrackColor,
        disabledLimitationMarkColor = disabledLimitationMarkColor,
    )
}