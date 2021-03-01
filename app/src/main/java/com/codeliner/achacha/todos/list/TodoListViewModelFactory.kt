package com.codeliner.achacha.todos.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TodoListViewModelFactory(
    private val app: Application
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            return TodoListViewModel(
                app
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}