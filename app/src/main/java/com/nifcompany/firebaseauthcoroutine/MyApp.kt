package com.nifcompany.firebaseauthcoroutine

import android.app.Application
import com.nifcompany.firebaseauthcoroutine.module.firebaseViewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(firebaseViewModule)
        }
    }
}