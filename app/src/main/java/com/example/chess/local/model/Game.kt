package com.example.chess.local.model

data class Game (
    val gameId: Long,
    val whiteName: String,
    val blackName: String,
    val winnerName: String?,
    val gameType: GameType,
    val whiteTimeRemaining: Long,
    val blackTimeRemaining: Long,
    val date: Long
)