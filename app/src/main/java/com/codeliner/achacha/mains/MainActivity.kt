package com.codeliner.achacha.mains

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityMainBinding
import com.example.helpers.ui.AnimationManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()
    , BottomNavigationView.OnNavigationItemReselectedListener
{


    private lateinit var nc: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        nc = this.findNavController(R.id.activityMain_navHostFragment)
        app = application

        // note. for bottom navigation showing/hiding animation when move to todo create fragment
        initAnimations()
        // note. for main view model
        initViewModel()
        // note. for bottom navigation with nav controller
        initBottomNavi()
    }

    private fun initViewModel() {
        val app = requireNotNull(this).application
        val viewModelFactory = MainViewModelFactory(app)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun initBottomNavi() {
        binding.activityMainBottomNav.setupWithNavController(nc)
        binding.activityMainBottomNav.setOnNavigationItemReselectedListener(this)
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        // note. not yet
    }

    companion object {
        private lateinit var binding: ActivityMainBinding
        private lateinit var viewModel: MainViewModel
        private var app: Application? = null
        private lateinit var animHide: Animation
        private lateinit var animShow: Animation

        fun initAnimations() {
            app?.let {
                animHide = AnimationManager.getBottomNavHide(it.applicationContext)
                animShow = AnimationManager.getBottomNavShow(it.applicationContext)
            }
        }

        fun onBottomNavigationSwitch() {
            when (viewModel.isBottomNavigationShowing.value) {
                true -> binding.activityMainBottomNav.startAnimation(animHide)
                false -> binding.activityMainBottomNav.startAnimation(animShow)
            }
            viewModel.onBottomNavigationShowingSwitch()
        }
    }
}