package com.codeliner.achacha.ui.accounts.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codeliner.achacha.databinding.FragmentAccountCreateBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountCreateFragment : Fragment() {

    private lateinit var binding: FragmentAccountCreateBinding
    private val viewModel: AccountCreateViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initialize(inflater)

        return binding.root
    }

    private fun initialize(inflater: LayoutInflater) {
        initializeBinding(inflater)
    }

    private fun initializeBinding(inflater: LayoutInflater) {
        binding = FragmentAccountCreateBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}