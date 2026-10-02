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
    SHOULDERS,
    TRICEPS,
    LATS,
    UPPER_BACK,
    TRAPS,
    BICEPS,
    FOREARMS,
    CORE,
    LOWER_BACK,
    QUADS,
    HAMSTRINGS,
    GLUTES,
    ADDUCTORS,
    ABDUCTORS,
    CALVES,
}
