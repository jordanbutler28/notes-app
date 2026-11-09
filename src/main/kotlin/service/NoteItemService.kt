package service

import model.NoteItem

class NoteItemService {
    private var noteItems = ArrayList<NoteItem>()

    fun addItemToNote(noteId: Int, itemId: Int): Boolean {

        if (noteItems.any { it.noteId == noteId && it.itemId == itemId }) {
            return false
        }
        noteItems.add(NoteItem(noteId, itemId))
        return true
    }
}