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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerSheet(scope, drawerState, navController)
        },
        gesturesEnabled = true,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopBar(scope, drawerState)
            },
            bottomBar = {
                BottomBar(navController) {
                    navigateAndClearStack(navController, it)
                }
            }
        ) { innerPadding ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    CenterAlignedTopAppBar(
        title = { Text("네비게이션") },
        navigationIcon = {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "메뉴 아이콘"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Composable
fun BottomBar(
    navController: NavHostController,
    onNavigate: (String) -> Unit
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar {
        NavigationBarItem(
            label = {
                Text("노래")
            },
            icon = {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "노래 아이콘"
                )
            },
            selected = currentDestination?.route == SONG_SCREEN,
            onClick = {
                onNavigate(SONG_SCREEN)
            }
        )
        NavigationBarItem(
            label = {
                Text("가수")
            },
            icon = {
                Icon(
                    Icons.Default.Face,
                    contentDescription = "가수 아이콘"
                )
            },
            selected = currentDestination?.route == SINGER_SCREEN,
            onClick = {
                onNavigate(SINGER_SCREEN)
            }
        )
    }
}