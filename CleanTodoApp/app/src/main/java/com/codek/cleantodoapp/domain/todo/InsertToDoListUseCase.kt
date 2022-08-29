package com.codek.cleantodoapp.domain.todo

import com.codek.cleantodoapp.data.entity.ToDoEntity
import com.codek.cleantodoapp.data.repository.TodoRepository
import com.codek.cleantodoapp.domain.UseCase

internal class  InsertToDoListUseCase(
    private val todoRepository: TodoRepository
): UseCase{

    suspend operator fun invoke(todoList: List<ToDoEntity>) {
        return todoRepository.insertTodoList(todoList)
    }
}