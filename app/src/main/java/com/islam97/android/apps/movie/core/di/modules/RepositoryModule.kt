package com.islam97.android.apps.movie.core.di.modules

import com.islam97.android.apps.movie.data.repository.MovieRepositoryImpl
import com.islam97.android.apps.movie.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideMovieRepository(repository: MovieRepositoryImpl): MovieRepository {
        return repository
    }
}