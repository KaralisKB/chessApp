package com.example.chess.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.chess.data.db.converters.Converters
import com.example.chess.data.db.dao.ActionDao
import com.example.chess.data.db.entity.ActionEntity


@Database(
    entities = [ActionEntity::class],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ChessDatabase : RoomDatabase() {
    abstract fun actionDao(): ActionDao
}