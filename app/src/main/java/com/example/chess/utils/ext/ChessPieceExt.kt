package com.example.chess.utils.ext

import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor

fun ChessPiece?.isWhite(): Boolean = this?.color?.equals(PieceColor.WHITE) == true