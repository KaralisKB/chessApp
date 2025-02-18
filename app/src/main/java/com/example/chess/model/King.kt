package com.example.chess.model

import kotlin.time.Duration.Companion.seconds

class King(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.KING
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0

    override fun getPossibleMoves(boardState: BoardState): List<Position> {
        val possibleMoves: MutableList<Position> = mutableListOf()

        val potentialMoves = getPotentialMoves(boardState)

        var allEnemyMoves: MutableList<Position> = mutableListOf<Position>()


        for (piece in boardState.board.flatten()) {
            if (piece != null && piece.color != color && piece.type != PieceType.KING) {
                val moves = piece.getPossibleMoves(boardState)
                if (moves != null) {
                    allEnemyMoves.addAll(moves)
                }
            }
        }

        println(allEnemyMoves)

        for (move in potentialMoves) {
            val isValid = isMoveValid(move, boardState)

            if (allEnemyMoves.contains(Position(move.first, move.second, FieldState.VALID)) && isValid != 0 ) {
                possibleMoves.add(Position(move.first, move.second, FieldState.BLOCKED))
            } else {
                when (isValid) {
                    1 -> possibleMoves.add(Position(move.first, move.second, FieldState.VALID))
                    2 -> possibleMoves.add(Position(move.first, move.second, FieldState.ATTACK))
                    0 -> possibleMoves.add(Position(move.first, move.second, FieldState.EMPTY))
                }
            }
        }
        return possibleMoves
    }

    override fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>> {
        val potentialMoves = mutableListOf<Pair<Int, Int>>()

        potentialMoves.addAll( listOf(
            Pair(position.row + 1, position.col),
            Pair(position.row + 1, position.col + 1),
            Pair(position.row, position.col + 1),
            Pair(position.row - 1, position.col + 1),
            Pair(position.row - 1, position.col),
            Pair(position.row - 1, position.col - 1),
            Pair(position.row, position.col - 1),
            Pair(position.row + 1, position.col - 1)
            )
        )
        return potentialMoves
    }

    override fun isMoveValid(to: Pair<Int, Int>, boardState: BoardState): Int {
        val row = to.first
        val col = to.second

        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return 0
        }

        val targetPiece = boardState.board[row][col]


        return when {
            targetPiece == null -> 1
            targetPiece.color != color -> 2
            else -> 0
        }

    }
}