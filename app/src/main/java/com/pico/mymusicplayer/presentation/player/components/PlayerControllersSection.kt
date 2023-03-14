package com.pico.mymusicplayer.presentation.player.components

import android.text.format.Time
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pico.mymusicplayer.R
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerControllersSection(modifier: Modifier = Modifier, onPlayClicked: () -> Unit, onSliderValueChange: (Long) -> Unit, isPlaying: Boolean, songDuration: Long, duration: State<Long>) {
    val sliderState by remember {
        duration
    }
    Column(modifier = modifier) {
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
            Text(text = "0${duration.value / 1000 / 60}:${duration.value / 1000 % 60}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "0${songDuration / 1000 / 60}:${songDuration / 100 % 60}", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.fast_rewind_fill0_wght400_grad0_opsz48),
                    contentDescription = "fast rewind"
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            FilledIconButton(modifier = Modifier.scale(1.25f), onClick = { onPlayClicked() }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource( if(isPlaying) R.drawable.pause_fill1_wght400_grad0_opsz48 else R.drawable.play_arrow_fill1_wght400_grad0_opsz48),
                    contentDescription = "play"
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.fast_forward_fill0_wght400_grad0_opsz48),
                    contentDescription = "fast forward"
                )
            }
        }
    }
}