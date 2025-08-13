package com.example.chess.domain.useCase.game

import com.example.chess.domain.repository.GameRepo
import com.example.chess.domain.useCase.FlowUseCase
import com.example.chess.local.model.Game
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGamesUseCase @Inject constructor(
    private val repo: GameRepo
): FlowUseCase<List<Game>>() {
    override fun execute(): Flow<List<Game>> = repo.getAllGames()
}