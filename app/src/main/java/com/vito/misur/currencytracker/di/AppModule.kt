package com.vito.misur.currencytracker.di

import android.app.Application
import android.content.SharedPreferences
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

    viewModel { HomeViewModel(get(), get()) }
    viewModel { WelcomeViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }
}

fun provideSharedPrefs(androidApplication: Application): SharedPreferences =
    androidApplication.getSharedPreferences(
        "com.vito.misur.currencytracker",
        android.content.Context.MODE_PRIVATE
    )