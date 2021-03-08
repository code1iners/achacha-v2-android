package com.codeliner.achacha.mains

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeliner.achacha.ui.accounts.list.AccountListViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

class MainViewModel(
    app: Application
): AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun test() {
        Timber.w("hello i am main view model")
    }

    private val _isBottomNavigationShowing = MutableLiveData(true)
    val isBottomNavigationShowing: LiveData<Boolean> get() = _isBottomNavigationShowing

    fun setBottomNavigationShowing(status: Boolean) {
        _isBottomNavigationShowing.value = status
    }

    private val _currentBottomNavPosition = MutableLiveData(0)
    val currentBottomNavPosition: LiveData<Int> get() = _currentBottomNavPosition
    fun setCurrentBottomNavPosition(position: Int) {
        _currentBottomNavPosition.value = position
    }

    private val _isFavCollapsed = MutableLiveData(true)
    val isFavCollapsed: LiveData<Boolean> get() = _isFavCollapsed

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

    fun favTestButtonJob() {
        Timber.d("hello i am test button")
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}