package com.aveworks.eagle.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aveworks.eagle.R
import com.aveworks.eagle.data.Transaction
import com.aveworks.eagle.data.Wallet
import com.aveworks.eagle.databinding.ItemListBalanceBinding
import com.aveworks.eagle.databinding.ItemListTransactionBinding
import com.aveworks.eagle.fragments.TransactionListFragmentDirections
import kotlin.collections.ArrayList


class TransactionAdapter : ListAdapter<Transaction, RecyclerView.ViewHolder>(
    TransactionDiffCallback()
) {
    var wallet : Wallet? = null
    var xpub: String? = null

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_TRANSACTION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_HEADER -> {
                return BalanceViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_list_balance,
                        parent,
                        false
                    )
                )
            }
            else -> {
                return TransactionViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_list_transaction,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            wallet?.let {
                (holder as BalanceViewHolder).bind(it, xpub ?: " - ")
            }
        } else {
            getItem(position)?.let {
                (holder as TransactionViewHolder).bind(it)
            }
        }
    }

    override fun submitList(list: List<Transaction>?) {
        super.submitList(ArrayList(list).also { it.add(0, null) })
    }

    class BalanceViewHolder(private val binding: ItemListBalanceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Wallet, x: String) {
            with(binding) {
                wallet = item
                xpub = x
                executePendingBindings()
            }
        }
    }

    class TransactionViewHolder(private val binding: ItemListTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.setClickListener {
                binding.tx?.let {
                    navigateToDetails(it, itemView)
                }
            }
        }

        private fun navigateToDetails(transaction : Transaction,  view: View) {
            val direction = TransactionListFragmentDirections.actionTransactionListFragmentToTransactionDetailsFragment(transaction)
            view.findNavController().navigate(direction)
        }

        fun bind(item: Transaction) {
            with(binding) {
                tx = item
                executePendingBindings()
            }
        }
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_TRANSACTION = 1
    }
}

internal class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem.hash == newItem.hash
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return oldItem == newItem
    }
}