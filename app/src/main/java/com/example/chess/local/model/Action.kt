package com.example.chess.local.model

import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor

data class Action(
    val turnId: Int,
    val gameId: Int,
    val originalPiece: ChessPiece,
    val type: ActionType,
    val time: Long,
    val originalPosition: Position = originalPiece.position,
    val newPosition: Position,
    val killedPiece: ChessPiece?,
    val promotedToPiece: ChessPiece?,
    val castleIsLong: Boolean?,
    val colorInCheck: PieceColor?
    )