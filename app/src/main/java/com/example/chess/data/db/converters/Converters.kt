package com.example.chess.data.db.converters

import androidx.room.TypeConverter
import com.example.chess.local.model.ActionType
import com.example.chess.local.model.GameType
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType

class Converters {
    @TypeConverter
    fun fromPieceType(value: PieceType?): String? = value?.name

    @TypeConverter
    fun toPieceType(value: String?): PieceType? = value?.let { PieceType.valueOf(it) }

    @TypeConverter
    fun fromPieceColor(value: PieceColor?): String? = value?.name

    @TypeConverter
    fun toPieceColor(value: String?): PieceColor? = value?.let { PieceColor.valueOf(it) }

    @TypeConverter
    fun fromActionType(value: ActionType?): String? = value?.name

    @TypeConverter
    fun toActionType(value: String?): ActionType? = value?.let { ActionType.valueOf(it) }

    @TypeConverter
    fun fromGameType(value: GameType?): String? = value?.name

    @TypeConverter
    fun toGameType(value: String?): GameType? = value?.let { GameType.valueOf(it) }
}