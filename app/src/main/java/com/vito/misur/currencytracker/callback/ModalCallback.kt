package com.vito.misur.currencytracker.callback

import com.vito.misur.currencytracker.database.entity.Currency

interface ModalCallback {
    fun onModalItemClick(currency: Currency)
}