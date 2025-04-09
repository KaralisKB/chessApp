package com.example.chess.local.model

data class Game (
    val gameId: Int,
    val whiteName: String,
    val blackName: String,
    val winnerName: String,
    val timeRemaining: Long?,
    val date: Long
)