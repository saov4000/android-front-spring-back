package com.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.music.presentation.screens.CreateMusicScreen
import com.music.presentation.screens.DetailMusicScreen
import com.music.presentation.screens.EditMusicScreen
import com.music.presentation.screens.MusicListScreen
import com.music.presentation.theme.MusicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppTheme {
                MusicAppNavigation()
            }
        }
    }
}

@Composable
fun MusicAppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "music_list"
    ) {

        composable("music_list") {
            MusicListScreen(
                onNavigateToDetail = { musicId ->
                    navController.navigate("music_detail/$musicId")
                },
                onNavigateToCreate = {
                    navController.navigate("music_create")
                }
            )
        }

        composable(
            route = "music_detail/{musicId}",
            arguments = listOf(
                navArgument("musicId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val musicId = backStackEntry.arguments?.getString("musicId") ?: ""
            DetailMusicScreen(
                musicId = musicId,
                onDelete = { deletedMusicId ->
                    navController.popBackStack()
                },
                onEdit = { music ->
                    navController.navigate("music_edit/${music.id}")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("music_create") {
            CreateMusicScreen(
                onMusicCreated = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }

        composable(
            route = "music_edit/{musicId}",
            arguments = listOf(
                navArgument("musicId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val musicId = backStackEntry.arguments?.getString("musicId") ?: ""
            EditMusicScreen(
                musicId = musicId,
                onMusicUpdated = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}