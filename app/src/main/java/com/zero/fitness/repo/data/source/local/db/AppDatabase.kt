package com.zero.fitness.repo.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zero.fitness.repo.data.source.local.db.user.UserDAO
import com.zero.fitness.repo.data.source.local.db.user.UserEntity

@Database(
    entities = [
        UserEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME: String = "zero_fitness_db"
    }

    abstract fun userDao(): UserDAO
}
