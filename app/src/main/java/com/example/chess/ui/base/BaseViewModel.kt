package com.example.chess.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    fun <T> ioToUi(io: suspend () -> T, ui: suspend (T) -> Unit) {
        viewModelScope.launch {
            val res = withContext(ioDispatcher) { io() }

            withContext(mainDispatcher) { ui(res) }
        }
    }

    fun ioToUnit(io: suspend () -> Unit) {
        viewModelScope.launch {
            withContext(ioDispatcher) { io() }
        }
    }

    fun <T> Flow<T>.toStateFlow(
        sharingIn: SharingStarted = SharingStarted.Eagerly,
        initial: T
    ): StateFlow<T> = this.stateIn(viewModelScope, sharingIn, initial)
}