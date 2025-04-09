package com.example.chess.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chess.local.model.ActionType
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType

@Entity(tableName = "actions")
data class ActionEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val turnId: Int,
    val originalPieceType: PieceType,
    val originalPieceColor: PieceColor,
    val actionType: ActionType,
    val time: Long,
    val fromRow: Int,
    val fromCol: Int,
    val toRow: Int,
    val toCol: Int,
    val killedPieceType: PieceType? = null,
    val killedPieceColor: PieceColor? = null,
    val promotedPieceType: PieceType? = null,
    val castleIsLong: Boolean? = null,
    val colorInCheck: PieceColor? = null
    )