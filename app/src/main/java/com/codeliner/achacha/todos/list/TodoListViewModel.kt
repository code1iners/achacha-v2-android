package com.codeliner.achacha.todos.list

import android.app.Application
import android.os.Build
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.transition.AutoTransition
import com.codeliner.achacha.R
import com.codeliner.achacha.domains.todos.Todo
import com.codeliner.achacha.domains.todos.TodoDatabaseDao
import com.codeliner.achacha.mains.MainActivity
import com.codeliner.achacha.utils.Date
import com.example.helpers.ui.AnimationManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TodoListViewModel(
    app: Application,
    private val todoDatabaseDao: TodoDatabaseDao
): AndroidViewModel(app) {

    private val _date = MutableLiveData<Date>()
    val date: LiveData<Date> get() = _date

    private val _todos = todoDatabaseDao.getAllTodos()
    val todos: LiveData<List<Todo>> get() = _todos

    val tasks = Transformations.map(todos) {
        it.size
    }

    // note. fab
    private val _isFavCollapsed = MutableLiveData<Boolean>()
    val isFavCollapsed: LiveData<Boolean> get() = _isFavCollapsed

    private val _onNavigateToCreateTodo = MutableLiveData<Boolean>()
    val onNavigateToCreateTodo: LiveData<Boolean> get() = _onNavigateToCreateTodo

    // note. animations
    val animLeft = AnimationManager.getRotateLeft45(app.applicationContext)
    val animRight = AnimationManager.getRotateRight45(app.applicationContext)
    val animHide = AnimationManager.getFadeOut(app.applicationContext)
    val animShow = AnimationManager.getFadeIn(app.applicationContext)
    val transition = AutoTransition()

    init {
        initDate()
        initBottomNav()
    }

    private fun initBottomNav() {
        transition.duration = 300
        transition.interpolator = AccelerateDecelerateInterpolator()
        animHide.fillAfter = true
        animShow.fillAfter = true
        _onNavigateToCreateTodo.value = false
    }

    private fun initDate() {
        _date.value = Date()
    }

    fun switchCollapse() {
        when (isFavCollapsed.value) {
            null -> {
                _isFavCollapsed.value = false
            }
            true -> {
                _isFavCollapsed.value = false
            }
            false -> {
                _isFavCollapsed.value = true
            }
        }
    }

    fun onNavigateToCreateTodo() {
        _onNavigateToCreateTodo.value = true
    }

    fun navigateToCreateTodoComplete() {
        _onNavigateToCreateTodo.value = false
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

    fun onUpdateTodo(todo: Todo) {
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
}
