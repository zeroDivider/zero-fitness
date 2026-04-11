package com.zero.fitness.domain.model

enum class BlockType { REGULAR, SUPERSET, CIRCUIT }

enum class SetType { WARMUP, WORK, DROP, AMRAP, FAILURE }

enum class ExerciseType { COMPOUND, ISOLATION }

enum class WeightUnit { KG, LB }

enum class DifficultyLevel { BEGINNER, INTERMEDIATE, ADVANCED, ELITE }

enum class Equipment {
    BODYWEIGHT,
    BARBELL,
    DUMBBELL,
    KETTLEBELL,
    CABLE,
    MACHINE,
    RESISTANCE_BAND
}

enum class MuscleGroup {
    CHEST,
    BACK,
    SHOULDERS,
    BICEPS,
    TRICEPS,
    CORE,
    QUADS,
    HAMSTRINGS,
    GLUTES,
    CALVES,
    LOWER_BACK
}
