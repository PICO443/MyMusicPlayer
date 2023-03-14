package com.pico.mymusicplayer.presentation.player

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.pico.mymusicplayer.domain.model.Song
import com.pico.mymusicplayer.domain.use_case.GetSongsUseCase
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
    private val getSongsUseCase: GetSongsUseCase
) : ViewModel() {

    private lateinit var player: Player

    private var currentPlayingSong: Song? = null

    var uiState by mutableStateOf(PlayerUiState(isPlaying = false, currentPosition = flow {
        while (true) {
            delay(100)
            player.currentMediaItem?.apply {
                if (Song.fromMediaItem(this).id.toString() == savedStateHandle["songId"])
                    emit(player.currentPosition)
            }
        }

    }))
        private set

    init {
        viewModelScope.launch {
            uiState =
                uiState.copy(currentSong = getSongsUseCase().firstOrNull { it.id.toString() == savedStateHandle["songId"] })
            getSongsUseCase().forEach {
                Log.d("songIds", "id: ${it.id}")
            }
        }
        futureMediaController.addListener({
            player = futureMediaController.get()
            currentPlayingSong = if(player.currentMediaItem == null){
                uiState.currentSong
            } else {
                Song.fromMediaItem(player.currentMediaItem!!)
            }
            player.addListener(object : Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    super.onPlaybackStateChanged(playbackState)
                    when (playbackState) {

                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    mediaItem?.apply {
                        currentPlayingSong = Song.fromMediaItem(mediaItem)
                    }
                }
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    player.currentMediaItem?.apply {
                        if (Song.fromMediaItem(this).id == uiState.currentSong?.id)
                            uiState = uiState.copy(isPlaying = isPlaying)
                    }

                }

            })
            player.prepare()
        }, MoreExecutors.directExecutor())
    }

    fun onEvent(event: PlayerUiEvents) {
        when (event) {
            is PlayerUiEvents.Play -> {
                currentPlayingSong?.apply {
                    if(id != uiState.currentSong?.id)
                        player.setMediaItem(uiState.currentSong?.toMediaItem()!!)
                }
                player.play()
            }
            is PlayerUiEvents.Pause -> {
                player.pause()
            }
            is PlayerUiEvents.TogglePlay -> {
                uiState = if (uiState.isPlaying) {
                    player.pause()
                    uiState.copy(isPlaying = false)
                } else {
                    onEvent(PlayerUiEvents.Play)
                    uiState.copy(isPlaying = true)
                }

            }
            is PlayerUiEvents.SeekTo -> {
                if(uiState.isPlaying){
                    player.seekTo(event.position)
                }
            }
        }
    }


}

data class PlayerUiState(
    val currentSong: Song? = null,
    val isPlaying: Boolean,
    val currentPosition: Flow<Long>
)

sealed class PlayerUiEvents {
    data class SeekTo(val position: Long) : PlayerUiEvents()
    object Play : PlayerUiEvents()
    object Pause : PlayerUiEvents()
    object TogglePlay : PlayerUiEvents()
}