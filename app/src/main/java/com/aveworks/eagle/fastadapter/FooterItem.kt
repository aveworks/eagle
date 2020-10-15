package com.aveworks.eagle.fastadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.aveworks.eagle.R
import com.aveworks.eagle.databinding.ItemListFooterBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class FooterItem(private val isLoading: Boolean,private val error : String?) : AbstractBindingItem<ItemListFooterBinding>() {

    override val type: Int
        get() = R.id.fastadapter_footer_item_id

    override fun bindView(binding: ItemListFooterBinding, payloads: List<Any>) {
        binding.isLoading = isLoading
        binding.error = error
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemListFooterBinding {
        return ItemListFooterBinding.inflate(inflater, parent, false)
    }
}