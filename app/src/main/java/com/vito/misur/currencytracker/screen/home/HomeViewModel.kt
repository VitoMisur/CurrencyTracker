package com.vito.misur.currencytracker.screen.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vito.misur.currencytracker.database.FavoriteCurrency
import com.vito.misur.currencytracker.network.home.HomeRepository
import com.vito.misur.currencytracker.screen.base.BaseModel.*
import com.vito.misur.currencytracker.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    application: Application,
    private val repository: HomeRepository
) :
    BaseViewModel(application) {

    fun fetchAmount() {
        stateMutableLiveData.postValue(HomeConversionState)
    }

    protected val favoriteCurrenciesMutableLiveData = MutableLiveData<List<FavoriteCurrency>>()
    val favoriteCurrenciesLiveData: LiveData<List<FavoriteCurrency>>
        get() = favoriteCurrenciesMutableLiveData

    fun fetchFavoriteCurrencies() {
        stateMutableLiveData.postValue(LoadingState(true))
        launch {
            withContext(Dispatchers.IO) {
                repository.getFavoriteCurrencies().let { response ->
                    if (response.isNullOrEmpty()) {
                        stateMutableLiveData.postValue(EmptyState(repository.getMainCurrencySymbol()))
                    } else favoriteCurrenciesMutableLiveData.postValue(response)
                }
            }
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(LoadingState(false))
        }
    }
}