package com.codeliner.achacha.mains

import android.app.Application
import timber.log.Timber

class AchachaApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}