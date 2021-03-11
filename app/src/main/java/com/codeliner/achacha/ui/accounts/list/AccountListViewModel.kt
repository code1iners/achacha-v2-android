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

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // note. fab create
    private val _onNavigateToAccountCreateAnimation = MutableLiveData(false)
    val onNavigateToAccountCreateAnimation: LiveData<Boolean> get() = _onNavigateToAccountCreateAnimation
    fun navigateToAccountCreateAnimation() {
        uiScope.launch {
            setNavigateToAccountCreateAnimation(true)
            delay(ANIMATION_DURATION_SHORT + 100L)
            navigateToAccountCreateAnimationComplete()
        }
    }
    private suspend fun setNavigateToAccountCreateAnimation(status: Boolean) {
        withContext(Dispatchers.Main) {
            _onNavigateToAccountCreateAnimation.value = status
        }
    }
    private fun navigateToAccountCreateAnimationComplete() {
        uiScope.launch {
            navigateToAccountCreateJob()
            setNavigateToAccountCreateAnimation(false)
        }
    }

    private val _onNavigateToAccountCreateJob = MutableLiveData(false)
    val onNavigateToAccountCreateJob: LiveData<Boolean> get() = _onNavigateToAccountCreateJob
    private suspend fun navigateToAccountCreateJob() {
        withContext(Dispatchers.Main) {
            _onNavigateToAccountCreateJob.value = true
        }
    }
    fun navigateToAccountCreateJobComplete() {
        _onNavigateToAccountCreateJob.value = false
    }

    // note. data
    val accounts = accountRepository.readAllOrderedById()

    fun clearAccounts() {
        uiScope.launch {
            clearAccountsJob()
        }
    }

    private suspend fun clearAccountsJob() {
        withContext(Dispatchers.IO) {
            accountRepository.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object {

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
}