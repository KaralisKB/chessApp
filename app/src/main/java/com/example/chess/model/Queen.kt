package com.example.chess.model

class Queen(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.QUEEN
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0

    override fun getPossibleMoves(boardState: BoardState): Pair<List<Position>?, List<Position>?> {
        TODO("Not yet implemented")
    }

    override fun isMoveValid(to: Pair<Int, Int>, boardState: BoardState): Int {
        TODO("Not yet implemented")
    }

    override fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>> {
        TODO("Not yet implemented")
    }
}