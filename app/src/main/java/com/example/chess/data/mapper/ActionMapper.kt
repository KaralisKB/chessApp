package com.example.chess.data.mapper

import com.example.chess.data.db.entity.ActionEntity
import com.example.chess.local.model.Action
import com.example.chess.local.model.Bishop
import com.example.chess.local.model.FieldState
import com.example.chess.local.model.King
import com.example.chess.local.model.Knight
import com.example.chess.local.model.Pawn
import com.example.chess.local.model.Position
import com.example.chess.local.model.Queen
import com.example.chess.local.model.Rook
import com.example.chess.ui.components.PieceType

fun Action.toEntity(): ActionEntity {
    return ActionEntity(
        turnId = turnId,
        gameId = gameId,
        originalPieceType = originalPiece.type,
        originalPieceColor = originalPiece.color,
        actionType = type,
        time = time,
        fromRow = originalPosition.row,
        fromCol = originalPosition.col,
        toRow = newPosition.row,
        toCol = newPosition.col,
        killedPieceType = killedPiece?.type,
        killedPieceColor = killedPiece?.color,
        promotedPieceType = promotedToPiece?.type,
        castleIsLong = castleIsLong,
        colorInCheck = colorInCheck
    )
}

fun ActionEntity.toAction(): Action {
    return Action(

        turnId = turnId,
        gameId = gameId,
        originalPiece = when (originalPieceType) {
            PieceType.PAWN -> Pawn(originalPieceColor, Position(fromRow, fromCol, FieldState.EMPTY))
            PieceType.KNIGHT -> Knight(originalPieceColor, Position(fromRow, fromCol, FieldState.EMPTY))
            PieceType.BISHOP -> Bishop(originalPieceColor, Position(fromRow, fromCol, FieldState.EMPTY))
            PieceType.ROOK -> Rook(originalPieceColor, Position(fromRow, fromCol, FieldState.EMPTY))
            PieceType.QUEEN -> Queen(originalPieceColor, Position(fromRow, fromCol, FieldState.EMPTY))
            PieceType.KING -> King(originalPieceColor, Position(fromRow, fromCol, FieldState.EMPTY))
        },
        type = actionType,
        time = time,
        originalPosition = Position(fromRow, fromCol, FieldState.EMPTY),
        newPosition = Position(toRow, toCol, FieldState.EMPTY),
        killedPiece = if (killedPieceColor != null) {
            when (killedPieceType) {
                PieceType.PAWN -> Pawn(killedPieceColor, Position(toRow, toCol, FieldState.EMPTY))
                PieceType.KNIGHT -> Knight(killedPieceColor, Position(toRow, toCol, FieldState.EMPTY))
                PieceType.BISHOP -> Bishop(killedPieceColor, Position(toRow, toCol, FieldState.EMPTY))
                PieceType.ROOK -> Rook(killedPieceColor, Position(toRow, toCol, FieldState.EMPTY))
                PieceType.QUEEN -> Queen(killedPieceColor, Position(toRow, toCol, FieldState.EMPTY))
                PieceType.KING -> King(killedPieceColor, Position(toRow, toCol, FieldState.EMPTY))
                else -> null
            }
        } else null,
        promotedToPiece = when (promotedPieceType) {
            PieceType.PAWN -> Pawn(originalPieceColor, Position(toRow, toCol, FieldState.EMPTY))
            PieceType.KNIGHT -> Knight(originalPieceColor, Position(toRow, toCol, FieldState.EMPTY))
            PieceType.BISHOP -> Bishop(originalPieceColor, Position(toRow, toCol, FieldState.EMPTY))
            PieceType.ROOK -> Rook(originalPieceColor, Position(toRow, toCol, FieldState.EMPTY))
            PieceType.QUEEN -> Queen(originalPieceColor, Position(toRow, toCol, FieldState.EMPTY))
            PieceType.KING -> King(originalPieceColor, Position(toRow, toCol, FieldState.EMPTY))
            else -> null
        },
        castleIsLong = castleIsLong,
        colorInCheck = colorInCheck,
    )
}