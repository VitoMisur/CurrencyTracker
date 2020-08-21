package com.vito.misur.currencytracker.network.welcome

import com.vito.misur.currencytracker.database.SupportedCurrenciesDao
import com.vito.misur.currencytracker.network.CurrencyAPIService
import com.vito.misur.currencytracker.network.data.Currency

class WelcomeRepository private constructor(
    private val currencyAPIService: CurrencyAPIService,
    private val supportedCurrenciesDao: SupportedCurrenciesDao
) {

    suspend fun fetchSymbols() = currencyAPIService
        .getSupportedSymbolsAsync()
        .await()

    suspend fun insertAll(currencyList: List<Currency>) =
        supportedCurrenciesDao.insertNewCurrencies(currencyList)

    fun getSupportedCurrencies() =
        supportedCurrenciesDao.getSupportedCurrencies()

    fun getMainCurrency() =
        supportedCurrenciesDao.getMainCurrency()

    suspend fun fetchMainCurrency(currencyId: Long) {
        supportedCurrenciesDao.setAsMainCurrency(currencyId)
    }

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