package com.vito.misur.currencytracker.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vito.misur.currencytracker.baseUrl
import com.vito.misur.currencytracker.network.api.CurrencyAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @Named("retro")
    fun provideRestApiRetrofit(): Retrofit = getRestApiRetrofit(
        httpClient = getRestOkHttpClient(),
        jsonConverterFactory = getJsonConverterFactory()
    )

    @Singleton
    @Provides
    fun provideCurrencyApiService(
        @Named("retro") retrofit: Retrofit
    ): CurrencyAPIService = getCurrencyApiService(retrofit)

    private fun getNetworkAuthorizationProvider() =
        HttpLoggingInterceptor().apply { level = Level.BASIC }

    private fun getJsonConverterFactory() = GsonConverterFactory.create()

    private fun getRestOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(getNetworkAuthorizationProvider())
        .build()

    private fun getRestApiRetrofit(
        httpClient: OkHttpClient,
        jsonConverterFactory: Converter.Factory
    ) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(jsonConverterFactory)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(httpClient)
        .build()

    // API
    private fun getCurrencyApiService(@Named("retro") retrofit: Retrofit) =
        retrofit.create(CurrencyAPIService::class.java)
}
