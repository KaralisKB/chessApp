package com.example.chess.local.model

import com.example.chess.R
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType
import com.example.chess.utils.ext.isValidMove

class Queen(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.QUEEN
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0
    override suspend fun getEnemyMoves(boardState: BoardState): MutableSet<Position> {
        TODO("Not yet implemented")
    }
    override var inCheck: Boolean = false

    override suspend fun getPossibleMoves(
        boardState: BoardState,
        skippedPosition: Position?,
        king: ChessPiece?
    ): List<Position> {
        val possibleMoves: MutableList<Position> = mutableListOf()

        val potentialMoves = getPotentialMoves(boardState)

        for (set in 1..8) {
            var flag = false
            val subList = when (set) {
                1 -> potentialMoves.subList(0, 7)
                2 -> potentialMoves.subList(8, 15)
                3 -> potentialMoves.subList(16, 23)
                4 -> potentialMoves.subList(24, 31)
                5 -> potentialMoves.subList(32, 39)
                6 -> potentialMoves.subList(40, 47)
                7 -> potentialMoves.subList(48, 55)
                8 -> potentialMoves.subList(56, 63)
                else -> throw Exception("Out of scope ")
            }

            subList.fold(mutableListOf<Position>()) { acc, move ->
                if (!flag) {
                    val isValid = getMovementType(move, boardState)
                    when {
                        skippedPosition != null && skippedPosition.row == move.first && skippedPosition.col == move.second -> {
                            possibleMoves.add(Position(move, FieldState.VALID))
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

    override suspend fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>> {
        val potentialMoves = mutableListOf<Pair<Int, Int>>()

        for (direction in 1..8) {
            when (direction) {
                1 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row + square, position.col))
                }

                2 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row + square, position.col + square))
                }

                3 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row, position.col + square))
                }

                4 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row - square, position.col + square))
                }

                5 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row - square, position.col))
                }

                6 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row - square, position.col - square))
                }

                7 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row, position.col - square))
                }

                8 -> for (square in 1..8) {
                    potentialMoves.add(Pair(position.row + square, position.col - square))
                }
            }
        }
        return potentialMoves
    }

    override fun getMovementType(to: Pair<Int, Int>, boardState: BoardState): Int {
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

    override fun getImage(): Int {
        if (color == PieceColor.WHITE) return R.drawable.chess_qlt60 else return R.drawable.chess_qdt60
    }
}