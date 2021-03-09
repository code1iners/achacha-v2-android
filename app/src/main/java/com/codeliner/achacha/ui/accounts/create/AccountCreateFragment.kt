package com.codeliner.achacha.ui.accounts.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.R
import com.codeliner.achacha.databinding.FragmentAccountCreateBinding
import com.codeliner.achacha.mains.MainViewModel
import com.codeliner.achacha.utils.Const
import com.example.helpers.ui.getFadeOut
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AccountCreateFragment : Fragment() {

    private lateinit var binding: FragmentAccountCreateBinding
    private val viewModel: AccountCreateViewModel by viewModel()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initialize(inflater)
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

        return binding.root
    }

    private fun initialize(inflater: LayoutInflater) {
        initializeBinding(inflater)
        initBackPressed()
    }

    private fun initializeBinding(inflater: LayoutInflater) {
        binding = FragmentAccountCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.backReady()
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