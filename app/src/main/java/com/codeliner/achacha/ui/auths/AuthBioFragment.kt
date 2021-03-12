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
        binding = FragmentAuthBioBinding.inflate(inflater)

        // note. pattern
        binding.patternView.addPatternLockListener(patternListener)

        executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt(this, executor, object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Timber.w("Authentication error: $errorCode")
                context?.toastingShort("$errString")

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
                context?.toastingShort("Authentication succeeded")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                context?.toastingShort("Authentication failed")
            }
        })

//        val deviceAPILevel = android.os.Build.VERSION.SDK_INT
//        Timber.d("deviceAPILevel: $deviceAPILevel")
//        when (deviceAPILevel > 29) {
//            true -> {
//
//            }
//
//            false -> {
//
//            }
//        }

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("negative button text")
            .build()

        biometricPrompt.authenticate(promptInfo)

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.w("requestCode: $requestCode, resultCode: $resultCode, data: $data")
    }
}