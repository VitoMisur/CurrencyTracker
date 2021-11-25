package com.vito.misur.currencytracker.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.repository.FavoritesRepository
import com.vito.misur.currencytracker.view.data.FavoriteCurrencyItem
import com.vito.misur.currencytracker.viewmodel.base.BaseModel.EmptyState
import com.vito.misur.currencytracker.viewmodel.base.BaseModel.LoadingState
import com.vito.misur.currencytracker.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    application: Application,
    private val repository: FavoritesRepository
) : BaseViewModel(application) {

    fun fetchFavorite(currencyId: Long, isFavorite: Boolean, searchQuery: String) {
        launch {
            withContext(Dispatchers.IO) {
                repository.fetchCurrencyFavoriteStatus(currencyId, isFavorite)
                availableCurrenciesMutableLiveData.postValue(
                    repository.getAvailableCurrenciesFromDatabaseFiltered(
                        searchQuery
                    )
                )
            }
        }
    }

    protected val availableCurrenciesMutableLiveData = MutableLiveData<List<FavoriteCurrencyItem>>()
    val availableCurrenciesLiveData: LiveData<List<FavoriteCurrencyItem>>
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