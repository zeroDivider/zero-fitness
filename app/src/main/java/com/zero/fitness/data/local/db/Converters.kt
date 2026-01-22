package com.zero.fitness.data.local.db

import androidx.room.TypeConverter
import com.zero.fitness.domain.model.BlockType
import com.zero.fitness.domain.model.SetType
import com.zero.fitness.domain.model.WeightUnit
import java.util.UUID

class Converters {
    @TypeConverter fun fromUUID(v: UUID?): String? = v?.toString()
    @TypeConverter fun toUUID(v: String?): UUID? = v?.let(UUID::fromString)

    @TypeConverter fun fromBlockType(v: BlockType?): String? = v?.name
    @TypeConverter fun toBlockType(v: String?): BlockType? = v?.let(BlockType::valueOf)

    @TypeConverter fun fromSetType(v: SetType?): String? = v?.name
    @TypeConverter fun toSetType(v: String?): SetType? = v?.let(SetType::valueOf)

    @TypeConverter fun fromWeightUnit(v: WeightUnit?): String? = v?.name
    @TypeConverter fun toWeightUnit(v: String?): WeightUnit? = v?.let(WeightUnit::valueOf)
}
