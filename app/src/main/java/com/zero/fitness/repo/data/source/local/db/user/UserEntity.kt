package com.zero.fitness.repo.data.source.local.db.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zero.fitness.repo.data.model.User

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
