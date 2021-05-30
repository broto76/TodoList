package com.broto.todolist.data.repository

import androidx.lifecycle.LiveData
import com.broto.todolist.data.TodoDAO
import com.broto.todolist.data.models.ToDoData

class TodoRepository(private val todoDAO: TodoDAO) {

    val getAllData: LiveData<List<ToDoData>> = todoDAO.getAllData()
    val getListSortedHighPriority = todoDAO.sortItemsByHighPriority()
    val getListSortedLowPriority = todoDAO.sortItemsByLowPriority()

    suspend fun insertData(data: ToDoData) {
        todoDAO.insertData(data)
    }

    suspend fun updateData(data: ToDoData) {
        todoDAO.updateData(data)
    }

    suspend fun deleteData(data: ToDoData) {
        todoDAO.deleteData(data)
    }

    suspend fun deleteAll() {
        todoDAO.deleteAll()
    }

    fun searchDataBase(searchQuery: String): LiveData<List<ToDoData>> {
        return todoDAO.searchDataBase(searchQuery)
    }
}