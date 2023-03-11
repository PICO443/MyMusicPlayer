package com.pico.mymusicplayer.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pico.mymusicplayer.R
import com.pico.mymusicplayer.domain.model.Song

@Composable
fun SongListItem(song: Song, modifier: Modifier = Modifier) {
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
            .clickable { TODO() },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter =songImage,
            contentDescription = stringResource(R.string.song_cover),
            modifier = Modifier.size(56.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = song.artist, style = MaterialTheme.typography.labelMedium)
            Text(text = song.name)
        }
        Icon(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(20.dp),
            imageVector = Icons.Default.PlayArrow,
            contentDescription = null
        )


    }
}

@Preview(showSystemUi = true)
@Composable
fun SongListItemPreview() {
    MaterialTheme() {
        Box(contentAlignment = Alignment.Center) {
            SongListItem(Song("Song Name", "Artist", null))
        }
    }
}