package com.codeliner.achacha.ui.auths

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.andrognito.patternlockview.PatternLockView
import com.andrognito.patternlockview.listener.PatternLockViewListener
import com.codeliner.achacha.databinding.FragmentAuthBioBinding
import com.codeliner.achacha.mains.MainViewModel
import com.example.helpers.ui.toastingShort
import timber.log.Timber
import java.util.concurrent.Executor

class AuthBioFragment: Fragment() {

    private lateinit var binding: FragmentAuthBioBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private val patternListener = object: PatternLockViewListener {
        override fun onStarted() {
            Timber.w("onStarted")
        }

        override fun onProgress(progressPattern: MutableList<PatternLockView.Dot>?) {
            Timber.w("onProgress")
            progressPattern?.let {
                for (dot in it) Timber.v("dot: $dot")
            }
        }

        override fun onComplete(pattern: MutableList<PatternLockView.Dot>?) {
            Timber.w("onComplete")
            pattern?.let {
                for (dot in it) Timber.v("dot: $dot")
            }
        }

        override fun onCleared() {
            Timber.w("onCleared")
        }

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
        binding = FragmentAuthBioBinding.inflate(inflater)

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
            binding.biometricContainer.visibility = View.GONE
            binding.patternContainer.visibility = View.VISIBLE
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

                        findNavController().navigate(AuthBioFragmentDirections.actionAuthBioFragmentToTodoListFragment())
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Timber.w("Authentication failed")
                    }
                })
    }

    private fun initializePromptInfo() {
        promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("negative button text")
                .build()
    }

    private fun biometricStart() {
        biometricPrompt.authenticate(promptInfo)
    }
}