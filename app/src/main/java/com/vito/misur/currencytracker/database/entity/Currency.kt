package com.vito.misur.currencytracker.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Entity(
    tableName = "supported_currency"
    /*, indices = [Index(value = ["name"], unique = true)]*/
)
@Parcelize
data class Currency(
    @field:SerializedName("symbol") @ColumnInfo(name = "symbol") val symbol: String,
    @field:SerializedName("name") @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "is_main_currency") val isMainCurrency: Boolean = false
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var currencyId: Long = 0
}