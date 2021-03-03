package com.codeliner.achacha.todos.create

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeliner.achacha.domains.todos.Todo
import com.codeliner.achacha.domains.todos.TodoDatabaseDao
import com.codeliner.achacha.utils.KeyboardManager
import kotlinx.coroutines.*
import timber.log.Timber

class TodoCreateViewModel(
    private val app: Application,
    private val dataSourceDao: TodoDatabaseDao
): AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    
    private val _work = MutableLiveData<String>()
    val work: LiveData<String> get() = _work
    
    fun onSaveTodo() {
        work.value?.let { newWork ->
            uiScope.launch {
            // note. validation work
                insert(Todo().apply {
                    work = newWork
                    help = "Test message"
                })
                _work.value = null
            }
        }
    }

    private suspend fun insert(todo: Todo) {
        withContext(Dispatchers.IO) {
            dataSourceDao.insert(todo)
        }
    }
    
    fun updateWork(text: String) {
        _work.value = text
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}