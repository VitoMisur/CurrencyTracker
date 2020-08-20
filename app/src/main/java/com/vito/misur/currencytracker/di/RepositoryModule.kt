package com.vito.misur.currencytracker.di

import android.app.Application
import com.vito.misur.currencytracker.database.AppDatabase
import com.vito.misur.currencytracker.network.CurrencyAPIService
import com.vito.misur.currencytracker.network.HomeRepository
import com.vito.misur.currencytracker.network.favorites.FavoritesRepository
import com.vito.misur.currencytracker.network.welcome.WelcomeRepository
import org.koin.dsl.module

fun repositoryModule() = module {

    single { provideWelcomeRepository(get(), get()) }
    single { provideFavoritesRepository(get(), get()) }
    single { HomeRepository() }
}

private fun provideWelcomeRepository(
    currencyAPIService: CurrencyAPIService,
    app: Application
): WelcomeRepository =
    WelcomeRepository.getInstance(
        currencyAPIService,
        AppDatabase.getInstance(app.applicationContext).supportedCurrenciesDao()
    )

private fun provideFavoritesRepository(
    currencyAPIService: CurrencyAPIService,
    app: Application
): FavoritesRepository =
    FavoritesRepository.getInstance(
        currencyAPIService,
        AppDatabase.getInstance(app.applicationContext).favoriteCurrenciesDao(),
        AppDatabase.getInstance(app.applicationContext).supportedCurrenciesDao()
    )

