package com.vito.misur.currencytracker.screen.home

import android.app.Application
import com.vito.misur.currencytracker.network.HomeRepository
import com.vito.misur.currencytracker.screen.base.BaseViewModel


class HomeViewModel(application: Application, private val repository: HomeRepository) :
    BaseViewModel(application) {
    // TODO: conversions
}