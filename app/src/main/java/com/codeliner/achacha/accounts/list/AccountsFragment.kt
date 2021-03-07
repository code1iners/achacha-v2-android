package com.codeliner.achacha.accounts.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.codeliner.achacha.databinding.FragmentAccountsBinding

class AccountsFragment: Fragment() {

    private lateinit var binding: FragmentAccountsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountsBinding.inflate(inflater)
        binding.lifecycleOwner = this


        return binding.root
    }
}