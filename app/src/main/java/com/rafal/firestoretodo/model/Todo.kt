package com.rafal.firestoretodo.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@IgnoreExtraProperties
data class Todo(
    var id: String? = null,
    var title: String? = null,
    var desc: String? = null,
    @ServerTimestamp
    var date: Date? = null,
    var url: String? = null
) {
    companion object {
        const val FIELD_DATE = "date"
    }
}
