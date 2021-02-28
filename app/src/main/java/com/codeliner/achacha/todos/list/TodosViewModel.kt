package com.codeliner.achacha.todos.list

import android.app.Application
import android.os.Build
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.transition.AutoTransition
import com.codeliner.achacha.R
import com.codeliner.achacha.mains.MainActivity
import com.codeliner.achacha.utils.Date
import com.example.helpers.ui.AnimationManager

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TodosViewModel(app: Application): AndroidViewModel(app) {

    private val _date = MutableLiveData<Date>()
    val date: LiveData<Date> get() = _date

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
        _date.value = Date()
        transition.duration = 300
        transition.interpolator = AccelerateDecelerateInterpolator()
        animHide.fillAfter = true
        animShow.fillAfter = true
        _onNavigateToCreateTodo.value = false
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
        val app: Application = getApplication()
        Toast.makeText(app.applicationContext, app.applicationContext.getString(R.string.preparing_service), Toast.LENGTH_SHORT).show()
    }
}
