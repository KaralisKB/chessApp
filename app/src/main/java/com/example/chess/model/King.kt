package com.example.chess.model

import com.example.chess.R
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType
import com.example.chess.utils.ext.isValidMove

class King(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.KING
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0
    override var inCheck = false

    override fun getEnemyMoves(boardState: BoardState): MutableSet<Position> {
        val allEnemyMoves = mutableSetOf<Position>()
        val filteredPositions = boardState.board.flatten()
            .filterNotNull()


        boardState.board.asSequence()
            .flatMap { pieces -> pieces.filter { piece -> piece != null && piece.color != color && piece.type != PieceType.KING } }
            .fold(allEnemyMoves) { acc, move ->
                if (move?.type == PieceType.PAWN) {
                    val res = (move as Pawn).getAttackMoves(boardState)
                    acc.addAll(res)
                } else {
                    // TODO this .getPossibleMoves doesent take into consideration the moved king, this causes xray move error
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
            if (piece is Pawn && piece.color != color) acc.addAll(piece.getPotentialAttackMoves())
            acc
        }
        return allEnemyMoves
    }

    override fun getPossibleMoves(boardState: BoardState, skippedPosition: Position?): List<Position> {
        val potentialMoves = getPotentialMoves(boardState)
        val allEnemyMoves = getEnemyMoves(boardState)
        val possibleMoves = mutableListOf<Position>()

        for (move in potentialMoves) {
            val (row, col) = move
            if (row !in 0..7 || col !in 0..7) continue

            val movementType = getMovementType(move, boardState)
            val movePosition = Position(move, FieldState.EMPTY)
            val targetPiece = boardState.board[row][col]

            if (targetPiece != null && targetPiece.color == this.color) {
                possibleMoves.add(Position(move, FieldState.EMPTY))
            } else if (allEnemyMoves.any { position -> position.isValidMove(move) } && movementType != 0) {
                possibleMoves.add(Position(move, FieldState.BLOCKED))
            } else if (movementType != 0 && !boardState.checkKingMoveInCheck(this, boardState, movePosition)) {
                when (movementType) {
                    1 -> possibleMoves.add(Position(move, FieldState.VALID))
                    2 -> possibleMoves.add(Position(move, FieldState.ATTACK))
                }
            } else {
                possibleMoves.add(Position(move, FieldState.BLOCKED))
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

        if (movesMade == 0 && color == PieceColor.WHITE) {
            if (boardState.board[0][6] == null && boardState.board[0][5] == null && castlePossible(
                    this,
                    boardState
                ).second
            ) {
                potentialMoves.add(Pair(0, 6))
            }
            if (boardState.board[0][1] == null && boardState.board[0][2] == null && boardState.board[0][3] == null && castlePossible(
                    this,
                    boardState
                ).first
            ) {
                potentialMoves.add(Pair(0, 2))
            }
        } else if (movesMade == 0 && color == PieceColor.BLACK) {
            if (boardState.board[7][6] == null && boardState.board[7][5] == null && castlePossible(
                    this,
                    boardState
                ).second
            ) {
                potentialMoves.add(Pair(7, 6))
            }
            if (boardState.board[7][1] == null && boardState.board[7][2] == null && boardState.board[7][3] == null && castlePossible(
                    this,
                    boardState
                ).first
            ) {
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

    private fun castlePossible(piece: ChessPiece, boardState: BoardState): Pair<Boolean, Boolean> {
        var longCastleAvailable = false
        var shortCastleAvailable = false
        val board = boardState.board
        when {
            piece.color == PieceColor.WHITE -> {
                if (board[0][0]?.movesMade == 0 && !this.inCheck) longCastleAvailable = true
                if (board[0][7]?.movesMade == 0 && !this.inCheck) shortCastleAvailable = true
            }

            piece.color == PieceColor.BLACK -> {
                if (board[7][0]?.movesMade == 0 && !this.inCheck) longCastleAvailable = true
                if (board[7][7]?.movesMade == 0 && !this.inCheck) shortCastleAvailable = true
            }
        }
        return Pair(longCastleAvailable, shortCastleAvailable)
    }

    override fun getImage(): Int {
        if (color == PieceColor.WHITE) return R.drawable.chess_klt60 else return R.drawable.chess_kdt60
    }
}