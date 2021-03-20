package com.codeliner.achacha.ui.todos.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.data.todos.TodoRepository
import kotlinx.coroutines.*
import timber.log.Timber

class TodoDetailViewModel(
        private val repository: TodoRepository
): ViewModel() {
        private val viewModelJob = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        private val _todo = MutableLiveData<Todo>()
        val todo: LiveData<Todo> get() = _todo

        fun setTodo(inputTodo: Todo) {
                _todo.value = inputTodo
        }

        private val _onOpenTextInputJob = MutableLiveData<Boolean>()
        val onOpenTextInputJob: LiveData<Boolean> get() = _onOpenTextInputJob
        fun openTextInputJob() {
                _onOpenTextInputJob.value = true
        }
        fun openTextInputJobComplete() {
                _onOpenTextInputJob.value = false
        }

        fun todoMemoUpdate(memo: String?) {
                _todo.value?.let { oldTodo ->
                        uiScope.launch {
                                val newTodo = oldTodo.copy()
                                newTodo.memo = memo
                                _todo.value = newTodo
                                todoUpdate(newTodo)
                        }
                }
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
                _todo.value?.let { oldTodo ->
                        uiScope.launch {
                                val newTodo = oldTodo.copy()
                                newTodo.isFinished = !newTodo.isFinished
                                todoUpdate(newTodo)
                                _onBack.value = true
                        }
                }
        }

        private suspend fun todoUpdate(todo: Todo) {
                withContext(Dispatchers.IO) {
                        repository.updateTodo(todo)
                }
        }

        override fun onCleared() {
                super.onCleared()
                viewModelJob.cancel()
        }
}