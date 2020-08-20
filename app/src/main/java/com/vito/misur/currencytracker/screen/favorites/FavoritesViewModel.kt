package com.vito.misur.currencytracker.screen.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import com.vito.misur.currencytracker.database.FavoriteCurrency
import com.vito.misur.currencytracker.network.favorites.FavoritesRepository
import com.vito.misur.currencytracker.screen.base.BaseModel
import com.vito.misur.currencytracker.screen.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    application: Application,
    private val repository: FavoritesRepository
) : BaseViewModel(application) {

    val availableCurrenciesLiveData: LiveData<List<FavoriteCurrency>>
        get() = repository.receiveAvailableCurrencies()

    fun fetchFavorite(currencyId: Long, isFavorite: Boolean) {
        stateMutableLiveData.postValue(BaseModel.LoadingState(true))
        launch {
            withContext(Dispatchers.IO) {
                repository.fetchCurrencyFavoriteStatus(currencyId, isFavorite)
                // TODO: on update notify adapter
            }
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(BaseModel.LoadingState(false))
        }
    }

    fun fetchAvailableCurrencies() {
        stateMutableLiveData.postValue(BaseModel.LoadingState(true))
        launch {
            withContext(Dispatchers.IO) {
                repository.fetchAvailableCurrencies().let { response ->
                    response.takeIf { response.success }?.let { successfulResponse ->
                        successfulResponse.apply {
                            exchangeRates.map {
                                FavoriteCurrency(
                                    symbol = it.key,
                                    exchangeRate = it.value,
                                    baseCurrency = baseCurrency
                                )
                            }.apply {
                                if (isNullOrEmpty()) {
                                    stateMutableLiveData.postValue(BaseModel.EmptyState)
                                } else {
                                    repository.insertAvailableCurrenciesList(this)
                                }
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