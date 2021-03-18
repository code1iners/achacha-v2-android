package com.codeliner.achacha.ui

import androidx.lifecycle.ViewModel
import com.codeliner.achacha.data.todos.TodoRepository

class TextInputViewModel(
    private val repository: TodoRepository
): ViewModel() {
}