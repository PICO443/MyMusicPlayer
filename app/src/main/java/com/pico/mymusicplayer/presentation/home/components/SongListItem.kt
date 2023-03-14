package com.pico.mymusicplayer.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.pico.mymusicplayer.R
import com.pico.mymusicplayer.domain.model.Song

@Composable
fun SongListItem(
    song: Song,
    onSongClicked: (Song) -> Unit,
    modifier: Modifier = Modifier,
    isCurrentlyPlaying: Boolean = false,
    isPaused: Boolean = true,
    onPlayClicked: (Song) -> Unit
) {
    val songImage =
        if (song.image == null)
            painterResource(id = R.drawable.song_cover)
        else
            remember(song.image) {
                BitmapPainter(
                    song.image.asImageBitmap(),
                    filterQuality = FilterQuality.Low
                )
            }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSongClicked(song) },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = songImage,
            contentDescription = stringResource(R.string.song_cover),
            modifier = Modifier.size(56.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = song.artist, style = MaterialTheme.typography.labelMedium)
            Text(text = song.name, color = if (isCurrentlyPlaying) Color.Red else Color.Black)
        }
        IconButton(modifier = Modifier
            .padding(end = 16.dp), onClick = { onPlayClicked(song) }) {
            Icon(
                modifier = Modifier
                    .size(20.dp),
                imageVector = if (!isPaused)
                    ImageVector.vectorResource(id = R.drawable.pause_fill0_wght400_grad0_opsz48) else Icons.Default.PlayArrow,
                contentDescription = null
            )
        }


    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun SongListItemPreview() {
//    MaterialTheme() {
//        Box(contentAlignment = Alignment.Center) {
//            SongListItem(Song("Song Name", "Artis.t", null))
//        }
//    }
//}