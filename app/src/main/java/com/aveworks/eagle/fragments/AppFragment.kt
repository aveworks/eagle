package com.aveworks.eagle.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.aveworks.eagle.Analytics
import com.aveworks.eagle.R
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * AppFragment
 *
 * This class is a useful abstract base class. Extend all other Fragments if possible.
 * Some of the features can be turned on/off in the constructor.
 *
 * It's crucial every AppFragment implementation to call @AndroidEntryPoint
 *
 * @property layout the layout id of the fragment
 * @property enableAnalytics you can turn off analytics when needed. eg. in viewpager where onResume
 * is called when the fragment is not actually visible
 *
 */

abstract class AppFragment<T : ViewDataBinding>(
    @LayoutRes var layout: Int,
    var enableAnalytics: Boolean
) : Fragment() {

    @Inject
    lateinit var analytics: Analytics

    internal lateinit var binding: T

    /**
     * You can register any subscription for disposal when the fragment is destroyed
     */
    internal val onDestroyDispose = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding = DataBindingUtil.inflate(layoutInflater, layout, container, false)
        binding.lifecycleOwner = this

        binding.root.findViewById<Toolbar>(R.id.toolbar)?.let {
            (activity as AppCompatActivity).setSupportActionBar(it)
            it.setupWithNavController(navController, appBarConfiguration)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if (enableAnalytics) {
            analytics.trackScreenView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyDispose.clear()
    }

}