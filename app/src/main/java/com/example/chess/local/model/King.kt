package com.example.chess.local.model

import androidx.compose.foundation.text2.input.TextFieldLineLimits
import com.example.chess.R
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType
import com.example.chess.utils.ext.isCastleValid
import com.example.chess.utils.ext.isValidMove
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class King(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.KING
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0
    override var inCheck = false

    override suspend fun getEnemyMoves(boardState: BoardState): MutableSet<Position> = withContext(
        Dispatchers.Default
    ) {
        val allEnemyMoves = mutableSetOf<Position>()
        val enemyPieces = boardState.board.flatten()
            .filterNotNull()
            .filter { it.color != color && it.type != PieceType.KING }

        val moveJobs = enemyPieces.map { piece ->
            async {
                when (piece.type) {
                    PieceType.PAWN -> (piece as Pawn).getAttackMoves(boardState)
                    PieceType.KING -> emptyList()
                    else -> piece.getPossibleMoves(boardState, null) ?: emptyList()
                }
            }
        }

        val enemyMoves = moveJobs.awaitAll().flatten()
        allEnemyMoves.addAll(enemyMoves)

        val enemyKingMoves = boardState.board.flatten()
            .firstOrNull() { it?.type == PieceType.KING && it.color != color }
            ?.getPotentialMoves(boardState)
            ?.map { Position(it.first, it.second, FieldState.ATTACK) }
            ?: emptyList()

        allEnemyMoves.addAll(enemyKingMoves)
        return@withContext allEnemyMoves
    }

    override suspend fun getPossibleMoves(
        boardState: BoardState,
        skippedPosition: Position?,
        king: ChessPiece?,
        lineOfAttack: List<Position>?
    ): List<Position>? = withContext(Dispatchers.Default) {
        val potentialMoves = getPotentialMoves(boardState)
        val allEnemyMoves = async { getEnemyMoves(boardState) }.await()
        val possibleMoves = mutableSetOf<Position>()


        potentialMoves.fold(possibleMoves) { acc, move ->
            val row = move.first
            val col = move.second
            if (row in 0..7 && col in 0..7) {
                val movementType = getMovementType(move, boardState)
                val movePosition = Position(move, FieldState.EMPTY)
                val targetPiece = boardState.board[row][col]

                val moveResult = when {
                    targetPiece != null && targetPiece.color == color ->
                        Position(move, FieldState.EMPTY)

                    allEnemyMoves.any { position -> position.isValidMove(move) } && movementType != 0 ->
                        Position(move, FieldState.BLOCKED)

                    movementType != 0 && (king == null || !king.inCheck || boardState.blockCheck(
                        movePosition,
                        lineOfAttack!!
                    )) -> {
                        when (movementType) {
                            1 -> Position(move, FieldState.VALID)
                            2 -> Position(move, FieldState.ATTACK)
                            else -> Position(move, FieldState.BLOCKED)
                        }
                    }

                    else -> Position(move, FieldState.BLOCKED)
                }
                acc.add(moveResult)
            }
            acc
        }

        return@withContext possibleMoves.toList()
    }

    override suspend fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>> =
        withContext(Dispatchers.Default) {
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

            val isCastlePossible = castlePossible(this@King, boardState)
            if (movesMade == 0) {
                when (color) {
                    PieceColor.WHITE -> {
                        if (boardState.isCastleValid(0, 6, false) && isCastlePossible.second
                        ) potentialMoves.add(Pair(0, 6))

                        if (boardState.isCastleValid(0, 2, true) && isCastlePossible.first)
                            potentialMoves.add(Pair(0, 2))
                    }

                    PieceColor.BLACK -> {
                        if (boardState.isCastleValid(7, 6, false) && isCastlePossible.second)
                            potentialMoves.add(Pair(7, 6))

                        if (boardState.isCastleValid(7, 2, true) && isCastlePossible.first)
                            potentialMoves.add(Pair(7, 2))
                    }
                }
            }
            potentialMoves
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
        return if (color == PieceColor.WHITE) R.drawable.chess_klt60 else R.drawable.chess_kdt60
    }


}