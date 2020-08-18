package com.vito.misur.currencytracker.network.data

import com.google.gson.annotations.SerializedName

open class ResponseError(
    @field:SerializedName("code") val code: Int,
    @field:SerializedName("type") val type: String,
    @field:SerializedName("info") val info: String
)