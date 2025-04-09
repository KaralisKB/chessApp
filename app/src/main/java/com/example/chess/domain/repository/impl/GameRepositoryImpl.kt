package com.example.chess.domain.repository.impl

import com.example.chess.data.db.dao.ActionDao
import com.example.chess.data.db.dao.GameDao
import com.example.chess.data.mapper.toEntity
import com.example.chess.data.mapper.toAction
import com.example.chess.data.mapper.toGame
import com.example.chess.domain.repository.GameRepo
import com.example.chess.local.model.Action
import com.example.chess.local.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val actionDao: ActionDao,
    private val gameDao: GameDao
) : GameRepo {

    override suspend fun saveAction(action: Action) = actionDao.insertAction(action.toEntity())

    override fun getAllActions(): Flow<List<Action>> =
        actionDao.getAllActions().map { entityList -> entityList.map { it.toAction() } }

    override suspend fun clearActions() = actionDao.clearActions()



    override suspend fun createGame(game: Game) = gameDao.insertGame(game.toEntity())

    override fun getAllGames(): Flow<List<Game>> =
        gameDao.getAllGames().map { entityList -> entityList.map { it.toGame() } }

    override suspend fun deleteGame(game: Game) = gameDao.deleteGame(game.toEntity())
}