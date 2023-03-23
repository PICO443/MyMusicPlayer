package com.pico.mymusicplayer.data.model

import android.net.Uri
import com.pico.mymusicplayer.domain.model.Song

data class Audio(val id: Long, val uri: Uri, val name: String, val artist: String, val duration: Long, val path:String) {
    fun toSong(): Song {
        return Song(
            id = id,
            uri = uri,
            name = name,
            artist = artist,
            image = null,
            duration = duration,
            path = path
        )
    }
}


