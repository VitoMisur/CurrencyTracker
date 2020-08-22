package com.vito.misur.currencytracker.network

import com.vito.misur.currencytracker.database.FavoriteCurrenciesDao
import com.vito.misur.currencytracker.database.SupportedCurrenciesDao

class HomeRepository(
    private val currencyAPIService: CurrencyAPIService,
    private val favoriteCurrenciesDao: FavoriteCurrenciesDao,
    private val supportedCurrenciesDao: SupportedCurrenciesDao
) {

    suspend fun fetchConversion(
        mainCurrencySymbol: String,
        convertibleCurrencySymbol: String,
        amount: String
    ) = currencyAPIService.getConversionResultAsync(
        from = mainCurrencySymbol,
        to = convertibleCurrencySymbol,
        amount = amount
    ).await()

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: HomeRepository? = null

        fun getInstance(
            currencyAPIService: CurrencyAPIService,
            favoriteCurrenciesDao: FavoriteCurrenciesDao,
            supportedCurrenciesDao: SupportedCurrenciesDao
        ) =
            instance ?: synchronized(this) {
                instance ?: HomeRepository(
                    currencyAPIService,
                    favoriteCurrenciesDao,
                    supportedCurrenciesDao
                ).also { instance = it }
            }
    }
}