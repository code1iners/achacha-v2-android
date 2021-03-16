package com.codeliner.achacha.ui.todos.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.data.todos.TodoRepository
import kotlinx.coroutines.*

class TodoDetailViewModel(
        private val repository: TodoRepository
): ViewModel() {
        private val viewModelJob = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        private val _todo = MutableLiveData<Todo>()
        val todo: LiveData<Todo> get() = _todo
        fun setTodo(todo: Todo) {
                _todo.value = todo.copy()
        }

        private val _onBack = MutableLiveData<Boolean>()
        val onBack: LiveData<Boolean> get() = _onBack
        fun backComplete() {
                _onBack.value = null
        }

        fun todoRemoveJob() {
                todo.value?.let { todo ->
                        uiScope.launch {
                                todoRemove(todo)
                                _onBack.value = true
                        }
                }
        }

        private suspend fun todoRemove(todo: Todo) {
                withContext(Dispatchers.IO) {
                        repository.deleteTodoById(todo)
                }
        }

        fun todoFinishJob() {
                todo.value?.let { oldTodo ->
                        uiScope.launch {
                                val isFinished = oldTodo.isFinished
                                oldTodo.isFinished = !isFinished
                                todoFinish(oldTodo)
                                _onBack.value = true
                        }
                }
        }

        private suspend fun todoFinish(todo: Todo) {
                withContext(Dispatchers.IO) {
                        repository.updateTodo(todo)
                }
        }

}