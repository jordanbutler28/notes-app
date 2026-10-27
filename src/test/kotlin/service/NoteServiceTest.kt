package service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import model.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import persistence.XMLSerializer
import persistence.JSONSerializer
import java.io.File

class NoteServiceTest {

    private lateinit var emptyNoteService: NoteService
    private lateinit var populatedNoteService: NoteService
    private lateinit var note1: Note
    private lateinit var note2: Note
    private lateinit var note3: Note
    private lateinit var note4: Note
    private lateinit var note5: Note
    private lateinit var note6: Note

    @AfterEach
    fun tearDown() {
        // Clean up all possible test files to prevent state pollution between runs
        val filesToDelete = listOf("notes.json", "empty-notes.json", "notes.xml", "empty-notes.xml", "missing.json", "missing.xml", "zero.json", "zero.xml")
        for (fileName in filesToDelete) {
            val file = File(fileName)
            if (file.exists()) {
                file.delete()
            }
        }
        println("Test Completed and files cleaned up")
    }

    @BeforeEach
    fun setUp() {
        note1 = Note(0, "Test1", "Body1", 1, "Work", false)
        note2 = Note(0, "Test2", "Body2", 2, "Home", true)
        note3 = Note(0, "Test3", "Body3", 3, "College", false)
        note4 = Note(0, "Test4", "Body4", 4, "Social", true)

        //For use in tests
        note5 = Note(0, "Test5", "Body1", 1, "Extra", false)
        note6 = Note(0, "Test6", "Body1", 2, "Extra", false)

        emptyNoteService = NoteService(JSONSerializer(File("notes.json")))
        populatedNoteService = NoteService(JSONSerializer(File("empty-notes.json")))

        populatedNoteService.addNote(note1)
        populatedNoteService.addNote(note2)
        populatedNoteService.addNote(note3)
        populatedNoteService.addNote(note4)
    }

    @Nested
    inner class AddNotes {

        @Test
        fun `add note to an empty service increases size correctly`() {
            assertEquals(0, emptyNoteService.getNotes().size)
            emptyNoteService.addNote(note1)
            assertEquals(1, emptyNoteService.getNotes().size)
        }

        @Test
        fun `add note to a populated service increases size correctly`() {
            assertEquals(4, populatedNoteService.getNotes().size)
            populatedNoteService.addNote(note5)
            assertEquals(5, populatedNoteService.getNotes().size)
        }

        @Test
        fun `add note stores correct values`() {
            emptyNoteService.addNote(note1)

            val retrievedNote = emptyNoteService.getNotes().first()
            assertEquals(note1.title, retrievedNote.title)
            assertEquals(note1.body, retrievedNote.body)
            assertEquals(note1.priority, retrievedNote.priority)
            assertEquals(note1.category, retrievedNote.category)
        }

        @Test
        fun `add note sets isArchived to false`() {
            emptyNoteService.addNote(note1)
            emptyNoteService.addNote(note2)

            val retrievedNote1 = emptyNoteService.findNoteById(0)
            val retrievedNote2 = emptyNoteService.findNoteById(0)

            assertEquals(false, retrievedNote1?.isArchived)
            assertEquals(false, retrievedNote2?.isArchived)
        }

        @Test
        fun `ensure ids remain unique after multiple inserts`() {
            assertEquals(0, populatedNoteService.findNoteById(0)?.id)
            assertEquals(1, populatedNoteService.findNoteById(1)?.id)
            assertEquals(2, populatedNoteService.findNoteById(2)?.id)
            assertEquals(3, populatedNoteService.findNoteById(3)?.id)
        }
    }

