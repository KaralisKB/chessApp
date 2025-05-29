package com.example.chess.ui.navigation

import com.example.chess.local.model.GameType
import com.example.chess.ui.components.ChessPiece
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen() {

    @Serializable
    data object MainMenu : Screen()
    @Serializable
    data class Game(val player1: String, val player2: String, val gameType: GameType, val gameId: Long) : Screen()
    @Serializable
    data object GameHistory : Screen()
}