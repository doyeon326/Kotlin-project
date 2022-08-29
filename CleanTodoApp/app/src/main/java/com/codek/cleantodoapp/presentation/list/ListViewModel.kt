package com.codek.cleantodoapp.presentation.list

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codek.cleantodoapp.data.entity.ToDoEntity
import com.codek.cleantodoapp.domain.todo.DeleteAllToDoItemUseCase
import com.codek.cleantodoapp.domain.todo.GetToDoListUseCase
import com.codek.cleantodoapp.domain.todo.UpdateToDoListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 필요한 UseCase
 * 1. [GetTodoListUseCase]
 * 2. [UpdateToDoUseCase]
 * 3. [DeleteAllTodoItemUsecase]
 * **/
internal class ListViewModel(
    private val getToDoListUseCase: GetToDoListUseCase,
    private val updateToDoUseCase: UpdateToDoListUseCase,
    private val deleteAllToDoItemUseCase: DeleteAllToDoItemUseCase
): ViewModel() {

    private var _todoListLiveData = MutableLiveData<ToDoListState>(ToDoListState.UnInitialized)
    val todoListLiveData: LiveData<ToDoListState> = _todoListLiveData

   // @SuppressLint("NullSafeMutableLiveData")
    fun fetchData(): Job = viewModelScope.launch {
       _todoListLiveData.postValue(ToDoListState.Loading)
       _todoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }

    fun updateEntity(toDoEntity: ToDoEntity) = viewModelScope.launch {
        updateToDoUseCase(toDoEntity)
    }

    fun deleteAll() = viewModelScope.launch {
        _todoListLiveData.postValue(ToDoListState.Loading)
        deleteAllToDoItemUseCase()
        _todoListLiveData.postValue(ToDoListState.Success(getToDoListUseCase()))
    }

}