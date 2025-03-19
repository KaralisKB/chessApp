package com.example.chess.ui.components

import com.example.chess.local.model.Position
import com.example.chess.local.model.BoardState

enum class PieceType {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
}

enum class PieceColor {
    WHITE, BLACK
}

interface ChessPiece {
    var inCheck: Boolean
    val type: PieceType
    val color: PieceColor
    var position: Position
    var isCaptured: Boolean
    suspend fun getPossibleMoves(boardState: BoardState, skippedPosition: Position? = null, king: ChessPiece? = null): List<Position>?
    suspend fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>>
    fun getMovementType(to: Pair<Int,Int>, boardState: BoardState): Int
    suspend fun getEnemyMoves(boardState: BoardState): MutableSet<Position>
    fun getImage(): Int
    var movesMade: Int
}