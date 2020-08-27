package com.vito.misur.currencytracker.viewmodel.base

import androidx.annotation.StringRes
import com.vito.misur.currencytracker.view.data.CurrencyItem
import com.vito.misur.currencytracker.view.data.FavoriteCurrencyItem

/**
 * Application events
 * Used mainly instead of functions
 */
sealed class BaseModel {
    class LoadingState(val isLoading: Boolean) : BaseModel()

    class ErrorState(val errorMessage: String? = null, @StringRes val messageResId: Int? = null) :
        BaseModel()

    class EmptyState(val currencySymbol: String, @StringRes val emptyMessage: Int? = null) :
        BaseModel()

    // Model states used instead of basic function calls
    data class MainCurrencyState(
        val currency: CurrencyItem
    ) : BaseModel()

    data class SupportedCurrenciesData(
        val currencies: List<CurrencyItem>
    ) : BaseModel()

    data class FavoriteCurrenciesData(
        val favoriteCurrencies: List<FavoriteCurrencyItem>
    ) : BaseModel()

    object HomeConversionState : BaseModel()
}