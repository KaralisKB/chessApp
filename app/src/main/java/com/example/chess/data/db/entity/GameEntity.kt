package com.example.chess.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chess.local.model.GameType


@Entity(tableName = "games")
data class GameEntity (
    @PrimaryKey(autoGenerate = true)
    val gameId: Long = 0L,
    val whiteName: String,
    val blackName: String,
    val winnerName: String?,
    val gameType: GameType,
    val whiteTimeRemaining: Long?,
    val blackTimeRemaining: Long?,
    val date: Long,
)