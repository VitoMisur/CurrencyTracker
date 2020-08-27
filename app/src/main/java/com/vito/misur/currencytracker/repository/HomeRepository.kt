package com.vito.misur.currencytracker.repository

import com.vito.misur.currencytracker.custom.convertToItemList
import com.vito.misur.currencytracker.database.dao.FavoriteCurrenciesDao
import com.vito.misur.currencytracker.database.dao.SupportedCurrenciesDao
import com.vito.misur.currencytracker.network.api.CurrencyAPIService
import com.vito.misur.currencytracker.view.data.FavoriteCurrencyItem

class HomeRepository(
    private val currencyAPIService: CurrencyAPIService,
    private val favoriteCurrenciesDao: FavoriteCurrenciesDao,
    private val supportedCurrenciesDao: SupportedCurrenciesDao
) {

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

    suspend fun getFavoriteCurrencies(): List<FavoriteCurrencyItem> =
        favoriteCurrenciesDao.updateFavoriteCurrenciesWithCalculatedValues(supportedCurrenciesDao.getMainCurrencySymbol())
            .convertToItemList()

    fun getMainCurrencySymbol() =
        supportedCurrenciesDao.getMainCurrencySymbol()

    // Paid API needed to convert all possible currencies
    suspend fun fetchConversion(
        convertibleCurrencySymbol: String,
        amount: String
    ) = currencyAPIService.getConversionResultAsync(
        from = supportedCurrenciesDao.getMainCurrencySymbol(),
        to = convertibleCurrencySymbol,
        amount = amount
    ).await()
}