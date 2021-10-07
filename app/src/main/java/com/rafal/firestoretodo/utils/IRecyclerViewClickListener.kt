package com.rafal.firestoretodo.utils

import com.rafal.firestoretodo.model.Todo

interface IRecyclerViewClickListener {
    fun click(todo: Todo)
    fun longClick(id: String)
}