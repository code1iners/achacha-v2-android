package com.codeliner.achacha.di

import com.codeliner.achacha.data.accounts.AccountRepository
import com.codeliner.achacha.data.todos.TodoRepository
import org.koin.dsl.module


val repositoryModules = module {

    single { TodoRepository(get()) }

    single { AccountRepository(get()) }
}
