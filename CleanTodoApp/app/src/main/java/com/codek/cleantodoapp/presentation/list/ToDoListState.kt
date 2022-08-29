package com.codek.cleantodoapp.presentation.list

import com.codek.cleantodoapp.data.entity.ToDoEntity

sealed class ToDoListState {
    object UnInitialized: ToDoListState()
    object Loading: ToDoListState()

    data class Success(
        val todoList: List<ToDoEntity>
    ): ToDoListState()

    object Error: ToDoListState()
}