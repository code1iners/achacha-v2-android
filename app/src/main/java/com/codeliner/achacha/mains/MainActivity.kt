package com.codeliner.achacha.mains

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityMainBinding
import com.codeliner.achacha.ui.accounts.list.AccountListViewModel
import com.codeliner.achacha.ui.todos.list.TodoListViewModel
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_CLEAR
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_CREATE
import com.codeliner.achacha.utils.Const.ACTION_ACCOUNT_TEST
import com.codeliner.achacha.utils.Const.ACTION_TODO_CLEAR
import com.codeliner.achacha.utils.Const.ACTION_TODO_CREATE
import com.codeliner.achacha.utils.Const.ACTION_TODO_TEST
import com.codeliner.achacha.utils.Const.ANIMATION_DURATION_SHORT
import com.codeliner.achacha.utils.Const.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
import com.example.helpers.ui.*
import com.example.helpers.utils.checkSelfPermissionCompat
import com.example.helpers.utils.requestPermissionsCompat
import com.example.helpers.utils.shouldShowRequestPermissionRationaleCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
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
        // note. for observers
        observers()
        // note. check device permissions
        checkPermissions()
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
        // note. for bottom navigation
        observeBottomNav()
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
        viewModel.isFavCollapsed.observe(this) { isCollapsed ->
//            Timber.d("isCollapsed: $isCollapsed")
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
        }

        MainViewModel.onFabAnimation.observe(this) {
            it?.let { isShowing ->
                when (isShowing) {
                    true -> {
                        binding.activityMainFabContainer.startAnimation(
                                this.getFadeIn().apply {
                                    duration = ANIMATION_DURATION_SHORT
                                    fillAfter = true
                                })
                    }

                    false -> {
                        binding.activityMainFabContainer.startAnimation(
                                this.getFadeOut().apply {
                                    duration = ANIMATION_DURATION_SHORT
                                    fillAfter = true
                                })
                    }
                }
            }
        }

        MainViewModel.onFabVisibility.observe(this) {
            it?.let { status ->
                when (status) {
                    true -> {
                        binding.activityMainFabContainer.visibility = View.VISIBLE
                    }
                    false -> {
                        binding.activityMainFabContainer.visibility = View.GONE
                    }
                }
                MainViewModel.setFabVisibilityComplete()
            }
        }
    }

    private fun observeFavActions() {
        viewModel.onClickCreateAction.observe(this) {
            it?.let { action ->
//                Timber.d("action: $action")
                when (action) {
                    ACTION_TODO_CREATE -> {
                        // note. update fab ui
                        MainViewModel.setFabAnimation(false, ANIMATION_DURATION_SHORT)
                        // note. bottom nav turn off
                        MainViewModel.setBottomNavigationAnimation(false, ANIMATION_DURATION_SHORT)
                        // note. move navigation
                        TodoListViewModel.todoCreateJob()
                    }

                    ACTION_TODO_CLEAR -> {
                        TodoListViewModel.todoClearJob()
                    }

                    ACTION_TODO_TEST -> {
                        TodoListViewModel.todoTestJob()
                    }

                    ACTION_ACCOUNT_CREATE -> {
                        // note. update fab ui
                        MainViewModel.setFabAnimation(false, ANIMATION_DURATION_SHORT)
                        // note. bottom nav turn off
                        MainViewModel.setBottomNavigationAnimation(false, ANIMATION_DURATION_SHORT)
                        // note. move navigation
                        AccountListViewModel.accountCreateJob()
                    }

                    ACTION_ACCOUNT_CLEAR -> {
                        AccountListViewModel.accountClearJob()
                    }

                    ACTION_ACCOUNT_TEST -> {
                        AccountListViewModel.accountTestJob()
                    }
                }
            }
        }
    }

    private fun observeBottomNav() {
        // note. for bottom nav position
        viewModel.currentBottomNavPosition.observe(this) { position ->
//            Timber.d("position: $position")
            when (position) {
                0 -> {

                }

                1 -> {

                }
            }
        }

        // note. for bottom nav
        MainViewModel.onBottomNavigationAnimation.observe(this) { isShowing ->
            when (isShowing) {
                true -> {
                    binding.activityMainBottomNav.startAnimation(
                            this.getBottomNavShow().apply {
                                duration = ANIMATION_DURATION_SHORT
                                fillAfter = true
                            }
                    )
                }

                false -> {
                    binding.activityMainBottomNav.startAnimation(
                            this.getBottomNavHide().apply {
                                duration = ANIMATION_DURATION_SHORT
                                fillAfter = true
                            }
                    )
                }
            }
        }

        MainViewModel.onBottomNavigationVisibility.observe(this) {
            it?.let { visible ->
                when (visible) {
                    true -> {
                        binding.activityMainBottomNav.visibility = View.VISIBLE
                    }

                    false -> {
                        binding.activityMainBottomNav.visibility = View.GONE
                    }
                }
                MainViewModel.setBottomNavigationVisibilityComplete()
            }
        }
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        // note. not yet
    }

    // note. permission check method
    private fun checkPermissions() {
        checkReadExternalStoragePermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_READ_EXTERNAL_STORAGE)
    }
    // note. permission request method
    private fun checkReadExternalStoragePermission(permission: String, requestCode: Int) {
        when (checkSelfPermissionCompat(permission) == PackageManager.PERMISSION_GRANTED) {
            true -> {
                // note. When user already have permission.
                // note. Implement user want action.

                // note. test log
//                Timber.w(getString(R.string.permission_granted))
//                toastingShort(R.string.permission_granted)
            }

            false -> {
                // note. When user does not have permission.
                when (shouldShowRequestPermissionRationaleCompat(permission)) {
                    // note. When the user pressed "Denied".
                    true -> {
                        // note. Show snackbar to request permission
                        binding.layout.snacking(
                            R.string.permission_required_message_storage,
                                Snackbar.LENGTH_INDEFINITE, R.string.ok) {
                            // note. Request permission to user.
                            requestPermissionsCompat(arrayOf(permission),
                                    requestCode)
                        }
                    }

                    // note. When the user pressed "Don't ask anymore"
                    false -> {
                        // note. Explain how to permission grant in app information
                        binding.layout.snacking(
                            R.string.permission_available_message
                        , Snackbar.LENGTH_LONG, R.string.setting) {
                            startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).also {
                                    it.data = Uri.fromParts("package", packageName, null)
                                })
                        }
                    }
                }
            }
        }
    }
    // note. permission response method
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_READ_EXTERNAL_STORAGE -> {
                when (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    true -> {
                        // note. permission granted successfully
//                        Timber.w("finally permission granted")
                        this.toastingShort(R.string.permission_granted)
                    }

                    false -> {
                        // note. permission denied successfully
//                        Timber.w("finally permission denied")
                        this.toastingShort(R.string.permission_denied)
                    }
                }
            }
        }
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
                    duration = ANIMATION_DURATION_SHORT
                    fillAfter = true
                }
                animShow = AnimationManager.getFadeIn(it).apply {
                    duration = ANIMATION_DURATION_SHORT
                    fillAfter = true
                }

                transition = AutoTransition().apply {
                    duration = ANIMATION_DURATION_SHORT
                    interpolator = AccelerateDecelerateInterpolator()
                }
            }
        }
    }
}