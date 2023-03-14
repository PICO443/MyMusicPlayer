package com.pico.mymusicplayer.data.repository

import com.pico.mymusicplayer.data.data_source.ContentResolverHelper
import com.pico.mymusicplayer.domain.model.Song
import com.pico.mymusicplayer.domain.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepositoryImpl@Inject constructor(private val contentResolverHelper: ContentResolverHelper): SongRepository {
    override suspend fun getSongs(): List<Song> = withContext(Dispatchers.IO) {
        contentResolverHelper.getAudioFiles().map {
            it.toSong()
        }
    }
}