package com.example.chess.utils

import com.example.chess.local.model.Bishop
import com.example.chess.ui.components.ChessPiece
import com.example.chess.local.model.FieldState
import com.example.chess.local.model.Knight
import com.example.chess.ui.components.PieceColor
import com.example.chess.local.model.Position
import com.example.chess.local.model.Queen
import com.example.chess.local.model.Rook

object Constants {
    fun getWhitePromotionPieces(): List<ChessPiece> =
        listOf(
            Queen(PieceColor.WHITE, Position(0, 0, FieldState.EMPTY)),
            Rook(PieceColor.WHITE, Position(0, 0, FieldState.EMPTY)),
            Bishop(PieceColor.WHITE, Position(0, 0, FieldState.EMPTY)),
            Knight(PieceColor.WHITE, Position(0, 0, FieldState.EMPTY))
        )

    fun getBlackPromotionPieces(): List<ChessPiece> =
        listOf(
            Queen(PieceColor.BLACK, Position(0, 0, FieldState.EMPTY)),
            Rook(PieceColor.BLACK, Position(0, 0, FieldState.EMPTY)),
            Bishop(PieceColor.BLACK, Position(0, 0, FieldState.EMPTY)),
            Knight(PieceColor.BLACK, Position(0, 0, FieldState.EMPTY))
        )
}