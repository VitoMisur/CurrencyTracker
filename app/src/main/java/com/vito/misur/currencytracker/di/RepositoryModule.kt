package com.vito.misur.currencytracker.di

import android.app.Application
import com.vito.misur.currencytracker.database.AppDatabase
import com.vito.misur.currencytracker.network.api.CurrencyAPIService
import com.vito.misur.currencytracker.repository.FavoritesRepository
import com.vito.misur.currencytracker.repository.HomeRepository
import com.vito.misur.currencytracker.repository.WelcomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideWelcomeRepository(
        currencyAPIService: CurrencyAPIService,
        app: Application
    ): WelcomeRepository = getWelcomeRepository(
        currencyAPIService,
        app
    )

    @Singleton
    @Provides
    fun provideFavoritesRepository(
        currencyAPIService: CurrencyAPIService,
        app: Application
    ): FavoritesRepository = getFavoritesRepository(
        currencyAPIService,
        app
    )

    @Singleton
    @Provides
    fun provideHomeRepository(
        currencyAPIService: CurrencyAPIService,
        app: Application
    ): HomeRepository = getHomeRepository(
        currencyAPIService,
        app
    )

    private fun getWelcomeRepository(
        currencyAPIService: CurrencyAPIService,
        app: Application
    ): WelcomeRepository =
        WelcomeRepository.getInstance(
            currencyAPIService,
            AppDatabase.getInstance(app.applicationContext).supportedCurrenciesDao()
        )

    private fun getFavoritesRepository(
        currencyAPIService: CurrencyAPIService,
        app: Application
    ): FavoritesRepository =
        FavoritesRepository.getInstance(
            currencyAPIService,
            AppDatabase.getInstance(app.applicationContext).favoriteCurrenciesDao(),
            AppDatabase.getInstance(app.applicationContext).supportedCurrenciesDao()
        )

    private fun getHomeRepository(
        currencyAPIService: CurrencyAPIService,
        app: Application
    ): HomeRepository =
        HomeRepository.getInstance(
            currencyAPIService,
            AppDatabase.getInstance(app.applicationContext).favoriteCurrenciesDao(),
            AppDatabase.getInstance(app.applicationContext).supportedCurrenciesDao()
        )
}
