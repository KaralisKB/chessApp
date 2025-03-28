package com.example.chess.data.di

import android.content.Context
import androidx.room.Room
import com.example.chess.data.db.dao.ActionDao
import com.example.chess.data.db.ChessDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ChessDatabase {
        return Room.databaseBuilder(
            context,
            ChessDatabase::class.java,
            "chess_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideActionDao(db: ChessDatabase): ActionDao = db.actionDao()
}