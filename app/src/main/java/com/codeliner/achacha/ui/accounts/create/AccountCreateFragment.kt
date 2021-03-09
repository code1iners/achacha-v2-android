package com.codeliner.achacha.ui.accounts.create

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.FragmentAccountCreateBinding
import com.codeliner.achacha.mains.MainViewModel
import com.codeliner.achacha.utils.Const
import com.codeliner.achacha.utils.KeyboardManager
import com.example.helpers.ui.getFadeIn
import com.example.helpers.ui.getFadeOut
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
    }

    private fun backObserve() {
        observeBack()
        observeAccountValue()
    }

    private fun observeAccountValue() {
        viewModel.onAccountValue.observe(viewLifecycleOwner, Observer {
            Timber.d("account updated: $it")
        })
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

    private fun listeners() {
        backListener()
        keyboardListener()
        accountValueListener()
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
            viewModel.setAccountValue("title", text)
        }
        // note. subtitle
        binding.subtitleValue.doOnTextChanged { text, _, _, _ ->
            viewModel.setAccountValue("subtitle", text)
        }
        // note. subtitle
        binding.usernameValue.doOnTextChanged { text, _, _, _ ->
            viewModel.setAccountValue("username", text)
        }
        // note. subtitle
        binding.passwordValue.doOnTextChanged { text, _, _, _ ->
            viewModel.setAccountValue("password", text)
        }
        // note. subtitle
        binding.hintValue.doOnTextChanged { text, _, _, _ ->
            viewModel.setAccountValue("hint", text)
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