package com.vito.misur.currencytracker.screen.base

import com.vito.misur.currencytracker.screen.welcome.WelcomeModel

sealed class BaseModel {

    object LoadingState : BaseModel()

    class ErrorState(val errorMessage: String? = "Something went wrong,\nplease check your connection.") :
        BaseModel()

    open class ScreenModel : BaseModel()

    object EmptyState : WelcomeModel()
}