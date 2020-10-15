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
    @Assisted private val xpub: String
) : ViewModel() {

    private val txLiveData: MutableLiveData<TransactionResponse> =
        MutableLiveData<TransactionResponse>()

    private val disposables = CompositeDisposable()

    val transactions: LiveData<TransactionResponse> by lazy {
        loadTransactions()
        txLiveData
    }

    private fun loadTransactions() {
        // TODO inject service

//disposables.clear()
//        Log.v("WTF", "disposables : " + disposables.size())

        // http 429 // too many requests

        // Do an asynchronous operation to fetch users.
        service
            .multiAddressObservable(xpub)
            .subscribeOn(Schedulers.io())
            // .observeOn(AndroidSchedulers.mainThread()) // no need as we are posting values to LiveData
            .doOnSubscribe {
                Log.v("WTF", "SHOW LOADER")
                txLiveData.postValue(TransactionResponse.Loading)
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
                    it.printStackTrace()
                    analytics.trackException(it)
                    txLiveData.postValue(TransactionResponse.Error(it))
                },
                onNext = {
                    txLiveData.postValue(TransactionResponse.Success(it.wallet, it.txs))
                }
            ).addTo(disposables)

    }

    override fun onCleared() {
        Log.v("WTF", "onCleared")
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

sealed class TransactionResponse {
    object Loading : TransactionResponse()
    class Success(val wallet: Wallet, val transactions: List<Transaction>) : TransactionResponse()
    class Error(val error: Throwable) : TransactionResponse()
}