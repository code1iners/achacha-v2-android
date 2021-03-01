package com.codeliner.achacha.todos.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeliner.achacha.domains.todos.Todo
import com.codeliner.achacha.domains.todos.TodoDatabaseDao
import kotlinx.coroutines.*

class TodoCreateViewModel(
    private val app: Application,
    private val dataSourceDao: TodoDatabaseDao
): AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    
    private val _work = MutableLiveData<String>()
    val work: LiveData<String> get() = _work

    
    fun onSaveTodo() {
        uiScope.launch {
            // note. validation work
            work.value?.let {
                insert(Todo().apply {
                    work = it
                    help = "Test message"
                })
            }

            _work.value = ""
        }
    }

    private suspend fun insert(todo: Todo) {
        withContext(Dispatchers.IO) {
            dataSourceDao.insert(todo)
        }
    }
    
    fun updateWork(work: String) {
        _work.value = work
    }
}