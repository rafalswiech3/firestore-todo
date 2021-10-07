package com.rafal.firestoretodo.view.main

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.rafal.firestoretodo.model.Todo
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class MainPagingSource(
    private val collection: CollectionReference
) : PagingSource<QuerySnapshot, Todo>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, Todo>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Todo> {
        return try {

            val currentPage = params.key ?: collection
                .orderBy(Todo.FIELD_DATE)
                .limit(PAGE_SIZE.toLong())
                .get()
                .await()

            val lastItem = currentPage.documents[currentPage.size() - 1]

            val nextPage = collection
                .orderBy(Todo.FIELD_DATE)
                .limit(PAGE_SIZE.toLong())
                .startAfter(lastItem)
                .get()
                .await()

            LoadResult.Page(
                data = currentPage.toObjects(Todo::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}