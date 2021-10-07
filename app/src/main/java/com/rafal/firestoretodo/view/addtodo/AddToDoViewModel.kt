package com.rafal.firestoretodo.view.addtodo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafal.firestoretodo.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToDoViewModel @Inject constructor(
    private val repo: AddToDoRepository
) : ViewModel() {

    private val _addTodoLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val addToDoLiveData: LiveData<Boolean> = _addTodoLiveData

    fun addToDo(todo: Todo) {
        viewModelScope.launch {
            val result = repo.addToDo(todo)
            _addTodoLiveData.postValue(result)
        }
    }
}