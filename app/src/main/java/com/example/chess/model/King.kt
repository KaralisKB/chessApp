package com.example.chess.model

import android.util.Log
import com.example.chess.utils.ext.isValidMove

class King(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.KING
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0

    override fun getPossibleMoves(
        boardState: BoardState, skippedPosition: Position?
    ): List<Position> {
        val possibleMoves: MutableList<Position> = mutableListOf()
        val potentialMoves = getPotentialMoves(boardState)
        val filteredPositions = boardState.board.flatten()
            .filterNotNull()

        val allEnemyMoves = mutableSetOf<Position>()
        boardState.board.asSequence()
            .flatMap { pieces -> pieces.filter { piece -> piece != null && piece.color != color && piece.type != PieceType.KING } }
            .fold(allEnemyMoves) { acc, move ->
                if (move?.type == PieceType.PAWN) {
                    val res = (move as Pawn).getAttackMoves(boardState)
                    acc.addAll(res)
                } else {
                    val res = move?.getPossibleMoves(boardState, null) ?: emptyList()
                    acc.addAll(res)
                }
                acc
            }

        allEnemyMoves.addAll(
            filteredPositions
                .first { it.type == PieceType.KING && it.color != color }
                .getPotentialMoves(boardState)
                .map { Position(it.first, it.second, FieldState.ATTACK) }
        )

        filteredPositions.fold(allEnemyMoves) { acc, piece ->
            if ( piece is Pawn && piece.color != color) acc.addAll(piece.getPotentialAttackMoves(boardState))
            acc
        }

        for (move in potentialMoves) {
            val movementType = getMovementType(move, boardState)

            if (allEnemyMoves.any { position -> position.isValidMove(move) } && movementType != 0) possibleMoves.add(
                Position(move.first, move.second, FieldState.BLOCKED)
            )
            else {
                when (movementType) {
                    1 -> possibleMoves.add(Position(move.first, move.second, FieldState.VALID))
                    2 -> {
                        val newAllEnemyMoves = (boardState.board.asSequence()
                            .flatMap { pieces -> pieces.filter { piece -> piece != null && piece.color != color && piece.type != PieceType.KING } }
                            .map {
                                if (it?.type != PieceType.KING) it?.getPossibleMoves(
                                    boardState, Position(move, FieldState.EMPTY)
                                ) else emptyList()
                            }
                            .toList()
                            .fold(mutableListOf<Position>(), { acc, list ->
                                if (list != null) acc.addAll(list)
                                acc
                            }))
                        val res = newAllEnemyMoves.any { position -> position.isValidMove(move) }


                        Log.d("King check", "move: $move \nContains: $res\n\n")
                        possibleMoves.add(
                            Position(
                                move.first,
                                move.second,
                                if (res && movementType != 0) FieldState.BLOCKED else FieldState.ATTACK
                            )
                        )
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

        if(movesMade == 0 && color == PieceColor.WHITE){
            if (boardState.board[0][6] == null && boardState.board[0][5] == null && castlePossible(this, boardState).second == true) {
                potentialMoves.add(Pair(0, 6))
            }
            if (boardState.board[0][1] == null && boardState.board[0][2] == null && boardState.board[0][3] == null && castlePossible(this, boardState).first == true) {
                potentialMoves.add(Pair(0, 2))
            }
        } else if (movesMade == 0 && color == PieceColor.BLACK) {
            if (boardState.board[7][6] == null && boardState.board[7][5] == null && castlePossible(this, boardState).second == true) {
                potentialMoves.add(Pair(7, 6))
            }
            if (boardState.board[7][1] == null && boardState.board[7][2] == null && boardState.board[7][3] == null && castlePossible(this, boardState).first == true) {
                potentialMoves.add(Pair(7, 2))
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

        return when {
            targetPiece == null -> 1
            targetPiece.color != color -> 2
            else -> 0
        }
    }

    fun castlePossible(piece: ChessPiece, boardState: BoardState): Pair<Boolean, Boolean> {
        var longCastleAvailable = false
        var shortCastleAvailable = false
        val board = boardState.board
        when {
            piece.color == PieceColor.WHITE -> {
                if (board[0][0]?.movesMade == 0) longCastleAvailable = true
                if (board[0][7]?.movesMade == 0) shortCastleAvailable = true
            }
            piece.color == PieceColor.BLACK -> {
                if (board[7][0]?.movesMade == 0) longCastleAvailable = true
                if (board[7][7]?.movesMade == 0) shortCastleAvailable = true
            }
        }
        return Pair(longCastleAvailable, shortCastleAvailable)
    }
}