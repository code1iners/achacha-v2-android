package com.codeliner.achacha.todos.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.databinding.FragmentTodoCreateBinding
import com.codeliner.achacha.domains.todos.TodoDatabase
import com.codeliner.achacha.mains.MainActivity
import timber.log.Timber

class TodoCreateFragment: Fragment() {

    private lateinit var binding: FragmentTodoCreateBinding
    private lateinit var viewModel: TodoCreateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodoCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this

        initBackPressed()
        initViewModel()
        binding.fragmentTodoCreateInput.doOnTextChanged { text, _, _, _ ->
            viewModel.updateWork(text.toString())
        }

        binding.fragmentTodoCreateSubmit.setOnClickListener {
            viewModel.onSaveTodo()
            binding.fragmentTodoCreateInput.text?.clear()
            back()
        }

        return binding.root
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            back()
        }
    }

    private fun back() {
        MainActivity.onBottomNavigationSwitch()
        this@TodoCreateFragment.findNavController().popBackStack()
    }

    private fun initViewModel() {
        val app = requireNotNull(activity).application
        val dataSourceDao = TodoDatabase.getInstance(requireContext()).todoDatabaseDao
        val viewModelFactory = TodoCreateViewModelFactory(app, dataSourceDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodoCreateViewModel::class.java)
        binding.viewModel = viewModel
    }
}