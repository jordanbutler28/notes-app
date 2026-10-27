import io.github.oshai.kotlinlogging.KotlinLogging
import model.Note
import persistence.JSONSerializer
import persistence.XMLSerializer
import service.NoteService
import utils.readNextBoolean
import utils.readNextInt
import utils.readNextLine
import java.io.File

//val noteService = NoteService(XMLSerializer(File("notes.xml")))
val noteService = NoteService(JSONSerializer(File("notes.json")))
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
        > |   2) List notes                |
        > |   3) Update a note             |
        > |   4) Archive a note            |
        > |   5) Delete a note             |
        > |   6) Save notes                |
        > |   7) Load notes                |
        > ----------------------------------
        > |   0) Exit                      |
        > ----------------------------------
        > """.trimMargin(">"))
        return readNextInt("==>> ")
    }

    fun listMenu(): Int {
        print("""
        > ----------------------------------
        > |        NOTE KEEPER APP         |
        > ----------------------------------
        > | LISTING MENU                   |
        > |   1) List all notes            |
        > |   2) List active notes         |
        > |   3) List archived notes       |
        > |   4) List by category          |
        > ----------------------------------
        > |   0) Exit to main menu         |
        > ----------------------------------  
        > """.trimMargin(">"))
        return readNextInt("==>> ")
    }

    fun runMenu() {
        var input : Int
        do {
            input = mainMenu()
            when(input) {
                1 -> addNote()
                2 -> runListMenu()
                3 -> updateNote()
                4 -> archiveNote()
                5 -> deleteNote()
                6 -> save()
                7 -> load()
                0 -> println("Exiting App")
                else -> println("Invalid Option")
            }
        } while (input != 0)
        logger.info { "Notes App Exiting" }
    }

    fun runListMenu() {
        var input : Int
        do {
            input = listMenu()
            when(input) {
                1 -> listAllNotes()
                2 -> listActiveNotes()
                3 -> listArchivedNotes()
                4 -> listNotesOfCategory()
                //5 -> listNotesOfPriority()
                0 -> runMenu()
                else -> println("Invalid Option")
            }
        } while (input != 0)
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

    fun listAllNotes() {
        val totalNotes = noteService.numberOfNotes()
        println("Total Notes: $totalNotes")
        println(noteService.getNotes().forEach { print(it) })
    }

    fun listActiveNotes() {
        val totalActiveNotes = noteService.numberOfActiveNotes()
        println("Total Active: $totalActiveNotes")
        println(noteService.getActiveNotes().forEach { print(it) })
    }

    fun listArchivedNotes() {
        val totalArchivedNotes = noteService.numberOfArchivedNotes()
        println("Total Archived: $totalArchivedNotes")
        println(noteService.getArchivedNotes().forEach { print(it) })
    }

    fun listNotesOfCategory() {
        val category = readNextLine("Search notes in category: ")
        val matchedNotes = noteService.numberOfNotesByCategory(category)
        println("Notes in $category: $matchedNotes")
        println(noteService.getNotesByCategory(category))
    }

    fun listNotesOfPriority() {

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

    fun archiveNote () {
        println(noteService.getActiveNotes().forEach { print(it) })
        print("Enter ID to archive: ")
        val id = readln().toInt()

        if (noteService.archiveNote(id)) {
            println("Archived Successfully")
        } else {
            println("Note cannot be archived. Invalid ID or note already archived.")
        }
    }

    fun save () {
        try {
            noteService.store()
        } catch (e: Exception) {
            System.err.println("Error writing to file: $e")
        }
    }

    fun load() {
        try {
            noteService.load()
        } catch (e: Exception) {
            System.err.println("Error reading from file: $e")
        }
    }