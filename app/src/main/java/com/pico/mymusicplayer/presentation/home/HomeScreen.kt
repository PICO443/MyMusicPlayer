package com.pico.mymusicplayer.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
        LazyColumn(
            contentPadding = paddingValues, modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            when(uiState.songs){
                is Resource.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    items(uiState.songs.data) { song ->
                        SongListItem(
                            song,
                            onSongClicked = {
                                onSongClicked(song.id.toString())
                            },
                            isCurrentlyPlaying = (uiState.currentSong?.song?.id == song.id),
                            isPaused = (if(uiState.currentSong?.song?.id == song.id) (uiState.currentSong.isPaused) else true),
                            onPlayClicked = {
                                viewModel.onEvent(HomeScreenUiEvents.TogglePlay(it))
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                is Resource.Error -> {
                    item {
                        Text(text = uiState.songs.message)
                    }
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