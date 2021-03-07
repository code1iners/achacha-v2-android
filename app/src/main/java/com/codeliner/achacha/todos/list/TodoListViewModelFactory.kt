package com.codeliner.achacha.todos.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codeliner.achacha.data.todos.TodoDatabaseDao
import com.codeliner.achacha.data.todos.TodoRepository

class TodoListViewModelFactory(
    private val app: Application,
    private val todoRepository: TodoRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            return TodoListViewModel(
                app,
                todoRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}