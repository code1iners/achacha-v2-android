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
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.Executor

class AuthenticateFragment: Fragment() {

    private lateinit var binding: FragmentAuthenticateBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val viewModel: AuthenticateViewModel by viewModel()

    private val patternListener = object: PatternLockViewListener {
        override fun onStarted() {}

        override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
//            progressPattern?.let {
//                for (dot in it) Timber.v("dot: $dot")
//            }
        }

        override fun onComplete(patternAsList: MutableList<PatternLockView.Dot>?) {
            patternAsList?.let { it ->
                val pattern = Pattern().apply {
                    patternAsString = it.joinToString()
                }
                patternCompleteJob(pattern)
            }

            binding.patternView.clearPattern()
        }

        override fun onCleared() {}

    }

    override fun onStart() {
        super.onStart()
        // note. fab gone
        MainViewModel.setFabVisibility(false)
        // note. bottom nav gone
        MainViewModel.setBottomNavigationVisibility(false)
    }

    override fun onStop() {
        super.onStop()
        // note. fab visible
        MainViewModel.setFabVisibility(true)
        // note. bottom nav visible
        MainViewModel.setBottomNavigationVisibility(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize(inflater)
        biometricStart()

        return binding.root
    }

    private fun initialize(inflater: LayoutInflater) {
        binding = FragmentAuthenticateBinding.inflate(inflater)

        // note. pattern
        initializeListeners()

        initializeBiometric()
    }

    private fun initializeListeners() {
        // note. pattern event listener
        binding.patternView.addPatternLockListener(patternListener)
        // note. biometric event listener
        binding.biometricAgain.setOnClickListener {
            biometricStart()
        }
        binding.usePattern.setOnClickListener {
            patternStart()
        }
    }

    private fun patternStart() {
        binding.biometricContainer.visibility = View.GONE
        binding.patternContainer.visibility = View.VISIBLE

        observePattern()
    }

    private fun observePattern() {
//        viewModel.patternMode.observe(viewLifecycleOwner) { mode ->
//            Timber.w("mode: $mode")
//        }

        viewModel.storedPattern.observe(viewLifecycleOwner) { pattern ->
            when (pattern == null) {
                true -> {
                    Timber.d("Need create new pattern")
                    binding.patternCreate.visibility = View.VISIBLE
                    binding.patternForgot.visibility = View.GONE


                }

                false -> {
                    Timber.d("Use pattern to authenticate app")
                    binding.patternCreate.visibility = View.GONE
                    binding.patternForgot.visibility = View.VISIBLE


                }
            }
        }

        viewModel.onLogin.observe(viewLifecycleOwner) {
            it?.let { isValid ->
                Timber.d("onLogin isValid: $isValid")
                when (isValid) {
                    true -> {
                        binding.patternForgot.setTextColor(ContextCompat.getColor(requireContext(), R.color.sexyGreen))
                        login()
                    }

                    false -> {
                        binding.patternForgot.setTextColor(ContextCompat.getColor(requireContext(), R.color.sexyRed))
                    }
                }
            }

            viewModel.loginJobComplete()
        }

        viewModel.onClearPattern.observe(viewLifecycleOwner) {
            it?.let { ask ->
                Timber.w("ask: $ask")
                when (ask) {
                    true -> {

                    }

                    false -> {

                    }
                }
            }
        }
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
                        //                context?.toastingShort("$errString")

                        when (errorCode) {
                            BiometricPrompt.ERROR_CANCELED -> {
                                Timber.w("ERROR_CANCELED")
                            }

                            BiometricPrompt.ERROR_HW_NOT_PRESENT -> {
                                Timber.w("ERROR_HW_NOT_PRESENT")
                            }

                            BiometricPrompt.ERROR_HW_UNAVAILABLE -> {
                                Timber.w("ERROR_HW_UNAVAILABLE")
                            }

                            BiometricPrompt.ERROR_LOCKOUT -> {
                                Timber.w("ERROR_LOCKOUT")
                            }

                            BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> {
                                Timber.w("ERROR_LOCKOUT_PERMANENT")
                            }

                            BiometricPrompt.ERROR_NEGATIVE_BUTTON -> {
                                Timber.w("ERROR_NEGATIVE_BUTTON")
                                binding.biometricAgain.visibility = View.VISIBLE
                                binding.usePattern.visibility = View.VISIBLE
                            }

                            BiometricPrompt.ERROR_NO_BIOMETRICS -> {
                                Timber.w("ERROR_NO_BIOMETRICS")

                                // note. using pattern lock
                                binding.patternContainer.visibility = View.VISIBLE
                            }

                            BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> {
                                Timber.w("ERROR_NO_DEVICE_CREDENTIAL")
                            }

                            BiometricPrompt.ERROR_NO_SPACE -> {
                                Timber.w("ERROR_NO_SPACE")
                            }

                            BiometricPrompt.ERROR_TIMEOUT -> {
                                Timber.w("ERROR_TIMEOUT")

                                binding.biometricAgain.visibility = View.VISIBLE
                            }

                            BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> {
                                Timber.w("ERROR_UNABLE_TO_PROCESS")
                            }

                            BiometricPrompt.ERROR_USER_CANCELED -> {
                                Timber.w("ERROR_USER_CANCELED")
                            }

                            BiometricPrompt.ERROR_VENDOR -> {
                                Timber.w("ERROR_VENDOR")
                            }
                        }
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Timber.w("Authentication succeeded")

                        login()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Timber.w("Authentication failed")
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
        biometricPrompt.authenticate(promptInfo)
    }

    private fun patternCompleteJob(pattern: Pattern) {
        when (viewModel.storedPattern.value == null) {
            true -> {
                viewModel.createPatternJob(pattern)
            }

            false -> {
                viewModel.loginJob(pattern)
            }
        }
    }
}