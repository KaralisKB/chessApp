package com.example.chess.domain.useCase.logging

import com.example.chess.domain.repository.GameRepo
import com.example.chess.local.model.Action
import javax.inject.Inject

class SaveActionUseCase @Inject constructor(
    private val repository: GameRepo
){
    suspend operator fun invoke(action: Action) = repository.saveAction(action)
}