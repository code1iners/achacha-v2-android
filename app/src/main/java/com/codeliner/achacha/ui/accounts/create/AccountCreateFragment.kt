package com.codeliner.achacha.ui.accounts.create

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.FragmentAccountCreateBinding
import com.codeliner.achacha.mains.MainViewModel
import com.codeliner.achacha.utils.*
import com.codeliner.achacha.utils.Const.HINT
import com.codeliner.achacha.utils.Const.PASSWORD
import com.codeliner.achacha.utils.Const.SUBTITLE
import com.codeliner.achacha.utils.Const.TITLE
import com.codeliner.achacha.utils.Const.USERNAME
import com.codeliner.achacha.utils.clearErrorsWithHelperText
import com.example.helpers.ui.getFadeIn
import com.example.helpers.ui.getFadeOut
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AccountCreateFragment : Fragment() {

    private lateinit var binding: FragmentAccountCreateBinding
    private val viewModel: AccountCreateViewModel by viewModel()
    private lateinit var app: Application

    override fun onStart() {
        super.onStart()
        enterAnim()
    }

    private fun enterAnim() {
        context?.let {
            val duration = resources.getInteger(R.integer.animation_duration_short).toLong()
            binding.layout.startAnimation(it.getFadeIn().apply {
                this.duration = duration
            })
        }
    }

    override fun onResume() {
        super.onResume()
        KeyboardManager.keyboardOpen(app, binding.titleValue)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initialize(inflater)
        observers()
        listeners()

        return binding.root
    }

    private fun initialize(inflater: LayoutInflater) {
        app = requireNotNull(activity).application
        initializeBinding(inflater)
        binding.titleValue.requestFocus()
    }

    private fun initializeBinding(inflater: LayoutInflater) {
        binding = FragmentAccountCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun observers() {
        backObserve()
        observeAccountValue()
        observeSubmit()
    }

    private fun backObserve() {
        observeBack()
    }

    private fun observeBack() {
        viewModel.onBackReady.observe(viewLifecycleOwner, Observer { ready ->
            if (ready) {
                // note. back (animation)
                binding.layout.startAnimation(
                        requireContext().getFadeOut().apply {
                            fillAfter = true
                            duration = resources.getInteger(R.integer.animation_duration_short).toLong()
                        }
                )
            }
        })
        viewModel.onBackJob.observe(viewLifecycleOwner, Observer { job ->
            if (job) {
                // note. back (feature)
                back()

                viewModel.backJobComplete()
            }
        })
    }

    private fun observeAccountValue() {
        viewModel.onAccountValue.observe(viewLifecycleOwner, Observer {
            Timber.d("account updated: $it")
        })
    }

    private fun observeSubmit() {
        viewModel.onSubmit.observe(viewLifecycleOwner) { started ->
            if (started) {
                val view = when (viewModel.currentField) {
                    TITLE -> binding.titleValue
                    SUBTITLE -> binding.subtitleValue
                    USERNAME -> binding.usernameValue
                    PASSWORD -> binding.passwordValue
                    HINT -> binding.hintValue
                    else -> binding.titleValue
                }
                // note. back
                KeyboardManager.keyboardClose(app, view as EditText)
                // note. complete
                viewModel.submitAccountComplete()
            }
        }

        viewModel.hasError.observe(viewLifecycleOwner) { values ->
            if (!values.first) {

                listOf(
                    binding.titleContainer
                    ,binding.subtitleContainer
                    ,binding.usernameContainer
                    ,binding.passwordContainer
                    ,binding.hintContainer
                ).clearErrorsWithHelperOff()

                val container = when (values.second) {
                    TITLE -> { binding.titleContainer }
                    SUBTITLE -> { binding.subtitleContainer }
                    USERNAME -> { binding.usernameContainer }
                    PASSWORD -> { binding.passwordContainer }
                    HINT -> { binding.hintContainer }
                    else -> null
                }

                container?.let {
                    it.error = getString(R.string.required)
                    it.getChildAt(0).requestFocus()
                }
            }
        }
    }

    private fun listeners() {
        backListener()
        keyboardListener()
        accountValueListener()
        formFocusListeners()
        formDoneListener()
    }

    private fun backListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.backReady()
        }
    }

    private fun keyboardListener() {
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
            }
        )
    }

    private fun accountValueListener() {
        // note. title
        binding.titleValue.doOnTextChanged { text, _, _, _ ->
            viewModel.setAccountValue(TITLE, text)
        }
        // note. subtitle
        binding.subtitleValue.doOnTextChanged { text, _, _, _ ->
            viewModel.setAccountValue(SUBTITLE, text)
        }
        // note. subtitle
        binding.usernameValue.doOnTextChanged { text, _, _, _ ->
            viewModel.setAccountValue(USERNAME, text)
        }
        // note. subtitle
        binding.passwordValue.doOnTextChanged { text, _, _, _ ->
            viewModel.setAccountValue(PASSWORD, text)
        }
        // note. subtitle
        binding.hintValue.doOnTextChanged { text, _, _, _ ->
            viewModel.setAccountValue(HINT, text)
        }
    }

    private fun formFocusListeners() {
        binding.titleValue.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) { viewModel.currentField = "title" }
        }
        binding.subtitleValue.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) { viewModel.currentField = "subtitle" }
        }
        binding.usernameValue.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) { viewModel.currentField = "username" }
        }
        binding.passwordValue.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) { viewModel.currentField = "password" }
        }
        binding.hintValue.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) { viewModel.currentField = "hint" }
        }
    }

    private fun formDoneListener() {
        binding.hintValue.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    viewModel.createAccountJob()
                    return@setOnEditorActionListener true
                }
            }
            false
        }
    }

    private fun back() {
        // note. fabs turn on
        MainViewModel.setFabAnimation(true, Const.ANIMATION_DURATION_SHORT)
        // note. bottom nav turn on
        MainViewModel.setBottomNavigationAnimation(true, Const.ANIMATION_DURATION_SHORT)
        // note. back
        findNavController().popBackStack()
    }
}