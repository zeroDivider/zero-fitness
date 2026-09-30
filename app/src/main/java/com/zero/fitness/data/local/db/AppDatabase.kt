package com.zero.fitness.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zero.fitness.data.local.db.dao.ExerciseDao
import com.zero.fitness.data.local.db.dao.UserDAO
import com.zero.fitness.data.local.db.entity.ExerciseEntity
import com.zero.fitness.data.local.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        ExerciseEntity::class,
    ],
    version = 3,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME: String = "zero_fitness_db"
    }

    abstract fun userDao(): UserDAO
    abstract fun exerciseDao(): ExerciseDao
}
