package com.vito.misur.currencytracker.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vito.misur.currencytracker.baseUrl
import com.vito.misur.currencytracker.network.CurrencyAPIService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun networkModule() = module {

    single {
        provideRestApiRetrofit(
            httpClient = provideRestOkHttpClient(),
            jsonConverterFactory = provideJsonConverterFactory()
        )
    }

    single { provideCurrencyApiService(retrofit = get()) }

}

private fun provideNetworkAuthorizationProvider() =
    HttpLoggingInterceptor().apply { level = Level.BASIC }

private fun provideJsonConverterFactory() = GsonConverterFactory.create()

private fun provideRestOkHttpClient() = OkHttpClient.Builder()
    .addInterceptor(provideNetworkAuthorizationProvider())
    .build()

private fun provideRestApiRetrofit(
    httpClient: OkHttpClient,
    jsonConverterFactory: Converter.Factory
) = Retrofit.Builder()
    .baseUrl(baseUrl)
    .addConverterFactory(jsonConverterFactory)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(httpClient)
    .build()

// API
private fun provideCurrencyApiService(retrofit: Retrofit) =
    retrofit.create(CurrencyAPIService::class.java)
