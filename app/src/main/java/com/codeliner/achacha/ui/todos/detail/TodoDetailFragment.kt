package com.codeliner.achacha.ui.todos.detail

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.data.todos.Todo
import com.codeliner.achacha.databinding.FragmentTodoDetailBinding
import com.codeliner.achacha.ui.inputs.chip.ChipInputActivity
import com.codeliner.achacha.ui.inputs.text.TextInputActivity
import com.codeliner.achacha.utils.Const.INPUT
import com.codeliner.achacha.utils.Const.TITLE
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class TodoDetailFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTodoDetailBinding
    private val viewModel: TodoDetailViewModel by viewModel()

    // note. For set text input activity title.
    private val memoInputDialogTitle = "Memo"
    private val tagInputDialogTitle = "Tags"

    // note. Text input activity.
    private val startForResultTextInput = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                Timber.w("data: $data")
                // note. Get input result text(memo) and update live data.
                viewModel.todoMemoUpdate(data.getStringExtra(INPUT))
            }
        }
    }
    // note. Chip input activity.
    private val startForResultChipInput = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { data ->
                Timber.w("data: $data")

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        observeTags()
    }

    private fun observeTodo() {
        viewModel.todo.observe(viewLifecycleOwner) {
            it?.let { todo ->
                // note. Memo visibility setting when memo exist or not.
                setTodoMemoVisibility(todo)
            }
        }
    }

    private fun setTodoMemoVisibility(todo: Todo) {
        when (todo.memo.isNullOrEmpty()) {
            true -> {
                binding.memoValue.visibility = View.GONE
                binding.memoUpdateButton.visibility = View.GONE
                binding.memoAddButton.visibility = View.VISIBLE
            }

            false -> {
                binding.memoValue.visibility = View.VISIBLE
                binding.memoUpdateButton.visibility = View.VISIBLE
                binding.memoAddButton.visibility = View.GONE

            }
        }
    }

    private fun observeOnBack() {
        viewModel.onBack.observe(viewLifecycleOwner) {
            it?.let { onBack ->
                if (onBack) {
                    // note. Destroy detail fragment view
                    back()

                    viewModel.backComplete()
                }
            }
        }
    }

    private fun observeMemo() {
        viewModel.onOpenTextInputJob.observe(viewLifecycleOwner) {
            it?.let { action ->
                if (action) {
                    // note. Open dialog for getting text.
                    startForResultTextInput.launch(Intent(activity, TextInputActivity::class.java).apply {
                        // note. Set dialog title.
                        putExtra(TITLE, memoInputDialogTitle)

                        // note. Pass memo data when exist the memo data.
                        viewModel.todo.value?.let {
                            putExtra(INPUT, it.memo)
                        }
                    })

                    viewModel.openTextInputJobComplete()
                }
            }
        }
    }

    private fun observeTags() {
        viewModel.onOpenChipInputJob.observe(viewLifecycleOwner) {
            it?.let { action ->
                if (action) {
                    // note. Open dialog for getting tags.
                    startForResultChipInput.launch(Intent(activity, ChipInputActivity::class.java).apply {
                        // note. Set dialog title.
                        putExtra(TITLE, tagInputDialogTitle)
                        // note. Pass the tags data basically.
                    })

                    viewModel.openChipInputComplete()
                }
            }
        }
    }

    private fun back() {
        findNavController().popBackStack()
    }
}