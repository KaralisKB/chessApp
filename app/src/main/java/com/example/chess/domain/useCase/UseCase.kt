package com.example.chess.domain.useCase

abstract class UseCase<R> {
    abstract suspend fun execute(): R
}