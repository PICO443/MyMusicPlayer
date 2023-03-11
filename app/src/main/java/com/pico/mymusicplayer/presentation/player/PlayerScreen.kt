package com.pico.mymusicplayer.presentation.player

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pico.mymusicplayer.presentation.player.components.PlayerControllersSection
import com.pico.mymusicplayer.presentation.player.components.PlayerSongInfoSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen() {
    Scaffold(
        topBar = { PlayerTopAppBar() }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            PlayerSongInfoSection()
            Spacer(modifier = Modifier.height(32.dp))
            PlayerControllersSection()
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