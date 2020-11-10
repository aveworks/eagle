package com.aveworks.eagle.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aveworks.common.data.Transaction
import com.aveworks.common.data.Wallet
import com.aveworks.eagle.repositories.TransactionsRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

class TransactionListViewModel @AssistedInject constructor(
    private val repository: TransactionsRepository,
    @Assisted val xpub: String
) : ViewModel() {
    private val disposables = CompositeDisposable()

    var hasMoreTransactions = true

    val events: MutableLiveData<Events> = MutableLiveData<Events>()
    val wallet: MutableLiveData<Wallet> = MutableLiveData<Wallet>()
    val transactions: MutableLiveData<List<Transaction>> = MutableLiveData(arrayListOf())

    private var transactionList = arrayListOf<Transaction>()

    private var transactionStream: MutableLiveData<List<Transaction>>? = null

    /**
     * Create a new `stream` of transactions to easily append data to the adapter
     */
    fun createTransactionStream(): MutableLiveData<List<Transaction>> {
        transactionStream = MutableLiveData<List<Transaction>>()
        return transactionStream!!
    }

    private fun loadTransactions() {
        disposables.clear()

        repository.getTransactions(xpub, transactionList.size)
            .subscribeOn(Schedulers.io())
            /**
             * No need to use observeOn(AndroidSchedulers.mainThread()) as we are posting values to LiveData
             * doOnSubscribe runs on Schedulers.io() and can easily miss that
             */
            // .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                events.postValue(Events.Loading(isLoadMore()))
            }
            .subscribeBy(
                onError = {
                    it.printStackTrace()
                    events.postValue(Events.Error(isLoadMore(), it))
                },
                onSuccess = {
                    if (transactionList.isEmpty()) {
                        wallet.postValue(it.wallet)
                    }

                    if (it.txs.isEmpty()) {
                        hasMoreTransactions = false
                    }

                    transactionList.addAll(it.txs)
                    transactions.postValue(transactionList)
                    // Send only the new transactions, this way we can append the data to the adapter
                    transactionStream?.postValue(it.txs)

                }
            ).addTo(disposables)

    }

    private fun isLoadMore(): Boolean = transactionList.isNotEmpty()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    fun fetch() {
        loadTransactions()
    }

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(xpub: String): TransactionListViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            xpub: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(xpub) as T
            }
        }
    }
}

sealed class Events {
    class Loading(val isLoadMore: Boolean) : Events()
    class Error(val isLoadMore: Boolean, val error: Throwable) : Events()
}