package com.vito.misur.currencytracker.screen.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import timber.log.Timber

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope by MainScope() {

    sealed class TaskResponse<out T>(val data: T?, val responseException: Throwable? = null) {
        class Data<out T>(data: T?) : TaskResponse<T>(data)
        class Exception<out T>(throwable: Throwable) : TaskResponse<T>(null, throwable)

        val success = responseException == null
    }

    protected val mutableLiveData = MutableLiveData<BaseModel>()
    val liveData: LiveData<BaseModel>
        get() = mutableLiveData

    override fun onCleared() {
        cancel()
        super.onCleared()
    }

    /**
     * Executes block in try/catch and returns a TaskResponse.
     */
    suspend fun <T> executeSafe(
        block: suspend CoroutineScope.() -> T
    ): TaskResponse<T> =
        try {
            val data = withContext(Dispatchers.IO) { block() }
            TaskResponse.Data(data)
        } catch (e: Throwable) {
            Timber.d(e, "Execute safe fail")
            TaskResponse.Exception(e)
        }

}