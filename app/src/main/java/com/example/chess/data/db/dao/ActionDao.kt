package com.example.chess.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chess.data.db.entity.ActionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAction(action: ActionEntity)

    @Query("Select * FROM actions ORDER BY time ASC")
    fun getAllActions(): Flow<List<ActionEntity>>

    @Query("DELETE FROM actions")
    suspend fun clearActions()

}