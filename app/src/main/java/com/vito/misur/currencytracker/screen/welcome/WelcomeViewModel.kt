package com.vito.misur.currencytracker.screen.welcome

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vito.misur.currencytracker.network.data.Currency
import com.vito.misur.currencytracker.network.welcome.WelcomeRepository
import com.vito.misur.currencytracker.screen.base.BaseModel
import com.vito.misur.currencytracker.screen.base.BaseViewModel
import kotlinx.coroutines.launch


class WelcomeViewModel(
    application: Application,
    private val repository: WelcomeRepository
) : BaseViewModel(application) {

    var isLoading = false

    protected val mainCurrencyMutableLiveData = MutableLiveData<Currency>()
    val mainCurrencyLiveData: LiveData<Currency>
        get() = mainCurrencyMutableLiveData

    fun fetchMainCurrency(currency: Currency) {
        mainCurrencyMutableLiveData.postValue(currency)
    }

    fun fetchSupportedSymbols() {
        isLoading = true
        mutableLiveData.postValue(BaseModel.LoadingState)

        launch {
            val response = executeSafe {
                repository.fetchSymbols().let { response ->
                    if (response.success) {
                        WelcomeModel.Data(response)
                    } else BaseModel.ErrorState(response.error.info)
                }
            }

            response.data.takeIf { response.success }?.let {
                mutableLiveData.postValue(it)
            }
                ?: mutableLiveData.postValue(BaseModel.ErrorState(response.responseException?.localizedMessage))
        }.invokeOnCompletion {
            isLoading = false
        }
    }
}