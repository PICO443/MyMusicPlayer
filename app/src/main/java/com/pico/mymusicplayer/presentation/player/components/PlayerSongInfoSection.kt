package com.pico.mymusicplayer.presentation.player.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pico.mymusicplayer.R
import com.pico.mymusicplayer.domain.model.Song

@Composable
fun PlayerSongInfoSection(song: Song,modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxWidth(),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        ImageBehindSnowfall(image = ImageBitmap.imageResource(id = R.drawable.song_cover), modifier.aspectRatio(1f).weight(1f))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = song.name, style = MaterialTheme.typography.titleLarge)
        Text(text = song.artist, style = MaterialTheme.typography.labelLarge)
    }
}
//@Preview(showSystemUi = true)
//@Composable
//fun PlayerSongInfoSectionPreview(){
//    MaterialTheme() {
//        Column(modifier = Modifier.fillMaxSize()) {
//            PlayerSongInfoSection(modifier = Modifier.weight(1f))
//        }
//    }
//}