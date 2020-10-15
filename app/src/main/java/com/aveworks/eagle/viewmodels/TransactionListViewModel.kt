package com.aveworks.eagle.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aveworks.eagle.Analytics
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

class TransactionListViewModel @AssistedInject constructor(
    private val analytics: Analytics,
    private val service: BlockchainService,
    @Assisted val xpub: String
) : ViewModel() {
    private val disposables = CompositeDisposable()

    var hasMoreTransactions = true

    val events: MutableLiveData<Events> = MutableLiveData<Events>()
    val wallet: MutableLiveData<Wallet> = MutableLiveData<Wallet>()
    val transactions: MutableLiveData<List<Transaction>> by lazy {
        loadTransactions()
        MutableLiveData(arrayListOf())
    }

    private var transactionList = arrayListOf<Transaction>()

    private var transactionStream: MutableLiveData<List<Transaction>>? = null

    /**
     * Use this stream to append data to the adapter
     */
    fun createTransactionStream(): MutableLiveData<List<Transaction>> {
        transactionStream = MutableLiveData<List<Transaction>>()
        return transactionStream!!
    }

    private fun loadTransactions() {
        disposables.clear()

        service
            .multiAddressObservable(xpub, offset = transactionList.size)
            .subscribeOn(Schedulers.io())
            /**
             * No need to use observeOn(AndroidSchedulers.mainThread()) as we are posting values to LiveData
             * doOnSubscribe runs on Schedulers.io() and can easily miss that
             */
            // .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                events.postValue(Events.Loading(isLoadMore()))
            }
            .map { it ->
                it.also { response ->
                    val conv = response.info.local["conversion"] as? Double

                    if (conv is Double) {
                        response.txs.map { tx ->
                            tx.also {
                                it.fiatAmount = (it.amount / 1e8 * conv * 100).toLong()
                            }
                        }
                    }
                }
            }
            .subscribeBy(
                onError = {
                    analytics.trackException(it)
                    events.postValue(Events.Error(isLoadMore(), it))
                },
                onNext = {
                    if (transactionList.isEmpty()) {
                        wallet.postValue(it.wallet)
                    }

                    if (it.txs.isEmpty()) {
                        hasMoreTransactions = false
                    }

                    transactionList.addAll(it.txs)
                    transactions.postValue(transactionList)
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