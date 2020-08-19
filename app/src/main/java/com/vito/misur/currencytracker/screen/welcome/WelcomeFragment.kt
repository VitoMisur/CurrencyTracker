package com.vito.misur.currencytracker.screen.welcome

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
import com.vito.misur.currencytracker.network.data.Currency
import com.vito.misur.currencytracker.screen.base.BaseModel
import kotlinx.android.synthetic.main.fragment_welcome.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class WelcomeFragment : Fragment() {

    private val welcomeViewModel by sharedViewModel<WelcomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_welcome, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcomeViewModel.liveData.observe(viewLifecycleOwner, Observer { render(it) })
        welcomeViewModel.mainCurrencyLiveData.observe(viewLifecycleOwner, Observer {
            render(
                BaseModel.MainCurrencyState(it)
            )
        })
        welcomeViewModel.fetchSupportedSymbols()

        initView()
    }

    private fun initView() {
        currencyName.text = resources.getString(R.string.empty_currency_name_placeholder)
        currencySymbol.text = resources.getString(R.string.empty_currency_symbol_placeholder)
        confirmAndContinue.setOnClickListener {
            findNavController().navigate(WelcomeFragmentDirections.toFavorites())
        }
    }

    private fun render(model: BaseModel) {
        when (model) {
            is WelcomeModel.Data -> {
                contentHolder?.visible()
                errorHolder?.gone()
                currencyHolder.setOnClickListener {
                    childFragmentManager.beginTransaction()
                        .add(ModalBottomFragment.newInstance(model.currencies.symbols.map {
                            Currency(
                                it.key,
                                it.value
                            )
                        }), "modal").commit()
                }

            }
            is BaseModel.ErrorState -> {
                contentHolder?.gone()
                errorHolder?.visible()
                alertText?.text = model.errorMessage
            }
            is BaseModel.LoadingState -> {
                errorHolder?.gone()
            }
            is BaseModel.MainCurrencyState -> {
                model.currency.apply {
                    currencyName.text = name
                    currencySymbol.text = symbol
                }
            }
        }
    }

}