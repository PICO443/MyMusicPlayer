package com.pico.mymusicplayer.domain.use_case

import com.pico.mymusicplayer.domain.model.Song
import com.pico.mymusicplayer.domain.repository.SongRepository

class GetSongsUseCase(private val songRepository: SongRepository) {
    suspend operator fun invoke(): List<Song> {
        return songRepository.getSongs()
    }
}