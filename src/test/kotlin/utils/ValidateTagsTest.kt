package utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ValidateTagsTest {

    @Test
    fun parseTagsReturnsEmptyListForBlankInput() {
        assertTrue(parseTags("").isEmpty())
        assertTrue(parseTags("   ").isEmpty())
    }

    @Test
    fun parseTagsSplitsAndTrimsMultipleTags() {
        val tags = parseTags("kotlin, assignment, github")

        assertEquals(3, tags.size)
        assertTrue(tags.contains("kotlin"))
        assertTrue(tags.contains("assignment"))
        assertTrue(tags.contains("github"))
    }

    @Test
    fun parseTagsHandlesSingleTag() {
        val tags = parseTags("urgent")

        assertEquals(1, tags.size)
        assertEquals("urgent", tags[0])
    }

    @Test
    fun parseTagsHandlesExtraSpacesGracefully() {
        val tags = parseTags("  code  ,  test ")

        assertEquals(2, tags.size)
        assertEquals("code", tags[0])
        assertEquals("test", tags[1])
    }
}