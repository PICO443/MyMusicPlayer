package com.pico.mymusicplayer.domain.repository

import com.pico.mymusicplayer.common.Resource
import com.pico.mymusicplayer.domain.model.Song

interface SongRepository {
    suspend fun getSongs(): Resource<List<Song>>
}