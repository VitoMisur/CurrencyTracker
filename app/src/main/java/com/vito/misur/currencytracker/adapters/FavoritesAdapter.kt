package com.vito.misur.currencytracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.callback.FavoritesCallback
import com.vito.misur.currencytracker.database.FavoriteCurrency
import kotlinx.android.synthetic.main.favorites_item.view.*

class FavoritesAdapter(private val favoritesCallback: FavoritesCallback) :
    ListAdapter<FavoriteCurrency, FavoritesAdapter.SupportedSymbolsViewHolder>(
        FavoritesAdapterCallback()
    ) {

    class FavoritesAdapterCallback : DiffUtil.ItemCallback<FavoriteCurrency>() {
        override fun areItemsTheSame(oldItem: FavoriteCurrency, newItem: FavoriteCurrency) =
            oldItem.favoriteCurrencyId == newItem.favoriteCurrencyId

        override fun areContentsTheSame(oldItem: FavoriteCurrency, newItem: FavoriteCurrency) =
            oldItem == newItem

        override fun getChangePayload(oldItem: FavoriteCurrency, newItem: FavoriteCurrency) =
            newItem
    }


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
                currencySymbol.text = symbol
                currencyName.text = symbol
                currencyExchangeRate.text =
                    resources.getString(R.string.exchange_rate_prefix, exchangeRate, baseCurrency)
            }
        }
    }
}