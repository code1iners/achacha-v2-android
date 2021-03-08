package com.codeliner.achacha.mains

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_CLEAR
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_CREATE
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_TEST
import com.codeliner.achacha.utils.Const.ACTION_TODO_CLEAR
import com.codeliner.achacha.utils.Const.ACTION_TODO_CREATE
import com.codeliner.achacha.utils.Const.ACTION_TODO_TEST
import kotlinx.coroutines.*
import timber.log.Timber

class MainViewModel(
    val app: Application
): AndroidViewModel(app) {

    companion object {

        private val viewModelJob = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        // note. fabs
        private val _onFabAnimation = MutableLiveData<Boolean>()
        val onFabAnimation: LiveData<Boolean> get() = _onFabAnimation

        fun setFabAnimation(status: Boolean, duration: Long) {
            uiScope.launch {
                _onFabAnimation.value = status
                delay(duration)
                setFabVisibility(status)
            }
        }

        private val _onFabVisibility = MutableLiveData<Boolean>()
        val onFabVisibility: LiveData<Boolean> get() = _onFabVisibility
        private fun setFabVisibility(status: Boolean) {
            _onFabVisibility.value = status
        }
        fun setFabVisibilityComplete() {
            _onFabVisibility.value = null
        }

        // note. bottom navigation
        private val _onBottomNavigationAnimation = MutableLiveData<Boolean>()
        val onBottomNavigationAnimation: LiveData<Boolean> get() = _onBottomNavigationAnimation
        fun setBottomNavigationAnimation(status: Boolean, duration: Long) {
            uiScope.launch {
                _onBottomNavigationAnimation.value = status
                delay(duration)
                setBottomNavigationVisibility(status)
            }
        }

        private val _onBottomNavigationVisibility = MutableLiveData<Boolean>()
        val onBottomNavigationVisibility: LiveData<Boolean> get() = _onBottomNavigationVisibility
        private fun setBottomNavigationVisibility(status: Boolean) {
            _onBottomNavigationVisibility.value = status
        }
        fun setBottomNavigationVisibilityComplete() {
            _onBottomNavigationVisibility.value = null
        }
    }

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

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

    fun setFabCollapse(status: Boolean) {
        _isFavCollapsed.value = status
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

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}