package com.example.chess.model

import com.example.chess.R
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType
import com.example.chess.utils.ext.isValidMove

class Knight(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.KNIGHT
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0
    override fun getEnemyMoves(state: BoardState): MutableSet<Position> {
        TODO("Not yet implemented")
    }
    override var inCheck: Boolean = false

    override fun getPossibleMoves(boardState: BoardState, skippedPosition: Position?): List<Position> {

        val possibleMoves: MutableList<Position> = mutableListOf()

        val potentialMoves = getPotentialMoves(boardState)

        for (move in potentialMoves) {
            if (skippedPosition != null && skippedPosition.isValidMove(move)) possibleMoves.add(Position(move.first, move.second, FieldState.VALID))
            val isValid = getMovementType(move, boardState)
            if (isValid == 1) {
                possibleMoves.add(Position(move.first, move.second, FieldState.VALID))
            } else if (isValid == 2) {
                possibleMoves.add(Position(move.first, move.second, FieldState.ATTACK))
            }
        }
        return possibleMoves
    }

    override fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>> {
        val potentialMoves = mutableListOf<Pair<Int, Int>>()

        // "Up" moves
        potentialMoves.add(Pair(position.row + 2, position.col - 1))
        potentialMoves.add(Pair(position.row + 2, position.col + 1))

        //"Right" moves
        potentialMoves.add(Pair(position.row + 1, position.col + 2))
        potentialMoves.add(Pair(position.row - 1, position.col + 2))

        //"Down" moves
        potentialMoves.add(Pair(position.row - 2, position.col - 1))
        potentialMoves.add(Pair(position.row - 2, position.col + 1))

        //"Left" moves
        potentialMoves.add(Pair(position.row + 1, position.col - 2))
        potentialMoves.add(Pair(position.row - 1, position.col - 2))

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
            } else {
                return 0
            }
        } else {
            return 1
        }
    }

    override fun getImage(): Int {
        if (color == PieceColor.WHITE) return R.drawable.chess_nlt60 else return R.drawable.chess_ndt60
    }
}