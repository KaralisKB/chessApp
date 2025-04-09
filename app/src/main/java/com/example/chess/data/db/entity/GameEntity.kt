package com.example.chess.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "games")
data class GameEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val gameId: Int,
    val whiteName: String,
    val blackName: String,
    val winnerName: String,
    val timeRemaining: Long?,
    val date: Long,
)