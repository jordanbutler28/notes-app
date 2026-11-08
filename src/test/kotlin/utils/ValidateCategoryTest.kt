package utils

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class ValidateCategoryTest {

    @Test
    fun categoriesReturnsFullCategoriesSet(){
        assertEquals(5, categories.size)
        assertTrue(categories.contains("Home"))
        assertTrue(categories.contains("College"))
        assertTrue(categories.contains("Hobbies"))
        assertTrue(categories.contains("Shopping"))
        assertTrue(categories.contains("Work"))
        assertFalse(categories.contains("Not here"))
    }

    @Test
    fun isValidCategoryTrueWhenCategoryExists(){
        assertTrue(isValidCategory("Home"))
        assertTrue(isValidCategory("home"))
        assertTrue(isValidCategory("COLLEGE"))
    }

    @Test
    fun isValidCategoryFalseWhenCategoryDoesNotExist(){
        assertFalse(isValidCategory("Hom"))
        assertFalse(isValidCategory("colllege"))
        assertFalse(isValidCategory(""))
    }
}