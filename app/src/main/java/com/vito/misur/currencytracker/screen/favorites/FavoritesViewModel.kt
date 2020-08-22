package com.vito.misur.currencytracker.screen.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vito.misur.currencytracker.database.FavoriteCurrency
import com.vito.misur.currencytracker.network.favorites.FavoritesRepository
import com.vito.misur.currencytracker.screen.base.BaseModel
import com.vito.misur.currencytracker.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class FavoritesViewModel(
    application: Application,
    private val repository: FavoritesRepository
) : BaseViewModel(application) {

    fun fetchFavorite(currencyId: Long, isFavorite: Boolean) {
        launch {
            withContext(Dispatchers.IO) {
                repository.fetchCurrencyFavoriteStatus(currencyId, isFavorite)
                availableCurrenciesMutableLiveData.postValue(repository.getAvailableCurrenciesFromDatabase())
            }
        }
    }

    protected val favoriteCurrenciesMutableLiveData = MutableLiveData<List<FavoriteCurrency>>()
    val favoriteCurrenciesLiveData: LiveData<List<FavoriteCurrency>>
        get() = favoriteCurrenciesMutableLiveData

    fun fetchFavoriteCurrencies() {
        stateMutableLiveData.postValue(BaseModel.LoadingState(true))
        launch {
            withContext(Dispatchers.IO) {
                repository.getFavoriteCurrencies().let { response ->
                    if (response.isNullOrEmpty()) {
                        stateMutableLiveData.postValue(BaseModel.EmptyState(repository.getMainCurrencySymbol()))
                    } else favoriteCurrenciesMutableLiveData.postValue(response)
                }
            }
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(BaseModel.LoadingState(false))
        }
    }

    protected val availableCurrenciesMutableLiveData = MutableLiveData<List<FavoriteCurrency>>()
    val availableCurrenciesLiveData: LiveData<List<FavoriteCurrency>>
        get() = availableCurrenciesMutableLiveData

    fun fetchAvailableCurrencies() {
        stateMutableLiveData.postValue(BaseModel.LoadingState(true))
        launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.getAvailableCurrencies().let { response ->
                        if (response.isNullOrEmpty()) {
                            stateMutableLiveData.postValue(BaseModel.EmptyState(repository.getMainCurrencySymbol()))
                        } else availableCurrenciesMutableLiveData.postValue(response)
                    }
                } catch (unknownHost: UnknownHostException) {
                    stateMutableLiveData.postValue(BaseModel.OfflineState)
                }
            }
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(BaseModel.LoadingState(false))
        }
    }
}