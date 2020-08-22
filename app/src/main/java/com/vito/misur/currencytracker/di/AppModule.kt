package com.vito.misur.currencytracker.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import com.vito.misur.currencytracker.screen.favorites.FavoritesViewModel
import com.vito.misur.currencytracker.screen.home.HomeViewModel
import com.vito.misur.currencytracker.screen.welcome.WelcomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Provides basic app related components.
 */
fun appModule() = module(override = true) {

    single { provideSharedPrefs(get()) }
    single<SharedPreferences.Editor> { provideSharedPrefs(get()).edit() }
    single { provideConnectivityManager(get()) }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { WelcomeViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
}

fun provideSharedPrefs(androidApplication: Application): SharedPreferences =
    androidApplication.getSharedPreferences(
        "com.vito.misur.currencytracker",
        Context.MODE_PRIVATE
    )

fun provideConnectivityManager(androidApplication: Application) =
    (androidApplication.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)





