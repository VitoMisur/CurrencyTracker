package com.vito.misur.currencytracker.screen.welcome

import com.vito.misur.currencytracker.network.data.SupportedSymbols
import com.vito.misur.currencytracker.screen.base.BaseModel

open class WelcomeModel : BaseModel.ScreenModel() {
    data class Data(
        val currencies: SupportedSymbols
    ) : WelcomeModel()

    object EmptyState : WelcomeModel()
}