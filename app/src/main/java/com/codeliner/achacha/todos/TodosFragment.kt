package com.codeliner.achacha.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.codeliner.achacha.databinding.FragmentTodosBinding
import com.example.helpers.ui.AnimationManager
import timber.log.Timber

class TodosFragment: Fragment() {

    private lateinit var binding: FragmentTodosBinding
    private lateinit var viewModelFactory: TodosViewModelFactory
    private lateinit var viewModel: TodosViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodosBinding.inflate(inflater)

        initViewModel()
        initFabs()

        return binding.root
    }

    private fun initFabs() {
        val cs = ConstraintSet()
        cs.clone(binding.fragmentTodosFabList)

        viewModel.isFavCollapsed.observe(viewLifecycleOwner, Observer { isCollapsed ->
            if (!isCollapsed) {
                cs.connect(binding.fragmentTodosFabTest1.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabMain.id, ConstraintSet.TOP)
                cs.connect(binding.fragmentTodosFabTest2.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabTest1.id, ConstraintSet.TOP)

                binding.fragmentTodosFabMain.startAnimation(viewModel.animRight)
                binding.fragmentTodosFabTest1.startAnimation(viewModel.animShow)
                binding.fragmentTodosFabTest2.startAnimation(viewModel.animShow)

            } else {
                cs.connect(binding.fragmentTodosFabTest1.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabList.id, ConstraintSet.BOTTOM)
                cs.connect(binding.fragmentTodosFabTest2.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabList.id, ConstraintSet.BOTTOM)

                binding.fragmentTodosFabMain.startAnimation(viewModel.animLeft)
                binding.fragmentTodosFabTest1.startAnimation(viewModel.animHide)
                binding.fragmentTodosFabTest2.startAnimation(viewModel.animHide)
            }

            TransitionManager.beginDelayedTransition(binding.fragmentTodosFabList, viewModel.transition)
            cs.applyTo(binding.fragmentTodosFabList)
        })
    }

    private fun initViewModel() {
        val app = requireNotNull(activity).application
        viewModelFactory = TodosViewModelFactory(app)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodosViewModel::class.java)
        // note. assignment view model into layout
        binding.viewModel = viewModel
    }
}