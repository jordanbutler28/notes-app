package service

import model.Note
import persistence.Serializer

class NoteService(serializerType: Serializer) {

    private var notes = ArrayList<Note>()
    private var lastId = 0
    private fun getId(): Int = lastId++
    private var serializer: Serializer = serializerType

    fun addNote(note: Note) {
        note.id = getId()
        note.isArchived = false
        notes.add(note)
    }

    fun getNotes(): List<Note> {
        return notes
    }

    fun getActiveNotes(): List<Note> {
        val result = ArrayList<Note>()

        for (note in notes) {
            if (!note.isArchived) {
                result.add(note)
            }
        }
        return result
    }

    fun getArchivedNotes(): List<Note> {
        val result = ArrayList<Note>()

        for (note in notes) {
            if (note.isArchived) {
                result.add(note)
            }
        }
        return result
    }

    fun getNotesByCategory(category: String): List<Note> {
        val result = ArrayList<Note>()

        for (note in notes) {
            if (note.category.equals(category, ignoreCase = true)) {
                result.add(note)
            }
        }
        return result
    }

    fun getNotesByPriority(priority: Int): List<Note> {
        val result = ArrayList<Note>()

        for (note in notes) {
            if (note.priority == priority) {
                result.add(note)
            }
        }
        return result
    }

    fun deleteNote(id: Int): Boolean {
        return notes.removeIf { it.id == id }
    }

    fun updateNote(id: Int, updated: Note): Boolean {
        val index = notes.indexOfFirst { it.id == id }
        if (index != -1) {
            updated.id = id
            notes[index] = updated
            return true
        }
        return false
    }

    fun findNoteById(id: Int): Note? {
        return notes.find { it.id == id }
    }

    fun archiveNote(id: Int): Boolean {
        val note = notes.find { it.id == id && !it.isArchived }

       if (note != null) {
           note.isArchived = true
           return true
       }
        return false
    }

    fun numberOfNotes(): Int {
        return notes.size
    }

    fun numberOfArchivedNotes(): Int {
        var count = 0

        for (note in notes) {
            if (note.isArchived) {
                count++
            }
        }
        return count
    }

    fun numberOfActiveNotes(): Int {
        var count = 0

        for (note in notes) {
            if (!note.isArchived) {
                count ++
            }
        }
        return count
    }

    fun numberOfNotesByCategory(category: String) : Int {
        var count = 0

        for (note in notes) {
            if (note.category.equals(category, ignoreCase = true)) {
                count++
            }
        }
        return count
    }

    fun numberOfNotesByPriority(priority: Int) : Int {
        var count = 0

        for (note in notes) {
            if (note.priority == priority) {
                count++
            }
        }
        return count
    }

    fun load() {
        try {
            val array = serializer.read(Array<Note>::class.java)
            notes = array.toCollection(ArrayList())
            // find the max id in loaded notes and add 1. If null start at 0
            lastId = notes.maxOfOrNull { it.id }?.plus(1) ?: 0
        } catch (e: Exception) {
            System.err.println("Error reading file: ${e.message}. Starting with an empty list.")
            notes = ArrayList()
            lastId = 0
        }
    }

    fun store() {
        serializer.write(notes)
    }

}

