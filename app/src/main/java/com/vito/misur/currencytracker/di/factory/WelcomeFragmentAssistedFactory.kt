package com.vito.misur.currencytracker.di.factory

import com.vito.misur.currencytracker.viewmodel.WelcomeViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface WelcomeFragmentAssistedFactory {
    fun create(activitySource: WelcomeViewModel.ActivitySource): WelcomeViewModel
}