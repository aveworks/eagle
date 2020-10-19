package com.aveworks.eagle.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.aveworks.eagle.R
import com.aveworks.eagle.api.getBlockchainError
import com.aveworks.eagle.databinding.FragmentTransactionListBinding
import com.aveworks.eagle.databinding.ItemListFooterBinding
import com.aveworks.eagle.fastadapter.BalanceItem
import com.aveworks.eagle.fastadapter.FooterItem
import com.aveworks.eagle.fastadapter.TransactionItem
import com.aveworks.eagle.viewmodels.Events
import com.aveworks.eagle.viewmodels.TransactionListViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericFastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mikepenz.itemanimators.ScaleUpAnimator
import com.mikepenz.itemanimators.SlideDownAlphaAnimator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class TransactionListFragment : AppFragment<FragmentTransactionListBinding>(
    R.layout.fragment_transaction_list,
    enableAnalytics = true
) {
    private val args: TransactionListFragmentArgs by navArgs()

    private lateinit var fastAdapter: GenericFastAdapter
    private lateinit var balanceAdapter: ItemAdapter<BalanceItem>
    private lateinit var txAdapter: ItemAdapter<TransactionItem>
    private lateinit var footerAdapter: ItemAdapter<FooterItem>

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

        balanceAdapter = ItemAdapter()
        txAdapter = ItemAdapter()
        footerAdapter = ItemAdapter()

        //create the managing FastAdapter, by passing in the itemAdapter
        fastAdapter = FastAdapter.with(listOf(balanceAdapter, txAdapter, footerAdapter))

        fastAdapter.onClickListener = { _, _, item, _ ->
            when (item) {
                is TransactionItem -> {
                    val direction =
                        TransactionListFragmentDirections.actionTransactionListFragmentToTransactionDetailsFragment(
                            item.tx
                        )
                    findNavController().navigate(direction)
                }
            }
            false
        }


        /**
         * Try Again click listener in FastAdapter
         */
        fastAdapter.addClickListener({ vh: ItemListFooterBinding -> vh.tryAgain }) { _, _, _, _ ->
            viewModel.fetch()

            var item = FooterItem(true, null)

            footerAdapter.clear()
            footerAdapter.add(item)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = SlideDownAlphaAnimator()
            adapter = fastAdapter

            addOnScrollListener(object : EndlessRecyclerOnScrollListener(footerAdapter) {
                override fun onLoadMore(currentPage: Int) {
                    postDelayed({
                        if (viewModel.hasMoreTransactions) {
                            footerAdapter.clear()
                            footerAdapter.add(FooterItem(true, null))
                            viewModel.fetch()
                        }
                    }, 1)
                }
            })
        }

        binding.tryAgain.setOnClickListener {
            viewModel.fetch()
        }

        viewModel.wallet.observe(viewLifecycleOwner) { wallet ->
            balanceAdapter.add(BalanceItem(viewModel.xpub, wallet))
        }

        viewModel.events.observe(viewLifecycleOwner) { event ->
            when (event) {
                is Events.Loading -> {
                    binding.isLoading = !event.isLoadMore
                    binding.error = null
                }
                is Events.Error -> {
                    binding.isLoading = false

                    val errorMessage =
                        getString(R.string.error_message, getBlockchainError(event.error))

                    if (!event.isLoadMore) {
                        binding.error = errorMessage
                    } else {
                        var item = FooterItem(false, errorMessage)
                        footerAdapter.clear()
                        footerAdapter.add(item)
                    }
                }
            }
        }

        // Add the already fetched transactions
        txAdapter.add(viewModel.transactions.value!!.map { TransactionItem(it) })

        // then listen to new transactions and append data
        viewModel.createTransactionStream().observe(viewLifecycleOwner) { transactions ->
            binding.isLoading = false
            binding.error = null

            txAdapter.add(transactions.map { TransactionItem(it) })

            footerAdapter.clear()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_about, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_about -> {
                Toast.makeText(context, R.string.app_name, Toast.LENGTH_SHORT).show()
                return true
            }

        }
        return false
    }
}