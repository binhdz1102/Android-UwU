package com.binhdz.androiduwu.slidertemp

import androidx.annotation.IntRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

//@Composable
//fun CustomRangeSlider(
//    modifier: Modifier = Modifier,
//    value: Float,
//    valueRange: ClosedFloatingPointRange<Float> = -1f..1f,
//    steps: Int = 0,
//    enabled: Boolean = true,
//    onValueChange: (Float) -> Unit,
//    onValueChangeFinished: (() -> Unit)? = null
//) {
//    val a = valueRange.start
//    val b = valueRange.endInclusive
//    require(steps >= 0) { "The slider's steps should be >= 0" }
//    require(a < 0) { "The slider's start value should be < 0f" }
//    require(b > 0) { "The slider's end value should be > 0f" }
//
//    var sliderSize by remember { mutableStateOf(IntSize.Zero) }
//    var sliderValue by remember { mutableFloatStateOf(value.coerceIn(valueRange)) }
//
//    // Biến để điều khiển giá trị strokeWidth động
//    var strokeWidth by remember { mutableStateOf(8f) }
//    // Thay đổi strokeWidth với animation khi giá trị thay đổi
//    val animatedStrokeWidth by animateFloatAsState(
//        targetValue = strokeWidth,
//        animationSpec = tween(durationMillis = 200)
//    )
//
//    Box(
//        modifier = modifier
//            .onSizeChanged { size ->
//                sliderSize = size
//            }
//            .pointerInput(enabled) {
//                if (enabled) {
//                    detectTapGestures(
//                        onPress = {
//                            // Khi chạm vào, tăng strokeWidth lên 12f
//                            strokeWidth = 15f
//                            tryAwaitRelease() // Chờ cho đến khi người dùng nhả tay
//                            // Khi nhả tay ra, quay lại strokeWidth ban đầu (8f)
//                            strokeWidth = 8f
//                        },
//                        onTap = { offset -> // cần xem xét phần này!!!!!
//                            // Khi tap, tính toán giá trị slider mới từ vị trí tap
//                            val newX = (offset.x / sliderSize.width) * (b - a) + a
//                            var newValue = newX.coerceIn(a, b)
//                            if (steps > 0) {
//                                val stepSize = (b - a) / steps
//                                newValue = ((newValue - a) / stepSize).roundToInt() * stepSize + a
//                            }
//                            sliderValue = newValue
//                            onValueChange(sliderValue)
//                            onValueChangeFinished?.invoke()
//                        }
//                    )
//                }
//            }
//            .pointerInput(enabled) {
//                if (enabled) {
//                    detectDragGestures(
//                        onDragEnd = {
//                            strokeWidth = 8f // Khi nhả tay ra, quay lại strokeWidth ban đầu (8f)
//                            onValueChangeFinished?.invoke()
//                        },
//                        onDrag = { change, _ ->
//                            strokeWidth = 15f // Khi chạm vào, tăng strokeWidth lên 12f
//                            val newX = (change.position.x / size.width) * (b - a) + a
//                            var newValue = newX.coerceIn(a, b)
//                            if (steps > 0) {
//                                val stepSize = (b - a) / steps
//                                newValue = ((newValue - a) / stepSize).roundToInt() * stepSize + a
//                            }
//                            sliderValue = newValue
//                            onValueChange(sliderValue)
//                        }
//                    )
//                }
//            }
//    ) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            val centerX = size.width * (-a) / (b - a)
//            val normalizedValue = (sliderValue - a) / (b - a)
//            val thumbX = normalizedValue * size.width
//
//            // vẽ track nền
//            drawLine(
//                color = Color.LightGray,
//                start = Offset(0f, size.height / 2),
//                end = Offset(size.width, size.height / 2),
//                strokeWidth = animatedStrokeWidth
//            )
//
//            // Vẽ track được chọn từ điểm 0 đến thumbX
//            drawLine(
//                color = Color.Blue,
//                start = Offset(centerX, size.height / 2),
//                end = Offset(thumbX, size.height / 2),
//                strokeWidth = animatedStrokeWidth
//            )
//
//            // Vẽ vạch mốc tại vị trí 0
//            drawLine(
//                color = Color.Blue,
//                start = Offset(centerX, 0f),
//                end = Offset(centerX, size.height),
//                strokeWidth = 2f
//            )
//        }
//
//        // Vẽ thumb
//        Box(
//            modifier = Modifier
//                .offset(x = (sliderSize.width * (sliderValue - a) / (b - a)).dp - 12.dp)
//                .align(Alignment.CenterStart)
//        ) {
//            Canvas(Modifier.size(24.dp)) {
//                drawCircle(color = Color.Red, radius = size.minDimension / 2)
//            }
//        }
//    }
//}


