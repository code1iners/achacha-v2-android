package com.codeliner.achacha.todos.create

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.databinding.FragmentTodoCreateBinding
import com.codeliner.achacha.domains.todos.TodoDatabase
import com.codeliner.achacha.mains.MainActivity
import com.codeliner.achacha.utils.KeyboardManager
import timber.log.Timber

class TodoCreateFragment: Fragment()
    , TextView.OnEditorActionListener
{

    private lateinit var binding: FragmentTodoCreateBinding
    private lateinit var viewModel: TodoCreateViewModel
    private lateinit var app: Application

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodoCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this
        app = requireNotNull(activity).application

        initBackPressed()
        initViewModel()
        initListeners()
        initFocus()

        return binding.root
    }

    private fun initFocus() {
        binding.fragmentTodoCreateInput.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        KeyboardManager.keyboardOpen(app, binding.fragmentTodoCreateInput)
        KeyboardManager.init(requireActivity()).status()?.observe(viewLifecycleOwner, Observer {
            Timber.d("status: ${it.name}, isBackPressed: ${viewModel.isBackPressed.value}")
            if (it == KeyboardManager.KeyboardStatus.OPEN) {
                viewModel.onBackPressed()
            } else if (it == KeyboardManager.KeyboardStatus.CLOSED && viewModel.isBackPressed.value == true) {
                back()
            }
        })
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            back()
        }
    }

    private fun initViewModel() {

        val dataSourceDao = TodoDatabase.getInstance(requireContext()).todoDatabaseDao
        val viewModelFactory = TodoCreateViewModelFactory(app, dataSourceDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TodoCreateViewModel::class.java)
        binding.viewModel = viewModel
    }

    private fun initListeners() {
        // note. text input done listener
        binding.fragmentTodoCreateInput.setOnEditorActionListener(this)
        
        // note. when enter the input text
        binding.fragmentTodoCreateInput.doOnTextChanged { text, _, _, _ ->
            viewModel.updateWork(text.toString())
        }
        // note. when clicked add button
        binding.fragmentTodoCreateSubmit.setOnClickListener {
            viewModel.onSaveTodo()
            exit()
        }

        binding.fragmentTodoCreateClose.setOnClickListener {
            exit()
        }
    }

    private fun exit() {
        viewModel.onBackPressed()
        KeyboardManager.keyboardClose(app, binding.fragmentTodoCreateInput)
    }

    private fun back() {
        MainActivity.onBottomNavigationSwitch()
        this@TodoCreateFragment.findNavController().popBackStack()
        viewModel.onBackPressedComplete()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        v?.let { view ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    when (view.id) {
                        binding.fragmentTodoCreateInput.id -> {
                            binding.fragmentTodoCreateSubmit.performClick()
                        }
                        else -> return false
                    }

                }
                else -> return false
            }
        }
        return true
    }
}