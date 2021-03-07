package com.codeliner.achacha.mains

import android.app.Application
import com.codeliner.achacha.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber


class AchachaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@AchachaApplication)
            androidLogger()
            modules(
                listOf(
                    persistenceModule
                    , repositoryModules
                    , viewModelModules
                )
            )
        }
    }
}