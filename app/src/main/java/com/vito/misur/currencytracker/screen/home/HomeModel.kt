package com.vito.misur.currencytracker.screen.home

import com.vito.misur.currencytracker.database.FavoriteCurrency
import com.vito.misur.currencytracker.network.data.Currency
import com.vito.misur.currencytracker.screen.base.BaseModel

open class HomeModel : BaseModel.ScreenModel() {
    data class Data(
        val favoriteCurrencies: List<FavoriteCurrency>
    ) : HomeModel()

    data class SupportedSymbols(
        val supportedCurrencies: List<Currency>
    ) : HomeModel()
}