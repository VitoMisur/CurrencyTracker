package com.vito.misur.currencytracker

import android.app.Application
import com.vito.misur.currencytracker.di.appModule
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            debugConfig()
        }
        configJodaTime()
        configDI()
    }

    private fun configDI() {
        startKoin {

            androidContext(this@App)
            androidLogger()

            modules(appModule)
        }
    }

    private fun debugConfig() {
        Timber.plant(Timber.DebugTree())
        Timber.tag(BuildConfig.BUILD_TYPE)
    }

    private fun configJodaTime() {
        JodaTimeAndroid.init(this)
    }
}