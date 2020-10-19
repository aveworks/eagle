package com.aveworks.eagle.fastadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aveworks.eagle.R
import com.aveworks.eagle.data.Transaction
import com.aveworks.eagle.data.Wallet
import com.aveworks.eagle.databinding.ItemListBalanceBinding
import com.aveworks.eagle.databinding.ItemListTransactionBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class BalanceItem(val xpub: String, val wallet: Wallet) : AbstractBindingItem<ItemListBalanceBinding>() {

    override val type: Int
        get() = R.id.fastadapter_balance_item_id

    override fun bindView(binding: ItemListBalanceBinding, payloads: List<Any>) {
        binding.wallet = wallet
        binding.xpub = xpub
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemListBalanceBinding {
        return ItemListBalanceBinding.inflate(inflater, parent, false)
    }
}