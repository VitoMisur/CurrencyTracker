package com.vito.misur.currencytracker.network.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
    @field:SerializedName("symbol") val symbol: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("rate") val baseCurrency: Float? = null
) : Parcelable