package com.example.chess.domain.repository.impl

import com.example.chess.data.db.dao.ActionDao
import com.example.chess.data.mapper.toEntity
import com.example.chess.data.mapper.toAction
import com.example.chess.domain.repository.GameRepo
import com.example.chess.local.model.Action
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val dao: ActionDao
) : GameRepo {
    override suspend fun saveAction(action: Action) = dao.insertAction(action.toEntity())


    override fun getAllActions(): Flow<List<Action>> =
        dao.getAllActions().map { entityList -> entityList.map { it.toAction() } }

    override suspend fun clearActions() = dao.clearActions()
}