package com.vito.misur.currencytracker.screen.base

import com.vito.misur.currencytracker.network.data.Currency

sealed class BaseModel {

    class LoadingState(val isLoading: Boolean) : BaseModel()

    class ErrorState(val errorMessage: String? = "Something went wrong,\nplease check your connection.") :
        BaseModel()

    open class ScreenModel : BaseModel()

    object EmptyState : BaseModel()

    data class MainCurrencyState(
        val currency: Currency
    ) : BaseModel()

    object OfflineState : BaseModel()

}