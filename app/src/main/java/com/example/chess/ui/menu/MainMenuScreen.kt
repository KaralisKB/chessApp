package com.example.chess.ui.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.chess.domain.useCase.game.CreateGameUseCase
import kotlinx.coroutines.launch

@Composable
fun MainMenuScreen(
    navController: NavController
) {
    val createGame = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Chess", style = MaterialTheme.typography.headlineLarge)

        Button(onClick = {
            createGame.launch {
                val gameId =
            }
        }) {
            Text("Start Game")
        }

        Button(onClick = { navController.navigate("history") }) {
            Text("Game History")
        }
    }
}