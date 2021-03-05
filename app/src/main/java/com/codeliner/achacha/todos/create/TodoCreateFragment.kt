package com.codeliner.achacha.todos.create

import android.app.Application
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.databinding.FragmentTodoCreateBinding
import com.codeliner.achacha.domains.todos.TodoDatabase
import com.codeliner.achacha.mains.MainActivity
import com.codeliner.achacha.utils.KeyboardManager
import com.google.android.material.chip.Chip
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import timber.log.Timber


class TodoCreateFragment: Fragment() {

    private lateinit var binding: FragmentTodoCreateBinding
    private lateinit var viewModel: TodoCreateViewModel
    private lateinit var app: Application

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this
        app = requireNotNull(activity).application

        initBackPressed()
        initViewModel()
        initListeners()
        initFocus()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        KeyboardManager.keyboardOpen(app, binding.fragmentTodoCreateInput)
    }

    private fun initFocus() {
        binding.fragmentTodoCreateInput.requestFocus()
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            back()
        }
    }

    private fun initViewModel() {
        val tasks = TodoCreateFragmentArgs.fromBundle(requireArguments()).tasks
        val dataSourceDao = TodoDatabase.getInstance(requireContext()).todoDatabaseDao
        val viewModelFactory = TodoCreateViewModelFactory(app, dataSourceDao, tasks)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodoCreateViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun initListeners() {
        // note. when enter done in device keyboard
        listenerKeyboardDone()
        // note. when enter the input text
        listenerInput()
        // note. when clicked add button
        listenerSubmit()
        // note. when clicked close button
        listenerClose()
        // note. keyboard show/hide
        listenerKeyboard()
        // note. when clicked chip item
        val checkedChipId = binding.fragmentTodoCreateChipGroup.checkedChipId // Returns View.NO_ID if singleSelection = false
        val checkedChipIds = binding.fragmentTodoCreateChipGroup.checkedChipIds // Returns a list of the selected chips' IDs, if any
        val list = mutableListOf<String>()
        for (i in 0 until binding.fragmentTodoCreateChipGroup.childCount) {
            val chip = binding.fragmentTodoCreateChipGroup.getChildAt(i) as Chip
            chip.setOnCheckedChangeListener { view, isChecked ->
                if (isChecked) {
                    list.add(view.text.toString())
                } else {
                    list.remove(view.text.toString())
                }
            }
        }
        Timber.d("checkedChipId: $checkedChipIds")
    }

    private fun listenerKeyboardDone() {
        binding.fragmentTodoCreateInput.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    binding.fragmentTodoCreateSubmit.performClick()
                    return@setOnEditorActionListener true
                }
            }
            false
        }
    }

    private fun listenerSubmit() {
        binding.fragmentTodoCreateSubmit.setOnClickListener {
            viewModel.onSaveTodo()
            KeyboardManager.keyboardClose(app, binding.fragmentTodoCreateInput)
        }
    }

    private fun listenerInput() {
        binding.fragmentTodoCreateInput.doOnTextChanged { text, _, _, _ ->
            viewModel.updateWork(text.toString())
        }
    }

    private fun listenerClose() {
        binding.fragmentTodoCreateClose.setOnClickListener {
            KeyboardManager.keyboardClose(app, binding.fragmentTodoCreateInput)
        }
    }

    private fun listenerKeyboard() {
        setEventListener(
                requireActivity(),
                viewLifecycleOwner,
                KeyboardVisibilityEventListener { visible ->
                    when (visible) {
                        true -> {
                            // note. open keyboard
                        }

                        false -> {
                            back()
                        }
                    }
                })
    }

    private fun back() {
        MainActivity.onBottomNavigationShow()
        this@TodoCreateFragment.findNavController().popBackStack()
    }
}