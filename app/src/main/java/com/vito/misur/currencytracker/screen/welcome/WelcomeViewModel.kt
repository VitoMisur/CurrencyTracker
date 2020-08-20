package com.vito.misur.currencytracker.screen.welcome

import android.app.Application
import androidx.lifecycle.LiveData
import com.vito.misur.currencytracker.network.data.Currency
import com.vito.misur.currencytracker.network.welcome.WelcomeRepository
import com.vito.misur.currencytracker.screen.base.BaseModel
import com.vito.misur.currencytracker.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WelcomeViewModel(
    application: Application,
    private val repository: WelcomeRepository
) : BaseViewModel(application) {

    val mainCurrencyLiveData: LiveData<Currency?>
        get() = repository.getMainCurrency()

    val supportedSymbols: LiveData<List<Currency>>
        get() = repository.getSupportedCurrencies()

    fun fetchMainCurrency(currencyId: Long) {
        launch {
            repository.fetchMainCurrency(currencyId)
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(BaseModel.LoadingState(false))
        }
    }

    fun fetchSupportedSymbols() {
        stateMutableLiveData.postValue(BaseModel.LoadingState(true))

        launch {
            withContext(Dispatchers.IO) {
                repository.fetchSymbols().let { response ->
                    response.takeIf { response.success }?.let { successfulResponse ->
                        successfulResponse.symbols.map {
                            Currency(
                                it.key,
                                it.value
                            )
                        }.apply {
                            if (isNullOrEmpty()) {
                                stateMutableLiveData.postValue(BaseModel.EmptyState)
                            } else {
                                repository.insertAll(this)
                            }
                        }
                    }
                        ?: stateMutableLiveData.postValue(BaseModel.ErrorState(response.error.type + " / " + response.error.code))
                }
            }
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(BaseModel.LoadingState(false))
        }
    }
}