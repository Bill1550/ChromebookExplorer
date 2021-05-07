package com.loneoaktech.apps.androidApp

import android.app.Application
import timber.log.Timber

class ExplorerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}