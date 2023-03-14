package com.pico.mymusicplayer.data.data_source

import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.WorkerThread
import com.pico.mymusicplayer.data.model.Audio
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContentResolverHelper @Inject constructor(private val appContext: Context) {

    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Albums.ALBUM_ID,
        MediaStore.Audio.Media.DURATION
    )

    @WorkerThread
    fun getAudioFiles(): List<Audio> {
        val audioList = mutableListOf<Audio>()
        appContext.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val artistColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            cursor.apply {
                while (moveToNext()) {
                    val audio =
                        Audio(
                            uri = ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                getLong(idColumnIndex)
                            ),
                            id = getLong(idColumnIndex),
                            name = getString(nameColumnIndex),
                            artist = getString(artistColumnIndex),
                            albumId = getLong(albumIdIndex),
                            image = null,
                            duration = getLong(durationIndex)
                        )
                    audioList.add(audio)
                }
            }
        }
        appContext.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, arrayOf(
                MediaStore.Audio.Albums.ALBUM_ART
            ), null, null, null
        )?.use { cursor ->
            val albumArtIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART)
            while (cursor.moveToNext()) {
                cursor.apply {
                    val filteredAudio =
                        audioList.firstOrNull { it.albumId == getLong(albumArtIndex) }
                    filteredAudio?.apply {
                            image = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                appContext.contentResolver.loadThumbnail(
                                    ContentUris.withAppendedId(
                                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                        albumId
                                    ),
                                    Size(56, 56),
                                    null
                                )
                            } else {
                                BitmapFactory.decodeFile(
                                    "file://${
                                        ContentUris.withAppendedId(
                                            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                                            albumId
                                        )
                                    }"
                                )
                            }

                    }
                }
            }
        }
        return audioList
    }

}