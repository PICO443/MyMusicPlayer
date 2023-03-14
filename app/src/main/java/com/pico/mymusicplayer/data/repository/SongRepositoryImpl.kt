package com.pico.mymusicplayer.data.repository

import com.pico.mymusicplayer.common.Resource
import com.pico.mymusicplayer.data.data_source.AudioMediaStore
import com.pico.mymusicplayer.domain.model.Song
import com.pico.mymusicplayer.domain.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepositoryImpl @Inject constructor(private val audioMediaStore: AudioMediaStore): SongRepository {
    override suspend fun getSongs(): Resource<List<Song>> = withContext(Dispatchers.IO) {
        try {
            return@withContext Resource.Success(data = audioMediaStore.getAudioFiles().map { it.toSong() })
        } catch (e: Exception){
            return@withContext Resource.Error(message = "Error while getting audios from mediaStore: ${e.message}")
        }
    }
}