package com.pico.mymusicplayer.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pico.mymusicplayer.R
import com.pico.mymusicplayer.common.Resource
import com.pico.mymusicplayer.presentation.home.components.SongListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onSongClicked: (String) -> Unit) {
    val uiState = viewModel.uiState
    Scaffold(
        topBar = { HomeTopAppBar() }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        } else {
            LazyColumn(
                contentPadding = paddingValues,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 0.dp)
                    .fillMaxSize()
            ) {
                items(uiState.songs) { song ->
                    SongListItem(
                        song,
                        onSongClicked = {
                            onSongClicked(song.id.toString())
                        },
                        isCurrentlyPlaying = (uiState.currentSong?.song?.id == song.id),
                        isPaused = (if (uiState.currentSong?.song?.id == song.id) (uiState.currentSong.isPaused) else true),
                        onPlayClicked = {
                            viewModel.onEvent(HomeScreenUiEvents.TogglePlay(it))
                        },
                        songPosition = uiState.currentSong?.position?.collectAsState(0)?.value ?: 0L,
                        onSongProgressChange = {
                            viewModel.onEvent(HomeScreenUiEvents.SeekTo(it))
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    TopAppBar(title = { Text(text = stringResource(R.string.songs)) })
}