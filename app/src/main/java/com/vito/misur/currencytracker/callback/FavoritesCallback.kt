package com.vito.misur.currencytracker.callback

import com.vito.misur.currencytracker.database.FavoriteCurrency

interface FavoritesCallback {
    fun onFavoriteClick(favoriteCurrency: FavoriteCurrency)
}