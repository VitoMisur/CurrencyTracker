package com.vito.misur.currencytracker.callback

import com.vito.misur.currencytracker.view.data.FavoriteCurrencyItem

interface FavoritesCallback {
    fun onFavoriteClick(favoriteCurrencyItem: FavoriteCurrencyItem)
}