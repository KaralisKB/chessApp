package com.example.chess.local.model

import com.example.chess.R
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType


class Pawn(override val color: PieceColor, startPosition: Position): ChessPiece {
    override val type: PieceType = PieceType.PAWN
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0
    override fun getEnemyMoves(boardState: BoardState): MutableSet<Position> {
        TODO("Not yet implemented")
    }
    override var inCheck: Boolean = false

    override fun getPossibleMoves(boardState: BoardState, skippedPosition: Position?): List<Position>? {
        val possibleMoves: MutableList<Position> = mutableListOf()

        val potentialMoves = getPotentialMoves(boardState)

        for (move in potentialMoves) {
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
        val direction = if (color == PieceColor.WHITE) 1 else -1

        // Forward moves
        potentialMoves.add(Pair(position.row + direction, position.col))
        if (movesMade == 0) {
            potentialMoves.add(Pair(position.row + 2 * direction, position.col))
        }

        // Diagonal attacks
        potentialMoves.add(Pair(position.row + direction, position.col + 1))
        potentialMoves.add(Pair(position.row + direction, position.col - 1))

        return potentialMoves
    }

    fun getPotentialAttackMoves(): List<Position> {
        val potentialAttackMoves = mutableListOf<Position>()
        val direction = if (color == PieceColor.WHITE) 1 else -1

        potentialAttackMoves.add(Position(position.row + direction, position.col + 1, FieldState.ATTACK))
        potentialAttackMoves.add(Position(position.row + direction, position.col - 1, FieldState.ATTACK))



        return potentialAttackMoves
    }

    // 0: invalid, 1: valid move, 2: valid attack
    override fun getMovementType(to: Pair<Int, Int>, boardState: BoardState): Int {
        val row = to.first
        val col = to.second

        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return 0
        }

        val targetPiece = boardState.board[row][col]

        val direction = if (color == PieceColor.WHITE) 1 else -1

        if (to.second == position.col) {
            if (targetPiece == null) {
                if (to.first == position.row + direction) {
                    return 1
                } else if (movesMade == 0 && to.first == position.row + 2 * direction) {

                    val intermediateRow = position.row + direction
                    if (boardState.board[intermediateRow][col] == null) {
                        return 1
                    }
                }
            } 
            return 0
        } else {
            if (targetPiece!= null && targetPiece.color!= color) {
                if (to.first == position.row + direction && (to.second == position.col + 1 || to.second == position.col - 1)) {
                    return 2
                }
            }
            return 0
        }
    }

    fun getAttackMoves(boardState: BoardState): List<Position> = getPotentialMoves(boardState)
            .fold<Pair<Int, Int>, MutableSet<Position>>(mutableSetOf()) { acc, move ->
                val isValid = getMovementType(move, boardState)
                if (isValid == 2) acc.add(Position(move, FieldState.ATTACK))

                acc
            }.toList()

    override fun getImage(): Int {
        if (color == PieceColor.WHITE) return R.drawable.chess_plt60 else return R.drawable.chess_pdt60
    }
}