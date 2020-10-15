package com.aveworks.eagle.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.aveworks.eagle.Analytics
import com.aveworks.eagle.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_transaction_list.*
import javax.inject.Inject

@AndroidEntryPoint
abstract class AppFragment(
    @LayoutRes var layout: Int,
    var enableAnalytics: Boolean
) : Fragment() {

    @Inject
    lateinit var analytics: Analytics

    internal lateinit var appBinding: ViewDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        appBinding = DataBindingUtil.inflate(layoutInflater, layout, container, false)
        appBinding.lifecycleOwner = this

        appBinding.root.findViewById<Toolbar>(R.id.toolbar)?.let{
            it.setupWithNavController(navController, appBarConfiguration)
        }

        return appBinding.root
    }

    override fun onResume() {
        super.onResume()

        if (enableAnalytics) {
            analytics.trackScreenView()
        }
    }

}