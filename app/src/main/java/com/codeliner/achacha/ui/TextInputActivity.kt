package com.codeliner.achacha.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.ActivityTextInputBinding
import com.codeliner.achacha.utils.Const.INPUT
import com.codeliner.achacha.utils.Const.TITLE
import com.codeliner.achacha.utils.KeyboardManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

class TextInputActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTextInputBinding
    private val viewModel: TextInputViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        observers()
    }

    private fun initialize() {
        // note. initialize binding.
        initializeBinding()
        // note. initialize passed data.
        initializePassedData()
        // note. Initialize listeners.
        initializeListeners()
        // note. initialize views.
        initializeViews()
    }

    private fun initializePassedData() {
        intent.extras?.let {
            // note. Set header title text.
            viewModel.setTitle(it.getString(TITLE))

            // note. Set body input text when if exist data
            it.getString(INPUT)?.let { passedInput ->
                viewModel.text = passedInput
                binding.bodyInput.setText(passedInput)
            }
        }
    }

    private fun initializeBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_text_input)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initializeListeners() {
        // note. When user input text.
        listenerBodyInput()
    }

    private fun listenerBodyInput() {
        // note. When body input text changed.
        binding.bodyInput.doOnTextChanged { text, _, _, _ ->
            viewModel.text = text.toString()
        }
        // note. When body input focus changed.
        binding.bodyInput.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                KeyboardManager.keyboardOpen(application, v)
            }
        }
    }

    private fun initializeViews() {
        // note. Request focus to body input for open keyboard.
        binding.bodyInput.requestFocus()
    }

    private fun observers() {
        // note. When user pressed close button.
        observeClose()
        // note. When user pressed save button.
        observeSave()
        // note. Set title When title already exist
        observeTitle()
    }

    private fun observeClose() {
        viewModel.onCloseWindow.observe(this) {
            it?.let { closeAction ->
                if (closeAction) {
                    finish()

                    viewModel.closeWindowComplete()
                }
            }
        }
    }

    private fun observeSave() {
        viewModel.onInputSave.observe(this) {
            it?.let { saveAction ->
                if (saveAction) {
                    // note. export result text input data
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(INPUT, viewModel.text)
                    })
                    finish()

                    viewModel.inputSaveComplete()
                }
            }
        }
    }

    private fun observeTitle() {
        viewModel.title.observe(this) { title ->
            when (title.isNullOrEmpty()) {
                true -> {
                    binding.headerTitle.text = getString(R.string.input_window)
                    binding.bodyInput.hint = "${getString(R.string.enter_text)}.."
                }

                false -> {
                    binding.headerTitle.text = title
                    binding.bodyInput.hint = "Enter ${title.toLowerCase(Locale.ROOT)}.."
                }
            }
        }
    }
}