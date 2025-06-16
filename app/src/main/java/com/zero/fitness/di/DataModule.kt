package com.zero.fitness.di

import android.app.Application
import androidx.room.Room
import com.zero.fitness.repo.data.source.local.db.AppDatabase
import com.zero.fitness.repo.data.source.local.db.DbMigrations
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

}
