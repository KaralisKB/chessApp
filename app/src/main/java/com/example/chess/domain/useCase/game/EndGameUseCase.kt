package com.example.chess.domain.useCase.game

import com.example.chess.domain.repository.GameRepo
import javax.inject.Inject

class EndGameUseCase @Inject constructor(
    private val repo: GameRepo
) {
    suspend fun execute(id: Long, winner: String, whiteTimeRemaining: Long, blackTimeRemaining: Long)
    {
        val res = repo.endGame(id, winner, whiteTimeRemaining, blackTimeRemaining)
        return res
    }
}