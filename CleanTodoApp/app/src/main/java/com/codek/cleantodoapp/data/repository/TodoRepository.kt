package com.codek.cleantodoapp.data.repository

import com.codek.cleantodoapp.data.entity.ToDoEntity

/**
 * 1. insertToDoList
 * 2. getToDoList
 * 3. updateTodoItem
 *
 * **/
interface TodoRepository {
    suspend fun getToDoList(): List<ToDoEntity>
    suspend fun insertTodoList(toDoList:List<ToDoEntity>)
    suspend fun updateTodoItem(todoItem: ToDoEntity) : Boolean
    suspend fun getToDoItem(itemId: Long): ToDoEntity?
    suspend fun deleteAll()
}