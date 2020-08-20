package com.vito.misur.currencytracker.screen.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope by MainScope() {

    protected val stateMutableLiveData = MutableLiveData<BaseModel>()
    val stateLiveData: LiveData<BaseModel>
        get() = stateMutableLiveData

    override fun onCleared() {
        cancel()
        super.onCleared()
    }
}