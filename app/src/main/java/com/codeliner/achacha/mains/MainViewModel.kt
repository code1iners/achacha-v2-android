package com.codeliner.achacha.mains

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

class MainViewModel(
    app: Application
): AndroidViewModel(app) {

    private val _isBottomNavigationShowing = MutableLiveData<Boolean>()
    val isBottomNavigationShowing: LiveData<Boolean> get() = _isBottomNavigationShowing
    fun onBottomNavigationShowingSwitch() {
        isBottomNavigationShowing.value?.let {
            _isBottomNavigationShowing.value = !it
        }
    }

    init {
        _isBottomNavigationShowing.value = true
    }
}

val mainViewModel = module {
    viewModel { MainViewModel(get()) }
}