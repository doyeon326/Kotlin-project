package com.codek.cleantodoapp.domain.todo

import com.codek.cleantodoapp.data.entity.ToDoEntity
import com.codek.cleantodoapp.data.repository.TodoRepository
import com.codek.cleantodoapp.domain.UseCase

internal class UpdateToDoListUseCase(
        private val todoRepository: TodoRepository
): UseCase {
        suspend operator fun invoke(toDoEntity: ToDoEntity): Boolean{
                return todoRepository.updateTodoItem(toDoEntity)
        }
}