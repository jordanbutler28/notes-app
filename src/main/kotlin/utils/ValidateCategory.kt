package utils

val categories = setOf("Home", "College", "Hobbies", "Shopping", "Work")

fun isValidCategory(categoryToCheck: String?) : Boolean =
    categories.any { it.equals(categoryToCheck, true) }