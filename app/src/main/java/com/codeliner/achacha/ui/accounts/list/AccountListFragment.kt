package com.codeliner.achacha.ui.accounts.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codeliner.achacha.databinding.FragmentAccountListBinding
import com.codeliner.achacha.mains.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountListFragment: Fragment() {

    private lateinit var binding: FragmentAccountListBinding
    private val viewModel: AccountListViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAccountListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        return binding.root
    }
}