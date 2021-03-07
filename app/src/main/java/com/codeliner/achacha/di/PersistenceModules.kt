package com.codeliner.achacha.di

import com.codeliner.achacha.data.AppDatabase
import org.koin.dsl.module


val persistenceModule = module {

    single { AppDatabase.getInstance(get()) }
}
