package com.example.chess.domain.useCase.board

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chess.domain.useCase.UseCaseParams
import com.example.chess.local.model.BoardState
import com.example.chess.local.model.Position
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType
import com.example.chess.utils.ext.isWhite
import javax.inject.Inject

class MovePieceUseCase @Inject constructor() : UseCaseParams<MovePieceUseCase.Params, List<Position>>() {

    class Params(
        val piece: ChessPiece?,
        val board: BoardState,
        val isWhiteKingAttacked: Boolean,
        val isBlackKingAttacked: Boolean,
        val lineOfAttack: List<Position>
    ) {
        companion object {
            fun create(
                piece: ChessPiece?,
                board: BoardState,
                isWhiteKingAttacked: Boolean,
                isBlackKingAttacked: Boolean,
                lineOfAttack: List<Position>
            ): Params = Params(piece, board, isWhiteKingAttacked, isBlackKingAttacked, lineOfAttack)
        }
    }

    override suspend fun execute(data: Params): List<Position> {
        val movements = data.piece?.getPossibleMoves(data.board, null) ?: listOf()
        val lineOfAttack = data.lineOfAttack

        return if (data.isWhiteKingAttacked || data.isBlackKingAttacked) {
            blockAttackMove(data, movements, lineOfAttack)
        } else {
            movements
        }
    }


    private suspend fun blockAttackMove(
        data: Params,
        possibleMoves: List<Position>,
        lineOfAttack: List<Position>
    ): List<Position> {
        with(data) {
            return when {
                (isWhiteKingAttacked || isBlackKingAttacked) && piece?.type != PieceType.KING -> {
                    val targetedKing = when {
                        (isWhiteKingAttacked && piece?.color == PieceColor.WHITE) -> board.whiteKing
                        (isBlackKingAttacked && piece?.color == PieceColor.BLACK) -> board.blackKing
                        else -> null
                    }

                    return possibleMoves.filter { move ->
                        val doesBlock = board.blockCheck(move,lineOfAttack)
                        println("Checking move ${move.row},${move.col} for ${piece?.type}: Blocks Check? $doesBlock")
                        doesBlock
                    }
                }

                (isWhiteKingAttacked || isBlackKingAttacked) && piece?.type == PieceType.KING ->
                    piece.getPossibleMoves(board, null) ?: listOf()

                else -> possibleMoves
            }
        }
    }
}