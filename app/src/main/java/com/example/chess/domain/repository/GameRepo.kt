package com.example.chess.domain.repository

import com.example.chess.local.model.Action
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
}