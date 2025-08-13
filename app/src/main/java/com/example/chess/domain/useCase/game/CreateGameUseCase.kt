package com.example.chess.domain.useCase.game

import com.example.chess.data.db.entity.GameEntity
import com.example.chess.domain.repository.GameRepo
import javax.inject.Inject

class CreateGameUseCase @Inject constructor(
    private val repo: GameRepo
) {
    suspend fun execute(game: GameEntity): Long {
        return repo.createGame(game)
    }

}