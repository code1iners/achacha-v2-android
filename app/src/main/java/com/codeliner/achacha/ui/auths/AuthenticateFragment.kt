package com.codeliner.achacha.ui.auths

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.codeliner.achacha.R
import com.codeliner.achacha.data.patterns.Pattern
import com.codeliner.achacha.databinding.FragmentAuthenticateBinding
import com.codeliner.achacha.mains.MainViewModel
import com.codeliner.achacha.ui.accounts.list.AccountListViewModel
import com.codeliner.achacha.ui.todos.list.TodoListViewModel
import com.codeliner.achacha.utils.setTextColorById
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.Executor

class AuthenticateFragment: Fragment() {

    private lateinit var binding: FragmentAuthenticateBinding

    // note. Biometrics.
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    // note. View models.
    private val viewModel: AuthenticateViewModel by viewModel()
    private val todoListViewModel: TodoListViewModel by viewModel()
    private val accountListViewModel: AccountListViewModel by viewModel()

    // note. Listeners.
    private val patternListener = object: PatternLockViewListener {
        override fun onStarted() {}

        override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {}

        override fun onComplete(patternAsList: MutableList<PatternLockView.Dot>?) {
            patternAsList?.let { it ->
                // note. Apply data into pattern data object
                val pattern = Pattern().apply {
                    patternAsString = it.joinToString()
                }
                // note. Start authenticate process After compare the two patterns.
                patternCompleteJob(pattern)
            }
            binding.patternView.clearPattern()
        }

        override fun onCleared() {}

    }

    override fun onStart() {
        super.onStart()
        // note. Fab gone.
        MainViewModel.setFabVisibility(false)
        // note. Bottom nav gone.
        MainViewModel.setBottomNavigationVisibility(false)
    }

