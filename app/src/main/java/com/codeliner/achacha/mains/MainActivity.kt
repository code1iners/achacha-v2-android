package com.codeliner.achacha.mains

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityMainBinding
import com.codeliner.achacha.ui.todos.list.TodoListFragmentDirections
import com.codeliner.achacha.utils.Const
import com.example.helpers.toastForShort
import com.example.helpers.ui.AnimationManager
import com.example.helpers.ui.getFadeIn
import com.example.helpers.ui.getFadeOut
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity()
    , BottomNavigationView.OnNavigationItemReselectedListener
{

    private lateinit var nc: NavController
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    private fun initialize() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        nc = this.findNavController(R.id.activityMain_navHostFragment)
        nc.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.todoListFragment -> {
                    viewModel.setCurrentBottomNavPosition(0)
                }

                R.id.accountListFragment -> {
                    viewModel.setCurrentBottomNavPosition(1)
                }
            }
        }
        app = application

        // note. for bottom navigation showing/hiding animation when move to todo create fragment
        initAnimations()
        // note. for bottom navigation with nav controller
        initBottomNavi()
        initObservers()
    }

    private fun initObservers() {
        // note. for fab buttons
        observeFabs()

        val d = resources.getInteger(R.integer.animation_duration_short).toLong()
        // note. for bottom nav position
        viewModel.currentBottomNavPosition.observe(this, Observer { position ->
            Timber.d("position: $position")
            when (position) {
                0 -> {

                }

                1 -> {

                }
            }
        })

        // note. for bottom nav
        viewModel.isBottomNavigationShowing.observe(this, Observer { isShowing ->
            when (isShowing) {
                true -> {
                    binding.activityMainBottomNav.startAnimation(animShow)
                    binding.activityMainBottomNav.visibility = View.VISIBLE
                }

                false -> {
                    binding.activityMainBottomNav.startAnimation(animHide)
                    binding.activityMainBottomNav.visibility = View.GONE
                }
            }
        })
    }

    private fun initBottomNavi() {
        binding.activityMainBottomNav.setupWithNavController(nc)
        binding.activityMainBottomNav.setOnNavigationItemReselectedListener(this)
    }

    private fun observeFabs() {
        // note. fab main
        observeFavMain()
        // note. fab menu 1
        observeFavOne()
        // note. fab menu test
        observeFabTest()
    }

    private fun observeFavMain() {
        val cs = ConstraintSet()
        cs.clone(binding.activityMainFabList)
        viewModel.isFavCollapsed.observe(this, Observer { isCollapsed ->
            if (!isCollapsed) {
                cs.connect(binding.activityMainFabCreate.id, ConstraintSet.BOTTOM, binding.activityMainFabMain.id, ConstraintSet.TOP)
                cs.connect(binding.activityMainFabClear.id, ConstraintSet.BOTTOM, binding.activityMainFabCreate.id, ConstraintSet.TOP)
                cs.connect(binding.activityMainFabTest.id, ConstraintSet.BOTTOM, binding.activityMainFabClear.id, ConstraintSet.TOP)

                binding.activityMainFabMain.startAnimation(animRotateRight)
                binding.activityMainFabCreate.startAnimation(animShow)
                binding.activityMainFabClear.startAnimation(animShow)
//                binding.activityMainFabTest.startAnimation(animShow)

            } else {
                cs.connect(binding.activityMainFabCreate.id, ConstraintSet.BOTTOM, binding.activityMainFabList.id, ConstraintSet.BOTTOM)
                cs.connect(binding.activityMainFabClear.id, ConstraintSet.BOTTOM, binding.activityMainFabList.id, ConstraintSet.BOTTOM)
                cs.connect(binding.activityMainFabTest.id, ConstraintSet.BOTTOM, binding.activityMainFabList.id, ConstraintSet.BOTTOM)

                binding.activityMainFabMain.startAnimation(animRotateLeft)
                binding.activityMainFabCreate.startAnimation(animHide)
                binding.activityMainFabClear.startAnimation(animHide)
//                binding.activityMainFabTest.startAnimation(animHide)
            }

            TransitionManager.beginDelayedTransition(binding.activityMainFabList, transition)
            cs.applyTo(binding.activityMainFabList)
        })
    }

    private fun observeFavOne() {
//        viewModel.onNavigateToCreateTodoReady.observe(viewLifecycleOwner, Observer { isReady ->
//            if (isReady) {
//                // note. update ui
//                MainActivity.onBottomNavigationHide()
//                binding.activityMainCalendarContainer.startAnimation(animHeaderHide)
//                binding.activityMainCalendarDividerBottom.startAnimation(animHeaderHide)
//                binding.activityMainTodoList.startAnimation(animHide)
//                binding.activityMainFabList.startAnimation(animHide)
//
//                viewModel.navigateToCreateTodoReady()
//                viewModel.navigateToCreateTodoComplete()
//            }
//        })
//
//        viewModel.onNavigateToCreateTodoProcess.observe(viewLifecycleOwner, Observer { start ->
//            if (start) {
//
//                findNavController().navigate(TodoListFragmentDirections.actionTodoListFragmentToTodoCreateFragment(viewModel.tasks.value ?: -1))
//
//                viewModel.navigateToCreateTodoProcessComplete()
//            }
//        })
    }

    private fun observeFabTest() {
//        viewModel.onTestTrigger.observe(viewLifecycleOwner, Observer {
//            if (it) {
//                context?.toastForShort("Test button clicked!")
//                viewModel.onTestComplete()
//            }
//        })
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        // note. not yet
    }

    companion object {
        private lateinit var binding: ActivityMainBinding
        private lateinit var viewModel: MainViewModel
        private var app: Application? = null
        private lateinit var animBottomNavHide: Animation
        private lateinit var animBottomNavShow: Animation

        // note. animations (ui)
        lateinit var animHeaderShow: Animation
        lateinit var animHeaderHide: Animation
        lateinit var animRotateLeft: Animation
        lateinit var animRotateRight: Animation
        lateinit var animHide: Animation
        lateinit var animShow: Animation
        lateinit var transition: AutoTransition

        fun initAnimations() {
            app?.let {
                animBottomNavHide = AnimationManager.getBottomNavHide(it.applicationContext)
                animBottomNavShow = AnimationManager.getBottomNavShow(it.applicationContext)

                animHeaderShow = AnimationManager.getHeaderShow(it)
                animHeaderHide = AnimationManager.getHeaderHide(it)

                animRotateLeft = AnimationManager.getRotateLeft45(it)
                animRotateRight = AnimationManager.getRotateRight45(it)

                animHide = AnimationManager.getFadeOut(it).apply {
                    duration = Const.animDefaultDuration
                    fillAfter = true
                }
                animShow = AnimationManager.getFadeIn(it).apply {
                    duration = Const.animDefaultDuration
                    fillAfter = true
                }

                transition = AutoTransition().apply {
                    duration = Const.animDefaultDuration
                    interpolator = AccelerateDecelerateInterpolator()
                }
            }
        }

        fun onBottomNavigationShow() {
            binding.activityMainBottomNav.startAnimation(animShow)
            binding.activityMainBottomNav.visibility = View.VISIBLE
            viewModel.setBottomNavigationShowing(true)
        }

        fun onBottomNavigationHide() {
            binding.activityMainBottomNav.startAnimation(animHide)
            binding.activityMainBottomNav.visibility = View.GONE
            viewModel.setBottomNavigationShowing(false)
        }
    }
}