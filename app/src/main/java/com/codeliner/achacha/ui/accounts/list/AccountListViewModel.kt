package com.codeliner.achacha.ui.accounts.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeliner.achacha.data.accounts.AccountRepository
import com.codeliner.achacha.utils.Const.ANIMATION_DURATION_SHORT
import kotlinx.coroutines.*
import timber.log.Timber

class AccountListViewModel(
    private val accountRepository: AccountRepository
): ViewModel() {

    companion object {

        private val viewModelJob = Job()
        private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

        // note. fab create
        private val _onAccountCreate = MutableLiveData(false)
        val onAccountCreate: LiveData<Boolean> get() = _onAccountCreate
        fun accountCreateJob() {
            _onAccountCreate.value = true
        }
        fun accountCreateJobComplete() {
            _onAccountCreate.value = false
        }

        // note. fab clear
        private val _onAccountClear = MutableLiveData(false)
        val onAccountClear: LiveData<Boolean> get() = _onAccountClear
        fun accountClearJob() {
            _onAccountClear.value = true
        }
        fun accountClearJobComplete() {
            _onAccountClear.value = false
        }

        // note. fab test
        private val _onAccountTest = MutableLiveData(false)
        val onAccountTest: LiveData<Boolean> get() = _onAccountTest
        fun accountTestJob() {
            _onAccountTest.value = true
        }
        fun accountTestJobComplete() {
            _onAccountTest.value = false
        }
    }

    // note. fab create
    private val _onNavigateToAccountCreateAnimation = MutableLiveData(false)
    val onNavigateToAccountCreateAnimation: LiveData<Boolean> get() = _onNavigateToAccountCreateAnimation
    fun navigateToAccountCreateAnimation() {
        uiScope.launch {
            _onNavigateToAccountCreateAnimation.value = true
            delay(ANIMATION_DURATION_SHORT + 100L)
            navigateToAccountCreateAnimationComplete()
        }
    }
    private fun navigateToAccountCreateAnimationComplete() {
        _onNavigateToAccountCreateAnimation.value = false
        navigateToAccountCreateJob()
    }

    private val _onNavigateToAccountCreateJob = MutableLiveData(false)
    val onNavigateToAccountCreateJob: LiveData<Boolean> get() = _onNavigateToAccountCreateJob
    private fun navigateToAccountCreateJob() {
        _onNavigateToAccountCreateJob.value = true
    }
    fun navigateToAccountCreateJobComplete() {
        _onNavigateToAccountCreateJob.value = false
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}