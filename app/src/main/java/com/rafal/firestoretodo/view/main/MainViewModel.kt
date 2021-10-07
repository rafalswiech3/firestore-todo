package com.rafal.firestoretodo.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rafal.firestoretodo.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MainRepository
) : ViewModel() {
    private var todosInitLoaded = false

    private val _todoLiveData: MutableLiveData<PagingData<Todo>> = MutableLiveData()
    val todoLiveData: LiveData<PagingData<Todo>> = _todoLiveData

    private val _removeTodoLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val removeTodoLiveData: LiveData<Boolean> = _removeTodoLiveData

    private val _removeTodoDialogLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val removeTodoDialogLiveData: LiveData<Boolean> = _removeTodoDialogLiveData

    var todoToRemoveID: String? = null

    fun getTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getTodos().cachedIn(viewModelScope).collect {
                _todoLiveData.postValue(it)
            }
        }
    }

    fun getTodosInit() {
        if (!todosInitLoaded) {
            getTodos()
            todosInitLoaded = true
        }
    }

    fun removeTodoDialogConfirmed() {
        _removeTodoDialogLiveData.value = true
    }

    fun removeTodo() {
        todoToRemoveID?. let {
            viewModelScope.launch(Dispatchers.IO) {
                _removeTodoLiveData.postValue(repo.removeTodo(it))
                todoToRemoveID = null
            }
        }
    }

}