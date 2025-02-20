package com.example.chess.model

import com.example.chess.utils.ext.isValidMove

class Bishop(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.BISHOP
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0

    override fun getPossibleMoves(
        boardState: BoardState,
        skippedPosition: Position?
    ): List<Position>? {
        val possibleMoves: MutableList<Position> = mutableListOf()

        val potentialMoves = getPotentialMoves(boardState)

        for (set in 1..4) {
            var flag = false
            val subList = when (set) {
                1 -> potentialMoves.subList(0, 7)
                2 -> potentialMoves.subList(8, 15)
                3 -> potentialMoves.subList(16, 23)
                4 -> potentialMoves.subList(24, 31)
                else -> throw Exception("u r too stupid")
            }

            subList.fold(mutableListOf<Position>()) { acc, move ->
                if (!flag) {
                    val isValid = isMoveValid(move, boardState)
                    when {
                        skippedPosition != null && skippedPosition.isValidMove(move) -> {
                            possibleMoves.add(Position(move, FieldState.VALID))
                            flag = true
                        }
                        isValid == 1 -> possibleMoves.add(Position(move, FieldState.VALID))
                        isValid == 2 -> {
                            possibleMoves.add(Position(move, FieldState.ATTACK))
                            flag = true
                        }

                        isValid == 0 -> flag = true
                    }
                }
                acc
            }
        }
        return possibleMoves
    }

    override fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>> {
        val potentialMoves = mutableListOf<Pair<Int, Int>>()

        for (direction in 1..4) {
            when (direction) {
                1 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row + square, position.col + square))
                }

                2 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row - square, position.col + square))
                }

                3 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row - square, position.col - square))
                }

                4 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row + square, position.col - square))
                }
            }
        }
        return potentialMoves
    }

    override fun isMoveValid(to: Pair<Int, Int>, boardState: BoardState): Int {
        val row = to.first
        val col = to.second

        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return 0
        }

        val targetPiece = boardState.board[row][col]

        if (targetPiece != null) {
            if (targetPiece.color != color) {
                return 2
            }
        } else {
            return 1
        }
        return 0
    }


}