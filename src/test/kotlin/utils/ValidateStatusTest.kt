package utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ValidateStatusTest {

    @Test
    fun isValidStatusTrueWhenValidCharacterGiven() {
        assertTrue(isValidStatus('t'))
        assertTrue(isValidStatus('T'))
        assertTrue(isValidStatus('i'))
        assertTrue(isValidStatus('I'))
        assertTrue(isValidStatus('d'))
        assertTrue(isValidStatus('D'))
    }

    @Test
    fun isValidStatusFalseWhenInvalidCharacterGiven() {
        assertFalse(isValidStatus('a'))
        assertFalse(isValidStatus('x'))
        assertFalse(isValidStatus(' '))
        assertFalse(isValidStatus('1'))
    }
}