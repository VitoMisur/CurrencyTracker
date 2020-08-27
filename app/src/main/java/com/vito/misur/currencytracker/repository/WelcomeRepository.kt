package com.vito.misur.currencytracker.repository

import com.vito.misur.currencytracker.custom.convertToCurrencyItemList
import com.vito.misur.currencytracker.database.dao.SupportedCurrenciesDao
import com.vito.misur.currencytracker.database.entity.Currency
import com.vito.misur.currencytracker.network.api.CurrencyAPIService
import com.vito.misur.currencytracker.view.data.CurrencyItem

class WelcomeRepository private constructor(
    private val currencyAPIService: CurrencyAPIService,
    private val supportedCurrenciesDao: SupportedCurrenciesDao
) {

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

    private suspend fun getSupportedSymbolsFromServer() = currencyAPIService
        .getSupportedSymbolsAsync()
        .await()

    private suspend fun insertAllSupportedCurrencies(currencyList: List<Currency>) =
        supportedCurrenciesDao.insertNewCurrencies(currencyList)

    fun getSupportedSymbolsFromDatabase() =
        supportedCurrenciesDao.getSupportedCurrencies().convertToCurrencyItemList()

    suspend fun fetchSupportedSymbols(symbolList: List<String>? = null): List<CurrencyItem> {
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
}