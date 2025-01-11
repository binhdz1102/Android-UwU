package com.binhdz.androiduwu

import android.graphics.Typeface
import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.abs
import kotlin.math.roundToInt

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import kotlin.math.max
import kotlin.math.roundToInt

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

// ------- Common Enums, Data Classes, and Defaults --------

enum class TooltipPosition {
    ABOVE,
    LEFT,
    NONE,
}

@Immutable
class OffsetSliderColors internal constructor(
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

object OffsetSliderDefaults {
    @Composable
    fun colors(
        thumbColor: Color = Color(0xFFFFFFFF),
        activeTrackColor: Color = Color(0xFF0037FF),
        inactiveTrackColor: Color = Color(0xFFA09C9C),
        limitationMarkColor: Color = Color(0xFF0037FF),
        disabledThumbColor: Color = Color(0xFFFFFFFF),
        disabledActiveTrackColor: Color = Color(0xFF0037FF).copy(alpha = 0.5f),
        disabledInactiveTrackColor: Color = Color(0xFFA09C9C).copy(alpha = 0.5f),
        disabledLimitationMarkColor: Color = Color(0xFF0037FF),
    ): OffsetSliderColors = OffsetSliderColors(
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

// ------- Common Drawing functions --------

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
    tooltipPosition: TooltipPosition,
    isDragging: Boolean,
    isTapping: Boolean,
    displayText: String,
    thumbWidthPx: Float,
    thumbHeightPx: Float,
    valuePosition: Float,
    center: Offset,
    tooltipTextSize: TextUnit = 28.sp,
    tooltipTextColor: Color = Color.Black,
    tooltipBackGroundColor: Color = Color(0xFFC8C8C8),
) {
    if (isDragging || isTapping) {
        drawIntoCanvas { canvas ->
            // Define content tooltip
            val textPaint = android.graphics.Paint().apply {
                isAntiAlias = true
                textSize = tooltipTextSize.toPx()
                color = tooltipTextColor.toArgb()
                typeface = Typeface.create(
                    Typeface.DEFAULT,
                    Typeface.BOLD
                )
                textAlign = android.graphics.Paint.Align.LEFT
            }
            val textWidth = textPaint.measureText(displayText)
            val textHeight = textPaint.descent() - textPaint.ascent()
            // Define shape tooltip
            val paddingHorizontal = 8.dp.toPx()
            val paddingVertical = 13.dp.toPx()
            val tooltipWidth = textWidth + paddingHorizontal * 2
            val tooltipHeight = textHeight + paddingVertical * 2

            val (tooltipX, tooltipY) = when (tooltipPosition) {
                TooltipPosition.LEFT -> {
                    val x = valuePosition - thumbWidthPx - tooltipWidth - 8.dp.toPx()
                    val y = center.y - tooltipHeight / 2
                    Pair(x, y)
                }

                TooltipPosition.ABOVE -> {
                    val x = valuePosition - tooltipWidth / 2
                    val y = center.y - thumbHeightPx / 2 - tooltipHeight - 16.dp.toPx()
                    Pair(x, y)
                }

                TooltipPosition.NONE -> {
                    Pair(Float.NaN, Float.NaN)
                }
            }

            if (tooltipPosition != TooltipPosition.NONE) {
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

                // Define arrow based on tooltipPosition
                val arrowWidth = 9.dp.toPx()
                val arrowSize = 14.dp.toPx()
                val arrowPath = android.graphics.Path().apply {
                    when (tooltipPosition) {
                        TooltipPosition.LEFT -> {
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

                        TooltipPosition.ABOVE -> {
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

                        TooltipPosition.NONE -> {}
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
                    displayText,
                    tooltipX + paddingHorizontal,
                    tooltipY + paddingVertical - textPaint.ascent(),
                    textPaint,
                )
            }
        }
    }
}

// ------- Utility functions for snapping and mapping --------

private fun snapValue(
    value: Float,
    start: Float,
    end: Float,
    steps: Int,
): Float {
    return if (steps > 0) {
        val stepSize = (end - start) / steps
        val stepIndex = ((value - start) / stepSize).roundToInt().coerceIn(0, steps)
        (start + stepIndex * stepSize).coerceIn(start, end)
    } else {
        value.coerceIn(start, end)
    }
}

private fun snapToNearestValue(value: Float, values: List<Float>): Float {
    return values.minByOrNull { abs(it - value) } ?: value
}

// ------- ContinuousOffsetSlider -------

@Composable
fun ContinuousOffsetSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = -1f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: OffsetSliderColors = OffsetSliderDefaults.colors(),
    tooltipPosition: TooltipPosition = TooltipPosition.NONE,
) {
    val minValue = valueRange.start
    val maxValue = valueRange.endInclusive
    require(minValue < 0f && maxValue > 0f) {
        "valueRange must contain 0 and must satisfy a<0<b. Current: $valueRange"
    }
    require(steps >= 0) { "steps ($steps) must be >= 0" }

    val clampedValue = value.coerceIn(minValue, maxValue)

    // UI related constants
    val density = LocalDensity.current
    val thumbSize = DpSize(width = 32.dp, height = 56.dp)
    val trackHeight: Dp = 16.dp
    val changedTrackHeight: Dp = 28.dp
    val limitationMarkSize: DpSize = DpSize(width = 4.dp, height = 40.dp)

    val thumbSizePx = with(density) { Size(thumbSize.width.toPx(), thumbSize.height.toPx()) }
    val trackHeightPx = with(density) { trackHeight.toPx() }
    val changedTrackHeightPx = with(density) { changedTrackHeight.toPx() }
    val limitationMarkSizePx =
        with(density) { Size(limitationMarkSize.width.toPx(), limitationMarkSize.height.toPx()) }

    var sliderWidth by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var isTapping by remember { mutableStateOf(false) }

    var strokeWidth by remember { mutableStateOf(trackHeightPx) }
    val animatedStrokeWidth by androidx.compose.animation.core.animateFloatAsState(
        targetValue = strokeWidth,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 200),
        label = "track animation"
    )

    val inactiveTrackColor: Color = colors.inactiveTrackColor(enabled)
    val activeTrackColor: Color = colors.activeTrackColor(enabled)
    val thumbColor: Color = colors.thumbColor(enabled)
    val limitationMarkColor: Color = colors.limitationMarkColor(enabled)

    val tooltipTextColor = Color.Black
    val tooltipTextSize = 28.sp
    val tooltipBackGroundColor = Color(0xFFC8C8C8)

    fun positionToValue(position: Float): Float {
        val startPosition = thumbSizePx.width
        val endPosition = sliderWidth - thumbSizePx.width
        val trackLength = endPosition - startPosition
        val fraction = ((position - startPosition) / trackLength).coerceIn(0f, 1f)
        val newValue = minValue + fraction * (maxValue - minValue)
        return snapValue(newValue, minValue, maxValue, steps)
    }

    Box(
        modifier = modifier
            .pointerInput(enabled) {
                if (enabled) {
                    detectTapGestures(
                        onTap = { offset ->
                            val snappedValue = positionToValue(offset.x)
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
                            } catch (e: java.util.concurrent.CancellationException) {
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
                            val snappedValue = positionToValue(change.position.x)
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

            val zeroFraction = (-minValue) / (maxValue - minValue)
            val zeroPosition = startPosition + zeroFraction * trackLength

            val valueFraction = (clampedValue - minValue) / (maxValue - minValue)
            val valuePosition = startPosition + valueFraction * trackLength

            // Vẽ inactive track
            drawLine(
                color = inactiveTrackColor,
                start = Offset(startPosition, center.y),
                end = Offset(endPosition, center.y),
                strokeWidth = animatedStrokeWidth,
                cap = StrokeCap.Round,
            )

            // Vẽ active track
            drawLine(
                color = activeTrackColor,
                start = Offset(zeroPosition, center.y),
                end = Offset(valuePosition, center.y),
                strokeWidth = animatedStrokeWidth,
            )

            // Vẽ limitation mark (0)
            drawLimitationMark(
                limitationMarkColor = limitationMarkColor,
                limitPosition = zeroPosition,
                markWidth = limitationMarkSizePx.width,
                markHeight = limitationMarkSizePx.height,
            )

            // Draw Thumb
            drawThumbWithShadow(
                thumbColor = thumbColor,
                valuePosition = valuePosition,
                centerY = center.y,
                thumbWidth = thumbSizePx.width,
                thumbHeight = thumbSizePx.height,
            )

            // Tooltip dùng roundToInt để hiển thị số nguyên gần nhất
            val tooltipValue = clampedValue.roundToInt().toString()
            drawTooltip(
                tooltipPosition = tooltipPosition,
                isDragging = isDragging,
                isTapping = isTapping,
                displayText = tooltipValue,
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

// ------- DiscreteOffsetSlider -------

@Composable
fun DiscreteOffsetSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    values: List<Float>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: OffsetSliderColors = OffsetSliderDefaults.colors(),
    tooltipPosition: TooltipPosition = TooltipPosition.NONE,
) {
    require(values.isNotEmpty()) { "values must not be empty" }
    require(values.any { it == 0f }) { "values list must contain 0" }

    val actualMin = values.minOrNull() ?: 0f
    val actualMax = values.maxOrNull() ?: 0f
    val clampedValue = snapToNearestValue(value.coerceIn(actualMin, actualMax), values)

    val density = LocalDensity.current
    val thumbSize = DpSize(width = 32.dp, height = 56.dp)
    val trackHeight: Dp = 16.dp
    val changedTrackHeight: Dp = 28.dp
    val limitationMarkSize: DpSize = DpSize(width = 4.dp, height = 40.dp)

    val thumbSizePx = with(density) { Size(thumbSize.width.toPx(), thumbSize.height.toPx()) }
    val trackHeightPx = with(density) { trackHeight.toPx() }
    val changedTrackHeightPx = with(density) { changedTrackHeight.toPx() }
    val limitationMarkSizePx =
        with(density) { Size(limitationMarkSize.width.toPx(), limitationMarkSize.height.toPx()) }

    var sliderWidth by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var isTapping by remember { mutableStateOf(false) }

    var strokeWidth by remember { mutableStateOf(trackHeightPx) }
    val animatedStrokeWidth by androidx.compose.animation.core.animateFloatAsState(
        targetValue = strokeWidth,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 200),
        label = "track animation"
    )

    val inactiveTrackColor: Color = colors.inactiveTrackColor(enabled)
    val activeTrackColor: Color = colors.activeTrackColor(enabled)
    val thumbColor: Color = colors.thumbColor(enabled)
    val limitationMarkColor: Color = colors.limitationMarkColor(enabled)

    val tooltipTextColor = Color.Black
    val tooltipTextSize = 28.sp
    val tooltipBackGroundColor = Color(0xFFC8C8C8)

    // Thay vì nội suy theo index, ta nội suy theo giá trị thực trong [actualMin, actualMax].
    // Khi người dùng chạm/kéo, ta tính ra value dựa trên fraction và snap vào danh sách.
    fun positionToValue(position: Float): Float {
        val startPosition = thumbSizePx.width
        val endPosition = sliderWidth - thumbSizePx.width
        val trackLength = endPosition - startPosition
        val fraction = ((position - startPosition) / trackLength).coerceIn(0f, 1f)

        // Nội suy giá trị thực từ fraction
        val interpolatedValue = actualMin + fraction * (actualMax - actualMin)

        // Snap vào giá trị trong danh sách
        return snapToNearestValue(interpolatedValue, values)
    }

    Box(
        modifier = modifier
            .pointerInput(enabled) {
                if (enabled) {
                    detectTapGestures(
                        onTap = { offset ->
                            val snappedValue = positionToValue(offset.x)
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
                            } catch (e: java.util.concurrent.CancellationException) {
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
                            val snappedValue = positionToValue(change.position.x)
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

            // Vị trí zero
            val zeroFraction = (0f - actualMin) / (actualMax - actualMin)
            val zeroPosition = startPosition + zeroFraction * trackLength

            // Vị trí value hiện tại
            val valueFraction = (clampedValue - actualMin) / (actualMax - actualMin)
            val valuePosition = startPosition + valueFraction * trackLength

            // Vẽ inactive track
            drawLine(
                color = inactiveTrackColor,
                start = Offset(startPosition, center.y),
                end = Offset(endPosition, center.y),
                strokeWidth = animatedStrokeWidth,
                cap = StrokeCap.Round,
            )

            // Vẽ active track
            drawLine(
                color = activeTrackColor,
                start = Offset(zeroPosition, center.y),
                end = Offset(valuePosition, center.y),
                strokeWidth = animatedStrokeWidth,
            )

            // Vẽ limitation mark (0)
            drawLimitationMark(
                limitationMarkColor = limitationMarkColor,
                limitPosition = zeroPosition,
                markWidth = limitationMarkSizePx.width,
                markHeight = limitationMarkSizePx.height,
            )

            // Draw Thumb
            drawThumbWithShadow(
                thumbColor = thumbColor,
                valuePosition = valuePosition,
                centerY = center.y,
                thumbWidth = thumbSizePx.width,
                thumbHeight = thumbSizePx.height,
            )

            // Tooltip hiển thị giá trị snap gần nhất
            val tooltipValue = clampedValue.toString()
            drawTooltip(
                tooltipPosition = tooltipPosition,
                isDragging = isDragging,
                isTapping = isTapping,
                displayText = tooltipValue,
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

// ------- Ví dụ sử dụng -------

@Composable
fun ContinuousOffsetSliderExample() {
    var currentValue by remember { mutableStateOf(0.0f) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Continuous Value: $currentValue")
        ContinuousOffsetSlider(
            modifier = Modifier
                .width(500.dp)
                .height(80.dp),
            value = currentValue,
            onValueChange = { currentValue = it },
            valueRange = -10f..10f,
//            steps = 10,
            tooltipPosition = TooltipPosition.ABOVE
        )
    }
}

@Composable
fun DiscreteOffsetSliderExample() {
    val discreteValues = listOf(-5f, -2f, 0f, 3f, 5f)
    var currentValue by remember { mutableStateOf(0f) }
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Discrete Value: $currentValue")
        DiscreteOffsetSlider(
            modifier = Modifier
                .width(500.dp)
                .height(80.dp),
            value = currentValue,
            onValueChange = { currentValue = it },
            values = discreteValues,
            tooltipPosition = TooltipPosition.ABOVE
        )
    }
}
