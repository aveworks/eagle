package com.aveworks.eagle.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.aveworks.common.data.MultiAddressResponse
import com.aveworks.common.data.Transaction
import com.aveworks.common.data.Wallet
import com.aveworks.eagle.ImmediateSchedulersRule
import com.aveworks.eagle.repositories.TransactionsRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.internal.schedulers.ImmediateThinScheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.lang.Exception

@RunWith(MockitoJUnitRunner::class)
class TransactionListViewModelUnitTests {

    private lateinit var viewModel: TransactionListViewModel

    @Mock
    private lateinit var repository: TransactionsRepository
    @Mock
    private lateinit var eventsObserver : Observer<Events>
    @Mock
    private lateinit var walletObserver : Observer<Wallet>
    @Mock
    private lateinit var transactionsObserver : Observer<List<Transaction>>

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val schedulersRule = ImmediateSchedulersRule()

    @Before
    fun setup(){
        whenever(repository.getTransactions(any(), any())).then {

            val response: MultiAddressResponse = mock()

            whenever(response.wallet).thenReturn(mock())
            // Enabled it only when needed
            // whenever(response.info).thenReturn(mock())
            whenever(response.txs).thenReturn(listOf(mock()))

            Single.just(response)
        }

        viewModel = TransactionListViewModel(repository, "xpub")

        viewModel.events.observeForever(eventsObserver)
        viewModel.wallet.observeForever(walletObserver)
        viewModel.transactions.observeForever(transactionsObserver)
    }

    @Test
    fun onFetch_wallet(){
        viewModel.fetch()

        verify(walletObserver).onChanged(isA<Wallet>())
    }

    @Test
    fun onFetch_transactions(){
        viewModel.fetch()

        // Empty list
        verify(transactionsObserver).onChanged(argThat { isEmpty() })
        // And then the actual response
        verify(transactionsObserver).onChanged(argThat { isNotEmpty() })
    }

    @Test
    fun onError_getErrorEvent(){
        setupRepositoryWithError(repository)

        viewModel.fetch()

        verify(eventsObserver).onChanged(isA<Events.Loading>())
        verify(eventsObserver).onChanged(isA<Events.Error>())
    }

    @Test
    fun onFetch_getLoadingEvent(){
        viewModel.fetch()

        verify(eventsObserver).onChanged(isA<Events.Loading>())
    }

    private fun setupRepositoryWithError(repository: TransactionsRepository){
        // Reset the already successful definition
        reset(repository)

        whenever(repository.getTransactions(any(), any())).then {
            Single.error<Exception>(mock<Exception>())
        }
    }
}