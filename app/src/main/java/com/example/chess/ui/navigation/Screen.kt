package com.example.chess.ui.navigation

sealed class Screen(val route: String) {
    object MainMenu : Screen("main_menu")
    object Game : Screen("game")
    object GameHistory : Screen("history")
}