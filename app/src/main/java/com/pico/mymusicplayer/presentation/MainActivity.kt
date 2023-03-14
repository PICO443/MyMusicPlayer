package com.pico.mymusicplayer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pico.mymusicplayer.common.Constants
import com.pico.mymusicplayer.presentation.home.HomeScreen
import com.pico.mymusicplayer.presentation.player.PlayerScreen
import com.pico.mymusicplayer.ui.theme.MyMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyMusicPlayerTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screens.HomeScreen.route
                    ) {
                        composable(Screens.HomeScreen.route) {
                            HomeScreen(onSongClicked = { navController.navigate(Screens.PlayerScreen.route + "/${it}") })
                        }
                        composable(Screens.PlayerScreen.route + "/{${Constants.PARAM_SONG_ID}}") {
                            PlayerScreen()
                        }
                    }
                }
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MyMusicPlayerTheme {
//        HomeScreen()
//    }
//}