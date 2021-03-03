package com.codeliner.achacha.todos.create

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codeliner.achacha.domains.todos.TodoDatabaseDao

class TodoCreateViewModelFactory(
    private val app: Application,
    private val dataSourceDao: TodoDatabaseDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoCreateViewModel::class.java)) {
            return TodoCreateViewModel(
                app,
                dataSourceDao
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}