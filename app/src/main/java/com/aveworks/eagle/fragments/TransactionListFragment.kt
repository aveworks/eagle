package com.aveworks.eagle.fragments

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aveworks.eagle.R
import com.aveworks.eagle.adapters.TransactionAdapter
import com.aveworks.eagle.api.getBlockchainError
import com.aveworks.eagle.databinding.FragmentTransactionListBinding
import com.aveworks.eagle.viewmodels.TransactionListViewModel
import com.aveworks.eagle.viewmodels.TransactionResponse
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject


@AndroidEntryPoint
class TransactionListFragment : AppFragment(
    R.layout.fragment_transaction_list,
    enableAnalytics = true
) {

    private val args: TransactionListFragmentArgs by navArgs()

    private lateinit var binding: FragmentTransactionListBinding

    @Inject
    lateinit var viewModelFactory: TransactionListViewModel.AssistedFactory

    private val viewModel: TransactionListViewModel by viewModels {
        TransactionListViewModel.provideFactory(
            viewModelFactory,
            args.xpub
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding = appBinding as FragmentTransactionListBinding

        val transactionAdapter = TransactionAdapter()
        transactionAdapter.xpub = args.xpub

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }

        binding.tryAgain.setOnClickListener {
            viewModel.fetch()
        }

        viewModel.transactions.observe(viewLifecycleOwner) { transactionResponse ->

            when (transactionResponse) {
                is TransactionResponse.Loading -> {
                    binding.isLoading = true
                    binding.error = null
                }

                is TransactionResponse.Success -> {
                    Log.v("TransactionListFragment", transactionResponse.transactions.toString())

                    binding.isLoading = false
                    binding.error = null

                    transactionAdapter.wallet = transactionResponse.wallet
                    transactionAdapter.submitList(transactionResponse.transactions)
                }

                is TransactionResponse.Error -> {
                    binding.isLoading = false
                    binding.error = getString(R.string.error_message, getBlockchainError(transactionResponse.error))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_about, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_about ->
                // Not implemented here
                return false
            else -> {
            }
        }
        return false
    }
}