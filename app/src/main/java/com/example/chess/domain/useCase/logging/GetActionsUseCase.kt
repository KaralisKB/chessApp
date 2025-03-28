package com.example.chess.domain.useCase.logging

import com.example.chess.domain.repository.GameRepo
import com.example.chess.domain.useCase.FlowUseCase
import com.example.chess.local.model.Action
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActionsUseCase @Inject constructor(
    private val repository: GameRepo
): FlowUseCase<List<Action>>() {

    override fun execute(): Flow<List<Action>> = repository.getAllActions()
}