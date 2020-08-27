package com.vito.misur.currencytracker.custom

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Editable
import android.view.View
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.ln
import kotlin.math.pow

/**
 * Global app extensions
 * weren't splitted
 *
 */

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Editable.getString() =
    this.toString().trim().ifBlank {
        null
    }

fun String.toScaledDouble() =
    BigDecimal(this.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toDouble()

fun Double.toScaledDouble(scale: Int = 3): Double {
    BigDecimal(this).setScale(scale, RoundingMode.HALF_EVEN).toDouble().apply {
        return if (isFinite()) this else 0.0
    }
}

fun withDividingSuffix(count: Double): String? {
    if (count < 10000) return "" + count
    val exp = (ln(count) / ln(1000.0)).toInt()
    return String.format(
        "%.1f %c",
        count / 1000.0.pow(exp.toDouble()),
        "kMGTPE"[exp - 1]
    )
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