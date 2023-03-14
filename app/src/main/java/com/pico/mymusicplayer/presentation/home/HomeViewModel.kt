package com.pico.mymusicplayer.presentation.home

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.pico.mymusicplayer.domain.model.Song
import com.pico.mymusicplayer.domain.use_case.GetSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getSongsUseCase: GetSongsUseCase,
    private val futureMediaController: ListenableFuture<MediaController>
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    private lateinit var mediaController: MediaController

    init {
        viewModelScope.launch {
            uiState = HomeUiState(songs = getSongsUseCase())
            futureMediaController.addListener({
                mediaController = futureMediaController.get()
                mediaController.prepare()
            }, MoreExecutors.directExecutor())
        }
    }

    fun onEvent(event: HomeScreenUiEvents) {
        when (event) {
            is HomeScreenUiEvents.SelectSong -> {
                val media = event.song.toMediaMetaData()
                mediaController.setMediaItem(media)
                mediaController.play()
                uiState = uiState.copy(currentlyPlayingSong = event.song)
            }
        }
    }
}

data class HomeUiState(val songs: List<Song> = emptyList(), val currentlyPlayingSong: Song? = null)

sealed class HomeScreenUiEvents {
    data class SelectSong(val song: Song) : HomeScreenUiEvents()
}