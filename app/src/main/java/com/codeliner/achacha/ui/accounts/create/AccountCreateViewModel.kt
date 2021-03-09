package com.codeliner.achacha.ui.accounts.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeliner.achacha.data.accounts.AccountDatabaseDao
import com.codeliner.achacha.data.accounts.AccountRepository
import com.codeliner.achacha.utils.Const
import com.codeliner.achacha.utils.Const.ANIMATION_DURATION_SHORT
import kotlinx.coroutines.*

class AccountCreateViewModel(
    repository: AccountRepository
): ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _onBackReady = MutableLiveData(false)
    val onBackReady: LiveData<Boolean> get() = _onBackReady
    fun backReady() {
        uiScope.launch {
            _onBackReady.value = true
            delay(ANIMATION_DURATION_SHORT + 100L)
            backReadyComplete()
        }
    }
    private fun backReadyComplete() {
        _onBackReady.value = false
        backJob()
    }

    // note. back
    private val _onBackJob = MutableLiveData(false)
    val onBackJob: LiveData<Boolean> get() = _onBackJob
    private fun backJob() {
        _onBackJob.value = true
    }
    fun backJobComplete() {
        _onBackJob.value = false
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}