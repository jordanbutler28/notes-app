package utils

fun isValidStatus(statusToCheck: Char) : Boolean {
    val input = statusToCheck.lowercaseChar()
    return input == 't' || input == 'i' || input == 'd'
}