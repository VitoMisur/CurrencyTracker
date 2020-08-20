package com.vito.misur.currencytracker.screen.favorites

import com.vito.misur.currencytracker.database.FavoriteCurrency
import com.vito.misur.currencytracker.screen.base.BaseModel

open class FavoritesModel : BaseModel.ScreenModel() {
    data class Data(
        val favoriteCurrencies: List<FavoriteCurrency>
    ) : FavoritesModel()
}