package com.vito.misur.currencytracker.di

import com.vito.misur.currencytracker.screen.favorites.FavoritesViewModel
import com.vito.misur.currencytracker.screen.home.HomeViewModel
import com.vito.misur.currencytracker.screen.welcome.WelcomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Provides basic app related components.
 */
fun appModule() = module(override = true) {
    viewModel { HomeViewModel(get()) }
    viewModel { WelcomeViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get()/*, get()*/) }
}

