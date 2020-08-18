package com.vito.misur.currencytracker.network.data

import com.google.gson.annotations.SerializedName

data class ExchangeRates(
    @field:SerializedName("timestamp") val timestamp: Long,
    @field:SerializedName("base") val baseCurrency: String,
    @field:SerializedName("date") val date: String,
    @field:SerializedName("rates") val exchangeRates: Map<String, Float>
)
