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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "song_screen",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("song_screen") {
                SongScreen(navController)
            }
            composable("singer_screen") {
                SingerScreen(navController)
            }
        }
    }
}

@Composable
fun SongScreen(navController: NavController) {
    Column {
        Text("노래 화면")
        Button(
            onClick = {
                navController.navigate("singer_screen") {
                    // 이미 화면이 스택에 있다면 새로 만들지 않고 기존 화면으로 이동
                    launchSingleTop = true

                    // 내비게이션 그래프의 시작 지점까지의 모든 화면을 스택에서 제거
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
        ) {
            Text("가수 화면으로 이동")
        }
    }
}

@Composable
fun SingerScreen(navController: NavController) {
    Column {
        Text("가수 화면")
        Button(
            onClick = {
                navController.navigate("song_screen") {
                    launchSingleTop = true

                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
        ) {
            Text("노래 화면으로 이동")
        }
    }
}
