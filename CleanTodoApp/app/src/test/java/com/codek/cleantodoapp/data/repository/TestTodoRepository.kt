package com.codek.cleantodoapp.data.repository

import com.codek.cleantodoapp.data.entity.ToDoEntity

class TestTodoRepository: TodoRepository{

    private val toDoList: MutableList<ToDoEntity> = mutableListOf()

    override suspend fun getToDoList(): List<ToDoEntity> {
        return toDoList
    }

    override suspend fun insertTodoList(toDoList: List<ToDoEntity>) {
        this.toDoList.addAll(toDoList)
    }

    override suspend fun updateTodoItem(todoItem: ToDoEntity): Boolean {
        val foundToDoEntity = toDoList.find { it.id == todoItem.id }
        if(foundToDoEntity == null) {
            return false
        } else {
            this.toDoList[toDoList.indexOf(foundToDoEntity)] = todoItem
            return true
        }
    }

    override suspend fun getToDoItem(itemId: Long): ToDoEntity? {
        return toDoList.find { it.id == itemId }
    }

    override suspend fun deleteAll() {
        toDoList.clear()
    }

}