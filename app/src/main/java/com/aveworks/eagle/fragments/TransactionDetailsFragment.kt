package com.aveworks.eagle.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.aveworks.eagle.R
import com.aveworks.eagle.databinding.FragmentTransactionDetailsBinding
import com.aveworks.eagle.viewmodels.TransactionDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionDetailsFragment : BottomSheetDialogFragment() {

    private val args: TransactionDetailsFragmentArgs by navArgs()

    private val viewModel: TransactionDetailsViewModel by viewModels {
        TransactionDetailsViewModel.TransactionDetailsViewModelFactory(args.transaction)
    }

    private lateinit var binding: FragmentTransactionDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_transaction_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.transactions.observe(viewLifecycleOwner) { transaction ->
            binding.tx = transaction
        }
    }
}