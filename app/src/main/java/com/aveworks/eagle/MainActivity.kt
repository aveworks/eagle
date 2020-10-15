package com.aveworks.eagle;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        findNavController(R.id.nav_host).addOnDestinationChangedListener { _, destination, _ ->
//            if(destination.id == R.id.homeFragment) {
//                toolbar.visibility = View.GONE
//            } else {
//                toolbar.visibility = View.VISIBLE
//            }
//        }

//        setSupportActionBar(toolbar)
//
//        val navController = findNavController(R.id.nav_host)
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}
