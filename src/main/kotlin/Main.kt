import io.github.oshai.kotlinlogging.KotlinLogging
import model.Item
import model.Note
import model.Status
import persistence.JSONSerializer
import persistence.XMLSerializer
import service.ItemService
import service.NoteService
import utils.readNextBoolean
import utils.readNextChar
import utils.readNextInt
import utils.readNextLine
import utils.readValidCategory
import utils.readValidPriority
import utils.readValidStatus
import utils.readValidTags
import java.io.File

//val noteService = NoteService(XMLSerializer(File("notes.xml")))
val noteService = NoteService(JSONSerializer(File("notes.json")))
val itemService = ItemService()
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
        > |   3) Search notes              |
        > |   4) Update a note             |
        > |   5) Archive a note            |
        > |   6) Delete a note             |
        > |   7) Save notes                |
        > |   8) Load notes                |
        > ----------------------------------
        > | ITEM MENU                      |
        > |   10) Add item                 |
        > |   11) List all items           |
        > |   12) Item Report              |
        > |   13) Search items             |
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
        > |   5) List by priority          |
        > ----------------------------------
        > |   0) Exit to main menu         |
        > ----------------------------------  
        > """.trimMargin(">"))
        return readNextInt("==>> ")
    }

    fun runMenu() {
        do {
            val input = mainMenu()
            when(input) {
                1 -> addNote()
                2 -> runListMenu()
                3 -> searchNotes()
                4 -> updateNote()
                5 -> archiveNote()
                6 -> deleteNote()
                7 -> save()
                8 -> load()
                10 -> addItem()
                11 -> listAllItems()
                12 -> itemReport()
                13 -> searchItems()
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
                5 -> listNotesOfPriority()
                0 -> runMenu()
                else -> println("Invalid Option")
            }
        } while (input != 0)
    }

    fun addNote() {
        val title = readNextLine("Title:")

        val body = readNextLine("Body:")

        val priority = readValidPriority("Priority (1-5):")

        val category = readValidCategory("Category:")

        val tagsList = readValidTags("Tags (separate by comma): ")

        noteService.addNote(
            Note(0, title, body, priority, status = Status.TO_DO, category, tags = tagsList, version = 1.0, isArchived = false)
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
        val priority = readNextInt("Search notes by priority: ")
        val matchedNotes = noteService.numberOfNotesByPriority(priority)
        println("Notes with priority level $priority: $matchedNotes")
        println(noteService.getNotesByPriority(priority))
    }

    fun searchNotes() {
        val foundNotes = noteService.searchNotesByContent(readNextLine("Search notes: "))
        if (foundNotes.isNotEmpty()) {
            println("${foundNotes.size} note(s) matched your search")
            println(foundNotes)
        } else {
            println("No notes found")
        }
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

        val priority = readValidPriority("Priority (1-5): ")

        val newStatus = readValidStatus("Status (t = To Do, i = in progress, d = done): ")

        val category = readValidCategory("Category: ")

        val tagsList = readValidTags("Tags (comma separated): ")

        val isArchived = readNextBoolean("Is archived (y/n): ")

        val updated = Note(id, title, body, priority, newStatus, category, tagsList, isArchived = isArchived)

        if (noteService.updateNote(id, updated)) {
            println("Updated")
        } else {
            println("Note not found")
        }
    }

    fun archiveNote () {
        println(noteService.getActiveNotes().forEach { print(it) })
        val id = readNextInt("Enter ID of Note to be Archived: ")

        if (noteService.archiveNote(id)) {
            println("Archived Successfully")
        } else {
            println("Note cannot be archived. Invalid ID or note already archived.")
        }
    }

//Items
    fun addItem() {
        val description = readNextLine("Description: ")
        itemService.addItem(
            Item(0, description, false)
        )
    }

    fun listAllItems() {
        itemService.getItems().forEach { print(it) }
    }

    fun searchItems() {
        val foundItems = itemService.searchItemsByContent(readNextLine("Search items for: "))
        if (foundItems.isNotEmpty()) {
            println("${foundItems.size} item(s) matched your search")
            println(foundItems)
        } else {
            println("No items found")
        }
    }

    fun itemReport() {
        val totalItems = itemService.numberOfItems()
        val completeItems = itemService.numberOfCompletedItems()
        val incompleteItems = itemService.numberOfIncompleteItems()
        print("""
        > --------------------------------------
        > |        ITEM REPORT                 |
        > --------------------------------------
        > | ${"Total Items: $totalItems".padEnd(34)} |
        > | ${"Completed Items: $completeItems".padEnd(34)} |
        > | ${"Incomplete Items: $incompleteItems".padEnd(34)} |
        > --------------------------------------
        > """.trimMargin(">"))
    }

//persistence

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