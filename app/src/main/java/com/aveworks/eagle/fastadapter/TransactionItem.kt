package com.aveworks.eagle.fastadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aveworks.eagle.R
import com.aveworks.eagle.data.Transaction
import com.aveworks.eagle.databinding.ItemListTransactionBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class TransactionItem(val tx :Transaction) : AbstractBindingItem<ItemListTransactionBinding>() {
    override val type: Int
        get() = R.id.fastadapter_transaction_item_id

    override fun bindView(binding: ItemListTransactionBinding, payloads: List<Any>) {
        binding.tx = tx
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemListTransactionBinding {
        return ItemListTransactionBinding.inflate(inflater, parent, false)
    }
}