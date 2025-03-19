package com.example.chess.domain.useCase

abstract class UseCaseParams<P, R> {
    abstract suspend fun execute(data: P): R
}