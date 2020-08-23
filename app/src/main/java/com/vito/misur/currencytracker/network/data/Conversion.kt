package com.vito.misur.currencytracker.network.data

import com.google.gson.annotations.SerializedName


data class Conversion(
    @field:SerializedName("query") val query: ConversionResult,
    @field:SerializedName("info") val info: ConversionInfo,
    @field:SerializedName("date") val date: String,
    @field:SerializedName("result") val result: Double
)

data class ConversionResult(
    @field:SerializedName("from") val from: String,
    @field:SerializedName("to") val to: String,
    @field:SerializedName("amount") val amount: Double
)

data class ConversionInfo(
    @field:SerializedName("timestamp") val timestamp: Long,
    @field:SerializedName("rate") val rate: Double
)