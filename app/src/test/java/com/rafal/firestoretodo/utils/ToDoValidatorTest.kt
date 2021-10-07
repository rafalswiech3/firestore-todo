package com.rafal.firestoretodo.utils

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(AndroidJUnit4::class)
class ToDoValidatorTest {
    @Test
    fun `empty title returns false`() {
        val result = ToDoValidator.validateTitle("")
        assertThat(result).isFalse()
    }

    @Test
    fun `too long title returns false`() {
        val tooLongTitle = "A".repeat(31)
        val result = ToDoValidator.validateTitle(tooLongTitle)
        assertThat(result).isFalse()
    }

    @Test
    fun `valid title returns true`() {
        val result = ToDoValidator.validateTitle( "Title")
        assertThat(result).isTrue()
    }

    @Test
    fun `empty description returns false`() {
        val result = ToDoValidator.validateDescription("")
        assertThat(result).isFalse()
    }

    @Test
    fun `too long description returns false`() {
        val tooLongDescription = "A".repeat(201)
        val result = ToDoValidator.validateTitle(tooLongDescription)
        assertThat(result).isFalse()
    }

    @Test
    fun `valid description returns true`() {
        val result = ToDoValidator.validateDescription("Description")
        assertThat(result).isTrue()
    }

    @Test
    fun `empty url returns true`() {
        val result = ToDoValidator.validateUrl("")
        assertThat(result).isTrue()
    }

    @Test
    fun `invalid url returns false`() {
        val invalidURL = "invalid.url"
        val result = ToDoValidator.validateUrl(invalidURL)
        assertThat(result).isFalse()
    }

    @Test
    fun `valid url returns true`() {
        val validURL = "http://www.google.com"
        val result = ToDoValidator.validateUrl(validURL)
        assertThat(result).isTrue()
    }
}