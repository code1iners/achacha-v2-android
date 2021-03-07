package com.codeliner.achacha.ui.accounts.list

import androidx.lifecycle.ViewModel
import com.codeliner.achacha.data.accounts.AccountRepository
import timber.log.Timber

class AccountListViewModel(
    private val accountRepository: AccountRepository
): ViewModel() {

    fun viewModelTest() {
        Timber.w("hello koin")
    }
}