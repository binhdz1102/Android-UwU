package com.binhdz.androiduwu.slidertemp//package com.binhdz.androiduwu
//
//import android.graphics.Typeface
//import androidx.annotation.FloatRange
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsDraggedAsState
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.LocalContentColor
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.layout.SubcomposeLayout
//import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Rect
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.*
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.*
//import kotlin.coroutines.cancellation.CancellationException
//import kotlin.math.abs
//import kotlin.math.roundToInt
//
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.CornerRadius
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.layout.Layout
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.unit.*
//import kotlin.math.max
//import kotlin.math.roundToInt
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Icon
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.*
//import androidx.compose.ui.unit.*
//import kotlin.math.roundToInt
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.gestures.detectDragGestures
//import androidx.compose.foundation.gestures.detectTapGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.Icon
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.*
//import androidx.compose.ui.unit.*
//import kotlin.math.roundToInt
//
////@Composable
////fun OffsetSlider(
////    value: Float,
////    onValueChange: (Float) -> Unit,
////    modifier: Modifier = Modifier,
////    enabled: Boolean = true,
////    valueRange: ClosedFloatingPointRange<Float> = -1f..1f,
////    steps: Int = 0,
////    onValueChangeFinished: (() -> Unit)? = null,
////    colors: OffsetSliderColors = OffsetSliderDefaults.colors(),
////    tooltipPosition: TooltipPosition = TooltipPosition.LEFT,
////) {
////    val minValue = valueRange.start
////    val maxValue = valueRange.endInclusive
////
////    // Input validation
////    require(minValue < 0f && maxValue > 0f) {
////        "valueRange must contain 0 and must satisfy a<0<b. Current: $valueRange"
////    }
////    require(steps >= 0) { "steps ($steps) must be >= 0" }
////
////    // Clamp the input value into the range
////    val clampedValue = value.coerceIn(minValue, maxValue)
////
////    // Chuyển đổi giữa dp và px
////    val density = LocalDensity.current
////    val thumbSize = DpSize(width = 32.dp, height = 56.dp)
////    val trackHeight: Dp = 16.dp
////    val changedTrackHeight: Dp = 28.dp
////    val limitationMarkSize: DpSize = DpSize(width = 4.dp, height = 40.dp)
////
////    val thumbSizePx = with(density) { Size(thumbSize.width.toPx(), thumbSize.height.toPx()) }
////    val trackHeightPx = with(density) { trackHeight.toPx() }
////    val changedTrackHeightPx = with(density) { changedTrackHeight.toPx() }
////    val limitationMarkSizePx =
////        with(density) { Size(limitationMarkSize.width.toPx(), limitationMarkSize.height.toPx()) }
////
////    var sliderWidth by remember { mutableFloatStateOf(0f) }
////    var isDragging by remember { mutableStateOf(false) }
////    var isTapping by remember { mutableStateOf(false) }
////    var valuePositionPx by remember { mutableFloatStateOf(0f) }
////
////    var strokeWidth by remember { mutableFloatStateOf(trackHeightPx) }
////    val animatedStrokeWidth by animateFloatAsState(
////        targetValue = strokeWidth,
////        animationSpec = tween(durationMillis = 200),
////        label = "track animation"
////    )
////
////    val inactiveTrackColor: Color = colors.inactiveTrackColor(enabled)
////    val activeTrackColor: Color = colors.activeTrackColor(enabled)
////    val thumbColor: Color = colors.thumbColor(enabled)
////    val limitationMarkColor: Color = colors.limitationMarkColor(enabled)
////
////    val tooltipTextColor = Color.Black
////    val tooltipTextSize = 28.sp
////    val tooltipBackGroundColor = Color(0xFFC8C8C8)
////
////    // Hàm chuyển từ tọa độ thành giá trị
////    fun positionToValue(position: Float): Float {
////        val startPosition = thumbSizePx.width
////        val endPosition = sliderWidth - thumbSizePx.width
////        val trackLength = endPosition - startPosition
////        val fraction = ((position - startPosition) / trackLength).coerceIn(0f, 1f)
////        val newValue = minValue + fraction * (maxValue - minValue)
////        return snapValue(newValue, minValue, maxValue, steps)
////    }
////
////    Box(
////        modifier = modifier
////            .pointerInput(enabled) {
////                if (enabled) {
////                    detectTapGestures(
////                        onTap = { offset ->
////                            val snappedValue = positionToValue(offset.x)
////                            onValueChange(snappedValue)
////                            onValueChangeFinished?.invoke()
////                        },
////                        onPress = {
////                            strokeWidth = changedTrackHeightPx
////                            isTapping = true
////                            try {
////                                awaitRelease()
////                                isTapping = false
////                                strokeWidth = trackHeightPx
////                            } catch (e: CancellationException) {
////                                isTapping = false
////                            }
////                        },
////                    )
////                }
////            }
////            .pointerInput(enabled) {
////                if (enabled) {
////                    detectDragGestures(
////                        onDragStart = {
////                            strokeWidth = changedTrackHeightPx
////                            isDragging = true
////                        },
////                        onDragEnd = {
////                            isDragging = false
////                            onValueChangeFinished?.invoke()
////                            strokeWidth = trackHeightPx
////                        },
////                        onDrag = { change, _ ->
////                            val snappedValue = positionToValue(change.position.x)
////                            onValueChange(snappedValue)
////                        },
////                    )
////                }
////            },
////    ) {
////        Canvas(modifier = Modifier.fillMaxSize()) {
////            sliderWidth = size.width
////            val startPosition = thumbSizePx.width
////            val endPosition = size.width - thumbSizePx.width
////            val trackLength = endPosition - startPosition
////
////            // Tính toán vị trí limitation mark tại giá trị 0
////            val zeroFraction = (-minValue) / (maxValue - minValue) // tỉ lệ của 0 trong range
////            val zeroPosition = startPosition + zeroFraction * trackLength
////
////            // Vị trí của giá trị hiện tại
////            val valueFraction = (clampedValue - minValue) / (maxValue - minValue)
////            val valuePosition = startPosition + valueFraction * trackLength
////            valuePositionPx = valuePosition
////
////            // Vẽ inactive track (toàn bộ range)
////            drawLine(
////                color = inactiveTrackColor,
////                start = Offset(startPosition, center.y),
////                end = Offset(endPosition, center.y),
////                strokeWidth = animatedStrokeWidth,
////                cap = StrokeCap.Round,
////            )
////
////            // Vẽ active track: từ zeroPosition tới valuePosition
////            drawLine(
////                color = activeTrackColor,
////                start = Offset(zeroPosition, center.y),
////                end = Offset(valuePosition, center.y),
////                strokeWidth = animatedStrokeWidth,
////            )
////
////            // Vẽ limitation mark (0)
////            drawLimitationMark(
////                limitationMarkColor = limitationMarkColor,
////                limitPosition = zeroPosition,
////                markWidth = limitationMarkSizePx.width,
////                markHeight = limitationMarkSizePx.height,
////            )
////
////            // Vẽ Thumb (có bóng)
////            drawThumbWithShadow(
////                thumbColor = thumbColor,
////                valuePosition = valuePosition,
////                centerY = center.y,
////                thumbWidth = thumbSizePx.width,
////                thumbHeight = thumbSizePx.height,
////            )
////
////            // Vẽ tooltip nếu đang kéo hoặc đang tap
////            drawTooltip(
////                tooltipPosition = tooltipPosition,
////                isDragging = isDragging,
////                isTapping = isTapping,
////                clampedValue = clampedValue,
////                thumbWidthPx = thumbSizePx.width,
////                thumbHeightPx = thumbSizePx.height,
////                valuePosition = valuePosition,
////                center = center,
////                tooltipTextSize = tooltipTextSize,
////                tooltipTextColor = tooltipTextColor,
////                tooltipBackGroundColor = tooltipBackGroundColor,
////            )
////        }
////    }
////}
////
////private fun DrawScope.drawLimitationMark(
////    limitationMarkColor: Color,
////    limitPosition: Float,
////    markWidth: Float = 4.dp.toPx(),
////    markHeight: Float = 40.dp.toPx(),
////) {
////    drawRect(
////        color = limitationMarkColor,
////        topLeft = Offset(
////            x = limitPosition - markWidth / 2,
////            y = (size.height - markHeight) / 2,
////        ),
////        size = Size(markWidth, markHeight),
////    )
////}
////
////private fun DrawScope.drawThumbWithShadow(
////    thumbColor: Color,
////    shadowColor: Color = Color.Gray,
////    shadowRadius: Float = 8.dp.toPx(),
////    valuePosition: Float,
////    centerY: Float,
////    thumbWidth: Float = 32.dp.toPx(),
////    thumbHeight: Float = 56.dp.toPx(),
////) {
////    // Draw shadow for thumb
////    drawIntoCanvas { canvas ->
////        val paint = Paint().apply {
////            color = shadowColor
////            asFrameworkPaint().apply {
////                setShadowLayer(shadowRadius, 0f, 4.dp.toPx(), shadowColor.toArgb())
////            }
////        }
////
////        canvas.drawRoundRect(
////            left = valuePosition - thumbWidth / 2,
////            top = centerY - thumbHeight / 2,
////            right = valuePosition + thumbWidth / 2,
////            bottom = centerY + thumbHeight / 2,
////            radiusX = thumbWidth / 2,
////            radiusY = thumbWidth / 2,
////            paint = paint,
////        )
////    }
////
////    // Draw thumb
////    drawRoundRect(
////        color = thumbColor,
////        size = Size(thumbWidth, thumbHeight),
////        topLeft = Offset(
////            x = valuePosition - thumbWidth / 2,
////            y = centerY - thumbHeight / 2,
////        ),
////        cornerRadius = CornerRadius(x = thumbWidth / 2, y = thumbWidth / 2),
////    )
////}
////
////private fun DrawScope.drawTooltip(
////    tooltipPosition: TooltipPosition,
////    isDragging: Boolean,
////    isTapping: Boolean,
////    clampedValue: Float,
////    thumbWidthPx: Float,
////    thumbHeightPx: Float,
////    valuePosition: Float,
////    center: Offset,
////    tooltipTextSize: androidx.compose.ui.unit.TextUnit,
////    tooltipTextColor: Color,
////    tooltipBackGroundColor: Color,
////) {
////    if (isDragging || isTapping) {
////        drawIntoCanvas { canvas ->
////            // Define content tooltip
////            val tooltipText = clampedValue.toInt().toString()
////            val textPaint = android.graphics.Paint().apply {
////                isAntiAlias = true
////                textSize = tooltipTextSize.toPx()
////                color = tooltipTextColor.toArgb()
////                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
////                textAlign = android.graphics.Paint.Align.LEFT
////            }
////            val textWidth = textPaint.measureText(tooltipText)
////            val textHeight = textPaint.descent() - textPaint.ascent()
////            // Define shape tooltip
////            val paddingHorizontal = 8.dp.toPx()
////            val paddingVertical = 13.dp.toPx()
////            val tooltipWidth = textWidth + paddingHorizontal * 2
////            val tooltipHeight = textHeight + paddingVertical * 2
////
////            // Calculate tooltip position based on tooltipPosition
////            val (tooltipX, tooltipY) = when (tooltipPosition) {
////                TooltipPosition.LEFT -> {
////                    val x =
////                        valuePosition - thumbWidthPx - tooltipWidth - 8.dp.toPx() // Distance between tooltip and thumb
////                    val y = center.y - tooltipHeight / 2
////                    Pair(x, y)
////                }
////
////                TooltipPosition.ABOVE -> {
////                    val x = valuePosition - tooltipWidth / 2
////                    val y =
////                        center.y - thumbHeightPx / 2 - tooltipHeight - 16.dp.toPx() // Distance above the thumb
////                    Pair(x, y)
////                }
////
////                TooltipPosition.NONE -> {
////                    // Do not display tooltip
////                    Pair(Float.NaN, Float.NaN)
////                }
////            }
////
////            if (tooltipPosition != TooltipPosition.NONE) {
////                val rect = android.graphics.RectF(
////                    tooltipX,
////                    tooltipY,
////                    tooltipX + tooltipWidth,
////                    tooltipY + tooltipHeight,
////                )
////                val backgroundPaint = android.graphics.Paint().apply {
////                    color = tooltipBackGroundColor.toArgb()
////                    isAntiAlias = true
////                    setShadowLayer(4f, 0f, 1f, android.graphics.Color.DKGRAY)
////                }
////
////                // Define arrow based on tooltipPosition
////                val arrowWidth = 9.dp.toPx()
////                val arrowSize = 14.dp.toPx()
////                val arrowPath = android.graphics.Path().apply {
////                    when (tooltipPosition) {
////                        TooltipPosition.LEFT -> {
////                            // Arrow on the right when tooltip is on the left
////                            moveTo(
////                                tooltipX + tooltipWidth - 0.4f,
////                                tooltipY + tooltipHeight / 2 - arrowSize / 2
////                            )
////                            lineTo(
////                                tooltipX + tooltipWidth + arrowWidth,
////                                tooltipY + tooltipHeight / 2
////                            )
////                            lineTo(
////                                tooltipX + tooltipWidth - 0.4f,
////                                tooltipY + tooltipHeight / 2 + arrowSize / 2
////                            )
////                        }
////
////                        TooltipPosition.ABOVE -> {
////                            // Arrow at the bottom when tooltip is above the thumb
////                            moveTo(
////                                tooltipX + tooltipWidth / 2 - arrowSize / 2,
////                                tooltipY + tooltipHeight
////                            )
////                            lineTo(
////                                tooltipX + tooltipWidth / 2,
////                                tooltipY + tooltipHeight + arrowWidth
////                            )
////                            lineTo(
////                                tooltipX + tooltipWidth / 2 + arrowSize / 2,
////                                tooltipY + tooltipHeight
////                            )
////                        }
////
////                        TooltipPosition.NONE -> {
////                            // Do not draw arrow
////                        }
////                    }
////                    close()
////                }
////                val arrowPaint = android.graphics.Paint().apply {
////                    color = tooltipBackGroundColor.toArgb()
////                    isAntiAlias = true
////                    setShadowLayer(1f, 1f, 0.5f, android.graphics.Color.DKGRAY)
////                }
////                // Draw tooltip background
////                canvas.nativeCanvas.drawRoundRect(
////                    rect,
////                    4.dp.toPx(),
////                    4.dp.toPx(),
////                    backgroundPaint,
////                )
////                // Draw arrow
////                canvas.nativeCanvas.drawPath(arrowPath, arrowPaint)
////                // Draw text
////                canvas.nativeCanvas.drawText(
////                    tooltipText,
////                    tooltipX + paddingHorizontal,
////                    tooltipY + paddingVertical - textPaint.ascent(),
////                    textPaint,
////                )
////            }
////        }
////    }
////}
////
////private fun snapValue(
////    value: Float,
////    start: Float,
////    end: Float,
////    steps: Int,
////): Float {
////    return if (steps > 0) {
////        // Tính step size
////        val stepSize = (end - start) / steps
////        val stepIndex = ((value - start) / stepSize).roundToInt().coerceIn(0, steps)
////        (start + stepIndex * stepSize).coerceIn(start, end)
////    } else {
////        value.coerceIn(start, end)
////    }
////}
////
////enum class TooltipPosition {
////    ABOVE,
////    LEFT,
////    NONE,
////}
////
////@Immutable
////class OffsetSliderColors internal constructor(
////    private val thumbColor: Color,
////    private val activeTrackColor: Color,
////    private val inactiveTrackColor: Color,
////    private val limitationMarkColor: Color,
////    private val disabledThumbColor: Color,
////    private val disabledActiveTrackColor: Color,
////    private val disabledInactiveTrackColor: Color,
////    private val disabledLimitationMarkColor: Color,
////) {
////    @Composable
////    internal fun thumbColor(enabled: Boolean): Color {
////        return if (enabled) thumbColor else disabledThumbColor
////    }
////
////    @Composable
////    internal fun activeTrackColor(enabled: Boolean): Color {
////        return if (enabled) activeTrackColor else disabledActiveTrackColor
////    }
////
////    @Composable
////    internal fun inactiveTrackColor(enabled: Boolean): Color {
////        return if (enabled) inactiveTrackColor else disabledInactiveTrackColor
////    }
////
////    @Composable
////    internal fun limitationMarkColor(enabled: Boolean): Color {
////        return if (enabled) limitationMarkColor else disabledLimitationMarkColor
////    }
////}
////
////// Defaults for the OffsetSlider
////object OffsetSliderDefaults {
////    @Composable
////    fun colors(
////        thumbColor: Color = Color(0xFFFFFFFF),
////        activeTrackColor: Color = Color(0xFF0037FF),
////        inactiveTrackColor: Color = Color(0xFFA09C9C),
////        limitationMarkColor: Color = Color(0xFF0037FF),
////        disabledThumbColor: Color =  Color(0xFFFFFFFF),
////        disabledActiveTrackColor: Color = Color(0xFF0037FF).copy(alpha = 0.5f),
////        disabledInactiveTrackColor: Color = Color(0xFFA09C9C).copy(alpha = 0.5f),
////        disabledLimitationMarkColor: Color = Color(0xFF0037FF),
////    ): OffsetSliderColors = OffsetSliderColors(
////        thumbColor = thumbColor,
////        activeTrackColor = activeTrackColor,
////        inactiveTrackColor = inactiveTrackColor,
////        limitationMarkColor = limitationMarkColor,
////        disabledThumbColor = disabledThumbColor,
////        disabledActiveTrackColor = disabledActiveTrackColor,
////        disabledInactiveTrackColor = disabledInactiveTrackColor,
////        disabledLimitationMarkColor = disabledLimitationMarkColor,
////    )
////}
//
//////////////////////////// logic mới, thanh slider có the hoat dong voi list cac values //////////////////////
//
//
//@Composable
//fun OffsetSlider(
//    value: Float,
//    onValueChange: (Float) -> Unit,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    valueRange: ClosedFloatingPointRange<Float> = -1f..1f,
//    steps: Int = 0,
//    onValueChangeFinished: (() -> Unit)? = null,
//    colors: OffsetSliderColors = OffsetSliderDefaults.colors(),
//    tooltipPosition: TooltipPosition = TooltipPosition.ABOVE,
//    values: List<Float>? = null, // Thêm tham số này
//) {
//    val minValue = valueRange.start
//    val maxValue = valueRange.endInclusive
//
//    // Kiểm tra tính hợp lệ: nếu không có values, thì range phải chứa 0
//    if (values == null || values.isEmpty()) {
//        require(minValue < 0f && maxValue > 0f) {
//            "valueRange must contain 0 and must satisfy a<0<b. Current: $valueRange"
//        }
//        require(steps >= 0) { "steps ($steps) must be >= 0" }
//    } else {
//        require(values.any { it == 0f }) {
//            "values list must contain 0"
//        }
//    }
//
//    // Nếu có values, xác định min/max từ values
//    val actualMin = if (values != null && values.isNotEmpty()) values.minOrNull() ?: 0f else minValue
//    val actualMax = if (values != null && values.isNotEmpty()) values.maxOrNull() ?: 0f else maxValue
//
//    // Clamp giá trị trong trường hợp không có values
//    val clampedValue = if (values == null || values.isEmpty()) {
//        value.coerceIn(actualMin, actualMax)
//    } else {
//        // Nếu có values, ta sẽ tìm trong values giá trị gần nhất cho giá trị hiện tại (nếu value không thuộc values)
//        val nearestValue = snapToNearestValue(value.coerceIn(actualMin, actualMax), values)
//        nearestValue
//    }
//
//    val density = LocalDensity.current
//    val thumbSize = DpSize(width = 32.dp, height = 56.dp)
//    val trackHeight: Dp = 16.dp
//    val changedTrackHeight: Dp = 28.dp
//    val limitationMarkSize: DpSize = DpSize(width = 4.dp, height = 40.dp)
//
//    val thumbSizePx = with(density) { Size(thumbSize.width.toPx(), thumbSize.height.toPx()) }
//    val trackHeightPx = with(density) { trackHeight.toPx() }
//    val changedTrackHeightPx = with(density) { changedTrackHeight.toPx() }
//    val limitationMarkSizePx =
//        with(density) { Size(limitationMarkSize.width.toPx(), limitationMarkSize.height.toPx()) }
//
//    var sliderWidth by remember { mutableFloatStateOf(0f) }
//    var isDragging by remember { mutableStateOf(false) }
//    var isTapping by remember { mutableStateOf(false) }
//    var valuePositionPx by remember { mutableFloatStateOf(0f) }
//
//    var strokeWidth by remember { mutableFloatStateOf(trackHeightPx) }
//    val animatedStrokeWidth by animateFloatAsState(
//        targetValue = strokeWidth,
//        animationSpec = tween(durationMillis = 200),
//        label = "track animation"
//    )
//
//    val inactiveTrackColor: Color = colors.inactiveTrackColor(enabled)
//    val activeTrackColor: Color = colors.activeTrackColor(enabled)
//    val thumbColor: Color = colors.thumbColor(enabled)
//    val limitationMarkColor: Color = colors.limitationMarkColor(enabled)
//
//    val tooltipTextColor = Color.Black
//    val tooltipTextSize = 28.sp
//    val tooltipBackGroundColor = Color(0xFFC8C8C8)
//
//    fun positionToValue(position: Float): Float {
//        val startPosition = thumbSizePx.width
//        val endPosition = sliderWidth - thumbSizePx.width
//        val trackLength = endPosition - startPosition
//        val fraction = ((position - startPosition) / trackLength).coerceIn(0f, 1f)
//
//        return if (values == null || values.isEmpty()) {
//            // Linear logic
//            val newValue = actualMin + fraction * (actualMax - actualMin)
//            snapValue(newValue, actualMin, actualMax, steps)
//        } else {
//            // Discrete logic dựa trên danh sách values
//            val index = (fraction * (values.size - 1)).roundToInt().coerceIn(0, values.size - 1)
//            values[index]
//        }
//    }
//
//    Box(
//        modifier = modifier
//            .pointerInput(enabled) {
//                if (enabled) {
//                    detectTapGestures(
//                        onTap = { offset ->
//                            val snappedValue = positionToValue(offset.x)
//                            onValueChange(snappedValue)
//                            onValueChangeFinished?.invoke()
//                        },
//                        onPress = {
//                            strokeWidth = changedTrackHeightPx
//                            isTapping = true
//                            try {
//                                awaitRelease()
//                                isTapping = false
//                                strokeWidth = trackHeightPx
//                            } catch (e: CancellationException) {
//                                isTapping = false
//                            }
//                        },
//                    )
//                }
//            }
//            .pointerInput(enabled) {
//                if (enabled) {
//                    detectDragGestures(
//                        onDragStart = {
//                            strokeWidth = changedTrackHeightPx
//                            isDragging = true
//                        },
//                        onDragEnd = {
//                            isDragging = false
//                            onValueChangeFinished?.invoke()
//                            strokeWidth = trackHeightPx
//                        },
//                        onDrag = { change, _ ->
//                            val snappedValue = positionToValue(change.position.x)
//                            onValueChange(snappedValue)
//                        },
//                    )
//                }
//            },
//    ) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            sliderWidth = size.width
//            val startPosition = thumbSizePx.width
//            val endPosition = size.width - thumbSizePx.width
//            val trackLength = endPosition - startPosition
//
//            // Tính zeroPosition: giá trị 0 trong range
//            // Nếu có values, zeroFraction dựa trên vị trí của 0 trong values.
//            val zeroFraction = if (values == null || values.isEmpty()) {
//                (-actualMin) / (actualMax - actualMin)
//            } else {
//                val zeroIndex = values.indexOf(0f).coerceAtLeast(0)
//                if (values.size > 1) zeroIndex.toFloat() / (values.size - 1) else 0f
//            }
//            val zeroPosition = startPosition + zeroFraction * trackLength
//
//            // Vị trí value hiện tại
//            val valueFraction = if (values == null || values.isEmpty()) {
//                (clampedValue - actualMin) / (actualMax - actualMin)
//            } else {
//                val idx = values.indexOf(clampedValue).coerceAtLeast(0)
//                if (values.size > 1) idx.toFloat() / (values.size - 1) else 0f
//            }
//            val valuePosition = startPosition + valueFraction * trackLength
//            valuePositionPx = valuePosition
//
//            // Vẽ inactive track
//            drawLine(
//                color = inactiveTrackColor,
//                start = Offset(startPosition, center.y),
//                end = Offset(endPosition, center.y),
//                strokeWidth = animatedStrokeWidth,
//                cap = StrokeCap.Round,
//            )
//
//            // Vẽ active track
//            drawLine(
//                color = activeTrackColor,
//                start = Offset(zeroPosition, center.y),
//                end = Offset(valuePosition, center.y),
//                strokeWidth = animatedStrokeWidth,
//            )
//
//            // Vẽ limitation mark (0)
//            drawLimitationMark(
//                limitationMarkColor = limitationMarkColor,
//                limitPosition = zeroPosition,
//                markWidth = limitationMarkSizePx.width,
//                markHeight = limitationMarkSizePx.height,
//            )
//
//            // Vẽ Thumb
//            drawThumbWithShadow(
//                thumbColor = thumbColor,
//                valuePosition = valuePosition,
//                centerY = center.y,
//                thumbWidth = thumbSizePx.width,
//                thumbHeight = thumbSizePx.height,
//            )
//
//            // Vẽ tooltip nếu đang kéo hoặc đang tap
//            drawTooltip(
//                tooltipPosition = tooltipPosition,
//                isDragging = isDragging,
//                isTapping = isTapping,
//                clampedValue = clampedValue,
//                thumbWidthPx = thumbSizePx.width,
//                thumbHeightPx = thumbSizePx.height,
//                valuePosition = valuePosition,
//                center = center,
//                tooltipTextSize = tooltipTextSize,
//                tooltipTextColor = tooltipTextColor,
//                tooltipBackGroundColor = tooltipBackGroundColor,
//            )
//        }
//    }
//}
//
//private fun snapValue(
//    value: Float,
//    start: Float,
//    end: Float,
//    steps: Int,
//): Float {
//    return if (steps > 0) {
//        val stepSize = (end - start) / steps
//        val stepIndex = ((value - start) / stepSize).roundToInt().coerceIn(0, steps)
//        (start + stepIndex * stepSize).coerceIn(start, end)
//    } else {
//        value.coerceIn(start, end)
//    }
//}
//
//private fun snapToNearestValue(value: Float, values: List<Float>): Float {
//    return values.minByOrNull { abs(it - value) } ?: value
//}
//
//private fun DrawScope.drawLimitationMark(
//    limitationMarkColor: Color,
//    limitPosition: Float,
//    markWidth: Float = 4.dp.toPx(),
//    markHeight: Float = 40.dp.toPx(),
//) {
//    drawRect(
//        color = limitationMarkColor,
//        topLeft = Offset(
//            x = limitPosition - markWidth / 2,
//            y = (size.height - markHeight) / 2,
//        ),
//        size = Size(markWidth, markHeight),
//    )
//}
//
//private fun DrawScope.drawThumbWithShadow(
//    thumbColor: Color,
//    shadowColor: Color = Color.Gray,
//    shadowRadius: Float = 8.dp.toPx(),
//    valuePosition: Float,
//    centerY: Float,
//    thumbWidth: Float = 32.dp.toPx(),
//    thumbHeight: Float = 56.dp.toPx(),
//) {
//    // Draw shadow for thumb
//    drawIntoCanvas { canvas ->
//        val paint = Paint().apply {
//            color = shadowColor
//            asFrameworkPaint().apply {
//                setShadowLayer(shadowRadius, 0f, 4.dp.toPx(), shadowColor.toArgb())
//            }
//        }
//
//        canvas.drawRoundRect(
//            left = valuePosition - thumbWidth / 2,
//            top = centerY - thumbHeight / 2,
//            right = valuePosition + thumbWidth / 2,
//            bottom = centerY + thumbHeight / 2,
//            radiusX = thumbWidth / 2,
//            radiusY = thumbWidth / 2,
//            paint = paint,
//        )
//    }
//
//    // Draw thumb
//    drawRoundRect(
//        color = thumbColor,
//        size = Size(thumbWidth, thumbHeight),
//        topLeft = Offset(
//            x = valuePosition - thumbWidth / 2,
//            y = centerY - thumbHeight / 2,
//        ),
//        cornerRadius = CornerRadius(x = thumbWidth / 2, y = thumbWidth / 2),
//    )
//}
//
//private fun DrawScope.drawTooltip(
//    tooltipPosition: TooltipPosition,
//    isDragging: Boolean,
//    isTapping: Boolean,
//    clampedValue: Float,
//    thumbWidthPx: Float,
//    thumbHeightPx: Float,
//    valuePosition: Float,
//    center: Offset,
//    tooltipTextSize: TextUnit,
//    tooltipTextColor: Color,
//    tooltipBackGroundColor: Color,
//) {
//    if (isDragging || isTapping) {
//        drawIntoCanvas { canvas ->
//            // Define content tooltip
//            val tooltipText = clampedValue.toInt().toString()
//            val textPaint = android.graphics.Paint().apply {
//                isAntiAlias = true
//                textSize = tooltipTextSize.toPx()
//                color = tooltipTextColor.toArgb()
//                typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
//                textAlign = android.graphics.Paint.Align.LEFT
//            }
//            val textWidth = textPaint.measureText(tooltipText)
//            val textHeight = textPaint.descent() - textPaint.ascent()
//            // Define shape tooltip
//            val paddingHorizontal = 8.dp.toPx()
//            val paddingVertical = 13.dp.toPx()
//            val tooltipWidth = textWidth + paddingHorizontal * 2
//            val tooltipHeight = textHeight + paddingVertical * 2
//
//            // Calculate tooltip position based on tooltipPosition
//            val (tooltipX, tooltipY) = when (tooltipPosition) {
//                TooltipPosition.LEFT -> {
//                    val x =
//                        valuePosition - thumbWidthPx - tooltipWidth - 8.dp.toPx() // Distance between tooltip and thumb
//                    val y = center.y - tooltipHeight / 2
//                    Pair(x, y)
//                }
//
//                TooltipPosition.ABOVE -> {
//                    val x = valuePosition - tooltipWidth / 2
//                    val y =
//                        center.y - thumbHeightPx / 2 - tooltipHeight - 16.dp.toPx() // Distance above the thumb
//                    Pair(x, y)
//                }
//
//                TooltipPosition.NONE -> {
//                    // Do not display tooltip
//                    Pair(Float.NaN, Float.NaN)
//                }
//            }
//
//            if (tooltipPosition != TooltipPosition.NONE) {
//                val rect = android.graphics.RectF(
//                    tooltipX,
//                    tooltipY,
//                    tooltipX + tooltipWidth,
//                    tooltipY + tooltipHeight,
//                )
//                val backgroundPaint = android.graphics.Paint().apply {
//                    color = tooltipBackGroundColor.toArgb()
//                    isAntiAlias = true
//                    setShadowLayer(4f, 0f, 1f, android.graphics.Color.DKGRAY)
//                }
//
//                // Define arrow based on tooltipPosition
//                val arrowWidth = 9.dp.toPx()
//                val arrowSize = 14.dp.toPx()
//                val arrowPath = android.graphics.Path().apply {
//                    when (tooltipPosition) {
//                        TooltipPosition.LEFT -> {
//                            // Arrow on the right when tooltip is on the left
//                            moveTo(
//                                tooltipX + tooltipWidth - 0.4f,
//                                tooltipY + tooltipHeight / 2 - arrowSize / 2
//                            )
//                            lineTo(
//                                tooltipX + tooltipWidth + arrowWidth,
//                                tooltipY + tooltipHeight / 2
//                            )
//                            lineTo(
//                                tooltipX + tooltipWidth - 0.4f,
//                                tooltipY + tooltipHeight / 2 + arrowSize / 2
//                            )
//                        }
//
//                        TooltipPosition.ABOVE -> {
//                            // Arrow at the bottom when tooltip is above the thumb
//                            moveTo(
//                                tooltipX + tooltipWidth / 2 - arrowSize / 2,
//                                tooltipY + tooltipHeight
//                            )
//                            lineTo(
//                                tooltipX + tooltipWidth / 2,
//                                tooltipY + tooltipHeight + arrowWidth
//                            )
//                            lineTo(
//                                tooltipX + tooltipWidth / 2 + arrowSize / 2,
//                                tooltipY + tooltipHeight
//                            )
//                        }
//
//                        TooltipPosition.NONE -> {
//                            // Do not draw arrow
//                        }
//                    }
//                    close()
//                }
//                val arrowPaint = android.graphics.Paint().apply {
//                    color = tooltipBackGroundColor.toArgb()
//                    isAntiAlias = true
//                    setShadowLayer(1f, 1f, 0.5f, android.graphics.Color.DKGRAY)
//                }
//                // Draw tooltip background
//                canvas.nativeCanvas.drawRoundRect(
//                    rect,
//                    4.dp.toPx(),
//                    4.dp.toPx(),
//                    backgroundPaint,
//                )
//                // Draw arrow
//                canvas.nativeCanvas.drawPath(arrowPath, arrowPaint)
//                // Draw text
//                canvas.nativeCanvas.drawText(
//                    tooltipText,
//                    tooltipX + paddingHorizontal,
//                    tooltipY + paddingVertical - textPaint.ascent(),
//                    textPaint,
//                )
//            }
//        }
//    }
//}
//
//enum class TooltipPosition {
//    ABOVE,
//    LEFT,
//    NONE,
//}
//
//@Immutable
//class OffsetSliderColors internal constructor(
//    private val thumbColor: Color,
//    private val activeTrackColor: Color,
//    private val inactiveTrackColor: Color,
//    private val limitationMarkColor: Color,
//    private val disabledThumbColor: Color,
//    private val disabledActiveTrackColor: Color,
//    private val disabledInactiveTrackColor: Color,
//    private val disabledLimitationMarkColor: Color,
//) {
//    @Composable
//    internal fun thumbColor(enabled: Boolean): Color {
//        return if (enabled) thumbColor else disabledThumbColor
//    }
//
//    @Composable
//    internal fun activeTrackColor(enabled: Boolean): Color {
//        return if (enabled) activeTrackColor else disabledActiveTrackColor
//    }
//
//    @Composable
//    internal fun inactiveTrackColor(enabled: Boolean): Color {
//        return if (enabled) inactiveTrackColor else disabledInactiveTrackColor
//    }
//
//    @Composable
//    internal fun limitationMarkColor(enabled: Boolean): Color {
//        return if (enabled) limitationMarkColor else disabledLimitationMarkColor
//    }
//}
//
//object OffsetSliderDefaults {
//    @Composable
//    fun colors(
//        thumbColor: Color = Color(0xFFFFFFFF),
//        activeTrackColor: Color = Color(0xFF0037FF),
//        inactiveTrackColor: Color = Color(0xFFA09C9C),
//        limitationMarkColor: Color = Color(0xFF0037FF),
//        disabledThumbColor: Color =  Color(0xFFFFFFFF),
//        disabledActiveTrackColor: Color = Color(0xFF0037FF).copy(alpha = 0.5f),
//        disabledInactiveTrackColor: Color = Color(0xFFA09C9C).copy(alpha = 0.5f),
//        disabledLimitationMarkColor: Color = Color(0xFF0037FF),
//    ): OffsetSliderColors = OffsetSliderColors(
//        thumbColor = thumbColor,
//        activeTrackColor = activeTrackColor,
//        inactiveTrackColor = inactiveTrackColor,
//        limitationMarkColor = limitationMarkColor,
//        disabledThumbColor = disabledThumbColor,
//        disabledActiveTrackColor = disabledActiveTrackColor,
//        disabledInactiveTrackColor = disabledInactiveTrackColor,
//        disabledLimitationMarkColor = disabledLimitationMarkColor,
//    )
//}
//
//
//
