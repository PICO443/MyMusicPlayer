package com.pico.mymusicplayer.presentation.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player.Listener
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.pico.mymusicplayer.common.Constants
import com.pico.mymusicplayer.common.Resource
import com.pico.mymusicplayer.domain.model.Song
import com.pico.mymusicplayer.domain.use_case.GetSongsUseCase
import com.pico.mymusicplayer.domain.use_case.TogglePlaySongUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val futureMediaController: ListenableFuture<MediaController>,
    private val savedStateHandle: SavedStateHandle,
    private val getSongsUseCase: GetSongsUseCase,
    private val togglePlaySongUseCase: TogglePlaySongUseCase
) : ViewModel() {

    private lateinit var mediaController: MediaController

    private val currentPositionFlow = flow {
        while (true) {
            delay(100)
            mediaController.currentMediaItem?.apply {
                if (Song.fromMediaItem(this).id.toString() == savedStateHandle[Constants.PARAM_SONG_ID])
                    emit(mediaController.currentPosition)
            }
        }
    }

    var uiState by mutableStateOf(
        PlayerUiState(
            isPlaying = false,
            currentPosition = currentPositionFlow,
            currentSong = Song.emptySong()
        )
    )
        private set

    init {
        futureMediaController.addListener({
            /**
             * 1. Initializing the mediaController
             */
            mediaController = futureMediaController.get()
            /**
             * 2. Initializing the uiState and if there is a song playing:
             *  - If the playing song is the current song -> set uiState.isPlaying = MediaController.isPlaying
             */
            viewModelScope.launch {
                when (val songs = getSongsUseCase()) {
                    is Resource.Success -> {
                        uiState = uiState.copy(
                            currentSong = songs.data.first { it.id.toString() == savedStateHandle[Constants.PARAM_SONG_ID] },
                            isPlaying = if ((mediaController.currentMediaItem != null) && (Song.fromMediaItem(
                                    mediaController.currentMediaItem!!
                                ).id.toString() == savedStateHandle[Constants.PARAM_SONG_ID])
                            ) mediaController.isPlaying else false
                        )
                    }
                    else -> {}
                }
            }
            /**
             * 3. Adding listeners to update the uiState according to the state of the player
             */
            mediaController.addListener(object : Listener {

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    mediaController.currentMediaItem?.apply {
                        if (Song.fromMediaItem(this).id == uiState.currentSong.id)
                            uiState = uiState.copy(isPlaying = isPlaying)
                    }

                }

            })
            mediaController.prepare()
        }, MoreExecutors.directExecutor())
    }

    fun onEvent(event: PlayerUiEvents) {
        when (event) {
            is PlayerUiEvents.Play -> {
                mediaController.currentMediaItem?.apply {
                    if (Song.fromMediaItem(this).id != uiState.currentSong.id)
                        mediaController.setMediaItem(uiState.currentSong.toMediaItem())
                }
                mediaController.play()
            }
            is PlayerUiEvents.Pause -> {
                mediaController.pause()
            }
            is PlayerUiEvents.TogglePlay -> {
                togglePlaySongUseCase(mediaController = mediaController, song = uiState.currentSong)
            }
            is PlayerUiEvents.SeekTo -> {
                if (uiState.isPlaying) {
                    mediaController.seekTo(event.position)
                }
            }
            is PlayerUiEvents.FastSeekForward -> {
                if (uiState.isPlaying) {
                    mediaController.seekForward()
                }
            }
            is PlayerUiEvents.FastSeekBackward -> {
                if (uiState.isPlaying) {
                    mediaController.seekBack()
                }
            }
        }
    }


}

data class PlayerUiState(
    val currentSong: Song,
    val isPlaying: Boolean,
    val currentPosition: Flow<Long>
)

sealed class PlayerUiEvents {
    data class SeekTo(val position: Long) : PlayerUiEvents()
    object Play : PlayerUiEvents()
    object Pause : PlayerUiEvents()
    object TogglePlay : PlayerUiEvents()
    object FastSeekForward : PlayerUiEvents()
    object FastSeekBackward : PlayerUiEvents()
}