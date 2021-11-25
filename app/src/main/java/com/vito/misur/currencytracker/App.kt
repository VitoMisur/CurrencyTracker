package com.vito.misur.currencytracker

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber

const val baseUrl = "http://data.fixer.io/api/"

@HiltAndroidApp()
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            debugConfig()
        }
        configJodaTime()
        configDI()
    }

    private fun configDI() {

    }

    private fun debugConfig() {
        Timber.plant(Timber.DebugTree())
        Timber.tag(BuildConfig.BUILD_TYPE)
    }

    private fun configJodaTime() {
        JodaTimeAndroid.init(this)
    }
}