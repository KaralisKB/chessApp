package com.example.chess.domain.useCase

import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<R> {
    abstract fun execute(): Flow<R>
}