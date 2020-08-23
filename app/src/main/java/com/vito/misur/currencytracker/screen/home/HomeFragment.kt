package com.vito.misur.currencytracker.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.adapters.FavoriteCurrenciesAdapter
import com.vito.misur.currencytracker.custom.getString
import com.vito.misur.currencytracker.custom.gone
import com.vito.misur.currencytracker.custom.visible
import com.vito.misur.currencytracker.screen.base.BaseModel
import com.vito.misur.currencytracker.screen.favorites.FavoritesViewModel
import com.vito.misur.currencytracker.screen.welcome.ModalBottomFragment
import com.vito.misur.currencytracker.screen.welcome.WelcomeViewModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel by viewModel<HomeViewModel>()
    private val welcomeViewModel by sharedViewModel<WelcomeViewModel>()
    private val favoritesViewModel by sharedViewModel<FavoritesViewModel>()

    private val adapter: FavoriteCurrenciesAdapter by lazy {
        FavoriteCurrenciesAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_home, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.stateLiveData.observe(viewLifecycleOwner, Observer { render(it) })

        homeViewModel.favoriteCurrenciesLiveData.observe(viewLifecycleOwner, Observer {
            render(
                BaseModel.FavoriteCurrenciesData(
                    it
                )
            )
        })

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
        homeViewModel.fetchFavoriteCurrencies()

        initView()
    }

    private fun initView() {
        favoritesRecyclerView.adapter = adapter
        favorites.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.toFavorites())
        }

        amountToExchange.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                homeViewModel.fetchAmount()
            }
            false
        }
    }

    private fun render(model: BaseModel) {
        when (model) {
            is BaseModel.FavoriteCurrenciesData -> {
                model.favoriteCurrencies.map { favCurrency ->
                    favCurrency.setConvertedAmount(amountToExchange?.text?.getString())
                }

                adapter.submitList(model.favoriteCurrencies)
                favoritesRecyclerView?.visible()
                errorHolder?.gone()
            }
            is BaseModel.ErrorState -> {
                favoritesRecyclerView?.gone()
                errorHolder?.visible()
                alertText?.text = model.errorMessage
            }
            is BaseModel.LoadingState -> {
                progressBar?.run {
                    if (model.isLoading) visible()
                    else gone()
                }
            }
            is BaseModel.EmptyState -> {
                favoritesRecyclerView?.gone()
                errorHolder?.visible()
                alertText?.text = resources.getString(
                    R.string.error_empty_result_for_main_currency,
                    model.currencySymbol
                )
            }
            is BaseModel.MainCurrencyState -> {
                model.currency.apply {
                    currencyName.text = symbol
                }
                homeViewModel.fetchFavoriteCurrencies()
            }
            is BaseModel.SupportedCurrenciesData -> {
                currencyName.setOnClickListener {
                    childFragmentManager.beginTransaction()
                        .add(ModalBottomFragment.newInstance(model.currencies), "modal").commit()
                }
            }
            is BaseModel.HomeConversionState -> {
                adapter.apply {
                    currentList.map { favCurrency ->
                        favCurrency.setConvertedAmount(amountToExchange?.text?.getString())
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }
}