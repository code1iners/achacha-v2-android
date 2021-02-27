package com.codeliner.achacha.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.codeliner.achacha.databinding.FragmentTodosBinding
import com.example.helpers.ui.AnimationManager

class TodosFragment: Fragment() {

    private lateinit var binding: FragmentTodosBinding
    private lateinit var viewModelFactory: TodosViewModelFactory
    private lateinit var viewModel: TodosViewModel
    // note. fab
    private var isFavCollapsed = true

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

        val fabLeft = AnimationManager.getRotateLeft45(requireContext())
        val fabRight = AnimationManager.getRotateRight45(requireContext())

        val fabHide = AnimationManager.getFadeOut(requireContext())
        fabHide.duration = 300
        fabHide.fillAfter = true
        val fabShow = AnimationManager.getFadeIn(requireContext())
        fabShow.duration = 300
        fabShow.fillAfter = true


        binding.fragmentTodosFabMain.setOnClickListener {
            if (isFavCollapsed) {
                cs.connect(binding.fragmentTodosFabTest1.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabMain.id, ConstraintSet.TOP)
                cs.connect(binding.fragmentTodosFabTest2.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabTest1.id, ConstraintSet.TOP)

                binding.fragmentTodosFabMain.startAnimation(fabRight)
                binding.fragmentTodosFabTest1.startAnimation(fabShow)
                binding.fragmentTodosFabTest2.startAnimation(fabShow)
            } else {
                cs.connect(binding.fragmentTodosFabTest1.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabList.id, ConstraintSet.BOTTOM)
                cs.connect(binding.fragmentTodosFabTest2.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabList.id, ConstraintSet.BOTTOM)

                binding.fragmentTodosFabMain.startAnimation(fabLeft)
                binding.fragmentTodosFabTest1.startAnimation(fabHide)
                binding.fragmentTodosFabTest2.startAnimation(fabHide)
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