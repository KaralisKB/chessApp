package com.example.chess.data.mapper

import com.example.chess.data.db.entity.GameEntity
import com.example.chess.local.model.Game

fun Game.toEntity(): GameEntity {
    return GameEntity(
        gameId = gameId,
        whiteName = whiteName,
        blackName = blackName,
        winnerName = winnerName,
        gameType = gameType,
        whiteTimeRemaining = whiteTimeRemaining,
        blackTimeRemaining = blackTimeRemaining,
        date = date
    )
}

fun GameEntity.toGame(): Game {
    return Game(
        gameId = gameId,
        whiteName = whiteName,
        blackName = blackName,
        winnerName = winnerName,
        gameType = gameType,
        whiteTimeRemaining = whiteTimeRemaining ?: 0L,
        blackTimeRemaining = blackTimeRemaining ?: 0L,
        date = date
    )
}