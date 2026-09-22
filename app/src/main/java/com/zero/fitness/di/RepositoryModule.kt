package com.zero.fitness.di

import com.zero.fitness.data.remote.ExerciseRemoteDataSource
import com.zero.fitness.data.remote.SupabaseExerciseRemoteDataSource
import com.zero.fitness.data.repository.ExerciseRepositoryImpl
import com.zero.fitness.domain.repository.ExerciseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindExerciseRemoteDataSource(
        impl: SupabaseExerciseRemoteDataSource,
    ): ExerciseRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindExerciseRepository(impl: ExerciseRepositoryImpl): ExerciseRepository
}
