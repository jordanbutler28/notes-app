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