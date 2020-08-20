package com.vito.misur.currencytracker.network.favorites

import com.vito.misur.currencytracker.database.FavoriteCurrenciesDao
import com.vito.misur.currencytracker.database.FavoriteCurrency
import com.vito.misur.currencytracker.database.SupportedCurrenciesDao
import com.vito.misur.currencytracker.network.CurrencyAPIService
import com.vito.misur.currencytracker.network.data.ExchangeRates

class FavoritesRepository(
    private val currencyAPIService: CurrencyAPIService,
    private val favoriteCurrenciesDao: FavoriteCurrenciesDao,
    private val supportedCurrenciesDao: SupportedCurrenciesDao
) {

    fun receiveAvailableCurrencies() =
        favoriteCurrenciesDao.getAvailableCurrencies()

    suspend fun insertAvailableCurrenciesList(availableCurrencies: List<FavoriteCurrency>) =
        favoriteCurrenciesDao.repopulateFavorites(availableCurrencies)

    suspend fun fetchAvailableCurrencies(symbolList: List<String>? = null): ExchangeRates {
        val symbol = supportedCurrenciesDao.getMainCurrencySymbol()
        return currencyAPIService
            .getLatestExchangeRates(
                baseCurrencySymbol = symbol,
                symbols = symbolList
            )
            .await()
    }

    fun fetchCurrencyFavoriteStatus(currencyId: Long, isFavorite: Boolean) =
        favoriteCurrenciesDao.setCurrencyAsFavorite(currencyId, isFavorite)

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: FavoritesRepository? = null

        fun getInstance(
            currencyAPIService: CurrencyAPIService,
            favoriteCurrenciesDao: FavoriteCurrenciesDao,
            supportedCurrenciesDao: SupportedCurrenciesDao
        ) =
            instance ?: synchronized(this) {
                instance ?: FavoritesRepository(
                    currencyAPIService,
                    favoriteCurrenciesDao,
                    supportedCurrenciesDao
                ).also { instance = it }
            }
    }
}
