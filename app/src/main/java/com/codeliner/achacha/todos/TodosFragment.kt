package com.codeliner.achacha.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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