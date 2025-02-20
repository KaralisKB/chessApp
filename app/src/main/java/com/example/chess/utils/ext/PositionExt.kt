package com.example.chess.utils.ext

import com.example.chess.model.Position

fun Position.isValidMove(move: Pair<Int, Int>): Boolean = row == move.first && col == move.second