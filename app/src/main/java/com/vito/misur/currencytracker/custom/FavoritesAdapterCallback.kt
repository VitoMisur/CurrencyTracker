package com.vito.misur.currencytracker.custom

import androidx.recyclerview.widget.DiffUtil
import com.vito.misur.currencytracker.database.entity.FavoriteCurrency

class FavoritesAdapterCallback : DiffUtil.ItemCallback<FavoriteCurrency>() {
    override fun areItemsTheSame(oldItem: FavoriteCurrency, newItem: FavoriteCurrency) =
        oldItem.favoriteCurrencyId == newItem.favoriteCurrencyId

    override fun areContentsTheSame(oldItem: FavoriteCurrency, newItem: FavoriteCurrency) =
        oldItem == newItem

    override fun getChangePayload(oldItem: FavoriteCurrency, newItem: FavoriteCurrency) =
        newItem
}