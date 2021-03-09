package com.codeliner.achacha.di

import com.codeliner.achacha.mains.MainViewModel
import com.codeliner.achacha.ui.accounts.create.AccountCreateViewModel
import com.codeliner.achacha.ui.accounts.list.AccountListViewModel
import com.codeliner.achacha.ui.todos.create.TodoCreateViewModel
import com.codeliner.achacha.ui.todos.list.TodoListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModules = module {

    viewModel { MainViewModel(get()) }

    viewModel { TodoListViewModel(get(), get()) }

    viewModel { TodoCreateViewModel(get(), get()) }

    viewModel { AccountListViewModel(get()) }

    viewModel { AccountCreateViewModel(get()) }
}
