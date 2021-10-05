package com.rafal.firestoretodo.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Todo(
    val title: String,
    val desc: String,
    val date: String,
    val url: String
)