package com.vito.misur.currencytracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.vito.misur.currencytracker.custom.toScaledDouble
import org.joda.time.DateTime

@Entity(tableName = "favorite_currencies")
data class
FavoriteCurrency(
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "base_currency") val baseCurrency: String,
    @ColumnInfo(name = "exchange_rate") val exchangeRate: Double,
    @ColumnInfo(name = "recent_date_time") val recentDateTime: DateTime = DateTime.now(),
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var favoriteCurrencyId: Long = 0
    @Ignore
    var convertedAmount: Double = exchangeRate

    fun setConvertedAmount(amount: String?) {
        convertedAmount = (amount?.let {
            exchangeRate * it.toDouble()
        } ?: exchangeRate).toScaledDouble()
    }
}