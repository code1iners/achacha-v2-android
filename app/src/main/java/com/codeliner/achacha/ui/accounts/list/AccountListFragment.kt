package com.codeliner.achacha.ui.accounts.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.codeliner.achacha.R
import com.codeliner.achacha.data.accounts.Account
import com.codeliner.achacha.databinding.FragmentAccountListBinding
import com.example.helpers.ui.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class AccountListFragment: Fragment()
    , AccountClickListener
{

    private lateinit var binding: FragmentAccountListBinding
    private val viewModel: AccountListViewModel by viewModel()
    private lateinit var accountListAdapter: AccountListAdapter

    override fun onStop() {
        exitAnim()
        super.onStop()
    }

    private fun exitAnim() {
        context?.let {
            val duration = resources.getInteger(R.integer.animation_duration_short).toLong()
            val animHeaderHide = it.getHeaderHide().apply {
                this.duration = duration
            }
            val animFadeOut = it.getFadeOut().apply {
                this.duration = duration
                this.fillAfter = true
            }

            // note. header
            binding.headerContainer.startAnimation(animHeaderHide)
            // note. header
            binding.headerDividerBottom.startAnimation(animHeaderHide)
            // note. body
            binding.accountList.startAnimation(animFadeOut)
        }
    }

    override fun onStart() {
        enterAnim()
        super.onStart()
    }

    private fun enterAnim() {
        context?.let {
            val duration = resources.getInteger(R.integer.animation_duration_short).toLong()
            val animHeaderShow = it.getHeaderShow().apply {
                this.duration = duration
            }
            val animFadeIn = it.getFadeIn().apply {
                this.duration = duration
                this.fillAfter = true
            }

            // note. header
            binding.headerContainer.startAnimation(animHeaderShow)
            // note. header
            binding.headerDividerBottom.startAnimation(animHeaderShow)
            // note. body
            binding.accountList.startAnimation(animFadeIn)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        initialize(inflater)
        observe()

        return binding.root
    }

    private fun initialize(inflater: LayoutInflater) {
        initializeBinding(inflater)
        initializeAdapter()
    }

    private fun initializeBinding(inflater: LayoutInflater) {
        binding = FragmentAccountListBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    private fun initializeAdapter() {
        accountListAdapter = AccountListAdapter(this)
        binding.accountList.adapter = accountListAdapter
    }

    private fun observe() {
        observeFabs()
        viewModel.accounts.observe(viewLifecycleOwner, { accounts ->
            accountListAdapter.submitList(accounts.map { it.copy() })
        })
    }

    private fun observeFabs() {
        observeFabCreate()
        observeFabClear()
        observeFabTest()
    }

    private fun observeFabCreate() {
        // note. when clicked create fab button from main
        AccountListViewModel.onAccountCreate.observe(viewLifecycleOwner, { started ->
            Timber.d("onAccountCreate: $started")
            if (started) {
                viewModel.navigateToAccountCreateAnimation()
                // note. complete navigate to create action
                AccountListViewModel.accountCreateJobComplete()
            }
        })

        // note. navigate (ui)
        viewModel.onNavigateToAccountCreateAnimation.observe(viewLifecycleOwner, { started ->
            Timber.d("onNavigateToAccountCreateAnimation: $started")
            if (started) {
                // note. start anim
                exitAnim()
            }
        })

        // note. navigate (feature)
        viewModel.onNavigateToAccountCreateJob.observe(viewLifecycleOwner, { started ->
            Timber.d("onNavigateToAccountCreateJob: $started")
            if (started) {
                findNavController().navigate(AccountListFragmentDirections.actionAccountListFragmentToAccountCreateFragment())

                viewModel.navigateToAccountCreateJobComplete()
            }
        })
    }

    private fun observeFabClear() {
        AccountListViewModel.onAccountClear.observe(viewLifecycleOwner, { started ->
            if (started) {

                viewModel.clearAccounts()

                AccountListViewModel.accountClearJobComplete()
            }
        })
    }

    private fun observeFabTest() {
        AccountListViewModel.onAccountTest.observe(viewLifecycleOwner, { started ->
            if (started) {

                AccountListViewModel.accountTestJobComplete()
            }
        })
    }

    override fun onClick(account: Account) {
        Timber.w("account: $account")
    }
}