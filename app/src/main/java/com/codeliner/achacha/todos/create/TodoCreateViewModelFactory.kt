package com.codeliner.achacha.todos.create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codeliner.achacha.data.todos.TodoDatabaseDao
import com.codeliner.achacha.data.todos.TodoRepository

class TodoCreateViewModelFactory(
    private val app: Application,
    private val todoRepository: TodoRepository,
    private val tasks: Int
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoCreateViewModel::class.java)) {
            return TodoCreateViewModel(
                app,
                todoRepository,
                tasks
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}