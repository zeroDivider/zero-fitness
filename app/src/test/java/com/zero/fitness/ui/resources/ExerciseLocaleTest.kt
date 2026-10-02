package com.zero.fitness.ui.resources

import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseLocaleTest {

    @Test
    fun serbianUsesSrWhenPresent() {
        assertEquals(
            "Čučanj",
            localizedName("Squat", "Čučanj", Locale.forLanguageTag("sr")),
        )
    }

    @Test
    fun serbianFallsBackWhenSrBlank() {
        assertEquals(
            "Squat",
            localizedName("Squat", "  ", Locale.forLanguageTag("sr")),
        )
    }

    @Test
    fun englishKeepsEnglishName() {
        assertEquals(
            "Squat",
            localizedName("Squat", "Čučanj", Locale.US),
        )
    }
}
