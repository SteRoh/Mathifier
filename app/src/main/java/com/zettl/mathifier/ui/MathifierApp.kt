package com.zettl.mathifier.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zettl.mathifier.AppContainer
import com.zettl.mathifier.ui.home.HomeScreen
import com.zettl.mathifier.ui.navigation.Screen
import com.zettl.mathifier.ui.progress.ProgressScreen
import com.zettl.mathifier.ui.practice.PracticeScreen
import com.zettl.mathifier.ui.settings.SettingsScreen

@Composable
fun MathifierApp(container: AppContainer) {
    val navController = rememberNavController()
    val currentProfileId by container.preferencesDataSource.currentProfileId.collectAsState(initial = null)

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                container = container,
                currentProfileId = currentProfileId,
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToPractice = { navController.navigate(Screen.Practice.route) },
                onNavigateToProgress = { navController.navigate(Screen.Progress.route) }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                container = container,
                currentProfileId = currentProfileId,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Practice.route) {
            PracticeScreen(
                container = container,
                currentProfileId = currentProfileId,
                onBack = { navController.popBackStack() },
                onSessionComplete = { navController.popBackStack() }
            )
        }
        composable(Screen.Progress.route) {
            ProgressScreen(
                container = container,
                currentProfileId = currentProfileId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
