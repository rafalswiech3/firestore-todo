package com.rafal.firestoretodo.view.addtodo

import com.google.firebase.firestore.CollectionReference
import com.rafal.firestoretodo.model.Todo
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class AddToDoRepository @Inject constructor(
    @Named("todos_collection") val todos: CollectionReference
) {
    suspend fun addToDo(todo: Todo): Boolean {
        var result: Boolean? = null
        val ref = todos.document()
        todo.id = ref.id
        ref.set(todo)
            .addOnSuccessListener {
                result = true
            }
            .addOnFailureListener {
                result = false
            }
            .await()
        return result!!
    }
}