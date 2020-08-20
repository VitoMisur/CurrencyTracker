package com.vito.misur.currencytracker.network

//import com.vito.misur.currencytracker.accessKey
import com.vito.misur.currencytracker.network.data.Conversion
import com.vito.misur.currencytracker.network.data.ExchangeRates
import com.vito.misur.currencytracker.network.data.SupportedSymbols
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val accessKey = "e46e65ff26eb6c80c8420ebe66fe3620"

interface CurrencyAPIService {

    @GET("latest")
    fun getLatestExchangeRates(
        @Query("access_key") key: String = accessKey,
        @Query("base") baseCurrencySymbol: String,
        @Query("symbols") symbols: List<String>? = null
    ): Deferred<ExchangeRates>

    @GET("symbols")
    fun getSupportedSymbols(
        @Query("access_key") key: String = accessKey
    ): Deferred<SupportedSymbols>

    @GET("convert")
    fun getConversionResult(
        @Query("access_key") key: String = accessKey,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    ): Deferred<Conversion>
}