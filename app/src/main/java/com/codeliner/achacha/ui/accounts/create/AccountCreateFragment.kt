package com.codeliner.achacha.ui.accounts.create

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.FragmentAccountCreateBinding
import com.codeliner.achacha.mains.MainViewModel
import com.codeliner.achacha.utils.*
import com.codeliner.achacha.utils.Const.HINT
import com.codeliner.achacha.utils.Const.PASSWORD
import com.codeliner.achacha.utils.Const.SUBTITLE
import com.codeliner.achacha.utils.Const.THUMBNAIL
import com.codeliner.achacha.utils.Const.TITLE
import com.codeliner.achacha.utils.Const.USERNAME
import com.example.helpers.GlideOptions
import com.example.helpers.ui.getFadeIn
import com.example.helpers.ui.getFadeOut
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*

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
        Timber.w("onResume called")
        super.onResume()
        val view = getCurrentFieldView()
        view.requestFocus()
        KeyboardManager.keyboardOpen(app, view)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
        observeThumbnail()
    }

    private fun observeThumbnail() {
        viewModel.onOpenGallery.observe(viewLifecycleOwner) { opened ->
            if (opened) {
                Intent(Intent.ACTION_PICK).also {
                    it.type = "image/*"
                    startActivityForResult(it, 1000)
                }
            }
        }

        viewModel.onThumbnailImage.observe(viewLifecycleOwner) { uri ->
            when (uri == null) {
                true -> {

                }

                false -> {
                    // note. set account value
                    viewModel.setAccountValue(THUMBNAIL, uri.toString())
                    // note. update ui
                    binding.thumbnailText.visibility = View.GONE
                    binding.thumbnailImage.alpha = 1.0F
                    Glide
                        .with(app)
                        .load(uri)
                        .centerCrop()
                        .apply(GlideOptions.centerCropAndRadius(16))
                        .into(binding.thumbnailImage)
                }
            }
        }
    }

    private fun backObserve() {
        observeBack()
    }

    private fun observeBack() {
        viewModel.onBackReady.observe(viewLifecycleOwner) { ready ->
            if (ready) {
                // note. back (animation)
                binding.layout.startAnimation(
                        requireContext().getFadeOut().apply {
                            fillAfter = true
                            duration = resources.getInteger(R.integer.animation_duration_short).toLong()
                        }
                )
            }
        }

        viewModel.onBackJob.observe(viewLifecycleOwner) { job ->
            if (job) {
                // note. back (feature)
                back()

                viewModel.backJobComplete()
            }
        }
    }

    private fun observeAccountValue() {
        viewModel.onAccountValue.observe(viewLifecycleOwner) {
            Timber.d("account updated: $it")
            when (it.title.isNullOrEmpty()) {
                true -> {
                    binding.thumbnailText.text = getString(R.string.icon)
                }

                false -> {
                    binding.thumbnailText.text = it.title.toString().capitalize(Locale.ROOT).firstOrNull().toString()
                }
            }
        }
    }

    private fun observeSubmit() {
        viewModel.onSubmit.observe(viewLifecycleOwner) { started ->
            if (started) {
                // note. hider keyboard
                val view = getCurrentFieldView()
                KeyboardManager.keyboardClose(app, view)
                // note. back process
                viewModel.backReady()
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
        accountValueListener()
        formFocusListeners()
        formDoneListener()
    }

    private fun backListener() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.backReady()
        }
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
        binding.titleValue.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) { viewModel.currentField = "title" }
        }
        binding.subtitleValue.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) { viewModel.currentField = "subtitle" }
        }
        binding.usernameValue.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) { viewModel.currentField = "username" }
        }
        binding.passwordValue.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) { viewModel.currentField = "password" }
        }
        binding.hintValue.setOnFocusChangeListener { _, hasFocus ->
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

    private fun getCurrentFieldView(): TextInputEditText {
        return when (viewModel.currentField) {
            TITLE -> binding.titleValue
            SUBTITLE -> binding.subtitleValue
            USERNAME -> binding.usernameValue
            PASSWORD -> binding.passwordValue
            HINT -> binding.hintValue
            else -> binding.titleValue
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.w("requestCode: $requestCode, resultCode: $resultCode, data: $data")
        when (requestCode) {
            1000 -> {
                when (resultCode) {
                    RESULT_OK -> {
                        data?.let {
                            it.data?.let { uri ->
                               viewModel.setThumbnailImage(uri)
                            }
                        }

                    }

                    RESULT_CANCELED -> {

                    }
                }

                viewModel.openGalleryComplete()
            }
        }
    }
}