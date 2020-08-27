package com.vito.misur.currencytracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.callback.FavoritesCallback
import com.vito.misur.currencytracker.custom.FavoritesAdapterCallback
import com.vito.misur.currencytracker.custom.toScaledDouble
import com.vito.misur.currencytracker.view.data.FavoriteCurrencyItem
import kotlinx.android.synthetic.main.favorites_item.view.*

class AvailableCurrenciesAdapter(private val favoritesCallback: FavoritesCallback) :
    ListAdapter<FavoriteCurrencyItem, AvailableCurrenciesAdapter.SupportedSymbolsViewHolder>(
        FavoritesAdapterCallback()
    ) {

    override fun getItemId(position: Int) = getItem(position).favoriteCurrencyId

    class SupportedSymbolsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SupportedSymbolsViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.favorites_item, parent, false)
    )

    override fun onBindViewHolder(holder: SupportedSymbolsViewHolder, position: Int) {
        holder.itemView.apply {
            getItem(position).apply {
                favoriteImageView.setOnClickListener {
                    favoriteImageView.setImageResource(if (!isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart)
                    favoritesCallback.onFavoriteClick(this)
                }
                favoriteImageView.setImageResource(if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart)
                currencyName.text = name
                currencySymbol.text = symbol
                currencyExchangeRate.text =
                    resources.getString(
                        R.string.exchange_rate_prefix,
                        exchangeRate.toScaledDouble().toString(),
                        /** Available in paid API
                        exchangeRate.toScaledDouble().toString(),
                         */
                        baseCurrency
                    )
            }
        }
    }
}