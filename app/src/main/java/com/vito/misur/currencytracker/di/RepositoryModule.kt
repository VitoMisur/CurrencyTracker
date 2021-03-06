package com.vito.misur.currencytracker.di

import android.app.Application
import com.vito.misur.currencytracker.database.AppDatabase
import com.vito.misur.currencytracker.network.api.CurrencyAPIService
import com.vito.misur.currencytracker.repository.FavoritesRepository
import com.vito.misur.currencytracker.repository.HomeRepository
import com.vito.misur.currencytracker.repository.WelcomeRepository
import org.koin.dsl.module

fun repositoryModule() = module {

    single { provideWelcomeRepository(get(), get()) }
    single { provideFavoritesRepository(get(), get()) }
    single { provideHomeRepository(get(), get()) }
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

private fun provideHomeRepository(
    currencyAPIService: CurrencyAPIService,
    app: Application
): HomeRepository =
    HomeRepository.getInstance(
        currencyAPIService,
        AppDatabase.getInstance(app.applicationContext).favoriteCurrenciesDao(),
        AppDatabase.getInstance(app.applicationContext).supportedCurrenciesDao()
    )


