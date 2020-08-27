package com.vito.misur.currencytracker.callback

import com.vito.misur.currencytracker.database.entity.FavoriteCurrency

interface FavoritesCallback {
    fun onFavoriteClick(favoriteCurrency: FavoriteCurrency)
}