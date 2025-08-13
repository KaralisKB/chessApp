package com.example.chess.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chess.data.db.entity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGame(game: GameEntity): Long

    @Query("UPDATE games SET winnerName = :winner, whiteTimeRemaining = :whiteTimeRemaining, blackTimeRemaining = :blackTimeRemaining WHERE gameId = :id")
    suspend fun endGame(id: Long, winner: String, whiteTimeRemaining: Long, blackTimeRemaining: Long)

    @Query("Select * FROM games")
    fun getAllGames(): Flow<List<GameEntity>>

    @Delete
    suspend fun deleteGame(game: GameEntity)

}