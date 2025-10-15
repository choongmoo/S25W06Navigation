package kr.ac.kumoh.s20171268.s25w06navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kr.ac.kumoh.s20171268.s25w06navigation.Destinations.SINGER_SCREEN
import kr.ac.kumoh.s20171268.s25w06navigation.Destinations.SONG_SCREEN
import kr.ac.kumoh.s20171268.s25w06navigation.ui.theme.S25W06NavigationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            S25W06NavigationTheme {
                MainScreen()
            }
        }
    }
}

object Destinations {
    const val SONG_SCREEN = "song_screen"
    const val SINGER_SCREEN = "singer_screen"
}

private fun navigateAndClearStack(
    navController: NavHostController,
    route: String
) {
    navController.navigate(route) {
        launchSingleTop = true

        popUpTo(navController.graph.findStartDestination().id) {
            inclusive = true
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SONG_SCREEN,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(SONG_SCREEN) {
                SongScreen {
                    navigateAndClearStack(navController, SINGER_SCREEN)
                }
            }
            composable(SINGER_SCREEN) {
                SingerScreen {
                    navigateAndClearStack(navController, SONG_SCREEN)
                }
            }
        }
    }
}

@Composable
fun SongScreen(onNavigateToSinger: () -> Unit) {
    Column {
        Text("노래 화면")
        Button(
            onClick = onNavigateToSinger
        ) {
            Text("가수 화면으로 이동")
        }
    }
}

@Composable
fun SingerScreen(onNavigateToSong: () -> Unit) {
    Column {
        Text("가수 화면")
        Button(
            onClick = onNavigateToSong
        ) {
            Text("노래 화면으로 이동")
        }
    }
}
