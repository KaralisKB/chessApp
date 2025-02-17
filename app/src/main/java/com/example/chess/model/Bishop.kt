package com.example.chess.model

class Bishop(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.BISHOP
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0

    override fun getPossibleMoves(boardState: BoardState): Pair<List<Position>?, List<Position>?> {
        val possibleMoves: Pair<MutableList<Position>?, MutableList<Position>?> =
            Pair(mutableListOf(), mutableListOf())

        val potentialMoves = getPotentialMoves(boardState)

        for (set in 1..4) {
            var sublist: List<Pair<Int, Int>>? = null
            when (set) {
                1 -> sublist = potentialMoves.subList(0, 7)
                2 -> sublist = potentialMoves.subList(8, 15)
                3 -> sublist = potentialMoves.subList(16, 23)
                4 -> sublist = potentialMoves.subList(24, 31)
            }

            if (sublist!= null) {
                for (move in sublist) {
                    val isValid = isMoveValid(move, boardState)
                    if (isValid == 1) {
                        possibleMoves.first?.add(Position(move.first, move.second))
                    } else if (isValid == 2) {
                        possibleMoves.second?.add(Position(move.first, move.second))
                        break
                    } else if (isValid == 0) {
                        break
                    }
                }
            }
        }
        return possibleMoves
    }

    override fun isMoveValid(to: Pair<Int, Int>, boardState: BoardState): Int {

    }

    override fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>> {
        TODO("Not yet implemented")
    }
}