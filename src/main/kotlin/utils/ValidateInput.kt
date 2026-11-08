package utils

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