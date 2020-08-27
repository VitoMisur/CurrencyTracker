package com.vito.misur.currencytracker.di

import com.vito.misur.currencytracker.viewmodel.FavoritesViewModel
import com.vito.misur.currencytracker.viewmodel.HomeViewModel
import com.vito.misur.currencytracker.viewmodel.WelcomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun viewModelModule() = module {

    viewModel { HomeViewModel(get(), get()) }
    viewModel { WelcomeViewModel(get(), get()) }
    viewModel { FavoritesViewModel(get(), get()) }

}