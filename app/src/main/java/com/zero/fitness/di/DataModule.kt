package com.zero.fitness.di

import android.app.Application
import androidx.room.Room
import com.zero.fitness.data.local.db.AppDatabase
import com.zero.fitness.data.local.db.DbMigrations
import com.zero.fitness.data.local.db.dao.ExerciseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .addMigrations(*DbMigrations.getAllMigrations())
            .fallbackToDestructiveMigrationOnDowngrade(false)
            .build()
    }

    @Singleton
    @Provides
    fun provideExerciseDao(db: AppDatabase): ExerciseDao = db.exerciseDao()
}
