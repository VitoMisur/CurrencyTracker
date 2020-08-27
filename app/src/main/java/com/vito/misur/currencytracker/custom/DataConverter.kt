package com.vito.misur.currencytracker.custom

import com.vito.misur.currencytracker.database.entity.Currency
import com.vito.misur.currencytracker.database.entity.FavoriteCurrency
import com.vito.misur.currencytracker.view.data.CurrencyItem
import com.vito.misur.currencytracker.view.data.FavoriteCurrencyItem

fun List<FavoriteCurrency>.convertToItemList() =
    this.map {
        it.convertToFavoriteItem()
    }

fun List<Currency>.convertToCurrencyItemList() =
    this.map {
        it.convertToCurrencyItem()
    }

fun Currency.convertToCurrencyItem() =
    CurrencyItem(
        currencyId,
        symbol,
        name,
        isMainCurrency
    )

fun FavoriteCurrency.convertToFavoriteItem() =
    FavoriteCurrencyItem(
        favoriteCurrencyId,
        symbol,
        name,
        baseCurrency,
        isFavorite,
        calculatedExchangeRate
    )
