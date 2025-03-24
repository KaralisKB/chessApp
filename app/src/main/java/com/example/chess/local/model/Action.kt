package com.example.chess.local.model

import com.example.chess.ui.components.ChessPiece

data class Action(
    val originalPiece: ChessPiece,
    val type: ActionType,
    val time: Long,
    val originalPosition: Position = originalPiece.position,
    val newPosition: Position,
    val killedPiece: ChessPiece?,
    val promotedToPiece: ChessPiece?,
    val castleIsLong: Boolean?,
    val whiteInCheck: Boolean?
    )