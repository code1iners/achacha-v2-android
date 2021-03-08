package com.codeliner.achacha.ui.todos.list

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.data.todos.TodoRepository
import com.codeliner.achacha.utils.Const
import com.codeliner.achacha.utils.Date
import kotlinx.coroutines.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TodoListViewModel(
    app: Application,
    private val todoRepository: TodoRepository
): AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _date = MutableLiveData(Date())
    val date: LiveData<Date> get() = _date

    val todos = todoRepository.readAllOrderedById()

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

//    fun switchCollapse() {
//        when (isFavCollapsed.value) {
//            true -> {
//                _isFavCollapsed.value = false
//            }
//            false -> {
//                _isFavCollapsed.value = true
//            }
//        }
//    }
    
    fun onClearTodos() {
        viewModelScope.launch {
            clear()
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            todoRepository.clear()
        }
    }

    fun onUpdateTodoIsFinished(todo: Todo) {
        uiScope.launch {
            update(todo)
        }
    }

    private suspend fun update(todo: Todo) {
        withContext(Dispatchers.IO) {
            val oldTodo = todo.copy()
            oldTodo.isFinished = !oldTodo.isFinished
            todoRepository.updateTodo(oldTodo)
        }
    }

    fun onUpdateTodos(todos: List<Todo>) {
        uiScope.launch {
            updateTodos(todos)
        }
    }

    private suspend fun updateTodos(todos: List<Todo>) {
        withContext(Dispatchers.IO) {
            todoRepository.updateTodos(todos)
        }
    }

    fun onRemoveTodo(todo: Todo) {
        viewModelScope.launch {
            remove(todo)
        }
    }

    private suspend fun remove(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoRepository.deleteTodoById(todo)
        }
    }

    private val _onTestTrigger = MutableLiveData<Boolean>()
    val onTestTrigger: LiveData<Boolean> get() = _onTestTrigger
    fun onTestStart() {
        uiScope.launch {

            _onTestTrigger.value = true
        }
    }
    fun onTestComplete() {
        _onTestTrigger.value = false
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}
