package com.vito.misur.currencytracker.screen.base

import androidx.annotation.StringRes
import com.vito.misur.currencytracker.database.FavoriteCurrency
import com.vito.misur.currencytracker.network.data.Currency

sealed class BaseModel {

    class LoadingState(val isLoading: Boolean) : BaseModel()

    class ErrorState(val errorMessage: String? = null, @StringRes val messageResId: Int? = null) :
        BaseModel()

    class EmptyState(val currencySymbol: String) : BaseModel()

    // Model states used instead of basic function calls
    data class MainCurrencyState(
        val currency: Currency
    ) : BaseModel()

    data class SupportedCurrenciesData(
        val currencies: List<Currency>
    ) : BaseModel()

    data class FavoriteCurrenciesData(
        val favoriteCurrencies: List<FavoriteCurrency>
    ) : BaseModel()

    object OfflineState : BaseModel()

}