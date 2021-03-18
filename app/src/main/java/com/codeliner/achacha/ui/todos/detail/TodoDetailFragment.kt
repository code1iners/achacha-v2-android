package com.codeliner.achacha.ui.todos.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.databinding.FragmentTodoDetailBinding
import com.codeliner.achacha.ui.TextInputActivity
import com.codeliner.achacha.utils.log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TodoDetailFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTodoDetailBinding
    private val viewModel: TodoDetailViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initialize(inflater)
        observers()

        return binding.root
    }

    private fun initialize(inflater: LayoutInflater) {
        initializeBinding(inflater)
    }

    private fun initializeBinding(inflater: LayoutInflater) {
        binding = FragmentTodoDetailBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        arguments?.let {
            viewModel.setTodo(TodoDetailFragmentArgs.fromBundle(it).todo)
        }
    }

    private fun observers() {
        observeTodo()
        observeOnBack()
        observeMemo()
    }

    private fun observeTodo() {
        viewModel.todo.observe(viewLifecycleOwner) {
            it?.let { todo ->

            }
        }
    }

    private fun observeOnBack() {
        viewModel.onBack.observe(viewLifecycleOwner) {
            it?.let { onBack ->
                if (onBack) {
                    back()

                    viewModel.backComplete()
                }
            }
        }
    }

    private fun observeMemo() {
        viewModel.onUpdateMemoJob.observe(viewLifecycleOwner) {
            it?.let { job ->
                if (job) {
//                    val intent = Intent(activity, TextInputActivity::class.java)
//                    startActivityForResult(intent, 0)

                    viewModel.updateMemoJobComplete()
                }
            }
        }
    }

    private fun back() {
        findNavController().popBackStack()
    }
}