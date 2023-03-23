package com.pico.mymusicplayer.presentation.player.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pico.mymusicplayer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerControllersSection(
    isPlaying: Boolean,
    songDuration: Long,
    duration: State<Long>,
    onPlayClicked: () -> Unit,
    onSliderValueChange: (Long) -> Unit,
    onFastSeekForwardClicked: () -> Unit,
    onFastSeekBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sliderState by remember {
        duration
    }

    fun fromMillisecondsToSeconds(milliseconds: Long): Long {
        return (milliseconds / 1000)
    }

    fun fromMillisecondsToMinutes(milliSeconds: Long): Long {
        return (fromMillisecondsToSeconds(milliSeconds) / 60)
    }

    Surface(modifier = modifier, tonalElevation = 0.dp, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Slider(
                value = sliderState / songDuration.toFloat(),
                onValueChange = { onSliderValueChange((it * songDuration).toLong()) },
                track = {
                    CustomSliderTrack(sliderPositions = it, trackHeight = 24.dp)
                }) {}
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
                    // Intended text format is like: "03:45"
                    Text(
                        text = "0${fromMillisecondsToMinutes(duration.value)}:${
                            fromMillisecondsToSeconds(
                                duration.value
                            ) % 60
                        }", style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "0${fromMillisecondsToMinutes(songDuration)}:${
                            fromMillisecondsToSeconds(
                                songDuration
                            ) % 60
                        }", style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                IconButton(onClick = onFastSeekBackClicked) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.fast_rewind_fill0_wght400_grad0_opsz48),
                        contentDescription = stringResource(R.string.fast_rewind)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                FilledIconButton(modifier = Modifier.scale(1.25f), onClick = { onPlayClicked() }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(if (isPlaying) R.drawable.pause_fill1_wght400_grad0_opsz48 else R.drawable.play_arrow_fill1_wght400_grad0_opsz48),
                        contentDescription = stringResource(R.string.play)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                IconButton(onClick = onFastSeekForwardClicked) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.fast_forward_fill0_wght400_grad0_opsz48),
                        contentDescription = stringResource(R.string.fast_forward)
                    )
                }
            }
        }
    }
}