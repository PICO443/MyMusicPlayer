package com.pico.mymusicplayer.domain.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

data class Song(
    val id: Long,
    val uri: Uri?,
    val name: String,
    val artist: String,
    val duration: Long,
    val image: Bitmap?
) {
    fun toMediaItem(): MediaItem {
        val bundle = Bundle()
        bundle.putLong("duration", duration)
        bundle.putLong("songId", id)
        return MediaItem.Builder().setMediaId(uri?.toString()!!).setMediaMetadata(
            MediaMetadata.Builder().setArtist(artist).setTitle(name).setExtras(bundle).build()
        ).build()
    }

    companion object {
        fun fromMediaItem(mediaItem: MediaItem): Song {
            mediaItem.mediaMetadata.apply {
                return Song(
                    name = title.toString(),
                    artist = artist.toString(),
                    id = extras?.getLong("songId") ?: -1,
                    image = null,
                    uri = null,
                    duration = extras?.getLong("duration") ?: -1
                )
            }
        }

    }
}
