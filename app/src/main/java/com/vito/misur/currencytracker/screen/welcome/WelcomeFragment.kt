package com.vito.misur.currencytracker.screen.welcome

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.custom.gone
import com.vito.misur.currencytracker.custom.visible
import com.vito.misur.currencytracker.screen.FIRST_CURRENCY_SELECTION
import com.vito.misur.currencytracker.screen.base.BaseModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class WelcomeFragment : Fragment() {

    private val sharedPreferences: SharedPreferences by inject()

    private val welcomeViewModel by sharedViewModel<WelcomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_welcome, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcomeViewModel.stateLiveData.observe(viewLifecycleOwner, Observer { render(it) })
        welcomeViewModel.supportedSymbolsLiveData.observe(viewLifecycleOwner, Observer {
            render(
                BaseModel.SupportedCurrenciesData(
                    it
                )
            )
        })
        welcomeViewModel.mainCurrencyLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                render(
                    BaseModel.MainCurrencyState(it)
                )
            }
        })

        welcomeViewModel.fetchSupportedSymbols()

        initView()
    }

    private fun initView() {
        currencySymbol.text = resources.getString(R.string.empty_currency_name_placeholder)
        currencyName.text = resources.getString(R.string.empty_currency_symbol_placeholder)
    }

    private fun render(model: BaseModel) {
        when (model) {
            is BaseModel.SupportedCurrenciesData -> {
                contentHolder?.visible()
                errorHolder?.gone()
                confirmAndContinue?.apply {
                    setImageResource(R.drawable.ic_right)
                    setOnClickListener {
                        sharedPreferences.edit().putBoolean(FIRST_CURRENCY_SELECTION, false)
                            .apply()
                        findNavController().navigate(WelcomeFragmentDirections.toFavorites())

                    }
                }
                currencyHolder.setOnClickListener {
                    childFragmentManager.beginTransaction()
                        .add(ModalBottomFragment.newInstance(model.currencies), "modal").commit()
                }

            }
            is BaseModel.ErrorState -> {
                contentHolder?.gone()
                errorHolder?.visible()
                model.messageResId?.let {
                    alertText?.text = resources.getString(it)
                } ?: run {
                    alertText?.text = model.errorMessage
                }
                confirmAndContinue?.apply {
                    visible()
                    setImageResource(R.drawable.ic_refresh)
                    setOnClickListener {
                        welcomeViewModel.fetchSupportedSymbols()
                    }
                }
            }
            is BaseModel.LoadingState -> {

            }
            is BaseModel.MainCurrencyState -> {
                model.currency.apply {
                    confirmAndContinue?.visible()
                    currencySymbol.text = name
                    currencyName.text = symbol
                }
            }
        }
    }

}