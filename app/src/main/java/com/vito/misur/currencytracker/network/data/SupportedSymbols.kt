package com.vito.misur.currencytracker.network.data

import com.google.gson.annotations.SerializedName

class SupportedSymbols(
    @field:SerializedName("symbols") val symbols: LinkedHashMap<String, String>,
    success: Boolean,
    error: ResponseError
) : Response(success, error)
