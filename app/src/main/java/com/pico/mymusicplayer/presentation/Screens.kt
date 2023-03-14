package com.pico.mymusicplayer.presentation

sealed class Screens(val route: String) {
    object HomeScreen: Screens("home_screen")
    object PlayerScreen: Screens("player_screen")
}
