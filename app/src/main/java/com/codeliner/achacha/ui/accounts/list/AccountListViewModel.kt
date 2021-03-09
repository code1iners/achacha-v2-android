package com.codeliner.achacha.ui.accounts.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codeliner.achacha.data.accounts.AccountRepository
import timber.log.Timber

class AccountListViewModel(
    private val accountRepository: AccountRepository
): ViewModel() {

    companion object {
        private val _onAccountCreate = MutableLiveData(false)
        val onAccountCreate: LiveData<Boolean> get() = _onAccountCreate
        fun accountCreateJob() {
            _onAccountCreate.value = true
        }
        fun accountCreateJobComplete() {
            _onAccountCreate.value = false
        }

        private val _onAccountClear = MutableLiveData(false)
        val onAccountClear: LiveData<Boolean> get() = _onAccountClear
        fun accountClearJob() {
            _onAccountClear.value = true
        }
        fun accountClearJobComplete() {
            _onAccountClear.value = false
        }

        private val _onAccountTest = MutableLiveData(false)
        val onAccountTest: LiveData<Boolean> get() = _onAccountTest
        fun accountTestJob() {
            _onAccountTest.value = true
        }
        fun accountTestJobComplete() {
            _onAccountTest.value = false
        }
    }

    private val _onNavigateToAccountCreate = MutableLiveData(false)
    val onNavigateToAccountCreate: LiveData<Boolean> get() = _onNavigateToAccountCreate
    fun navigateToAccountCreate() {
        _onNavigateToAccountCreate.value = true
    }
    fun navigateToAccountCreateComplete() {
        _onNavigateToAccountCreate.value = false
    }
}