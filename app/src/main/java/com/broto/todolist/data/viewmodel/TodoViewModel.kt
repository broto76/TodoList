package com.broto.todolist.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.broto.todolist.data.TodoDAO
import com.broto.todolist.data.TodoDatabase
import com.broto.todolist.data.models.ToDoData
import com.broto.todolist.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application): AndroidViewModel(application) {

    private val todoDAO: TodoDAO
    private val repository: TodoRepository
    val getAllData: LiveData<List<ToDoData>>

    init {
        todoDAO = TodoDatabase.getDatabase(application).getTodoDAO()
        repository = TodoRepository(todoDAO)
        getAllData = repository.getAllData
    }

    fun insertData(data: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(data)
        }
    }

    fun updateData(data: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(data)
        }
    }

    fun deleteData(data: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(data)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchQuery(query: String) : LiveData<List<ToDoData>> {
        return repository.searchDataBase(query)
    }
}