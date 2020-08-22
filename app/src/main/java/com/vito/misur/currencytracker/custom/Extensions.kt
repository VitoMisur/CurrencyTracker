package com.vito.misur.currencytracker.custom

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ConnectivityManager.isConnected(): Boolean {
    var isConnected = false
    apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getNetworkCapabilities(activeNetwork).apply {
                if (this != null) {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            isConnected = true
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            isConnected = true
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            isConnected = true
                        }
                    }
                }
            }
        } else {
            @Suppress("DEPRECATION")
            isConnected = activeNetworkInfo?.isConnectedOrConnecting == true
        }
    }
    return isConnected
}