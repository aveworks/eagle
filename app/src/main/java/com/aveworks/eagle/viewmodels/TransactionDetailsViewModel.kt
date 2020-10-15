package com.aveworks.eagle.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aveworks.eagle.api.BlockchainService
import com.aveworks.eagle.data.Transaction
import com.aveworks.eagle.data.Wallet
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException

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