package com.aveworks.eagle.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.aveworks.eagle.R
import com.aveworks.eagle.databinding.FragmentHomeBinding
import com.aveworks.eagle.viewmodels.HomeViewModel
import com.jakewharton.rxbinding4.widget.textChangeEvents
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.kotlin.addTo

@AndroidEntryPoint
class HomeFragment : AppFragment<FragmentHomeBinding>(R.layout.fragment_home, enableAnalytics = true) {

    private val demoXPub = "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ"

    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToTransactionListFragment(
                viewModel.xPub.value
            )
            findNavController().navigate(direction)
        }

        binding.demo.setOnClickListener {
            TransactionListFragmentDirections.actionTransactionListFragmentToTransactionDetailsFragment()
            binding.xpub.setText(demoXPub)
        }

        binding.xpub.setOnEditorActionListener { _, i, _ ->
            if (i and EditorInfo.IME_MASK_ACTION !== 0) {
                binding.button.performClick()
            } else {
                false
            }
        }

        binding.xpub.textChangeEvents().subscribe {
            viewModel.xPubChanged(it.text.toString())
        }.addTo(onDestroyDispose)

        viewModel.xPubValidLiveData.observe(viewLifecycleOwner, Observer {
            binding.button.isEnabled = it
        })
    }
}