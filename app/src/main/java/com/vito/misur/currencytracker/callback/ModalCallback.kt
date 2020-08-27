package com.vito.misur.currencytracker.callback

import com.vito.misur.currencytracker.view.data.CurrencyItem

interface ModalCallback {
    fun onModalItemClick(supportedCurrencyItem: CurrencyItem)
}