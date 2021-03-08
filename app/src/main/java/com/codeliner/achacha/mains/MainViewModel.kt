package com.codeliner.achacha.mains

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeliner.achacha.ui.accounts.list.AccountListViewModel
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_CLEAR
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_CREATE
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_TEST
import com.codeliner.achacha.utils.Const.ACTION_TODO_CLEAR
import com.codeliner.achacha.utils.Const.ACTION_TODO_CREATE
import com.codeliner.achacha.utils.Const.ACTION_TODO_TEST
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
    
    private val _onClickCreateAction = MutableLiveData<String>()
    val onClickCreateAction: LiveData<String> get() = _onClickCreateAction
    
    fun favAction(action: String) {
        when (currentBottomNavPosition.value) {
            0 -> {
                when (action) {
                    "create" -> {
                        _onClickCreateAction.value = ACTION_TODO_CREATE
                    }

                    "clear" -> {
                        _onClickCreateAction.value = ACTION_TODO_CLEAR
                    }

                    "test" -> {
                        _onClickCreateAction.value = ACTION_TODO_TEST
                    }
                }
            }

            1 -> {
                when (action) {
                    "create" -> {
                        _onClickCreateAction.value = ACTION_ACCOUNT_CREATE
                    }

                    "clear" -> {
                        _onClickCreateAction.value = ACTION_ACCOUNT_CLEAR
                    }

                    "test" -> {
                        _onClickCreateAction.value = ACTION_ACCOUNT_TEST
                    }
                }
            }
        }
        _onClickCreateAction.value = null
    }

    fun favClear() {
        when (currentBottomNavPosition.value) {
            0 -> {}
            1 -> {}
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