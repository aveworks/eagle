package com.aveworks.eagle.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.aveworks.eagle.R
import com.aveworks.eagle.databinding.FragmentHomeBinding


class HomeFragment : AppFragment(R.layout.fragment_home, enableAnalytics = true) {

    lateinit var binding : FragmentHomeBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = appBinding as FragmentHomeBinding

        binding.button.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToTransactionListFragment(
                binding.xpub.text.toString()
            )
            findNavController().navigate(direction)
        }

        binding.demo.setOnClickListener {
            binding.xpub.setText("xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ")
        }

        binding.xpub.setOnEditorActionListener { textView, i, keyEvent ->
            if (i and EditorInfo.IME_MASK_ACTION !== 0) {
                binding.button.performClick()
            } else {
                false
            }
        }

        
    }
}