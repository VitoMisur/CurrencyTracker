package com.vito.misur.currencytracker.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "favorite_currencies")
data class
FavoriteCurrency(
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "base_currency") val baseCurrency: String,
    // Euro only via Free APi
    @ColumnInfo(name = "exchange_rate") val exchangeRate: Double,
    @ColumnInfo(name = "recent_date_time") val recentDateTime: DateTime = DateTime.now(),
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "calculated_exchange_rate") val calculatedExchangeRate: Double = exchangeRate
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var favoriteCurrencyId: Long = 0

    fun calculateExchangeRate(requestedCurrencyEuroRate: Double) =
        exchangeRate / requestedCurrencyEuroRate
}