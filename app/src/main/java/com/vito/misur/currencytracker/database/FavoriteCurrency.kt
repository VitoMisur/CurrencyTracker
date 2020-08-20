package com.vito.misur.currencytracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "favoriteCurrency")
data class
FavoriteCurrency(
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "base_currency") val baseCurrency: String,
    @ColumnInfo(name = "exchange_rate") val exchangeRate: Float,
    /**
     * Indicates when the [Invoice] was scanned.
     */
    @ColumnInfo(name = "recent_date_time") val scanDate: DateTime = DateTime.now()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var favoriteCurrencyId: Long = 0
}