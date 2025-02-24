package com.example.chess.model

import android.util.Log
import com.example.chess.utils.ext.isValidMove

class King(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.KING
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0

    override fun getPossibleMoves(
        boardState: BoardState,
        skippedPosition: Position?
    ): List<Position> {
        val possibleMoves: MutableList<Position> = mutableListOf()
        val potentialMoves = getPotentialMoves(boardState)
        var allEnemyMoves: MutableList<Position> = mutableListOf()

        allEnemyMoves.addAll(
            boardState
                .board
                .asSequence()
                .flatMap { pieces -> pieces.filter { piece -> piece != null && piece.color != color && piece.type != PieceType.KING } }
//                .map { it?.getPossibleMoves(boardState, null) }
//                .filterNotNull()
//                .flatten()
//                .toList())
                .fold(mutableListOf()) { acc, move ->
                    if (move?.type == PieceType.PAWN) {

                        val res = (move as Pawn).getAttackMoves(boardState)
                        acc.addAll(res)
                    }
                    else {
                        val res = move?.getPossibleMoves(boardState, null) ?: emptyList()
                        acc.addAll(res)
                    }

                    acc
                }


                )

        for (move in potentialMoves) {
            val movementType = isMoveValid(move, boardState)

            if (allEnemyMoves.any { position -> position.isValidMove(move) } && movementType != 0)
                possibleMoves.add(Position(move.first, move.second, FieldState.BLOCKED))

            else {
                when (movementType) {
                    1 -> possibleMoves.add(Position(move.first, move.second, FieldState.VALID))
//                    2 -> possibleMoves.add(Position(move.first, move.second, FieldState.ATTACK))
                    2 -> {
                        val newAllEnemyMoves = (
                                boardState
                                    .board
                                    .asSequence()
                                    .flatMap { pieces -> pieces.filter { piece -> piece != null && piece.color != color && piece.type != PieceType.KING } }
                                    .map {
                                        if (it?.type != PieceType.KING) it?.getPossibleMoves(
                                            boardState,
                                            Position(move, FieldState.EMPTY)
                                        ) else emptyList()
                                    }
                                    .toList()
                                    .fold(mutableListOf<Position>(), { acc, list ->
                                        if (list != null) acc.addAll(list)
                                        acc
                                    })
                                )
                        val res = newAllEnemyMoves.any { position -> position.isValidMove(move) }
                        for(piece in boardState.board.flatten().filterNotNull()) {
                            val direction = if(piece.color == PieceColor.WHITE) 1 else -1
                            if(piece.type == PieceType.PAWN && piece.color != color && (piece.position.col == position.col || piece.position.col == position.col + 1 || piece.position.col == position.col - 1)) {
                                possibleMoves.add(
                                    Position(
                                        piece.position.row + direction,
                                        piece.position.col,
                                        FieldState.BLOCKED
                                    )
                                )
                                if(piece.position.col != position.col){
                                    possibleMoves.add(
                                        Position(
                                            piece.position.row + direction,
                                            piece.position.col,
                                            FieldState.BLOCKED
                                        )
                                    )
                                }
                            }
                        }

                        Log.d("King check", "move: $move \nContains: $res\n\n")
                        if (res && movementType != 0)
                            possibleMoves.add(Position(move.first, move.second, FieldState.BLOCKED))
                        else possibleMoves.add(Position(move.first, move.second, FieldState.ATTACK))

                    }

                    0 -> possibleMoves.add(Position(move.first, move.second, FieldState.EMPTY))
                }
            }
        }
        return possibleMoves
    }

    override fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>> {
        val potentialMoves = mutableListOf<Pair<Int, Int>>()

        potentialMoves.addAll(
            listOf(
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