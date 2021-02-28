package com.codeliner.achacha.todos.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionManager
import com.codeliner.achacha.databinding.FragmentTodosBinding

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
        initObservers()

        return binding.root
    }

    private fun initObservers() {
        // note. fabs
        val cs = ConstraintSet()
        cs.clone(binding.fragmentTodosFabList)
        viewModel.isFavCollapsed.observe(viewLifecycleOwner, Observer { isCollapsed ->
            if (!isCollapsed) {
                cs.connect(binding.fragmentTodosFabCreate.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabMain.id, ConstraintSet.TOP)
                cs.connect(binding.fragmentTodosFabClear.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabCreate.id, ConstraintSet.TOP)

                binding.fragmentTodosFabMain.startAnimation(viewModel.animRight)
                binding.fragmentTodosFabCreate.startAnimation(viewModel.animShow)
                binding.fragmentTodosFabClear.startAnimation(viewModel.animShow)

            } else {
                cs.connect(binding.fragmentTodosFabCreate.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabList.id, ConstraintSet.BOTTOM)
                cs.connect(binding.fragmentTodosFabClear.id, ConstraintSet.BOTTOM, binding.fragmentTodosFabList.id, ConstraintSet.BOTTOM)

                binding.fragmentTodosFabMain.startAnimation(viewModel.animLeft)
                binding.fragmentTodosFabCreate.startAnimation(viewModel.animHide)
                binding.fragmentTodosFabClear.startAnimation(viewModel.animHide)
            }

            TransitionManager.beginDelayedTransition(binding.fragmentTodosFabList, viewModel.transition)
            cs.applyTo(binding.fragmentTodosFabList)
        })

        // note. fab menu 1
        viewModel.onNavigateToCreateTodo.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    this.findNavController().navigate(TodosFragmentDirections.actionTodosFragmentToTodoCreateFragment())
                    viewModel.navigateToCreateTodoComplete()
                }
            }
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