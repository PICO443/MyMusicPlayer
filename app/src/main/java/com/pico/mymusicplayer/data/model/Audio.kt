package com.pico.mymusicplayer.data.model

import android.graphics.Bitmap
import android.net.Uri
import com.pico.mymusicplayer.domain.model.Song
import java.time.Duration

data class Audio(val id: Long, val albumId: Long, var image: Bitmap?, val uri: Uri, val name: String, val artist: String, val duration: Long) {
    fun toSong(): Song {
        return Song(
            id = id,
            uri = uri,
            name = name,
            artist = artist,
            image = image,
            duration = duration
        )
    }
}


