package utils

import model.Status

fun readValidPriority(prompt : String): Int {
    var input = readNextInt(prompt)
    do {
        if (validRange(input, 1, 5))
            return input
        else {
            println("Input priority between 1 and 5. You gave $input")
            input = readNextInt(prompt)
        }
    } while (true)
}

fun readValidCategory(prompt: String) : String {
    var input = readNextLine(prompt)
    do {
        if (isValidCategory(input))
            return input
        else {
        println("Category not available.  You gave $input")
        println("Category options are: Home, College, Hobbies, Shopping and Work. Please try again.")
        input = readNextLine(prompt)
        }
    } while (true)
}

fun readValidStatus(prompt: String): Status {
    var input = readNextChar(prompt)
    do {
        if (isValidStatus(input)) {
            return when (input.lowercaseChar()) {
                't' -> Status.TO_DO
                'i' -> Status.IN_PROGRESS
                'd' -> Status.DONE
                else -> Status.TO_DO
            }
        } else {
            println("Invalid status. You gave $input")
            println("Status options are: 't' (To Do), 'i' (In Progress), or 'd' (Done). Please try again.")
            input = readNextChar(prompt)
        }
    } while (true)
}

fun readValidTags(prompt: String) : MutableList<String> {
    val input = readNextLine(prompt)
    return parseTags(input)
}