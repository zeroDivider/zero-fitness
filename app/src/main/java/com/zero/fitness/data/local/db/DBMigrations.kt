package com.zero.fitness.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


object DbMigrations {
    fun getAllMigrations(): Array<Migration> {
        return arrayOf(
            object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL(
                        """
                    CREATE TABLE IF NOT EXISTS exercises (
                        id TEXT NOT NULL PRIMARY KEY,
                        name TEXT NOT NULL,
                        description TEXT,
                        allowWeight INTEGER NOT NULL DEFAULT 1,
                        equipment TEXT,
                        muscleGroupsCsv TEXT,
                        difficulty TEXT,
                        isCompound INTEGER NOT NULL DEFAULT 1,
                        videoUrl TEXT,
                        createdAtEpochMs INTEGER NOT NULL,
                        updatedAtEpochMs INTEGER NOT NULL,
                        syncPending INTEGER NOT NULL DEFAULT 1,
                        deletedLocally INTEGER NOT NULL DEFAULT 0
                    )
                    """.trimIndent(),
                    )
                }
            },
            object : Migration(2, 3) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("ALTER TABLE exercises ADD COLUMN nameSr TEXT")
                    db.execSQL("ALTER TABLE exercises ADD COLUMN descriptionSr TEXT")
                }
            },
        )
    }
}
