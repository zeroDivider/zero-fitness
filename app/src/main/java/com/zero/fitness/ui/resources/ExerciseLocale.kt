package com.zero.fitness.ui.resources

import com.zero.fitness.domain.model.Exercise
import java.util.Locale

fun Exercise.localizedName(locale: Locale): String =
    localizedName(name, nameSr, locale)

fun Exercise.localizedDescription(locale: Locale): String? =
    localizedDescription(description, descriptionSr, locale)

fun localizedName(name: String, nameSr: String?, locale: Locale): String =
    if (locale.language == "sr" && !nameSr.isNullOrBlank()) nameSr else name

fun localizedDescription(description: String?, descriptionSr: String?, locale: Locale): String? =
    if (locale.language == "sr" && !descriptionSr.isNullOrBlank()) descriptionSr else description
