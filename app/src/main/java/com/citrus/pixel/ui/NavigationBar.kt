package com.citrus.pixel.ui
import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.citrus.pixel.utils.ExportImage
import com.citrus.pixel.utils.MainViewModel


@Composable
fun CustomNavigationBar(
    viewModel: MainViewModel,
    isAnimatingIn: Boolean
) {
    val offsetY = animateDpAsState(
        targetValue = if (isAnimatingIn) 0.dp else 112.dp,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(112.dp)
            .offset(y = offsetY.value)
            .background(color = Color(0xFFFFFBD5), shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        contentAlignment = Alignment.Center
    ) {
        PixelizeControls(viewModel = viewModel, context = LocalContext.current)
    }
}

@Composable
fun PixelizeControls(
    viewModel: MainViewModel,
    context: Context
) {
    val sliderRange = remember { mutableStateOf(0.1f..1.5f) }
    LaunchedEffect(Unit) {
        sliderRange.value = viewModel.getSliderRangeForImage()
    }
    val pixelizedBitmap = viewModel.pixelizedBitmap.observeAsState().value
    val sliderValue = viewModel.sliderValue.observeAsState(0.1f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Slider(
            value = sliderValue.value,
            onValueChange = { newValue ->
                viewModel.updateSliderValue(newValue)
            },
            valueRange = sliderRange.value,
            colors = SliderDefaults.colors(
                thumbColor = Color.Yellow,
                activeTrackColor = Color.Green,
                inactiveTrackColor = Color.Gray
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CustomButton(text = "Save", onClick = {
                pixelizedBitmap?.let { bitmap ->
                    ExportImage().saveImage(bitmap, context)
                }
            })
        }
    }
}