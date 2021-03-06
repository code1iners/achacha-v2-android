package com.codeliner.achacha.todos.create

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.addCallback
import androidx.core.view.forEach
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.databinding.FragmentTodoCreateBinding
import com.codeliner.achacha.domains.todos.TodoDatabase
import com.codeliner.achacha.mains.MainActivity
import com.codeliner.achacha.utils.Const
import com.codeliner.achacha.utils.KeyboardManager
import com.example.helpers.toastForShort
import com.example.helpers.ui.AnimationManager
import com.google.android.material.chip.Chip
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import timber.log.Timber


class TodoCreateFragment: Fragment() {

    private lateinit var binding: FragmentTodoCreateBinding
    private lateinit var viewModel: TodoCreateViewModel
    private lateinit var app: Application

    override fun onResume() {
        super.onResume()
        KeyboardManager.keyboardOpen(app, binding.fragmentTodoCreateInput)
    }

    override fun onStop() {
        super.onStop()

        exitAnim()
    }

    override fun onStart() {
        super.onStart()

        enterAnim()
    }

    private fun exitAnim() {
        // note. header
        binding.fragmentTodoCreateHeaderContainer.startAnimation(
                AnimationManager.getHeaderHide(requireContext()).apply {
                    duration = Const.animDefaultDuration
                    fillAfter = true
                })
        // note. header
        binding.fragmentTodoCreateHeaderDividerBottom.startAnimation(
                AnimationManager.getHeaderHide(requireContext()).apply {
                    duration = Const.animDefaultDuration
                    fillAfter = true
                })
        // note. body
        binding.fragmentTodoCreateBodyContainer.startAnimation(
                AnimationManager.getFadeOut(requireContext()).apply {
                    duration = Const.animDefaultDuration
                    fillAfter = true
                }
        )
    }

    private fun enterAnim() {
        // note. header
        binding.fragmentTodoCreateHeaderContainer.startAnimation(
                AnimationManager.getHeaderShow(requireContext()).apply {
                    duration = Const.animDefaultDuration
                    fillAfter = true
                })
        // note. header
        binding.fragmentTodoCreateHeaderDividerBottom.startAnimation(
                AnimationManager.getHeaderShow(requireContext()).apply {
                    duration = Const.animDefaultDuration
                    fillAfter = true
                })
        // note. body
        binding.fragmentTodoCreateBodyContainer.startAnimation(
                AnimationManager.getFadeIn(requireContext()).apply {
                    duration = Const.animDefaultDuration
                    fillAfter = true
                }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTodoCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this
        app = requireNotNull(activity).application

        initBackPressed()
        initViewModel()
        initListeners()
        initObservers()
        initFocus()

        return binding.root
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.backReady()
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
        listenerChips()
    }

    private fun initObservers() {
        // note. when selected chip item
        observeHelps()
        // note. when called back (ui)
        observeBackPressed()
    }

    private fun observeBackPressed() {
        viewModel.onBackReady.observe(viewLifecycleOwner, Observer { isReady ->
            if (isReady) {
                // note. start animation
                exitAnim()
            }
        })
        // note. when called back (feature)
        viewModel.onBackStart.observe(viewLifecycleOwner, Observer { isStart ->
            if (isStart) {
                // note. real back
                back()

                viewModel.backStartComplete()
            }
        })
    }

    private fun observeHelps() {
        viewModel.helps.observe(viewLifecycleOwner, Observer { helps ->
            binding.fragmentTodoCreateInputContainer.helperText = helps
        })
    }

    private fun listenerChips() {
        binding.fragmentTodoCreateChipGroup.forEach { child ->
            (child as Chip).setOnCheckedChangeListener { _, _ ->
                val ids = binding.fragmentTodoCreateChipGroup.checkedChipIds
                val helps: ArrayList<CharSequence> = ArrayList()

                ids.forEach { id ->
                    helps.add(binding.fragmentTodoCreateChipGroup.findViewById<Chip>(id).text)
                }

                if (helps.isEmpty()) {
                    helps.add("Nothing")
                }

                viewModel.setHelps(helps.joinToString(", "))
            }
        }
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
                            viewModel.backReady()
                        }
                    }
                })
    }

    private fun initFocus() {
        binding.fragmentTodoCreateInput.requestFocus()
    }

    private fun back() {
        MainActivity.onBottomNavigationShow()
        this@TodoCreateFragment.findNavController().popBackStack()
    }
}