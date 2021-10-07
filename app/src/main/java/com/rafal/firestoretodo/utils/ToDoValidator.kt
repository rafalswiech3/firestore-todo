package com.rafal.firestoretodo.utils

import android.webkit.URLUtil

object ToDoValidator {
    fun validateTitle(title: String): Boolean {
        if(title.isEmpty() || title.length > 30) return false
        return true
    }

    fun validateDescription(desc: String): Boolean {
        if(desc.isEmpty() || desc.length > 200) return false
        return true
    }

    fun validateUrl(url: String): Boolean {
        if(url.isEmpty()) return true
        return URLUtil.isValidUrl(url)
    }
}