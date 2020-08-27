package com.vito.misur.currencytracker.custom

import androidx.recyclerview.widget.DiffUtil
import com.vito.misur.currencytracker.view.data.FavoriteCurrencyItem

class FavoritesAdapterCallback : DiffUtil.ItemCallback<FavoriteCurrencyItem>() {
    override fun areItemsTheSame(oldItem: FavoriteCurrencyItem, newItem: FavoriteCurrencyItem) =
        oldItem.favoriteCurrencyId == newItem.favoriteCurrencyId

    override fun areContentsTheSame(oldItem: FavoriteCurrencyItem, newItem: FavoriteCurrencyItem) =
        oldItem == newItem

    override fun getChangePayload(oldItem: FavoriteCurrencyItem, newItem: FavoriteCurrencyItem) =
        newItem
}