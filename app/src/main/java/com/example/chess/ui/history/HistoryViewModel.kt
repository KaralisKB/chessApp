package com.example.chess.ui.history

import com.example.chess.domain.useCase.game.GetGamesUseCase
import com.example.chess.local.model.Action
import com.example.chess.local.model.Game
import com.example.chess.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    getGamesUseCase: GetGamesUseCase
): BaseViewModel(Dispatchers.Default)  {

    val gamesList: StateFlow<List<Game>> =
        getGamesUseCase.execute().toStateFlow(initial = listOf())


}