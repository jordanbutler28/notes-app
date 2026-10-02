package utils

fun readNextInt(prompt: String = ""): Int {
    while (true) {
        print(prompt)

        val value = readln().trim().toIntOrNull()
        if (value != null) {
            return value
        }
        println("Please enter a valid integer")
    }
}

fun readNextLine(prompt: String = ""): String {
    print(prompt)
    return readln()
}

fun readNextChar(prompt: String = ""): Char {
    while (true) {
        print(prompt)
        val input = readln().trim()

        if (input.length == 1) {
            return input[0]
        }
        println("Please enter exactly one character")
    }
}