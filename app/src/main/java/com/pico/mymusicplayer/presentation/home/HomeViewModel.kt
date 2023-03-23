package com.pico.mymusicplayer.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.pico.mymusicplayer.common.Resource
import com.pico.mymusicplayer.domain.model.Song
import com.pico.mymusicplayer.domain.use_case.GetCurrentSongPositionUseCase
import com.pico.mymusicplayer.domain.use_case.GetSongsUseCase
import com.pico.mymusicplayer.domain.use_case.GetWaveformUseCase
import com.pico.mymusicplayer.domain.use_case.TogglePlaySongUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase,
    private val togglePlaySongUseCase: TogglePlaySongUseCase,
    private val getWaveformUseCase: GetWaveformUseCase,
    private val futureMediaController: ListenableFuture<MediaController>,
    private val getCurrentSongPositionUseCase: GetCurrentSongPositionUseCase
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    private lateinit var mediaController: MediaController

    init {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            when (val songsList = getSongsUseCase()) {
                is Resource.Success -> {
                    uiState = uiState.copy(songs = songsList.data.map {
                        it.copy(
                            waveform = getWaveformUseCase(it.path!!)
                        )
                    })
                    uiState = uiState.copy(isLoading = false)
                }
                else -> {}
            }
        }
        futureMediaController.addListener({
            mediaController = futureMediaController.get()
            // Getting the current playing song AFTER process death
            mediaController.currentMediaItem?.apply {
                uiState = uiState.copy(
                    currentSong = CurrentSong(
                        song = Song.fromMediaItem(this),
                        isPaused = mediaController.isPlaying.not(),
                        position = getCurrentSongPositionUseCase()
                    )
                )
            }
            mediaController.addListener(object : Listener {
                // Called when the current playing song changes
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    mediaItem?.apply {
                        uiState =
                            uiState.copy(
                                currentSong = CurrentSong(
                                    song = Song.fromMediaItem(
                                        this
                                    ),
                                    isPaused = mediaController.isPlaying.not(),
                                    position = getCurrentSongPositionUseCase()
                                )
                            )
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    uiState = uiState.copy(
                        currentSong = uiState.currentSong?.copy(isPaused = isPlaying.not())
                    )
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {
                        Player.STATE_ENDED -> uiState = uiState.copy(currentSong = null)
                        Player.STATE_BUFFERING -> {}
                        Player.STATE_IDLE -> {}
                        Player.STATE_READY -> {}
                    }
                }
            })
            mediaController.prepare()
        }, MoreExecutors.directExecutor())

    }

    fun onEvent(event: HomeScreenUiEvents) {
        when (event) {
            is HomeScreenUiEvents.PlaySong -> {
                mediaController.setMediaItem(event.song.toMediaItem())
                mediaController.play()
            }
            is HomeScreenUiEvents.TogglePlay -> {
                togglePlaySongUseCase(mediaController, event.song)
            }
            is HomeScreenUiEvents.SeekTo -> {
                mediaController.seekTo(event.position)
            }
        }
    }
}

data class HomeUiState(
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = true,
    val currentSong: CurrentSong? = null
)

data class CurrentSong(val song: Song, val isPaused: Boolean, val position: Flow<Long>)
sealed class HomeScreenUiEvents {
    data class PlaySong(val song: Song) : HomeScreenUiEvents()
    data class TogglePlay(val song: Song) : HomeScreenUiEvents()
    data class SeekTo(val position: Long): HomeScreenUiEvents()
}