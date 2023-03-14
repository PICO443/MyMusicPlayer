package com.pico.mymusicplayer.domain.use_case

import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.pico.mymusicplayer.domain.model.Song

class TogglePlaySongUseCase {
    operator fun invoke(mediaController: MediaController, song: Song){
        /**
         * Will pause or play the current song
         */
        mediaController.currentMediaItem?.apply {
            if (Song.fromMediaItem(this).id == song.id && mediaController.playbackState != Player.STATE_ENDED) {
                if (mediaController.isPlaying)
                    mediaController.pause()
                else
                    mediaController.play()
                return
            }
        }
        /**
         * Will execute when:
         * - The player state is IDLE - isn't playing any song
         * - When start playing other song than the currently playing one
         */
        mediaController.setMediaItem(song.toMediaItem())
        mediaController.play()
    }
}