package com.vito.misur.currencytracker.network.welcome

import com.vito.misur.currencytracker.network.CurrencyAPIService

class WelcomeRepository(private val currencyAPIService: CurrencyAPIService) {

    suspend fun fetchSymbols() = currencyAPIService
        .getSupportedSymbols()
        .await()
}