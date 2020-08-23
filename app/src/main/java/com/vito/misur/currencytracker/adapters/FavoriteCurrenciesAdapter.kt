package com.vito.misur.currencytracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.custom.FavoritesAdapterCallback
import com.vito.misur.currencytracker.custom.toScaledDouble
import com.vito.misur.currencytracker.database.FavoriteCurrency
import kotlinx.android.synthetic.main.home_item.view.*

class FavoriteCurrenciesAdapter :
    ListAdapter<FavoriteCurrency, FavoriteCurrenciesAdapter.FavoriteCurrenciesViewHolder>(
        FavoritesAdapterCallback()
    ) {

    class FavoriteCurrenciesViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavoriteCurrenciesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.home_item, parent, false)
        )

    override fun onBindViewHolder(holder: FavoriteCurrenciesViewHolder, position: Int) {
        holder.itemView.apply {
            getItem(position).apply {
                currencySymbol.text = symbol
                currencyName.text = symbol
                currencyAmount.text = convertedAmount.toScaledDouble().toString()
                currencyExchangeRate.text =
                    resources.getString(
                        R.string.exchange_rate_prefix,
                        exchangeRate.toScaledDouble().toString(),
                        baseCurrency
                    )
            }
        }
    }
}