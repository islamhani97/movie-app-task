package com.islam97.android.apps.movie.core.di.modules

import android.content.Context
import androidx.room.Room
import com.islam97.android.apps.movie.data.room.AppDatabase
import com.islam97.android.apps.movie.data.room.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context, AppDatabase::class.java, "MOVIES_DATABASE"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(dataBase: AppDatabase): MovieDao {
        return dataBase.movieDao()
    }
}