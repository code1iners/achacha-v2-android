package com.codeliner.achacha.ui.accounts.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.databinding.FragmentAccountListBinding
import com.codeliner.achacha.mains.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AccountListFragment: Fragment() {

    private lateinit var binding: FragmentAccountListBinding
    private val viewModel: AccountListViewModel by viewModel()
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize(inflater)
        observe()

        return binding.root
    }

    private fun initialize(inflater: LayoutInflater) {
        binding = FragmentAccountListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun observe() {
        observeFabs()
    }

    private fun observeFabs() {
        observeFabCreate()
        observeFabClear()
        observeFabTest()
    }

    private fun observeFabCreate() {
        AccountListViewModel.onAccountCreate.observe(viewLifecycleOwner, Observer { started ->
            if (started) {

                viewModel.navigateToAccountCreate()

                AccountListViewModel.accountCreateJobComplete()
            }
        })

        viewModel.onNavigateToAccountCreate.observe(viewLifecycleOwner, Observer { started ->
            if (started) {
                Timber.d("navigate to account create")

                findNavController().navigate(AccountListFragmentDirections.actionAccountListFragmentToAccountCreateFragment())

                viewModel.navigateToAccountCreateComplete()
            }
        })
    }

    private fun observeFabClear() {
        AccountListViewModel.onAccountClear.observe(viewLifecycleOwner, Observer { started ->
            if (started) {

                AccountListViewModel.accountClearJobComplete()
            }
        })
    }

    private fun observeFabTest() {
        AccountListViewModel.onAccountTest.observe(viewLifecycleOwner, Observer { started ->
            if (started) {

                AccountListViewModel.accountTestJobComplete()
            }
        })
    }
}