    override fun onStop() {
        super.onStop()
        // note. Fab visible.
        MainViewModel.setFabVisibility(true)
        // note. Bottom nav visible.
        MainViewModel.setBottomNavigationVisibility(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize(inflater)
        // note. Open biometric authenticate box.
        biometricStart()

        return binding.root
    }

    private fun initialize(inflater: LayoutInflater) {
        // note. Initialize binding.
        initializeBinding(inflater)
        // note. Initialize listener.
        initializeListeners()
        // note. Initialize biometric.
        initializeBiometric()
    }

    private fun initializeBinding(inflater: LayoutInflater) {
        binding = FragmentAuthenticateBinding.inflate(inflater)
        binding.viewModel = viewModel
    }

    private fun initializeListeners() {
        // note. Pattern event listener.
        binding.patternView.addPatternLockListener(patternListener)
        // note. Biometric event listener.
        binding.biometricAgain.setOnClickListener {
            // note. Open biometric authenticate box.
            biometricStart()
        }
        binding.usePattern.setOnClickListener {
            // note. Change authenticate mode as Pattern.
            patternModeStart()
        }
    }

    private fun patternModeStart() {
        // note. Set container visibilities(Biometric hide & Pattern show).
        biometricHidePatternShow()
        // note. From now on, will observe the pattern.
        observePattern()
    }

    private fun observePattern() {
        viewModel.storedPattern.observe(viewLifecycleOwner) { pattern ->
            when (pattern == null) {
                true -> {
                    // note. When pattern create.
                    binding.patternCreate.visibility = View.VISIBLE
                    binding.patternForgot.visibility = View.GONE
                }

                false -> {
                    // note. When pattern authenticate.
                    binding.patternCreate.visibility = View.GONE
                    binding.patternForgot.visibility = View.VISIBLE
                }
            }
        }

        viewModel.onLogin.observe(viewLifecycleOwner) { isValid ->
            when (isValid) {
                // note. Set success color.
                true -> {
                    binding.patternForgot.setTextColorById(R.color.sexyGreen)
                    login()
                }
                // note. Set error color.
                false -> binding.patternForgot.setTextColorById(R.color.sexyRed)
                // note. Set default color.
                else -> binding.patternForgot.setTextColorById(R.color.primaryTextColor)
            }
        }

        viewModel.onClearPatternAsk.observe(viewLifecycleOwner) {
            it?.let { ask ->
                if (ask) {
                    MaterialAlertDialogBuilder(requireContext())
                            .setTitle(getString(R.string.auth_pattern_initialize_message_title))
                            .setMessage(getString(R.string.auth_pattern_initialize_message_subtitle))
                            .setNegativeButton(getString(R.string.disagree)) { _, _ -> }
                            .setPositiveButton(getString(R.string.agree)) { _, _ ->
                                // note. Initialize pattern and clear all data.
                                clearAllStoredData()
                                // note. Update pattern guide text color by null.
                                viewModel.clearOnLogin()
                            }
                            .show()

                    viewModel.clearPatternAskComplete()
                }
            }
        }
    }

    private fun clearAllStoredData() {
        // note. Clear pattern.
        viewModel.clearPatternJob()
        // note. Clear to-do list.
        todoListViewModel.clearTodos()
        // note. Clear account list.
        accountListViewModel.clearAccounts()
    }

    private fun initializeBiometric() {
        initializeExecutor()
        initializeBiometricPrompt()
        initializePromptInfo()
    }

    private fun initializeExecutor() {
        executor = ContextCompat.getMainExecutor(context)
    }

    private fun initializeBiometricPrompt() {
        biometricPrompt =
                BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Timber.w("Authentication error\ncode: $errorCode, message: $errString")

                        when (errorCode) {
                            // note. When clicked cancel button in Authentication with biometric.
                            BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                                // note. Showing biometric again & use pattern button.
                                biometricPatternButtonsVisible()
                            }

                            // note. When no have Fingerprint data in User device.
                            BiometricPrompt.ERROR_NO_BIOMETRICS -> {

                                // note. Now start pattern authenticate mode.
                                patternModeStart()
                                // note. Set container visibilities.
                                biometricHidePatternShow()
                            }

                            // note. When user no input anything while long time.
                            BiometricPrompt.ERROR_TIMEOUT -> {

                                // note. Give the change what try again to authenticate with biometric.
                                binding.biometricAgain.visibility = View.VISIBLE
                            }

                            BiometricPrompt.ERROR_CANCELED -> Timber.e("ERROR_CANCELED")
                            BiometricPrompt.ERROR_HW_NOT_PRESENT -> Timber.e("ERROR_HW_NOT_PRESENT")
                            BiometricPrompt.ERROR_HW_UNAVAILABLE -> Timber.e("ERROR_HW_UNAVAILABLE")
                            BiometricPrompt.ERROR_LOCKOUT -> Timber.e("ERROR_LOCKOUT")
                            BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> Timber.e("ERROR_LOCKOUT_PERMANENT")
                            BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> Timber.e("ERROR_NO_DEVICE_CREDENTIAL")
                            BiometricPrompt.ERROR_NO_SPACE -> Timber.e("ERROR_NO_SPACE")
                            BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> Timber.e("ERROR_UNABLE_TO_PROCESS")
                            BiometricPrompt.ERROR_USER_CANCELED -> Timber.e("ERROR_USER_CANCELED")
                            BiometricPrompt.ERROR_VENDOR -> Timber.e("ERROR_VENDOR")
                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        // note. Login successfully (Navigate next view).
                        login()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                })
    }

    private fun login() {
        this@AuthenticateFragment.findNavController()
                .navigate(AuthenticateFragmentDirections.actionAuthBioFragmentToTodoListFragment())
    }

    private fun initializePromptInfo() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.auth_authenticate))
                .setSubtitle(getString(R.string.auth_authenticate_by_fingerprint))
                .setNegativeButtonText(getString(R.string.cancel))
                .build()
    }

    private fun biometricStart() {
        // note. Open biometric authenticate box.
        biometricPrompt.authenticate(promptInfo)
    }

    private fun patternCompleteJob(pattern: Pattern) {
        // note. Check exist stored pattern data.
        when (viewModel.storedPattern.value == null) {
            // note. Will create pattern after clear pattern data.
            true -> viewModel.createPatternJob(pattern)
            // note. Will login as pattern authenticate method.
            false -> viewModel.loginJob(pattern)
        }
    }

    private fun biometricHidePatternShow() {
        binding.biometricContainer.visibility = View.GONE
        binding.patternContainer.visibility = View.VISIBLE
    }

    private fun biometricPatternButtonsVisible() {
        binding.biometricAgain.visibility = View.VISIBLE
        binding.usePattern.visibility = View.VISIBLE
    }
}