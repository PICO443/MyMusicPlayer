package com.pico.mymusicplayer.presentation.home

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pico.mymusicplayer.R
import com.pico.mymusicplayer.domain.model.Song
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
            items(uiState.songs) { song ->
                SongListItem(
                    song,
                    onSongClicked = {
//                        viewModel.onEvent(HomeScreenUiEvents.SelectSong(it))
                        onSongClicked(song.id.toString())
                    },
                    isCurrentlyPlaying = (uiState.currentlyPlayingSong?.id == song.id),
                    onPlayClicked = {
                        viewModel.onEvent(HomeScreenUiEvents.SelectSong(it))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    TopAppBar(title = { Text(text = stringResource(R.string.songs)) })
}