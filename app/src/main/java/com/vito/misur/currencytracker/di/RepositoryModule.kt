package com.vito.misur.currencytracker.di

import com.vito.misur.currencytracker.network.HomeRepository
import com.vito.misur.currencytracker.network.favorites.FavoritesRepository
import com.vito.misur.currencytracker.network.welcome.WelcomeRepository
import org.koin.dsl.module

fun repositoryModule() = module {
    single { WelcomeRepository(get()) }
    single { FavoritesRepository() }
    single { HomeRepository() }
}