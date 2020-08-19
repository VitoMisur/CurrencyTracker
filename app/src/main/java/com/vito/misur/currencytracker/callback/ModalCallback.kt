package com.vito.misur.currencytracker.callback

import com.vito.misur.currencytracker.network.data.Currency

interface ModalCallback {
    fun onModalItemClick(currency: Currency)
}