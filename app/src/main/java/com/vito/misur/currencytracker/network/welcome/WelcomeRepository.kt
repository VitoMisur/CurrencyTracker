package com.vito.misur.currencytracker.network.welcome

import com.vito.misur.currencytracker.database.SupportedCurrenciesDao
import com.vito.misur.currencytracker.network.CurrencyAPIService
import com.vito.misur.currencytracker.network.data.Currency

class WelcomeRepository private constructor(
    private val currencyAPIService: CurrencyAPIService,
    private val supportedCurrenciesDao: SupportedCurrenciesDao
) {

    private suspend fun getSupportedSymbolsFromServer() = currencyAPIService
        .getSupportedSymbolsAsync()
        .await()

    private suspend fun insertAllSupportedCurrencies(currencyList: List<Currency>) =
        supportedCurrenciesDao.insertNewCurrencies(currencyList)

    fun getSupportedSymbolsFromDatabase() =
        supportedCurrenciesDao.getSupportedCurrencies()

    suspend fun fetchSupportedSymbols(symbolList: List<String>? = null): List<Currency> {
        getSupportedSymbolsFromServer().apply {
            insertAllSupportedCurrencies(symbols.map {
                Currency(
                    it.key,
                    it.value
                )
            })
        }
        return getSupportedSymbolsFromDatabase()
    }

    fun getMainCurrency() =
        supportedCurrenciesDao.getMainCurrencyLiveData()

    suspend fun setNewMainCurrency(currencyId: Long) =
        supportedCurrenciesDao.setAsMainCurrency(currencyId)

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: WelcomeRepository? = null

        fun getInstance(
            currencyAPIService: CurrencyAPIService,
            supportedCurrenciesDao: SupportedCurrenciesDao
        ) =
            instance ?: synchronized(this) {
                instance ?: WelcomeRepository(
                    currencyAPIService,
                    supportedCurrenciesDao
                ).also { instance = it }
            }
    }
}