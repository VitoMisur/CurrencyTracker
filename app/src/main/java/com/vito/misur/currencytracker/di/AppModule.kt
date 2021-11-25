package com.vito.misur.currencytracker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton


/**
 * Provides basic app related components.
 */
@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideSharedPreferences(app: Application): SharedPreferences = getSharedPreferences(app)

    @Singleton
    @Provides
    fun provideSharedPreferencesEditor(app: Application): SharedPreferences.Editor =
        getSharedPreferences(app).edit()

    @Provides
    fun provideConnectivityManager(app: Application): ConnectivityManager =
        getConnectivityManager(app)

    private fun getSharedPreferences(androidApplication: Application): SharedPreferences =
        androidApplication.getSharedPreferences(
            "com.vito.misur.currencytracker",
            Context.MODE_PRIVATE
        )

    private fun getConnectivityManager(androidApplication: Application) =
        (androidApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)

}



