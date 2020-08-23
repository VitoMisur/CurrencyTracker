package com.vito.misur.currencytracker.network.favorites

import com.vito.misur.currencytracker.custom.toScaledDouble
import com.vito.misur.currencytracker.database.FavoriteCurrenciesDao
import com.vito.misur.currencytracker.database.FavoriteCurrency
import com.vito.misur.currencytracker.database.SupportedCurrenciesDao
import com.vito.misur.currencytracker.network.CurrencyAPIService

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
        favoriteCurrenciesDao.getAvailableCurrencies()

    fun getAvailableCurrenciesFromDatabaseFiltered(searchQuery: String) =
        favoriteCurrenciesDao.getAvailableCurrenciesFiltered("%$searchQuery%")

    private suspend fun insertAvailableCurrenciesList(availableCurrencies: List<FavoriteCurrency>) =
        favoriteCurrenciesDao.repopulateFavorites(availableCurrencies)

    private suspend fun getAvailableCurrenciesFromServer(symbolList: List<String>? = null) =
        currencyAPIService
            .getLatestExchangeRatesAsync(
                symbols = symbolList
            )
            .await()

    fun fetchCurrencyFavoriteStatus(currencyId: Long, isFavorite: Boolean): Int =
        favoriteCurrenciesDao.setCurrencyAsFavorite(currencyId, isFavorite)

    suspend fun getAvailableCurrencies(symbolList: List<String>? = null): List<FavoriteCurrency> {
        getAvailableCurrenciesFromServer().let { response ->
            response.exchangeRates?.let { exchangeRates ->
                exchangeRates.map { exchangeRate ->
                    FavoriteCurrency(
                        symbol = exchangeRate.key,
                        name = supportedCurrenciesDao.getCurrencyName(exchangeRate.key),
                        exchangeRate = exchangeRate.value.toScaledDouble(),
                        baseCurrency = response.baseCurrency
                    )
                }
            }?.let { list ->
                insertAvailableCurrenciesList(list)
            }
        }
        favoriteCurrenciesDao.updateAllAvailableCurrenciesWithCalculatedValues(
            supportedCurrenciesDao.getMainCurrencySymbol()
        )
        return getAvailableCurrenciesFromDatabase()
    }

    fun getMainCurrencySymbol() =
        supportedCurrenciesDao.getMainCurrencySymbol()

}
