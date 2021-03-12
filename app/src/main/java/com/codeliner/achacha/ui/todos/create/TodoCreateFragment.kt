package com.codeliner.achacha.ui.todos.create

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
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.FragmentTodoCreateBinding
import com.codeliner.achacha.data.AppDatabase
import com.codeliner.achacha.data.todos.TodoRepository
import com.codeliner.achacha.mains.MainActivity
import com.codeliner.achacha.mains.MainViewModel
import com.codeliner.achacha.utils.Const
import com.codeliner.achacha.utils.Const.ANIMATION_DURATION_SHORT
import com.codeliner.achacha.utils.KeyboardManager
import com.example.helpers.ui.AnimationManager
import com.example.helpers.ui.toastingShort
import com.google.android.material.chip.Chip
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class TodoCreateFragment: Fragment() {

    private lateinit var binding: FragmentTodoCreateBinding
    private val viewModel: TodoCreateViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()
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
                    duration = Const.ANIMATION_DURATION_SHORT
                    fillAfter = true
                })
        // note. header
        binding.fragmentTodoCreateHeaderDividerBottom.startAnimation(
                AnimationManager.getHeaderHide(requireContext()).apply {
                    duration = Const.ANIMATION_DURATION_SHORT
                    fillAfter = true
                })
        // note. body
        binding.fragmentTodoCreateBodyContainer.startAnimation(
                AnimationManager.getFadeOut(requireContext()).apply {
                    duration = Const.ANIMATION_DURATION_SHORT
                    fillAfter = true
                }
        )
    }

    private fun enterAnim() {
        // note. header
        binding.fragmentTodoCreateHeaderContainer.startAnimation(
                AnimationManager.getHeaderShow(requireContext()).apply {
                    duration = ANIMATION_DURATION_SHORT
                    fillAfter = true
                })
        // note. header
        binding.fragmentTodoCreateHeaderDividerBottom.startAnimation(
                AnimationManager.getHeaderShow(requireContext()).apply {
                    duration = ANIMATION_DURATION_SHORT
                    fillAfter = true
                })
        // note. body
        binding.fragmentTodoCreateBodyContainer.startAnimation(
                AnimationManager.getFadeIn(requireContext()).apply {
                    duration = ANIMATION_DURATION_SHORT
                    fillAfter = true
                }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTodoCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        app = requireNotNull(activity).application

        initBackPressed()
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
            when (viewModel.work.value.isNullOrEmpty()) {
                true -> {
                    context?.toastingShort("작업을 입력해주세요.")
                }

                false -> {
                    // note. stop error discover observer
                    viewModel.setSubmit(true)
                    // note. save todo data
                    viewModel.onSaveTodo()
                    // note. leave fragment
                    KeyboardManager.keyboardClose(app, binding.fragmentTodoCreateInput)
                }
            }
        }
    }

    private fun listenerInput() {
        binding.fragmentTodoCreateInput.doOnTextChanged { text, _, _, _ ->
            Timber.d("text: ${text.toString()}")
            viewModel.updateWork(text.toString())
            when (text.toString().isEmpty()) {
                true -> viewModel.discoveredError(getString(R.string.error_message_text_input_empty))
                false -> viewModel.undiscoveredError()
            }
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

    private fun initObservers() {
        // note. when selected chip item
        observeHelps()
        // note. when called back (ui)
        observeBackPressed()
        // note. when discovered error
        observeInputError()
    }

    private fun observeInputError() {
        viewModel.hasError.observe(viewLifecycleOwner, Observer { error ->
            binding.fragmentTodoCreateInputContainer.error = error
        })
    }

    private fun observeHelps() {
        viewModel.helps.observe(viewLifecycleOwner, Observer { helps ->
            binding.fragmentTodoCreateInputContainer.helperText = helps
        })
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

    private fun initFocus() {
        binding.fragmentTodoCreateInput.requestFocus()
    }

    private fun back() {
        // note. fabs turn on
        MainViewModel.setFabAnimation(true, ANIMATION_DURATION_SHORT)
        // note. bottom nav turn on
        MainViewModel.setBottomNavigationAnimation(true, ANIMATION_DURATION_SHORT)
        // note. back
        this@TodoCreateFragment.findNavController().popBackStack()
    }
}