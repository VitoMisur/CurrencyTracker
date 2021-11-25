package com.vito.misur.currencytracker.di

import com.vito.misur.currencytracker.viewmodel.FavoritesViewModel
import com.vito.misur.currencytracker.viewmodel.HomeViewModel
import com.vito.misur.currencytracker.viewmodel.WelcomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun viewModelModule() = module {

    viewModel(named("home")) { HomeViewModel(get(), get()) }
    viewModel { (activitySource: WelcomeViewModel.ActivitySource) ->
        WelcomeViewModel(
            get(),
            get(),
            activitySource
        )
    }
    viewModel { FavoritesViewModel(get(), get()) }

}