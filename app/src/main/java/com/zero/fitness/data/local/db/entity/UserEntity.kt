package com.zero.fitness.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zero.fitness.data.local.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0L,
    val name: String = "",
)

fun UserEntity.toUser(): User {
    return User(
        id = id,
        name = name,
    )
}
