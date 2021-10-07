package com.rafal.firestoretodo.view.main

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.CollectionReference
import com.rafal.firestoretodo.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 10

class MainRepository @Inject constructor(
    @Named("todos_collection") val todos: CollectionReference
) {
    private lateinit var pagingSource: MainPagingSource

    fun getTodos(): Flow<PagingData<Todo>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MainPagingSource(todos).also {
                pagingSource = it
            } }
        ).flow
    }

    suspend fun removeTodo(id: String): Boolean {
        var result: Boolean? = null
        todos.document(id).delete()
            .addOnSuccessListener { result = true }
            .addOnFailureListener { result = false }
            .await()
        return result!!
    }

}