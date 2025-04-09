package com.example.chess.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chess.ui.board.BoardScreen
import com.example.chess.ui.board.BoardViewModel
import com.example.chess.ui.history.HistoryScreen
import com.example.chess.ui.menu.MainMenuScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    startDestination: String = Screen.MainMenu.route
    ) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.MainMenu.route) {
            MainMenuScreen(navController)
        }
        composable(Screen.Game.route) {
            BoardScreen(navController)
        }
        composable(Screen.GameHistory.route) {
            HistoryScreen(navController)
        }
    }
}