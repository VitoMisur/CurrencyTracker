package com.vito.misur.currencytracker.network.data

import com.google.gson.annotations.SerializedName

abstract class Response(
    @field:SerializedName("success") val success: Boolean,
    @field:SerializedName("error") val error: ResponseError
)