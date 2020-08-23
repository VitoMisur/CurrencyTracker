package com.vito.misur.currencytracker.screen.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.database.FavoriteCurrency
import com.vito.misur.currencytracker.network.favorites.FavoritesRepository
import com.vito.misur.currencytracker.screen.base.BaseModel.EmptyState
import com.vito.misur.currencytracker.screen.base.BaseModel.LoadingState
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

    protected val availableCurrenciesMutableLiveData = MutableLiveData<List<FavoriteCurrency>>()
    val availableCurrenciesLiveData: LiveData<List<FavoriteCurrency>>
        get() = availableCurrenciesMutableLiveData

    fun fetchAvailableCurrencies() {
        stateMutableLiveData.postValue(LoadingState(true))
        launch {
            withContext(Dispatchers.IO) {
                try {
                    repository.getAvailableCurrencies().let { response ->
                        if (response.isNullOrEmpty()) {
                            stateMutableLiveData.postValue(EmptyState(repository.getMainCurrencySymbol()))
                        } else availableCurrenciesMutableLiveData.postValue(response)
                    }
                } catch (unknownHost: UnknownHostException) {
                    repository.getAvailableCurrenciesFromDatabase().let { response ->
                        if (response.isNullOrEmpty()) {
                            stateMutableLiveData.postValue(EmptyState(repository.getMainCurrencySymbol()))
                        } else availableCurrenciesMutableLiveData.postValue(response)
                    }
                }
            }
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(LoadingState(false))
        }
    }

    fun fetchAvailableCurrenciesWithSearch(searchQuery: String) {
        stateMutableLiveData.postValue(LoadingState(true))
        launch {
            withContext(Dispatchers.IO) {
                repository.getAvailableCurrenciesFromDatabaseFiltered(searchQuery).let { response ->
                    if (response.isNullOrEmpty()) {
                        stateMutableLiveData.postValue(
                            EmptyState(
                                repository.getMainCurrencySymbol(),
                                R.string.empty_search_currencies
                            )
                        )
                    } else availableCurrenciesMutableLiveData.postValue(response)
                }

            }
        }.invokeOnCompletion {
            stateMutableLiveData.postValue(LoadingState(false))
        }
    }
}