package kr.ac.kumoh.s20171268.s25w06navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    // NOTE: Scaffold 안에 있는 navController 주석 처리할 것
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerSheet(scope, drawerState, navController)
        },
        gesturesEnabled = true,
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // val navController = rememberNavController()

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

@Composable
fun DrawerSheet(
    scope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    ModalDrawerSheet {
        NavigationDrawerItem(
            label = { Text("노래 화면") },
            selected = currentDestination?.route == SONG_SCREEN,
            onClick = {
                scope.launch {
                    drawerState.close()
                }
                navigateAndClearStack(navController, SONG_SCREEN)
            },
            icon = {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "노래 아이콘"
                )
            }
        )
        NavigationDrawerItem(
            label = { Text("가수 화면") },
            selected = currentDestination?.route == SINGER_SCREEN,
            onClick = {
                scope.launch {
                    drawerState.close()
                }
                navigateAndClearStack(navController, SINGER_SCREEN)
            },
            icon = {
                Icon(
                    Icons.Default.Face,
                    contentDescription = "가수 아이콘"
                )
            }
        )
    }
}