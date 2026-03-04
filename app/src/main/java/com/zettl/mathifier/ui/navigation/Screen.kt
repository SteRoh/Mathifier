package com.zettl.mathifier.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Settings : Screen("settings")
    data object Practice : Screen("practice")
    data object Progress : Screen("progress")
}
