package com.pico.mymusicplayer.data.data_source

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.pico.mymusicplayer.data.model.Audio
import javax.inject.Inject

class AudioMediaStore @Inject constructor(private val appContext: Context) {

    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DISPLAY_NAME,
        MediaStore.Audio.Media.ARTIST,
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
            val durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            cursor.apply {
                while (moveToNext()) {
                    audioList.add(
                        Audio(
                            uri = ContentUris.withAppendedId(
                                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                getLong(idColumnIndex)
                            ),
                            id = getLong(idColumnIndex),
                            name = getString(nameColumnIndex),
                            artist = getString(artistColumnIndex),
                            duration = getLong(durationIndex)
                        )
                    )
                }
            }
        }
        return audioList
    }

}