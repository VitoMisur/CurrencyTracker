package com.vito.misur.currencytracker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.adapters.AvailableCurrenciesAdapter
import com.vito.misur.currencytracker.callback.FavoritesCallback
import com.vito.misur.currencytracker.custom.gone
import com.vito.misur.currencytracker.custom.visible
import com.vito.misur.currencytracker.view.data.FavoriteCurrencyItem
import com.vito.misur.currencytracker.viewmodel.FavoritesViewModel
import com.vito.misur.currencytracker.viewmodel.base.BaseModel
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_favorites.headerHolder
import kotlinx.android.synthetic.main.search_holder.*
import org.koin.android.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(), FavoritesCallback {

    private val favoritesModel by viewModel<FavoritesViewModel>()

    private val adapter: AvailableCurrenciesAdapter by lazy {
        AvailableCurrenciesAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(
        R.layout.fragment_favorites, container, false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesModel.stateLiveData.observe(viewLifecycleOwner, Observer { render(it) })
        favoritesModel.availableCurrenciesLiveData.observe(
            viewLifecycleOwner,
            Observer { render(BaseModel.FavoriteCurrenciesData(it)) })

        favoritesModel.fetchAvailableCurrencies()

        initView()
    }

    private fun initView() {
        currencySearchRecyclerView.adapter = adapter
        confirmButton.setOnClickListener {
            findNavController().navigate(FavoritesFragmentDirections.toHome())
        }
        search?.setOnClickListener {
            favoritesModel.fetchAvailableCurrenciesWithSearch(
                searchEditText.text.toString().trim()
            )
        }
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                favoritesModel.fetchAvailableCurrenciesWithSearch(
                    searchEditText.text.toString().trim()
                )
            }
            false
        }
    }

    private fun render(model: BaseModel) {
        when (model) {
            is BaseModel.FavoriteCurrenciesData -> {
                adapter.submitList(model.favoriteCurrencies)
                currencySearchRecyclerView?.visible()
                headerHolder?.visible()
                errorHolder?.gone()
                confirmButton?.visible()
            }
            is BaseModel.ErrorState -> {
                currencySearchRecyclerView?.gone()
                headerHolder?.gone()
                confirmButton?.gone()
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
                currencySearchRecyclerView?.gone()
                errorHolder?.visible()
                alertText?.text = resources.getString(
                    model.emptyMessage ?: R.string.error_empty_result_for_main_currency,
                    model.currencySymbol
                )
            }
        }
    }

    override fun onFavoriteClick(favoriteCurrencyItem: FavoriteCurrencyItem) {
        favoritesModel.fetchFavorite(
            favoriteCurrencyItem.favoriteCurrencyId,
            !favoriteCurrencyItem.isFavorite,
            searchEditText.text.toString().trim()
        )
    }
}