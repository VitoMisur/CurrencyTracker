package com.vito.misur.currencytracker.di

import com.vito.misur.currencytracker.screen.favorites.FavoritesViewModel
import com.vito.misur.currencytracker.screen.home.HomeViewModel
import com.vito.misur.currencytracker.screen.welcome.WelcomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Provides basic app related components.
 */
val appModule = module(override = true) {
//    single { provideInvoiceRepository(androidApplication()) }

    viewModel { HomeViewModel(get()) }
    viewModel { WelcomeViewModel(get()) }
    viewModel { FavoritesViewModel(get()/*, get()*/) }
}

//private fun provideInvoiceRepository(app: Application): InvoiceRepository =
//    InvoiceRepository.getInstance(AppDatabase.getInstance(app.applicationContext).invoiceDao())
