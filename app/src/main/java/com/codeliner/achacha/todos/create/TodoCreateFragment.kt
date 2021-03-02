package com.codeliner.achacha.todos.create

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
    , Observer<KeyboardManager.KeyboardStatus>
{

    private lateinit var binding: FragmentTodoCreateBinding
    private lateinit var viewModel: TodoCreateViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTodoCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this

        initBackPressed()
        initViewModel()
        initListeners()

        return binding.root
    }

    private fun keyboardOpen() {
        Timber.w("keyboardOpen")
    }

    private fun keyboardClosed() {
        Timber.w("keyboardClosed")
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            back()
        }
    }

    private fun initViewModel() {
        val app = requireNotNull(activity).application
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
            binding.fragmentTodoCreateInput.text?.clear()
            back()
        }
    }

    private fun back() {
        MainActivity.onBottomNavigationSwitch()
        this@TodoCreateFragment.findNavController().popBackStack()
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

    override fun onResume() {
        super.onResume()
        KeyboardManager.init(requireActivity()).status()?.observeForever(this)
    }

    override fun onChanged(status: KeyboardManager.KeyboardStatus?) {
        status?.let {
            Timber.d(it.name)
            if (it == KeyboardManager.KeyboardStatus.CLOSED) {
                keyboardClosed()
            } else {
                keyboardOpen()
            }
        }
    }
}