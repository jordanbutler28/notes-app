import io.github.oshai.kotlinlogging.KotlinLogging
import model.Note
import service.NoteService
import utils.readNextBoolean
import utils.readNextInt
import utils.readNextLine

val noteService = NoteService()
private val logger = KotlinLogging.logger {}

fun main() {
    logger.info { "Notes App Started" }
    runMenu()
}

fun mainMenu(): Int {
    print("""
    > ----------------------------------
    > |        NOTE KEEPER APP         |
    > ----------------------------------
    > | NOTE MENU                      |
    > |   1) Add a note                |
    > |   2) List all notes            |
    > |   3) Update a note             |
    > |   4) Delete a note             |
    > ----------------------------------
    > |   0) Exit                      |
    > ----------------------------------
    > """.trimMargin(">"))
    return readNextInt("==>>")
}

fun runMenu() {
    var input : Int
    do {
        input = mainMenu()
        when(input) {
            1 -> addNote()
            2 -> listNotes()
            3 -> updateNote()
            4 -> deleteNote()
            0 -> println("Exiting App")
            else -> println("Invalid Option")
        }
    } while (input != 0)
    logger.info { "Notes App Exiting" }
}

fun addNote() {
    val title = readNextLine("Title:")

    val body = readNextLine("Body:")

    val priority = readNextInt("Priority (1-5):")

    val category = readNextLine("Category:")

    noteService.addNote(
        Note(0, title, body, priority, category, false)
    )
    println("Note Added")
}

fun listNotes() {
    println(noteService.getNotes().forEach { print(it) })
}

fun deleteNote() {
    print("Enter ID to delete: ")
    val id = readln().toInt()

    if (noteService.deleteNote(id)) {
        println("Deleted")
    } else {
        println("Note not found")
    }
}

fun updateNote() {
    val id = readNextInt("Enter ID to update: ")

    val title = readNextLine("Title: ")

    val body = readNextLine("Body: ")

    val priority = readNextInt("Priority (1-5): ")

    val category = readNextLine("Category: ")

    val isArchived = readNextBoolean("Is archived (y/n): ")

    val updated = Note(id, title, body, priority, category, isArchived)

    if (noteService.updateNote(id, updated)) {
        println("Updated")
    } else {
        println("Note not found")
    }

}
