package com.vito.misur.currencytracker.view.data

data class FavoriteCurrencyItem(
    val favoriteCurrencyId: Long,
    val symbol: String,
    val name: String,
    val baseCurrency: String,
    val isFavorite: Boolean,
    val exchangeRate: Double,
    var convertedAmount: Double = exchangeRate
) {
    fun setConvertedAmount(amount: String?) {
        convertedAmount = (amount?.let {
            exchangeRate * it.toDouble()
        } ?: exchangeRate)
    }
}