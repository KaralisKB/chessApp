package com.example.chess.model

enum class PieceType {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
}

enum class PieceColor {
    WHITE, BLACK
}

interface ChessPiece {
    val type: PieceType
    val color: PieceColor
    var position: Position
    var isCaptured: Boolean
    fun getPossibleMoves(boardState: BoardState): List<Position>?
    fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>>
    fun isMoveValid(to: Pair<Int,Int>, boardState: BoardState): Int
    var movesMade: Int

}