package com.zero.fitness.domain.model


sealed class WorkoutTarget {
    data class Reps(val reps: Int) : WorkoutTarget()
    data class Time(val seconds: Int) : WorkoutTarget()
    data class Distance(val meters: Int) : WorkoutTarget()
}
