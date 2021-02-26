package com.codeliner.achacha.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.FragmentTodosBinding

class TodosFragment: Fragment() {

    private lateinit var binding: FragmentTodosBinding
    private lateinit var viewModelFactory: TodosViewModelFactory
    private lateinit var viewModel: TodosViewModel
    // note. fab
    private var isFavCollapsed = true
    private lateinit var fabClock: Animation
    private lateinit var fabAntiClock: Animation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodosBinding.inflate(inflater)

        initViewModel()

        // note. fav collapse
        val cs = ConstraintSet()
        cs.clone(binding.fragmentTodosFabList)

        fabClock = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_clock)
        fabAntiClock = AnimationUtils.loadAnimation(context, R.anim.fab_rotate_anticlock)

        binding.fragmentTodosFabMain.setOnClickListener {
            if (isFavCollapsed) {
                cs.connect(binding.fragmentTodosFabTest1.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabMain.id, ConstraintSet.TOP)
                cs.connect(binding.fragmentTodosFabTest2.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabTest1.id, ConstraintSet.TOP)

                binding.fragmentTodosFabMain.startAnimation(fabClock)
            } else {
                cs.connect(binding.fragmentTodosFabTest1.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabList.id, ConstraintSet.BOTTOM)
                cs.connect(binding.fragmentTodosFabTest2.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabList.id, ConstraintSet.BOTTOM)

                binding.fragmentTodosFabMain.startAnimation(fabAntiClock)
            }

            val transition = AutoTransition()
            transition.duration = 300
            transition.interpolator = AccelerateDecelerateInterpolator()

            TransitionManager.beginDelayedTransition(binding.fragmentTodosFabList, transition)
            cs.applyTo(binding.fragmentTodosFabList)

            isFavCollapsed = !isFavCollapsed
        }

        return binding.root
    }

    private fun initViewModel() {
        val app = requireNotNull(activity).application
        viewModelFactory = TodosViewModelFactory(app)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodosViewModel::class.java)
        // note. assignment view model into layout
        binding.viewModel = viewModel
    }
}