package com.codeliner.achacha.ui.accounts.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeliner.achacha.data.accounts.Account
import com.codeliner.achacha.data.accounts.AccountDatabaseDao
import com.codeliner.achacha.data.accounts.AccountRepository
import com.codeliner.achacha.utils.Const
import com.codeliner.achacha.utils.Const.ANIMATION_DURATION_SHORT
import kotlinx.coroutines.*
import timber.log.Timber

class AccountCreateViewModel(
    private val repository: AccountRepository
): ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    // note. back
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

    // note. account value
    private val _onAccountValue = MutableLiveData(Account())
    val onAccountValue: LiveData<Account> get() = _onAccountValue
    fun setAccountValue(key: String, value: CharSequence?) {
        _onAccountValue.value?.let { account ->
            val newAccount = account.copy()
            when (key) {
                "title" -> { newAccount.title = value.toString() }
                "subtitle" -> { newAccount.subtitle = value.toString() }
                "username" -> { newAccount.username = value.toString() }
                "password" -> { newAccount.password = value.toString() }
                "hint" -> { newAccount.hint = value.toString() }
            }

            _onAccountValue.value = newAccount
        }
    }

    fun createAccountJob() {
        onAccountValue.value?.let {
            uiScope.launch {
                val account = it.copy()
                createAccount(account)
            }
        }
    }

    private suspend fun createAccount(account: Account) {
        withContext(Dispatchers.IO) {
            repository.createAccount(account)
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}