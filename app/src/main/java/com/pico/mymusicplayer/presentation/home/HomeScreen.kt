package com.pico.mymusicplayer.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pico.mymusicplayer.R
import com.pico.mymusicplayer.domain.model.Song
import com.pico.mymusicplayer.presentation.home.components.SongListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { HomeTopAppBar() }
    ) {
        LazyColumn(
            contentPadding = it, modifier = Modifier
                .fillMaxSize()
        ) {
            items(12) {
                SongListItem(Song("Song Name", "Artist", null))
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