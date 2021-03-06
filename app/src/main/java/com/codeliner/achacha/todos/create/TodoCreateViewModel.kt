package com.codeliner.achacha.todos.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.codeliner.achacha.domains.todos.Todo
import com.codeliner.achacha.domains.todos.TodoDatabaseDao
import com.codeliner.achacha.utils.Const
import kotlinx.coroutines.*
import timber.log.Timber

class TodoCreateViewModel(
    app: Application,
    private val dataSourceDao: TodoDatabaseDao,
    private val tasks: Int
): AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    
    private val _work = MutableLiveData<String>()
    val work: LiveData<String> get() = _work

    private val _hasError = MutableLiveData<String>()
    val hasError: LiveData<String> get() = _hasError

    fun discoveredError(error: String) {
        Timber.w("discoveredError: $error")
        _hasError.value = error
    }

    fun undiscoveredError() {
        _hasError.value = null
    }
    
    fun onSaveTodo() {
        when (work.value.isNullOrEmpty()) {
            true -> {

            }

            false -> {
                work.value?.let { newWork ->
                    uiScope.launch {
                        // note. validation work
                        val todo = Todo().apply {
                            work = newWork
                            help = helps.value
                            position = tasks
                        }
                        insert(todo)
                        _work.value = null
                    }
                }
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

    private val _helps = MutableLiveData("Nothing")
    val helps: LiveData<String> get() = _helps

    fun setHelps(helps: String) {
        _helps.value = helps
    }

    private val _onBackReady = MutableLiveData(false)
    val onBackReady: LiveData<Boolean> get() = _onBackReady
    fun backReady() {
        uiScope.launch {
            _onBackReady.value = true
            delay(Const.animDefaultDuration + 100L)
            backReadyComplete()
        }
    }

    fun backReadyComplete() {
        _onBackReady.value = false
        backStart()
    }

    private val _onBackStart = MutableLiveData(false)
    val onBackStart: LiveData<Boolean> get() = _onBackStart

    private fun backStart() {
        _onBackStart.value = true
    }

    fun backStartComplete() {
        _onBackStart.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}