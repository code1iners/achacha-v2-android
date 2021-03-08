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
import com.codeliner.achacha.ui.todos.list.TodoListViewModel
import com.codeliner.achacha.utils.Const
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_CLEAR
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_CREATE
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_TEST
import com.codeliner.achacha.utils.Const.ACTION_TODO_CLEAR
import com.codeliner.achacha.utils.Const.ACTION_TODO_CREATE
import com.codeliner.achacha.utils.Const.ACTION_TODO_TEST
import com.example.helpers.toastForShort
import com.example.helpers.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity()
    , BottomNavigationView.OnNavigationItemReselectedListener
{

    private lateinit var nc: NavController
    private val viewModel: MainViewModel by viewModel()
    private val todoListViewModel: TodoListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        // note. for observers
        observers()
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
    }

    private fun initBottomNavi() {
        binding.activityMainBottomNav.setupWithNavController(nc)
        binding.activityMainBottomNav.setOnNavigationItemReselectedListener(this)
    }

    private fun observers() {
        // note. for fab buttons
        observeFabs()

        val delayShort = resources.getInteger(R.integer.animation_duration_short).toLong()
        // note. for bottom nav position
        viewModel.currentBottomNavPosition.observe(this, Observer { position ->
            Timber.d("position: $position")
            when (position) {
                0 -> {

                }

                1 -> {

                }
            }

            closeFabs()
        })

        // note. for bottom nav
        viewModel.isBottomNavigationShowing.observe(this, Observer { isShowing ->
            when (isShowing) {
                true -> {
                    binding.activityMainBottomNav.startAnimation(
                            this.getBottomNavShow()
                    )
                    binding.activityMainBottomNav.visibility = View.VISIBLE
                }

                false -> {
                    binding.activityMainBottomNav.startAnimation(
                            this.getBottomNavHide()
                    )
                    binding.activityMainBottomNav.visibility = View.GONE
                }
            }
        })
    }

    private fun closeFabs() {
        if (viewModel.isFavCollapsed.value == false) {
            viewModel.switchCollapse()
        }
    }

    private fun observeFabs() {
        // note. fab main
        observeFavMain()
        // note. fab actions
        observeFavActions()
    }

    private fun observeFavMain() {
        val cs = ConstraintSet()
        cs.clone(binding.activityMainFabContainer)
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
                cs.connect(binding.activityMainFabCreate.id, ConstraintSet.BOTTOM, binding.activityMainFabContainer.id, ConstraintSet.BOTTOM)
                cs.connect(binding.activityMainFabClear.id, ConstraintSet.BOTTOM, binding.activityMainFabContainer.id, ConstraintSet.BOTTOM)
                cs.connect(binding.activityMainFabTest.id, ConstraintSet.BOTTOM, binding.activityMainFabContainer.id, ConstraintSet.BOTTOM)

                binding.activityMainFabMain.startAnimation(animRotateLeft)
                binding.activityMainFabCreate.startAnimation(animHide)
                binding.activityMainFabClear.startAnimation(animHide)
//                binding.activityMainFabTest.startAnimation(animHide)
            }

            TransitionManager.beginDelayedTransition(binding.activityMainFabContainer, transition)
            cs.applyTo(binding.activityMainFabContainer)
        })

        MainViewModel.isFabShowing.observe(this, Observer {
            it?.let { isShowing ->
                val d = resources.getInteger(R.integer.animation_duration_short).toLong()
                when (isShowing) {
                    true -> {
                        binding.activityMainFabContainer.startAnimation(
                                this.getFadeIn().apply {
                                    duration = d
                                    fillAfter = true
                                })
                    }

                    false -> {
                        binding.activityMainFabContainer.startAnimation(
                                this.getFadeOut().apply {
                                    duration = d
                                    fillAfter = true
                                })
                    }
                }
            }
        })

        MainViewModel.isFabShowingProcess.observe(this, Observer {
            it?.let { status ->
                when (status) {
                    true -> {
                        binding.activityMainFabContainer.visibility = View.VISIBLE
                    }
                    false -> {
                        binding.activityMainFabContainer.visibility = View.GONE
                    }
                }
                MainViewModel.setFabShowingProcessComplete()
            }
        })
    }

    private fun observeFavActions() {
        viewModel.onClickCreateAction.observe(this, Observer {
            it?.let { action ->
                Timber.d("action: $action")
                when (action) {
                    ACTION_TODO_CREATE -> {
                        closeFabs()
                        // note. fab
                        MainViewModel.setFabShowingUI(false)

                        TodoListViewModel.todoCreateJob()
                    }

                    ACTION_TODO_CLEAR -> {
                        TodoListViewModel.todoClearJob()
                    }

                    ACTION_TODO_TEST -> {
                        TodoListViewModel.todoTestJob()
                    }

                    ACTION_ACCOUNT_CREATE -> {

                    }

                    ACTION_ACCOUNT_CLEAR -> {

                    }

                    ACTION_ACCOUNT_TEST -> {

                    }
                }
            }
        })
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        // note. not yet
    }

    companion object {
        private lateinit var binding: ActivityMainBinding
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
    }
}