package com.zero.fitness.domain.model

data class Weight(
    val value: Double,
    val unit: WeightUnit = WeightUnit.KG
)
