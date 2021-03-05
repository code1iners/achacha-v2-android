package com.codeliner.achacha.todos.list

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.codeliner.achacha.domains.todos.Todo
import com.codeliner.achacha.domains.todos.TodoDatabaseDao
import com.codeliner.achacha.utils.Const
import com.codeliner.achacha.utils.Date
import kotlinx.coroutines.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TodoListViewModel(
    app: Application,
    private val todoDatabaseDao: TodoDatabaseDao
): AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _date = MutableLiveData(Date())
    val date: LiveData<Date> get() = _date

    private val _todos = todoDatabaseDao.getAllOrderedByPosition()
    val todos: LiveData<List<Todo>> get() = _todos

    val tasks = Transformations.map(todos) {
        it.size
    }

    // note. fab
    private val _isFavCollapsed = MutableLiveData(true)
    val isFavCollapsed: LiveData<Boolean> get() = _isFavCollapsed

    private val _onNavigateToCreateTodoReady = MutableLiveData(false)
    val onNavigateToCreateTodoReady: LiveData<Boolean> get() = _onNavigateToCreateTodoReady

    fun onNavigateToCreateTodoReady() {
        _onNavigateToCreateTodoReady.value = true
    }

    fun navigateToCreateTodoReady() {
        uiScope.launch {
            // note. delay for animations in ui
            delay(Const.animDefaultDuration + 100L)
            navigateToCreateTodoProcess()
        }
    }

    private val _onNavigateToCreateTodoProcess = MutableLiveData(false)
    val onNavigateToCreateTodoProcess: LiveData<Boolean> get() = _onNavigateToCreateTodoProcess

    private fun navigateToCreateTodoProcess() {
        _onNavigateToCreateTodoProcess.value = true
    }

    fun navigateToCreateTodoProcessComplete() {
        _onNavigateToCreateTodoProcess.value = false
    }

    fun navigateToCreateTodoComplete() {
        _onNavigateToCreateTodoReady.value = false
    }

    fun switchCollapse() {
        when (isFavCollapsed.value) {
            true -> {
                _isFavCollapsed.value = false
            }
            false -> {
                _isFavCollapsed.value = true
            }
        }
    }
    
    fun onClearTodos() {
        viewModelScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            todoDatabaseDao.clear()
        }
    }

    fun onUpdateTodoIsFinished(todo: Todo) {
        viewModelScope.launch {
            val oldTodo = todo
            oldTodo.isFinished = !todo.isFinished
            update(oldTodo)
        }
    }

    private suspend fun update(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoDatabaseDao.update(todo)
        }
    }

    fun onUpdateTodos(todos: List<Todo>) {
        uiScope.launch {
            updateTodos(todos)
        }
    }

    private suspend fun updateTodos(todos: List<Todo>) {
        withContext(Dispatchers.IO) {
            todoDatabaseDao.updateTodos(todos)
        }
    }

    fun onRemoveTodo(todo: Todo) {
        viewModelScope.launch {
            remove(todo)
        }
    }

    private suspend fun remove(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoDatabaseDao.deleteTodoById(todo.id)
        }
    }

    private val _onTestTrigger = MutableLiveData<Boolean>()
    val onTestTrigger: LiveData<Boolean> get() = _onTestTrigger
    fun onTestStart() {
        _onTestTrigger.value = true
    }
    fun onTestComplete() {
        _onTestTrigger.value = false
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}
