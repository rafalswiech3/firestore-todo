package com.rafal.firestoretodo.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ToDoValidatorTest {
    @Test
    fun `empty title returns false`() {
        val result = ToDoValidator.validateTitle( "")
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
    fun `valid description returns true`() {
        val result = ToDoValidator.validateDescription("Description")
        assertThat(result).isTrue()
    }

//    @Test
//    fun `invalid url returns false`() {
//        val result = ToDoValidator.validateUrl("invalid.url")
//        assertThat(result).isFalse()
//    }
//
//    @Test
//    fun `valid url returns true`() {
//        val result = ToDoValidator.validateUrl("http://google.com")
//        assertThat(result).isTrue()
//    }
}