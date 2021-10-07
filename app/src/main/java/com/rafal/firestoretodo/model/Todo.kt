package com.rafal.firestoretodo.model

import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@IgnoreExtraProperties
data class Todo(
    var id: String? = null,
    var title: String? = null,
    var desc: String? = null,
    @ServerTimestamp
    var date: Date? = null,
    var url: String? = null
) : Parcelable {
    companion object {
        const val FIELD_DATE = "date"
    }
}
