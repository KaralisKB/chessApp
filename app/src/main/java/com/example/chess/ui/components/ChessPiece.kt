package com.example.chess.ui.components

import com.example.chess.model.BoardState
import com.example.chess.model.Position

enum class PieceType {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
}

enum class PieceColor {
    WHITE, BLACK
}

interface ChessPiece {
    abstract var inCheck: Boolean
    val type: PieceType
    val color: PieceColor
    var position: Position
    var isCaptured: Boolean
    fun getPossibleMoves(boardState: BoardState, skippedPosition: Position?): List<Position>?
    fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>>
    fun getMovementType(to: Pair<Int,Int>, boardState: BoardState): Int
    fun getEnemyMoves(boardState: BoardState): MutableSet<Position>
    fun getImage(): Int
    var movesMade: Int
}