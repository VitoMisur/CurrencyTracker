package com.vito.misur.currencytracker.network.favorites

import com.vito.misur.currencytracker.custom.toScaledDouble
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

    fun getAvailableCurrenciesFromDatabase() =
        favoriteCurrenciesDao.getAvailableCurrencies(supportedCurrenciesDao.getMainCurrencySymbol())

    fun getAvailableCurrenciesFromDatabaseFiltered(searchQuery: String) =
        favoriteCurrenciesDao.getAvailableCurrenciesFiltered(
            supportedCurrenciesDao.getMainCurrencySymbol(),
            "%$searchQuery%"
        )

    private suspend fun insertAvailableCurrenciesList(availableCurrencies: List<FavoriteCurrency>) =
        favoriteCurrenciesDao.repopulateFavorites(availableCurrencies)

    private suspend fun getAvailableCurrenciesFromServer(symbolList: List<String>? = null): ExchangeRates? {
        val symbol = supportedCurrenciesDao.getMainCurrencySymbol()
        return currencyAPIService
            .getLatestExchangeRatesAsync(
                baseCurrencySymbol = symbol,
                symbols = symbolList
            )
            .await()
    }

    fun fetchCurrencyFavoriteStatus(currencyId: Long, isFavorite: Boolean): Int =
        favoriteCurrenciesDao.setCurrencyAsFavorite(currencyId, isFavorite)

    suspend fun getAvailableCurrencies(symbolList: List<String>? = null): List<FavoriteCurrency> {
        getAvailableCurrenciesFromServer()?.let {
            it.exchangeRates?.let { exchangeRates ->
                exchangeRates.map { exchangeRate ->
                    FavoriteCurrency(
                        symbol = exchangeRate.key,
                        exchangeRate = exchangeRate.value.toScaledDouble(),
                        baseCurrency = it.baseCurrency
                    )
                }
            }?.let { list ->
                insertAvailableCurrenciesList(list)
            }
        }
        return getAvailableCurrenciesFromDatabase()
    }

    fun getMainCurrencySymbol() =
        supportedCurrenciesDao.getMainCurrencySymbol()

}
