package com.vito.misur.currencytracker.view.data

data class CurrencyItem(
    val currencyId: Long,
    val symbol: String,
    val name: String,
    val isMainCurrency: Boolean
)