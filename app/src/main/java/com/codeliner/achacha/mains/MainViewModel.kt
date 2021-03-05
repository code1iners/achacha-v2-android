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

    private val _isBottomNavigationShowing = MutableLiveData(true)
    val isBottomNavigationShowing: LiveData<Boolean> get() = _isBottomNavigationShowing
    fun onBottomNavigationShowingSwitch() {
        when (isBottomNavigationShowing.value) {
            true -> _isBottomNavigationShowing.value = false
            false -> _isBottomNavigationShowing.value = true
        }
    }

    fun setBottomNavigationShowing(status: Boolean) {
        _isBottomNavigationShowing.value = status
    }
}

val mainViewModel = module {
    viewModel { MainViewModel(get()) }
}