package com.vito.misur.currencytracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vito.misur.currencytracker.R
import com.vito.misur.currencytracker.callback.ModalCallback
import com.vito.misur.currencytracker.network.data.Currency
import kotlinx.android.synthetic.main.modal_item.view.*

class SupportedSymbolsAdapter(private val modalCallBack: ModalCallback) :
    ListAdapter<Currency, SupportedSymbolsAdapter.SupportedSymbolsViewHolder>(
        SupportedSymbolsAdapterCallback()
    ) {

    class SupportedSymbolsAdapterCallback : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency) =
            oldItem == newItem
    }

    class SupportedSymbolsViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SupportedSymbolsViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.modal_item, parent, false)
    )

    override fun onBindViewHolder(holder: SupportedSymbolsViewHolder, position: Int) {
        holder.itemView.apply {
            getItem(position).apply {
                modalItemPlaceholder.setOnClickListener {
                    modalCallBack.onModalItemClick(this)
                }
                currencyName.text = symbol
                currencySymbol.text = name
            }
        }
    }
}