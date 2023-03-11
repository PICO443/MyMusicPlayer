package com.pico.mymusicplayer.presentation.player.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderPositions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSliderTrack(
    sliderPositions: SliderPositions,
    modifier: Modifier = Modifier,
    trackHeight: Dp
) {
    val inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
    val activeTrackColor = MaterialTheme.colorScheme.primary
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(trackHeight)
    ) {
        val sliderStart = Offset(0f, center.y)
        val sliderEnd = Offset(size.width, center.y)
        drawLine(
            color = inactiveTrackColor,
            start = sliderStart,
            end = sliderEnd,
            strokeWidth = trackHeight.toPx(),
            StrokeCap.Round
        )
        val sliderValueEnd =
            Offset(sliderStart.x + sliderEnd.x * sliderPositions.positionFraction, center.y)
        drawLine(
            color = activeTrackColor,
            start = sliderStart,
            end = sliderValueEnd,
            strokeWidth = trackHeight.toPx(),
            cap = StrokeCap.Round
        )
    }
}