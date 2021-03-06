package com.vito.misur.currencytracker.view.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrencyItem(
    val currencyId: Long,
    val symbol: String,
    val name: String,
    val isMainCurrency: Boolean
) : Parcelable