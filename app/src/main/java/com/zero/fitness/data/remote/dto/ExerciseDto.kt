package com.zero.fitness.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    val id: String,
    val name: String,
    val description: String? = null,
    @SerialName("name_sr")
    val nameSr: String? = null,
    @SerialName("description_sr")
    val descriptionSr: String? = null,
    @SerialName("allow_weight")
    val allowWeight: Boolean = true,
    val equipment: String? = null,
    @SerialName("muscle_groups_csv")
    val muscleGroupsCsv: String? = null,
    val difficulty: String? = null,
    @SerialName("is_compound")
    val isCompound: Boolean = true,
    @SerialName("video_url")
    val videoUrl: String? = null,
    @SerialName("created_at_epoch_ms")
    val createdAtEpochMs: Long,
    @SerialName("updated_at_epoch_ms")
    val updatedAtEpochMs: Long,
)
