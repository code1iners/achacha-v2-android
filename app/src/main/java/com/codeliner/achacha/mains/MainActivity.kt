package com.codeliner.achacha.mains

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var nc: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        nc = this.findNavController(R.id.activityMain_navHostFragment)

        // note. for bottom navigation with nav controller
        binding.activityMainBottomNav.setupWithNavController(nc)
    }


}