package com.codeliner.achacha.todos

import android.app.Application
import android.os.Build
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.transition.AutoTransition
import com.codeliner.achacha.R
import com.codeliner.achacha.utils.Date
import com.example.helpers.ui.AnimationManager
import timber.log.Timber

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class TodosViewModel(app: Application): AndroidViewModel(app) {

    private val _date = MutableLiveData<Date>()
    val date: LiveData<Date> get() = _date

    // note. fab
    private val _isFavCollapsed = MutableLiveData<Boolean>()
    val isFavCollapsed: LiveData<Boolean> get() = _isFavCollapsed

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
}
