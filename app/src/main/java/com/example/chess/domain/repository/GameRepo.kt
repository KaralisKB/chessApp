package com.example.chess.domain.repository

import com.example.chess.data.db.entity.GameEntity
import com.example.chess.local.model.Action
import com.example.chess.local.model.Game
import kotlinx.coroutines.flow.Flow

interface GameRepo {
    /**
     * saves action log of the game to the database
     *
     * @param action is instance of [Action], which keeps data about user's movement
     */
    suspend fun saveAction(action: Action)

    fun getAllActions(): Flow<List<Action>>

    suspend fun clearActions()


    suspend fun createGame(game: GameEntity): Long

    suspend fun endGame(id: Long, winner: String, whiteTimeRemaining: Long, blackTimeRemaining: Long)

    fun getAllGames(): Flow<List<Game>>

    suspend fun deleteGame(game: Game)
}