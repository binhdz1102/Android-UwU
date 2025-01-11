package com.binhdz.androiduwu

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.binhdz.androiduwu.ui.theme.AndroidUwUTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidUwUTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {



//                    TestHOffsetSlider()
                    TestHOffsetSliderChangedThumb()


//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        ContinuousOffsetSliderExample()
//                        Spacer(modifier = Modifier.height(30.dp))
//                        DiscreteOffsetSliderExample()
//                    }



                }
            }
        }
    }
}


@Composable
fun TestHOffsetSlider() {
    var sliderValue by remember { mutableStateOf(50f) }
    val valueRange = 0f..100f
    val steps = 9

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Giá trị HOffsetSlider: ${sliderValue}")
        Spacer(modifier = Modifier.height(20.dp))
        HLimitedSlider(
            modifier = Modifier
                .width(500.dp)
                .height(80.dp)
                .background(Color.Yellow),
            value = sliderValue,
            valueRange = valueRange,
//            step = steps,
            limitedSliderTooltipPosition = LimitedSliderTooltipPosition.ABOVE,
            onValueChange = {
                sliderValue = it
            },
            limitStart = 30f,
            limitEnd = 70f,
            onValueChangeFinished = {
                Log.d("check123", "Update slider value mới: ${sliderValue} ")
            }
        )
    }
}


@Composable
fun TestHOffsetSliderChangedThumb() {
    var sliderValue by remember { mutableStateOf(50f) }
    val valueRange = 0f..100f
    val steps = 9

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Giá trị HOffsetSlider: ${sliderValue}")
        Spacer(modifier = Modifier.height(20.dp))
        HLimitedSliderChangedThumb(
            modifier = Modifier
                .width(1500.dp)
                .height(80.dp)
                .background(Color.Yellow),
            value = sliderValue,
            valueRange = valueRange,
//            step = steps,
            limitedSliderChangedThumbTooltipPosition = LimitedSliderChangedThumbTooltipPosition.ABOVE,
            onValueChange = {
                sliderValue = it
            },
            limitStart = 25f,
            limitEnd = 85f,
            onValueChangeFinished = {
                Log.d("check123", "Update slider value mới: ${sliderValue} ")
            }
        )
    }
}
