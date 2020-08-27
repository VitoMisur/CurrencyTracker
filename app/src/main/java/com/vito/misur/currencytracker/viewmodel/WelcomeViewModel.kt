package com.vito.misur.currencytracker.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.database.entity.Currency
import com.vito.misur.currencytracker.repository.WelcomeRepository
import com.vito.misur.currencytracker.view.data.CurrencyItem
import com.vito.misur.currencytracker.viewmodel.base.BaseModel.ErrorState
import com.vito.misur.currencytracker.viewmodel.base.BaseModel.LoadingState
import com.vito.misur.currencytracker.viewmodel.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class WelcomeViewModel(
    application: Application,
    private val repository: WelcomeRepository
) : BaseViewModel(application) {

    val mainCurrencyLiveData: LiveData<Currency?>
        get() = repository.getMainCurrency()

    fun fetchMainCurrency(currencyId: Long) {
        launch {
            repository.setNewMainCurrency(currencyId)
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(LoadingState(false))
        }
    }

    protected val supportedSymbolsMutableLiveData = MutableLiveData<List<CurrencyItem>>()
    val supportedSymbolsLiveData: LiveData<List<CurrencyItem>>
        get() = supportedSymbolsMutableLiveData

    fun fetchSupportedSymbols() {
        stateMutableLiveData.postValue(LoadingState(true))
        launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.fetchSupportedSymbols().let { response ->
                        supportedSymbolsMutableLiveData.postValue(response)
                    }
                }
                // DUMMY no connection handler
                catch (unknownHost: UnknownHostException) {
                    repository.getSupportedSymbolsFromDatabase().let { response ->
                        supportedSymbolsMutableLiveData.postValue(response)
                    }
                    stateMutableLiveData.postValue(ErrorState(messageResId = R.string.error_connection_required))
                }
            }
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(LoadingState(false))
        }
    }

}