    @Nested
    inner class GetNotes {
        @Test
        fun `getNotes returns an empty list when service is empty`() {
            assertTrue(emptyNoteService.getNotes().isEmpty())
        }

        @Test
        fun `getNotes returns all notes when populated`() {
            val notes = populatedNoteService.getNotes()
            assertEquals(4, notes.size)
        }

        @Test
        fun `getActiveNotes returns only non-archived notes` () {
            populatedNoteService.archiveNote(note1.id)
            populatedNoteService.archiveNote(note3.id)

            val activeList = populatedNoteService.getActiveNotes()

            assertEquals(2, activeList.size)
            assertTrue(activeList.all { !it.isArchived })
        }

        @Test
        fun `getActiveNotes returns empty list when no notes are stored` () {
            val activeList = emptyNoteService.getActiveNotes()
            assertTrue(activeList.isEmpty())
        }

        @Test
        fun `getActiveNotes returns empty list when all notes are archived` () {
            populatedNoteService.archiveNote(note1.id)
            populatedNoteService.archiveNote(note2.id)
            populatedNoteService.archiveNote(note3.id)
            populatedNoteService.archiveNote(note4.id)

            val activeList = populatedNoteService.getActiveNotes()

            assertTrue(activeList.isEmpty())
        }

        @Test
        fun `getActiveNotes returns all notes when none are archived` () {
            val activeList = populatedNoteService.getActiveNotes()

            assertEquals(4, activeList.size)
            assertTrue(activeList.none { it.isArchived })
        }

        @Test
        fun `getActiveNotes preserves correct count and content` () {
            populatedNoteService.archiveNote(note2.id)

            val activeList = populatedNoteService.getActiveNotes()

            assertEquals(3, activeList.size)
            assertTrue(activeList.all { !it.isArchived })

            //Assert: correct notes are present
            assertTrue(activeList.contains(note1))
            assertTrue(activeList.contains(note3))
            assertTrue(activeList.contains(note4))
        }

        @Test
        fun `getArchivedNotes only returns archived notes` () {
            populatedNoteService.archiveNote(note2.id)
            populatedNoteService.archiveNote(note4.id)

            val archivedList = populatedNoteService.getArchivedNotes()

            assertEquals(2, archivedList.size)
            assertTrue(archivedList.all { it.isArchived })
        }

        @Test
        fun `getArchivedNotes returns empty list when no notes are stored` () {
            val archivedList = emptyNoteService.getActiveNotes()
            assertTrue(archivedList.isEmpty())
        }

        @Test
        fun `getArchived notes returns empty list when all notes are active` () {
            val archivedList = populatedNoteService.getArchivedNotes()
            assertEquals(0, archivedList.size)
            assertTrue(archivedList.isEmpty())
        }

        @Test
        fun `getArchivedNotes returns all notes when all are archived` () {
            populatedNoteService.archiveNote(note1.id)
            populatedNoteService.archiveNote(note2.id)
            populatedNoteService.archiveNote(note3.id)
            populatedNoteService.archiveNote(note4.id)

            val archivedList = populatedNoteService.getArchivedNotes()
            assertEquals(4, archivedList.size)
            assertTrue(archivedList.all { it.isArchived })
        }

        @Test
        fun `getArchivedNotes returns correct count and content` () {
            populatedNoteService.archiveNote(note2.id)
            populatedNoteService.archiveNote(note4.id)

            val archivedList = populatedNoteService.getArchivedNotes()
            assertEquals(2, archivedList.size)
            assertTrue(archivedList.all { it.isArchived })

            //assert correct notes are present
            assertTrue(archivedList.contains(note2))
            assertTrue(archivedList.contains(note4))

        }

        @Test
        fun `getNotesByCategory handles empty list` () {
            val result = emptyNoteService.getNotesByCategory("Work")
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getNotesByCategory returns all matching notes when all match` () {
            emptyNoteService.addNote(Note(0, "A", "Body A", 1, "Work", false))
            emptyNoteService.addNote(Note(0, "B", "Body B", 2, "Work", false))
            emptyNoteService.addNote(Note(0, "C", "Body C", 3, "Work", false))

            assertEquals(3, emptyNoteService.numberOfNotes())
            val result = emptyNoteService.getNotesByCategory("Work")

            assertEquals(3, result.size)
            assertTrue(result.all { it.category == "Work" })
        }

        @Test
        fun `getNotesByCategory returns no notes when the category is not in the list` () {
            val result = populatedNoteService.getNotesByCategory("Invalid Category")
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getNotesByCategory returns matching categories regardless of case` () {
            val result = populatedNoteService.getNotesByCategory("home")
            assertEquals(1, result.size)
            assertTrue(result.all { it.category == "Home" })
        }

        @Test
        fun `getNotesByCategory excludes notes whose categories do not match` () {
            assertEquals(4, populatedNoteService.numberOfNotes())

            val result = populatedNoteService.getNotesByCategory("Work")
            assertEquals(1, result.size)

            assertTrue(result.contains(note1))
            assertFalse(result.contains(note2))
            assertFalse(result.contains(note3))
            assertFalse(result.contains(note4))
        }

        @Test
        fun `getNotesByPriority handles empty list` () {
            val result = emptyNoteService.getNotesByPriority(1)
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getNotesByPriority returns all matching notes when all match` () {
            emptyNoteService.addNote(Note(0, "Test", "Body1", 5, "Extra", false) )
            emptyNoteService.addNote(Note(0, "Test", "Body1", 5, "Extra", false) )

            assertEquals(2, emptyNoteService.numberOfNotes())
            println("Empty note service ${emptyNoteService.getNotes()}")
            val result = emptyNoteService.getNotesByPriority(5)
            assertEquals(2, result.size)
            assertTrue(result.all { it.priority == 5})
        }

        @Test
        fun `getNotesByPriority returns no notes when none match` () {
            val result = populatedNoteService.getNotesByPriority(5)
            assertTrue(result.isEmpty())
        }

        @Test
        fun `getNotesByPriority excludes notes whose priorities do not match` () {
            assertEquals(4, populatedNoteService.numberOfNotes())

            val result = populatedNoteService.getNotesByPriority(2)
            assertEquals(1, result.size)

            assertTrue(result.contains(note2))
            assertFalse(result.contains(note1))
            assertFalse(result.contains(note3))
            assertFalse(result.contains(note4))
        }

    }

    @Nested
    inner class DeleteNotes {
        @Test
        fun `delete existing note returns true`() {
            assertTrue(populatedNoteService.deleteNote(1))
        }

        @Test
        fun `delete not existing note returns false`() {
            assertFalse(populatedNoteService.deleteNote(5))
        }

        @Test
        fun `delete from empty array returns false`() {
            assertFalse(emptyNoteService.deleteNote(1))
        }

        @Test
        fun `delete from populated note service reduces size of array by one`() {
            val sizeBefore = populatedNoteService.getNotes().size
            populatedNoteService.deleteNote(3)
            assertEquals(sizeBefore - 1, populatedNoteService.getNotes().size)
        }

        @Test
        fun `delete existing note removes correct note`() {
            populatedNoteService.deleteNote(2)
            assertFalse(populatedNoteService.getNotes().any { it.id == 2})
        }

        @Test
        fun `delete one note does not remove other notes`(){
            populatedNoteService.deleteNote(1)
            assertTrue(populatedNoteService.getNotes().any { it.id == 0 })
            assertTrue(populatedNoteService.getNotes().any { it.id == 2 })
            assertTrue(populatedNoteService.getNotes().any { it.id == 3 })
        }
    }

    @Nested
    inner class UpdateNotes {

        @Test
        fun `update existing note returns true`() {
            val updated = Note(1, "Updated Title", "Updated Body", 5, "Personal", true )
            assertTrue(populatedNoteService.updateNote(1, updated))
        }

        @Test
        fun `update non-existing note returns false`() {
            val updated = Note(88, "Updated Title", "Updated Body", 5, "Personal", true )
            assertFalse(populatedNoteService.updateNote(88, updated))
        }

        @Test
        fun `update empty service returns false`() {
            val updated = Note(1, "Updated Title", "Updated Body", 5, "Personal", true )
            assertFalse(emptyNoteService.updateNote(1, updated))
        }

        @Test
        fun `update existing note changes its stored values`() {
            val updated = Note(1, "Updated Title", "Updated Body", 5, "Personal", true)

            populatedNoteService.updateNote(1, updated)

            val retrieved = populatedNoteService.findNoteById(1)

            assertEquals("Updated Title", retrieved?.title)
            assertEquals("Updated Body", retrieved?.body)
            assertEquals(5, retrieved?.priority)
            assertEquals("Personal", retrieved?.category)
            assertEquals(true,retrieved?.isArchived)
        }

        @Test
        fun `update existing note does not change number of notes` () {
            val sizeBefore = populatedNoteService.getNotes().size

            val updated = Note(1, "Updated Title", "Updated Body", 5, "Personal", true)
            populatedNoteService.updateNote(1, updated)

            assertEquals(sizeBefore, populatedNoteService.getNotes().size)
        }

        @Test
        fun `update existing note keeps its id` () {
            val updated = Note(99, "Updated Title", "Updated Body", 5, "Work", false)

            populatedNoteService.updateNote(1, updated)

            assertEquals(1, populatedNoteService.findNoteById(1)?.id)
        }
    }

    @Nested
    inner class CountingFunctions {

        @Test
        fun `empty list returns 0` () {
            assertEquals(0, emptyNoteService.numberOfNotes())
        }

        @Test
        fun `populated list returns correct count` () {
            assertEquals(4, populatedNoteService.numberOfNotes())
        }

        @Test
        fun `populated list returns correct active notes` () {
            //new notes are added as active
            assertEquals(4, populatedNoteService.numberOfActiveNotes())
        }

        @Test
        fun `populated list returns correct archived notes` () {
            assertTrue(populatedNoteService.archiveNote(2))
            assertTrue(populatedNoteService.archiveNote(3))
            assertEquals(2, populatedNoteService.numberOfArchivedNotes())
        }

        @Test
        fun `numberOfActiveNotes returns zero when all notes are archived` () {
            emptyNoteService.addNote(note1)
            emptyNoteService.addNote(note2)
            emptyNoteService.addNote(note3)

            emptyNoteService.archiveNote(0)
            emptyNoteService.archiveNote(1)
            emptyNoteService.archiveNote(2)

            assertEquals(0, emptyNoteService.numberOfActiveNotes())

        }

        @Test
        fun `correctly counts notes with given category` () {
            populatedNoteService.addNote(note5)
            populatedNoteService.addNote(note6)
            assertEquals(2, populatedNoteService.numberOfNotesByCategory("Extra"))
        }

        @Test
        fun `no notes in category returns 0` () {
            assertEquals(0, populatedNoteService.numberOfNotesByCategory("Christmas"))
        }

        @Test
        fun `correctly counts notes with given priority` () {
            assertEquals(1, populatedNoteService.numberOfNotesByPriority(4))
        }

        @Test
        fun `multiple notes with same priority are all counted`() {
            populatedNoteService.addNote(note5)
            assertEquals(2, populatedNoteService.numberOfNotesByPriority(1))
        }

        @Test
        fun `no notes with given priority returns 0` () {
            assertEquals(0, populatedNoteService.numberOfNotesByPriority(99))
        }

    }
    @Nested
        inner class PersistenceTests {

            @Test
            fun `saving and loading an empty collection in XML doesn't crash app` () {
                //saving empty notes.xml file
                val storingNotes = NoteService(XMLSerializer(File("notes.xml")))
                storingNotes.store()

                //loading the empty notes.xml file into a new object
                val loadedNotes = NoteService(XMLSerializer(File("notes.xml")))
                loadedNotes.load()

                //comparing both
                assertEquals(0, storingNotes.numberOfNotes())
                assertEquals(0, loadedNotes.numberOfNotes())
                assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            }

            @Test
            fun `saving notes to XML and then loading them doesn't loose data`() {
                // Storing 3 notes to the notes.XML file.
                val storingNotes = NoteService(XMLSerializer(File("notes.xml")))
                storingNotes.addNote(note1)
                storingNotes.addNote(note2)
                storingNotes.addNote(note3)
                storingNotes.store()

                //Loading notes.xml into a different collection
                val loadedNotes = NoteService(XMLSerializer(File("notes.xml")))
                loadedNotes.load()

                //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
                assertEquals(3, storingNotes.numberOfNotes())
                assertEquals(3, loadedNotes.numberOfNotes())
                assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
                assertEquals(storingNotes.findNoteById(0), loadedNotes.findNoteById(0))
                assertEquals(storingNotes.findNoteById(1), loadedNotes.findNoteById(1))
                assertEquals(storingNotes.findNoteById(2), loadedNotes.findNoteById(2))
            }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app` () {
            //saving empty notes.json file
            val storingNotes = NoteService(JSONSerializer(File("notes.json")))
            storingNotes.store()

            //loading the empty notes.json file into a new object
            val loadedNotes = NoteService(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //comparing both
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving notes to JSON and then loading them doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteService(JSONSerializer(File("notes.json")))
            storingNotes.addNote(note1)
            storingNotes.addNote(note2)
            storingNotes.addNote(note3)
            storingNotes.store()

            //Loading notes.xml into a different collection
            val loadedNotes = NoteService(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the JSON loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNoteById(0), loadedNotes.findNoteById(0))
            assertEquals(storingNotes.findNoteById(1), loadedNotes.findNoteById(1))
            assertEquals(storingNotes.findNoteById(2), loadedNotes.findNoteById(2))
        }

        @Test
        fun `loading from a non-existent file handles gracefully without crashing`() {
            val noteService = NoteService(JSONSerializer(File("missing.json")))

            // This should not throw a FileNotFoundException
            noteService.load()

            // It should just initialize with 0 notes
            assertEquals(0, noteService.numberOfNotes())
        }

        @Test
        fun `loading a zero-byte file handles gracefully and does not crash`() {
            // Create an entirely empty file (0 bytes)
            val emptyFile = File("zero.json")
            emptyFile.createNewFile()

            val noteService = NoteService(JSONSerializer(emptyFile))

            // This should not throw an EOFException or JsonParseException
            noteService.load()

            // It should just initialize with 0 notes
            assertEquals(0, noteService.numberOfNotes())
        }

        @Test
        fun `loading from a non-existent XML file handles gracefully without crashing`() {
            val noteService = NoteService(XMLSerializer(File("missing.xml")))

            // This should not throw a FileNotFoundException
            noteService.load()

            // It should just initialize with 0 notes
            assertEquals(0, noteService.numberOfNotes())
        }

        @Test
        fun `loading a zero-byte XML file handles gracefully and does not crash`() {
            // Create an entirely empty file
            val emptyFile = File("zero.xml")
            emptyFile.createNewFile()

            val noteService = NoteService(XMLSerializer(emptyFile))

            // This should not throw an EOFException or parsing exception
            noteService.load()

            // Confirm initialize with 0 notes
            assertEquals(0, noteService.numberOfNotes())
        }

        @Test
        fun `loading existing JSON notes updates the next ID correctly to prevent duplicates`() {
            val storingNotes = NoteService(JSONSerializer(File("notes.json")))
            storingNotes.addNote(Note(0, "Title 1", "Body", 1, "Work", false))
            storingNotes.addNote(Note(0, "Title 2", "Body", 2, "Home", false))
            storingNotes.store()

            val loadedNotes = NoteService(JSONSerializer(File("notes.json")))
            loadedNotes.load()
            loadedNotes.addNote(Note(0, "Title 3", "Body", 3, "College", false))

            val latestNote = loadedNotes.findNoteById(2)
            assertNotNull(latestNote, "The new note was not assigned ID 2. The ID counter likely reset to 0!")
            assertEquals("Title 3", latestNote?.title)
        }

        @Test
        fun `loading existing XML notes updates the next ID correctly to prevent duplicates`() {
            val storingNotes = NoteService(XMLSerializer(File("notes.xml")))
            storingNotes.addNote(Note(0, "Title 1", "Body", 1, "Work", false))
            storingNotes.addNote(Note(0, "Title 2", "Body", 2, "Home", false))
            storingNotes.store()

            val loadedNotes = NoteService(XMLSerializer(File("notes.xml")))
            loadedNotes.load()
            loadedNotes.addNote(Note(0, "Title 3", "Body", 3, "College", false))

            val latestNote = loadedNotes.findNoteById(2)
            assertNotNull(latestNote, "The new note was not assigned ID 2. The ID counter likely reset to 0!")
            assertEquals("Title 3", latestNote?.title)
        }

        }
}