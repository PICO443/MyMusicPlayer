package com.pico.mymusicplayer.presentation.player

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pico.mymusicplayer.domain.model.Song
import com.pico.mymusicplayer.presentation.player.components.PlayerControllersSection
import com.pico.mymusicplayer.presentation.player.components.PlayerSongInfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(viewModel: PlayerViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState
    val songPosition by uiState.currentPosition.collectAsState(initial = 5000)
    Scaffold(
        topBar = { PlayerTopAppBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            PlayerSongInfoSection(
                song = uiState.currentSong ?: Song(
                    id = 3,
                    name = "sss",
                    artist = "somm",
                    image = null,
                    uri = null,
                    duration = 0
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            PlayerControllersSection(onPlayClicked = {
                viewModel.onEvent(PlayerUiEvents.TogglePlay)
            },
                isPlaying = uiState.isPlaying, duration = uiState.currentPosition.collectAsState(initial = 5000) , songDuration = uiState.currentSong?.duration ?: -1,
                onSliderValueChange = {viewModel.onEvent(PlayerUiEvents.SeekTo(it)); Log.d("Slider", "${it}")}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerTopAppBar() {
    TopAppBar(
        modifier = Modifier.padding(4.dp),
        navigationIcon = {
            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }, title = {}, actions = {
            IconButton(
                modifier = Modifier.size(48.dp),
                onClick = { /*TODO*/ }
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }
        })
}