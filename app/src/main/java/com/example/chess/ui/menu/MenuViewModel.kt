package com.example.chess.ui.menu

import com.example.chess.data.db.entity.GameEntity
import com.example.chess.domain.useCase.game.CreateGameUseCase
import com.example.chess.local.model.GameType
import com.example.chess.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@HiltViewModel
class MenuViewModel @Inject constructor(
    private val createGameUseCase: CreateGameUseCase
) : BaseViewModel(Dispatchers.Default) {

    suspend fun createGame(player1: String, player2: String, gameType: GameType): Long {
        val game = GameEntity(
            gameId = 0L,
            whiteName = player1,
            blackName = player2,
            winnerName = null,
            gameType = gameType,
            whiteTimeRemaining = null,
            blackTimeRemaining = null,
            date = System.currentTimeMillis()
        )
        return createGameUseCase.execute(game)
    }
}