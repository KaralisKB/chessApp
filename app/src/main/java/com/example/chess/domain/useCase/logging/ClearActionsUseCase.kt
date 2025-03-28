package com.example.chess.domain.useCase.logging

import com.example.chess.domain.repository.GameRepo
import com.example.chess.domain.useCase.UseCase
import javax.inject.Inject

class ClearActionsUseCase @Inject constructor(
    private val repo: GameRepo
): UseCase<Unit>() {

    override suspend fun execute() = repo.clearActions()
}