@Composable
fun CustomRangeSlider(
    modifier: Modifier = Modifier,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float> = -1f..1f,
    @IntRange(from = 0)
    steps: Int = 0,
    enabled: Boolean = true,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: (() -> Unit)? = null
) {
    val a = valueRange.start
    val b = valueRange.endInclusive
    require(steps >= 0) { "The slider's steps should be >= 0" }
    require(a < 0) { "The slider's start value should be < 0f" }
    require(b > 0) { "The slider's end value should be > 0f" }

    var sliderSize by remember { mutableStateOf(IntSize.Zero) }
    var sliderValue by remember { mutableFloatStateOf(value.coerceIn(valueRange)) }

    var strokeWidth by remember { mutableStateOf(8f) }
    val animatedStrokeWidth by animateFloatAsState(
        targetValue = strokeWidth,
        animationSpec = tween(durationMillis = 200)
    )

    // Hàm tính toán giá trị mới dựa trên vị trí x
    fun calculateSliderValueFromPosition(positionX: Float): Float {
        val newX = (positionX / sliderSize.width) * (b - a) + a
        var newValue = newX.coerceIn(a, b)
        if (steps > 0) {
            val stepSize = (b - a) / steps
            newValue = ((newValue - a) / stepSize).roundToInt() * stepSize + a
        }
        return newValue
    }

    Box(
        modifier = modifier
            .onSizeChanged { sliderSize = it }
            .pointerInput(enabled) {
                if (enabled) {
                    detectTapGestures(
                        onPress = {
                            strokeWidth = 15f
                            tryAwaitRelease()
                            strokeWidth = 8f
                        },
                        onTap = { offset ->
                            sliderValue = calculateSliderValueFromPosition(offset.x)
                            onValueChange(sliderValue)
                            onValueChangeFinished?.invoke()
                        }
                    )
                }
            }
            .pointerInput(enabled) {
                if (enabled) {
                    detectDragGestures(
                        onDragEnd = {
                            strokeWidth = 8f
                            onValueChangeFinished?.invoke()
                        },
                        onDrag = { change, _ ->
                            strokeWidth = 15f
                            sliderValue = calculateSliderValueFromPosition(change.position.x)
                            onValueChange(sliderValue)
                        }
                    )
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width * (-a) / (b - a)
            val normalizedValue = (sliderValue - a) / (b - a)
            val thumbX = normalizedValue * size.width

            drawLine(
                color = Color.LightGray,
                start = Offset(0f, size.height / 2),
                end = Offset(size.width, size.height / 2),
                strokeWidth = animatedStrokeWidth
            )

            drawLine(
                color = Color.Blue,
                start = Offset(centerX, size.height / 2),
                end = Offset(thumbX, size.height / 2),
                strokeWidth = animatedStrokeWidth
            )

            drawLine(
                color = Color.Blue,
                start = Offset(centerX, 0f),
                end = Offset(centerX, size.height),
                strokeWidth = 2f
            )
        }

        Box(
            modifier = Modifier
                .offset(x = (sliderSize.width * (sliderValue - a) / (b - a)).dp - 12.dp)
                .align(Alignment.CenterStart)
        ) {
            Canvas(Modifier.size(24.dp)) {
                drawCircle(color = Color.Red, radius = size.minDimension / 2)
            }
        }
    }
}