package com.aveworks.eagle.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aveworks.common.data.Transaction

class TransactionDetailsViewModel constructor(
    tx: Transaction
) : ViewModel() {

    val transaction: LiveData<Transaction> = MutableLiveData(tx)

    class TransactionDetailsViewModelFactory(private val tx: Transaction) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            TransactionDetailsViewModel(tx) as T
    }
}