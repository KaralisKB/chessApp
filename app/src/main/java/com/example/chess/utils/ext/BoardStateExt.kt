package com.example.chess.utils.ext

import com.example.chess.local.model.BoardState

fun BoardState.isCastleValid(row: Int, col: Int, isLong: Boolean): Boolean =
    board[row][col - 1] == null && board[row][col] == null && (board[row][col + 1] == null || !isLong)
