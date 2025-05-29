package com.example.chess.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.chess.local.model.Game
import com.example.chess.ui.board.BoardScreen
import com.example.chess.ui.board.GameViewModel
import com.example.chess.ui.history.HistoryScreen
import com.example.chess.ui.menu.MainMenuScreen

@Composable
fun NavigationHost(
    navController: NavHostController,
    startDestination: Screen = Screen.MainMenu
    ){
    NavHost(navController = navController, startDestination = startDestination) {
        composable<Screen.MainMenu>(
            enterTransition = { return@composable slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
            )},
            exitTransition = { return@composable slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
            )},
            popEnterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }
        ) {
            MainMenuScreen(navController, navigateToGame = { player1, player2, gameType, gameId ->
                val gameScreen = Screen.Game(player1, player2, gameType, gameId)
                navController.navigate(gameScreen)
            }
            )
        }
        composable<Screen.Game> { backStackEntry ->
            val screen = backStackEntry.toRoute<Screen.Game>()
            BoardScreen(
                navController = navController,
                player1 = screen.player1,
                player2 = screen.player2,
                gameType = screen.gameType,
                gameId = screen.gameId
            )
        }
        composable<Screen.GameHistory>(
            enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up, tween(700)
                )
            },
            exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            },
            popExitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down, tween(700)
                )
            }

        ) {
            HistoryScreen(navController)

        }
    }
}