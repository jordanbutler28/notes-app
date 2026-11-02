package service

import model.Note
import persistence.Serializer
import kotlin.text.category

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

    fun getActiveNotes(): List<Note> = notes.filter { !it.isArchived }

    fun getArchivedNotes(): List<Note> = notes.filter { it.isArchived }

    fun getNotesByCategory(category: String): List<Note> = notes.filter { it.category.equals(category, ignoreCase = true) }

    fun getNotesByPriority(priority: Int): List<Note> = notes.filter { it.priority == priority }

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

    fun searchNotesByContent(input: String): List<Note> = notes.filter { it.title.contains(input, ignoreCase = true) || it.body.contains(input, ignoreCase = true) }

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

    fun numberOfArchivedNotes() = notes.count { note: Note -> note.isArchived }

    fun numberOfActiveNotes() = notes.count { note: Note -> !note.isArchived }

    fun numberOfNotesByCategory(category: String) = notes.count { note: Note -> note.category.equals(category, ignoreCase = true) }

    fun numberOfNotesByPriority(priority: Int) = notes.count { note: Note -> note.priority == priority }

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

