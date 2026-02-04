package com.onedev.moviedb

import android.app.Application
import com.onedev.moviedb.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieDBApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@MovieDBApp)
            modules(appModule)
        }
    }
}
